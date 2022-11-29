package com.shopping.service;

import com.shopping.constant.OrderStatus;
import com.shopping.constant.ProductStatus;
import com.shopping.dto.OrderDto;
import com.shopping.entity.Member;
import com.shopping.entity.Order;
import com.shopping.entity.OrderProduct;
import com.shopping.entity.Product;
import com.shopping.repository.MemberRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService; // OrderServiceTest01

    @Autowired
    private OrderRepository orderRepository; // OrderServiceTest01

    @Autowired
    ProductRepository productRepository; // OrderServiceTest01

    @Autowired
    MemberRepository memberRepository; // OrderServiceTest01

    public Product saveProduct(){ // 주문할 상품 정보 // OrderServiceTest01
        Product product = new Product();
        product.setName("사과");
        product.setPrice(1000);
        product.setDescription("너무 너무 아삭해요.^^");
        product.setProductStatus(ProductStatus.SELL);
        product.setStock(100);
        return productRepository.save(product);
    }

    public Member saveMember(){ // 주문자 정보 // OrderServiceTest01
        Member member = new Member();
        member.setEmail("gomdori@naver.com");
        return memberRepository.save(member);

    }

    @Test // OrderServiceTest01
    @DisplayName("주문 테스트")
    public void order(){ // OrderServiceTest01
        Product product = saveProduct();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10); // 주문 수량
        orderDto.setProductId(product.getId()); // 주문할 상품

        Long orderId = orderService.order(orderDto, member.getEmail()); // 주문 번호

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderProduct> orderProducts = order.getOrderProducts();

        // 총가격 = 주문 수량 * 단가
        int totalPrice = orderDto.getCount()*product.getPrice();

        // 주문한 상품들의 총가격과 데이터 베이스 내의 상품 가격을 비교합니다.
        assertEquals(totalPrice, order.getTotalPrice());

        System.out.println(member.toString());
        System.out.println(orderDto.toString());
        System.out.println(order.toString());
    }

    @Test // OrderServiceTest02
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){
        Product product = saveProduct() ; // 상품 객체 생성
        Member member = saveMember() ; // 회원 객체 생성

        OrderDto orderDto = new OrderDto() ;
        orderDto.setCount(10);
        orderDto.setProductId(product.getId());

        // 주문 데이터 생성하기
        Long orderId = orderService.order(orderDto, member.getEmail()) ;

        // 주문 Entity 조회하기
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new) ;
        orderService.cancelOrder(orderId); // 주문 취소하기

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus()); // 주문 상태 체크
        assertEquals(100, product.getStock()) ; // 재고 수량 체크
    }

}
