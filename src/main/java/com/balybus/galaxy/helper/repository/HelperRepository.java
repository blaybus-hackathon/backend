package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.global.utils.matching.dto.MatchingResponseDto;
import com.balybus.galaxy.helper.domain.TblHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HelperRepository extends JpaRepository<TblHelper, Long> {
    Optional<TblHelper> findByUserId(Long userId);

    @Query(value = """
                select
                    d.helper_seq,
                    ((d.location_score * 30)
                        + (d.time_score * 15)
                        + (d.date_score * 10)
                        + (((d.work_type)
                            + (d.welfare)
                            + (d.care_level)
                            + (d.dementia_symptom)
                            + (d.inmate_state)
                            + (d.gender)
                            + (d.service_meal)
                            + (d.service_mobility)
                            + (d.service_toilet)
                            + (d.service_daily) / 10) * 20)
                        + (d.helper_exp * 15)
                        + (d.cert_score * 5)
                        + (d.wage_score * 5)) as total_score,
                    d.location_score,	-- 0~1 / 4로 나눔
                    d.time_score,		-- 0~1 / 주 단위 요청 시간으로 나눔
                    d.date_score,		-- 0~1 / 7로 나눔
                    d.work_type,		-- 0~1 / 선택된 개수로 나눔
                    d.welfare,			-- 0~1 / 선택된 개수로 나눔
                    d.care_level,		-- 0~1 / 선택된 개수로 나눔
                    d.dementia_symptom,	-- 0~1 / 선택된 개수로 나눔
                    d.inmate_state,		-- 0~1 / 선택된 개수로 나눔
                    d.gender,			-- 0~1 / 선택된 개수로 나눔
                    d.service_meal,		-- 0~1 / 선택된 개수로 나눔
                    d.service_mobility,	-- 0~1 / 선택된 개수로 나눔
                    d.service_toilet,	-- 0~1 / 선택된 개수로 나눔
                    d.service_daily,		-- 0~1 / 선택된 개수로 나눔
                    d.wage_score,		-- 0,1 / 계산된 시금,주급,일급 기준으로 해당 금액보다 적거나 같으면 1, 아니면 0
                    d.helper_exp,		-- 0,1 / 신규:0 경력:1
                    d.cert_score		-- 0~1 / 4로 나눔
                from (select
                    c.*,
                    th2.helper_exp,
                    ((case when th2.helper_post_partum_cert_no is not null then 1 else 0 end +
                    case when th2.helper_nurse_cert_no is not null then 1 else 0 end +
                    case when th2.helper_other_certs is not null then 1 else 0 end +
                    case when th2.helper_care_cert_no is not null then 1 else 0 end)/4) as cert_score,
                    ((case
                        when th2.helper_wage_state = 1 then th2.helper_wage <= tpl.pl_time_wage
                        when th2.helper_wage_state = 2 then th2.helper_wage <= tpl.pl_day_wage
                        else th2.helper_wage <= tpl.pl_week_wage end) + th2.helper_wage_negotiation) as wage_score,
                    case when (tpl.care_seq_work_type = 0) then 1 else (bit_count(th2.care_seq_work_type & tpl.care_seq_work_type) / bit_count(tpl.care_seq_work_type)) end as work_type,
                    case when (tpl.care_seq_welfare = 0) then 1 else (bit_count(th2.care_seq_welfare & tpl.care_seq_welfare) / bit_count(tpl.care_seq_welfare)) end as welfare,
                    case when (tpl.care_seq_care_level = 0) then 1 else (bit_count(th2.care_seq_care_level & tpl.care_seq_care_level) / bit_count(tpl.care_seq_care_level)) end as care_level,
                    case when (tpl.care_seq_dementia_symptom = 0) then 1 else (bit_count(th2.care_seq_dementia_symptom & tpl.care_seq_dementia_symptom) / bit_count(tpl.care_seq_dementia_symptom)) end as dementia_symptom,
                    case when (tpl.care_seq_inmate_state = 0) then 1 else (bit_count(th2.care_seq_inmate_state & tpl.care_seq_inmate_state) / bit_count(tpl.care_seq_inmate_state)) end as inmate_state,
                    case when (tpl.care_seq_gender = 0) then 1 else (bit_count(th2.care_seq_gender & tpl.care_seq_gender) / bit_count(tpl.care_seq_gender)) end as gender,
                    case when (tpl.care_seq_service_meal = 0) then 1 else (bit_count(th2.care_seq_service_meal & tpl.care_seq_service_meal) / bit_count(tpl.care_seq_service_meal)) end as service_meal,
                    case when (tpl.care_seq_service_mobility = 0) then 1 else (bit_count(th2.care_seq_service_mobility & tpl.care_seq_service_mobility) / bit_count(tpl.care_seq_service_mobility)) end as service_mobility,
                    case when (tpl.care_seq_service_toilet = 0) then 1 else (bit_count(th2.care_seq_service_toilet & tpl.care_seq_service_toilet) / bit_count(tpl.care_seq_service_toilet)) end as service_toilet,
                    case when (tpl.care_seq_service_daily = 0) then 1 else (bit_count(th2.care_seq_service_daily & tpl.care_seq_service_daily) / bit_count(tpl.care_seq_service_daily)) end as service_daily
                from (select
                        coalesce(a.helper_seq, b.helper_seq) as helper_seq,
                        coalesce(a.location_score, 0) as location_score,
                        coalesce(b.time_score, 0) as time_score,
                        coalesce(b.date_score, 0) as date_score
                    from (
                        select
                            sub.helper_seq,
                            ((case when max(sub.af_match) then 1 else 0 end +
                            case when max(sub.af_match) and max(sub.as_match) then 1 else 0 end +
                            case when max(sub.af_match) and max(sub.as_match) and max(sub.at_match) then 1 else 0 end +
                            case when max(sub.af_match) and max(sub.as_match) and max(sub.fin_match) then 1 else 0 end)/4) as location_score
                        from (
                            select thwl.helper_seq,
                                exists (select 1 from tbl_patient_log tp where tp.pl_seq = :plSeq and tp.af_seq = thwl.af_seq) as af_match,
                                exists (select 1 from tbl_patient_log tp where tp.pl_seq = :plSeq and tp.as_seq = thwl.as_seq or thwl.as_seq = tp.af_seq * 1000) as as_match,
                                exists (select 1 from tbl_patient_log tp where tp.pl_seq = :plSeq and tp.at_seq = thwl.at_seq or thwl.at_seq = tp.as_seq * 1000) as at_match,
                                exists (select 1 from tbl_patient_log tp where tp.pl_seq = :plSeq and tp.at_seq = thwl.at_seq) as fin_match
                            from tbl_helper_work_location thwl
                        ) as sub
                        group by sub.helper_seq
                        having max(af_match) = 1
                    ) as a
                    left join (
                        select
                            thwt.helper_seq,
                            th.helper_time_negotiation,
                            sum(case
                                when greatest(thwt.hwt_start_time, tpt.ptl_start_time) <= least(thwt.hwt_end_time, tpt.ptl_end_time)
                                then floor(timestampdiff(second,
                                    greatest(thwt.hwt_start_time, tpt.ptl_start_time),
                                    least(thwt.hwt_end_time, tpt.ptl_end_time)) / 60) /
                                    (timestampdiff(second, tpt.ptl_start_time, tpt.ptl_end_time) / 60)
                                else 0
                            end) as time_score,
                            (count(case
                                when greatest(thwt.hwt_start_time, tpt.ptl_start_time) <= least(thwt.hwt_end_time, tpt.ptl_end_time)
                                then 1
                                else null
                            end)/7) as date_score
                        from tbl_helper_work_time thwt
                        join tbl_helper th on th.helper_seq = thwt.helper_seq
                        join tbl_patient_time_log tpt on tpt.pl_seq = :plSeq
                        and (
                            th.helper_time_negotiation = 1
                            or (thwt.hwt_date = tpt.ptl_date
                                and thwt.hwt_end_time >= tpt.ptl_start_time
                                and thwt.hwt_start_time <= tpt.ptl_end_time)
                        )
                        group by thwt.helper_seq, th.helper_time_negotiation
                    ) as b
                    on a.helper_seq = b.helper_seq) as c
                join tbl_helper th2 on th2.helper_seq = c.helper_seq
                join tbl_patient_log tpl on tpl.pl_seq = :plSeq) as d
                order by d.location_score, total_score desc
                limit 10
            """, nativeQuery = true)
    List<MatchingResponseDto.Score> findTop10HelperScores(@Param("plSeq") Long plSeq);
}
