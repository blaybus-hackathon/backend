package com.balybus.galaxy.global.domain.tblPatientLog;

import com.balybus.galaxy.global.domain.tblAddressFirst.QTblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.QTblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.QTblAddressThird;
import com.balybus.galaxy.global.domain.tblCare.QTblCare;
import com.balybus.galaxy.global.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.global.domain.tblMatching.MatchState;
import com.balybus.galaxy.global.domain.tblMatching.QTblMatching;
import com.balybus.galaxy.global.domain.tblMatching.SelectMatchStatus;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TblPatientLogRepositoryImpl implements TblPatientLogRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public TblPatientLogRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MatchingStatusResponseDto.MatchingPatientInfo> matchStatePatientLog(Long managerSeq, SelectMatchStatus selectMatchStatus) {
        QTblPatientLog tpl = QTblPatientLog.tblPatientLog;
        QTblAddressFirst taf = QTblAddressFirst.tblAddressFirst;
        QTblAddressSecond tas = QTblAddressSecond.tblAddressSecond;
        QTblAddressThird tat = QTblAddressThird.tblAddressThird;
        QTblCare tc1 = new QTblCare("tc1");
        QTblCare tc2 = new QTblCare("tc2");
        QTblCare tc3 = new QTblCare("tc3");
        QTblMatching m = QTblMatching.tblMatching;

        JPAQuery<MatchingStatusResponseDto.MatchingPatientInfo> query = queryFactory
                .select(Projections.constructor(MatchingStatusResponseDto.MatchingPatientInfo.class,
                        tpl.patient.id.as("patientSeq"),
                        tpl.id.as("patientLogSeq"),
                        tpl.name.as("name"),
                        tpl.birthDate.as("birthDate"),
                        tc1.careName.as("gender"),
                        tc2.careName.as("workType"),
                        tc3.careName.as("careLevel"),
                        taf.name.as("addressFirst"),
                        tas.name.as("addressSecond"),
                        tat.name.as("addressThird")
                ))
                .from(tpl)
                .join(taf).on(taf.id.eq(tpl.tblAddressFirst.id))
                .join(tas).on(tas.id.eq(tpl.tblAddressSecond.id))
                .join(tat).on(tat.id.eq(tpl.tblAddressThird.id))
                .join(tc1).on(tc1.care.id.eq(TblCareTopEnum.GENDER.getCareSeq()).and(tc1.careVal.eq(tpl.gender)))
                .join(tc2).on(tc2.care.id.eq(TblCareTopEnum.WORK_TYPE.getCareSeq()).and(tc2.careVal.eq(tpl.workType)))
                .join(tc3).on(tc3.care.id.eq(TblCareTopEnum.CARE_LEVEL.getCareSeq()).and(tc3.careVal.eq(tpl.careLevel)))
                .where(tpl.manager.id.eq(managerSeq));

        // 조회 기준
        BooleanExpression matchingState = null;
        if(selectMatchStatus.equals(SelectMatchStatus.ONLY_SELECT_MATCHING)) {
            //매칭 요청 전(INIT) 상태가 전부는 아니면서, 매칭 완료는 아직 이뤄지지 않은 공고들 조회
            matchingState = JPAExpressions.selectOne()
                    .from(m)
                    .where(m.patientLog.id.eq(tpl.id)
                            .and(m.matchState.eq(MatchState.MATCH_FIN)))
                    .notExists()
                    .and(JPAExpressions.selectOne()
                            .from(m)
                            .where(m.patientLog.id.eq(tpl.id)
                                    .and(m.matchState.ne(MatchState.INIT)))
                            .exists());

        } else if(selectMatchStatus.equals(SelectMatchStatus.ONLY_SELECT_INIT)){
            // 요양보호사 기준으로 전부 매칭 요청 전(INIT) 상태인 공고만 조회
            matchingState = JPAExpressions.selectOne()
                    .from(m)
                    .where(m.patientLog.id.eq(tpl.id)
                            .and(m.matchState.ne(MatchState.INIT)))
                    .notExists();
        } else if(selectMatchStatus.equals(SelectMatchStatus.ONLY_SELECT_MATCH_FIN)){
            //매칭 완료된 공고만 조회
            matchingState = JPAExpressions.selectOne()
                    .from(m)
                    .where(m.patientLog.id.eq(tpl.id)
                            .and(m.matchState.eq(MatchState.MATCH_FIN)))
                    .exists();
        }

        if(matchingState != null) {
            query.where(matchingState);
        }

        return query.fetch();
    }
}
