package com.shopping.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@SpringBootTest
@Transactional
public class OrderTestC extends EntityMapping{
    @Test
    @DisplayName("지연 로딩 테스트")
    public void LazyLoagingTest(){
        // 주문된 상품 중에서 1번째 상품 정보를 조회해 봅니다.
        Order order = super.createOrder() ;

        // 주문 상품 중에서 첫 번째 상품의 아이디
        Long orderProductId = order.getOrderProducts().get(0).getId() ;

        super.em.flush(); // PersistenceContext의 내용을 database에 반영합니다.
        super.em.clear(); // PersistenceContext의 상태를 초기화 시킵니다.

        OrderProduct orderProduct = super.orderProductRepository.findById(orderProductId)
                .orElseThrow(EntityNotFoundException::new) ;
    }
}
