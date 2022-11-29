package com.shopping.repository;

import com.shopping.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> { // OrderRepository01
    // OrderRepository02) 주문 데이터를 최근 주문부터 먼저 보여 줍니다.
    @Query(" select o from Order o" +
            " where o.member.email = :email" +
            " order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    // OrderRepository02) 이 회원의 주문 개수 구하기
    @Query(" select count(o) from Order o" +
            " where o.member.email = :email")
    Long countOrder(@Param("email") String email) ;
}
