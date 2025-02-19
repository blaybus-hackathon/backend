package com.balybus.galaxy.helper.repositoryImpl.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperExperience;
import com.balybus.galaxy.helper.dto.request.HelperSearchRequest;
import com.balybus.galaxy.helper.repositoryImpl.HelperRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HelperRepositoryCustomImpl implements HelperRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TblHelper> searchHelpers(HelperSearchRequest request) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TblHelper> cq = cb.createQuery(TblHelper.class);
        Root<TblHelper> root = cq.from(TblHelper.class);

        List<Predicate> predicates = new ArrayList<>();

        /*──────────────────────────────────────────────
         * 1. 성별 필터링 (genders)
         * - TblHelper.gender 는 Integer (0: 남자, 1: 여자)
         *──────────────────────────────────────────────*/
        if (request.getGenders() != null && !request.getGenders().isEmpty()) {
            CriteriaBuilder.In<Integer> inGender = cb.in(root.get("gender"));
            for (String genderStr : request.getGenders()) {
                if ("남자".equals(genderStr)) {
                    inGender.value(0);
                } else if ("여자".equals(genderStr)) {
                    inGender.value(1);
                }
            }
            predicates.add(inGender);
        }

        /*──────────────────────────────────────────────
         * 2. 나이 필터링 (ages)
         * - TblHelper에는 나이 필드가 없으므로, birthday(문자열, "yyyy-MM-dd")
         *   를 Date로 변환한 후 현재 날짜와의 연도 차이를 계산합니다.
         * - MySQL의 STR_TO_DATE와 TIMESTAMPDIFF 함수를 사용합니다.
         *──────────────────────────────────────────────*/
        if (request.getAges() != null && !request.getAges().isEmpty()) {
            // ageExpr = TIMESTAMPDIFF(YEAR, STR_TO_DATE(birthday, '%Y-%m-%d'), CURDATE())
            Expression<Integer> ageExpr = cb.function(
                    "TIMESTAMPDIFF",
                    Integer.class,
                    cb.literal("YEAR"),
                    cb.function("STR_TO_DATE", java.sql.Date.class, root.get("birthday"), cb.literal("%Y-%m-%d")),
                    cb.currentDate()
            );
            List<Predicate> agePredicates = new ArrayList<>();
            for (String ageRange : request.getAges()) {
                if ("20대".equals(ageRange)) {
                    agePredicates.add(cb.between(ageExpr, 20, 29));
                } else if ("30대".equals(ageRange)) {
                    agePredicates.add(cb.between(ageExpr, 30, 39));
                } else if ("40대".equals(ageRange)) {
                    agePredicates.add(cb.between(ageExpr, 40, 49));
                } else if ("50대 이상".equals(ageRange)) {
                    agePredicates.add(cb.greaterThanOrEqualTo(ageExpr, 50));
                }
            }
            if (!agePredicates.isEmpty()) {
                // 조건 중 하나라도 만족하면 되도록 OR 처리
                predicates.add(cb.or(agePredicates.toArray(new Predicate[0])));
            }
        }

        /*──────────────────────────────────────────────
         * 3. 경력 필터링 (experiences)
         * - TblHelperExperience 엔티티에 경력 정보가 있으므로,
         *   Helper와 연관된 경험 기록을 통해 필터링합니다.
         * - "신입": 해당 Helper의 경험 기록이 없어야 함.
         * - "1년", "3~5년", "5년 이상": TIMESTAMPDIFF(YEAR, he_start_date, he_end_date)를 이용
         *──────────────────────────────────────────────*/
        if (request.getExperiences() != null && !request.getExperiences().isEmpty()) {
            // 만약 "신입"만 선택된 경우: 해당 Helper에 경험 기록이 없어야 함.
            if (request.getExperiences().size() == 1 && request.getExperiences().contains("신입")) {
                // 서브쿼리를 이용하여 경험 기록이 없는 Helper를 찾습니다.
                Subquery<TblHelperExperience> subquery = cq.subquery(TblHelperExperience.class);
                Root<TblHelperExperience> expRoot = subquery.from(TblHelperExperience.class);
                subquery.select(expRoot);
                subquery.where(cb.equal(expRoot.get("helper"), root));
                predicates.add(cb.not(cb.exists(subquery)));
            } else {
                // "신입" 이외의 조건 처리 (예: "1년", "3~5년", "5년 이상")
                List<Predicate> expDurationPredicates = new ArrayList<>();
                // 서브쿼리: TblHelperExperience와의 연관 관계가 있고, 기간 조건을 만족하는지 체크
                Subquery<TblHelperExperience> subquery = cq.subquery(TblHelperExperience.class);
                Root<TblHelperExperience> expRoot = subquery.from(TblHelperExperience.class);
                // Helper와 매칭되는 조건
                Predicate helperMatch = cb.equal(expRoot.get("helper"), root);
                // 각 경험 조건에 대해 TIMESTAMPDIFF(YEAR, he_start_date, he_end_date)
                // 주의: 데이터베이스가 MySQL임을 전제로 합니다.
                Expression<Integer> durationExpr = cb.function(
                        "TIMESTAMPDIFF",
                        Integer.class,
                        cb.literal("YEAR"),
                        expRoot.get("he_start_date"),
                        expRoot.get("he_end_date")
                );
                for (String expCond : request.getExperiences()) {
                    if ("1년".equals(expCond)) {
                        expDurationPredicates.add(cb.equal(durationExpr, 1));
                    } else if ("3~5년".equals(expCond)) {
                        expDurationPredicates.add(cb.between(durationExpr, 3, 5));
                    } else if ("5년 이상".equals(expCond)) {
                        expDurationPredicates.add(cb.greaterThanOrEqualTo(durationExpr, 5));
                    }
                }
                if (!expDurationPredicates.isEmpty()) {
                    Predicate durationOr = cb.or(expDurationPredicates.toArray(new Predicate[0]));
                    subquery.select(expRoot);
                    subquery.where(helperMatch, durationOr);
                    // Helper가 위 서브쿼리 조건을 만족하는 경험 기록을 가지고 있어야 함
                    predicates.add(cb.exists(subquery));
                }
            }
        }

        // 4. 근무기간(terms) 필터링
        // 가정: TblHelper에 term (Integer) 필드가 존재하며, 단위는 예를 들어 개월 또는 연 단위
        // 예: "6개월"은 6, "1년"은 12, "2년 이상"은 24 이상
        if (request.getTerms() != null && !request.getTerms().isEmpty()) {
            List<Predicate> termPredicates = new ArrayList<>();
            for (String term : request.getTerms()) {
                if ("6개월".equals(term)) {
                    termPredicates.add(cb.equal(root.get("term"), 6));
                } else if ("1년".equals(term)) {
                    termPredicates.add(cb.equal(root.get("term"), 12));
                } else if ("2년 이상".equals(term)) {
                    termPredicates.add(cb.greaterThanOrEqualTo(root.get("term"), 24));
                }
            }
            if (!termPredicates.isEmpty()) {
                predicates.add(cb.or(termPredicates.toArray(new Predicate[0])));
            }
        }

        // 최종 where 절: 각 카테고리별 조건은 AND로 모두 만족해야 함
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<TblHelper> query = em.createQuery(cq);
        return query.getResultList();
    }
}
