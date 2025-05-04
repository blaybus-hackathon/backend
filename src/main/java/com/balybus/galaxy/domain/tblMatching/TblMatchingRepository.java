package com.balybus.galaxy.domain.tblMatching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblMatchingRepository extends JpaRepository<TblMatching, Long> {
    List<TblMatching> findByPatientLog_id(Long plSeq);
    Optional<TblMatching> findByPatientLog_idAndHelper_id(Long plSeq, Long helperSeq);
    List<TblMatching> findByPatientLog_idAndMatchState(Long plSeq, MatchState matchState);

    @Query(value = """
            select
            	count(case when (a.reject_count = 0 and a.match_fin_count = 0 and a.permit_tune_count = 0 and a.match_request_count = 0 and a.init_count > 0) then 1 end) as new_match_count	-- 신규 매칭 건수 : 해당 계정으로 관리하고 있는 완료되지 않은 매칭(공고) 건수 - init 만으로 구성된공고 개수
            	,count(a.pl_seq) as total -- 전체 매칭 건수 : 해당 계정으로 관리하고 있는 매칭(공고) 건수
            	,count(case when (a.match_fin_count = 0 and a.permit_tune_count > 0) then 1 end) as in_progress_count -- 상태별 매칭 건수 - 진행중 : PERMIT_TUNE(2, "수락함(조율중)") 상태의 공고 개수 - 매칭 완료가 아닌 공고 중 수락함(조율 중) 상태의 건수가 하나라도 존재하는 경우, 진행중으로 여긴다.
            	,count(case when a.match_fin_count > 0 then 1 end) as match_fin_count -- 상태별 매칭 건수 - 완료 : MATCH_FIN(3, "매칭 완료") 상태의 공고 개수 - 매칭보호사 리스트 중 한 명이라도 완료가 되면, 해당 건은 매칭이 완료된 것으로 여긴다.
            	,sum(a.permit_tune_count + a.match_fin_count) as permit_count -- 매칭 비율 - 수락률 : 매칭 리스트 전체에 대한 조율 매칭 완료 개수(수락)의 비율 - 수락/(수락+거절)
            	,sum(a.reject_count) as reject_count -- 매칭 비율 - 거절률 : 매칭 리스트 전체에 대한 거절 개수(거절)의 비율 - 거절/(수락+거절)
            from(
            	select
            		tm.pl_seq,
            		count(case when tm.match_state = 'INIT' then 1 end) AS init_count,
            		count(case when tm.match_state = 'MATCH_REQUEST' then 1 end) AS match_request_count,
            		count(case when tm.match_state = 'PERMIT_TUNE' then 1 end) AS permit_tune_count,
            		count(case when tm.match_state = 'MATCH_FIN' then 1 end) AS match_fin_count,
            		count(case when tm.match_state = 'REJECT' then 1 end) AS reject_count
            	from tbl_matching tm
            	join tbl_patient_log tpl on tpl.pl_seq = tm.pl_seq and tpl.manager_seq = :cmSeq
            	where tm.use_yn = 1
            	group by tm.pl_seq
            ) as a
            """, nativeQuery = true)
    List<Object[]> findByCmSeqToGetStatisticsDashboard(@Param("cmSeq") Long cmSeq);
}
