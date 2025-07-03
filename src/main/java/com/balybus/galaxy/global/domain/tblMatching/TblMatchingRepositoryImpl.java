package com.balybus.galaxy.global.domain.tblMatching;

import com.balybus.galaxy.careAssistant.domain.QTblHelper;
import com.balybus.galaxy.global.domain.tblCare.QTblCare;
import com.balybus.galaxy.global.domain.tblCare.TblCareTopEnum;
import com.balybus.galaxy.patient.matchingStatus.dto.MatchingStatusResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TblMatchingRepositoryImpl implements TblMatchingRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    public TblMatchingRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MatchingStatusResponseDto.MatchedHelperInfo> findMatchingHelperInfo(Long patientLogId, MatchState matchState) {
        QTblMatching m = QTblMatching.tblMatching;
        QTblHelper h = QTblHelper.tblHelper;
        QTblCare c = QTblCare.tblCare;

        JPAQuery<MatchingStatusResponseDto.MatchedHelperInfo> query = queryFactory
                .select(Projections.constructor(MatchingStatusResponseDto.MatchedHelperInfo.class,
                        h.id.as("helperSeq")
                        ,h.name.as("name")
                        ,c.careName.as("gender")
                        ,h.birthday.as("birthDate")
                ))
                .from(m)
                .join(h).on(h.id.eq(m.helper.id))
                .join(c).on(c.care.id.eq(TblCareTopEnum.GENDER.getCareSeq()).and(c.careVal.eq(h.careGender)))
                .where(m.patientLog.id.eq(patientLogId)
                        , m.matchState.eq(matchState));

        return query.fetch();
    }
}
