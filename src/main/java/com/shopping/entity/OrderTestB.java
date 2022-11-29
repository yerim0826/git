package com.shopping.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderTestB extends EntityMapping{
    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = super.createOrder();
        order.getOrderProducts().remove(0) ; // 0번째 요소 제거하기
        super.em.flush(); // PersistenceContext에 들어 있는 내용을 database에 반영해 줍니다.
    }
}
