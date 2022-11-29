package com.shopping.repository;

import com.shopping.entity.Saram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaramRepository extends JpaRepository<Saram, String>, QuerydslPredicateExecutor<Saram> {
    // 이름 순으로 정렬하기
    List<Saram> findByOrderByNameAsc() ;

    // 거주지가 '마포'인 사람만 조회하기
    List<Saram> findByAddressEquals(String address) ;

    // 급여가 높은 순으로 정렬하기
    List<Saram> findByOrderBySalaryDesc() ;

    @Query(value = "select sa from Saram sa where sa.salary >= :salary ")
    List<Saram> findBySalaryGreaterThan01(@Param("salary") Integer salary);

    @Query(value = "select * from sarams sa where sa.salary >= :salary ", nativeQuery = true)
    List<Saram> findBySalaryGreaterThan02(@Param("salary") Integer salary);
}
