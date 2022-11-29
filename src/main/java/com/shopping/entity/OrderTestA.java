package com.shopping.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

@SpringBootTest
//@Transactional
public class OrderTestA extends EntityMapping{
    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();
        int data_length = 3 ;// 주문할 아이템 개수

        for (int i = 0; i < data_length; i++) {
            Product product = super.createProduct( i );
            super.productRepository.save(product) ;

            OrderProduct orderProduct = new OrderProduct() ;
            orderProduct.setProduct(product);
            orderProduct.setCount(10);
            orderProduct.setOrderPrice(10000);
            orderProduct.setOrder(order);

            order.getOrderProducts().add(orderProduct) ;
        }
        // 강제로 Flush 명령어를 호출하여 PersistenceContext의 내용을 Database에 반영시킵니다.
        super.orderRepository.saveAndFlush(order) ;

        // PersistenceContext의 내용을 초기화  상태로 변경합니다.
        em.clear();

        // order) 저장할 주문 정보를 담고 있는 객체
        // savedOrder) 실제 데이터베이스에 저장된 주문 객체
        Order savedOrder = super.orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new) ;

        // 상품 개수와 주문 정보의 상품 개수를 비교합니다.
        Assertions.assertEquals(data_length, savedOrder.getOrderProducts().size());
    }
}









