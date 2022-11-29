package com.shopping.repository;

import com.shopping.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> { // CartRepository01

    // CartRepository02) 로그인 한 회원의 카트 정보를 조회합니다.
    // 회원과 카트는 일대일 연관 관계를 맺고 있습니다.
    Cart findByMemberId(Long memberId);
}
