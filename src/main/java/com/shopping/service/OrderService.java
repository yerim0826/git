package com.shopping.service;

import com.shopping.dto.OrderDto;
import com.shopping.dto.OrderHistDto;
import com.shopping.dto.OrderProductDto;
import com.shopping.entity.*;
import com.shopping.repository.MemberRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.ProductImageRepository;
import com.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService { // OrderService01
    private final ProductRepository productRepository ; // OrderService01
    private final MemberRepository memberRepository ; // OrderService01
    private final OrderRepository orderRepository ; // OrderService01

    public Long order(OrderDto orderDto, String email){ // OrderService01
        // orderDto 객체에는 상품의 아이디와 구매 수량 정보가 들어 있습니다.

        // 상품 아이디를 이용하여 해당 상품 정보를 구합니다.
        Product product = this.productRepository.findById(orderDto.getProductId())
                .orElseThrow(EntityNotFoundException::new) ;

        // 로그인한 이메일 정보를 이용하여 회원 정보를 구합니다.
        Member member = this.memberRepository.findByEmail(email) ;

        // 상품 Entity와 주문 수량을 이용하여 OrderProduct에 대한 Entity를 생성합니다.
        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, orderDto.getCount()) ;

        List<OrderProduct> orderProductList = new ArrayList<>( );
        orderProductList.add(orderProduct) ;

        // 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문(Order) Entity를 생성합니다.
        Order order = Order.createOrder(member, orderProductList) ;
        orderRepository.save(order) ;

        return order.getId() ; // 송장 번호(invoice number)
    }

    // OrderService02) 사용자의 이메일과 페이징 조건을 이용하여 주문 목록을 조회합니다.
    private final ProductImageRepository productImageRepository ;

    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){ // OrderService02)
        // orders) 주문 목록
        List<Order> orders = this.orderRepository.findOrders(email, pageable);

        // 주문 내역 개수
        Long totalCount = this.orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>() ;

        for(Order order : orders){ // 주문 목록 반복
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderProduct> orderProducts = order.getOrderProducts() ;

            for(OrderProduct bean : orderProducts){
                ProductImage productImage
                        = this.productImageRepository.findByProductIdAndRepImageYesNo(bean.getProduct().getId(), "Y") ;

                OrderProductDto beanDto = new OrderProductDto(bean, productImage.getImageUrl());
                orderHistDto.addOrderProductDto(beanDto);
            }

            orderHistDtos.add(orderHistDto) ;
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    // OrderService03) 로그인한 사람과 상품 주문자의 이메일 주소가 동일한지 검사합니다.
    public boolean validateOrder(Long orderId, String email){
        Member member = this.memberRepository.findByEmail(email) ; // 로그인 한 사람

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new) ;

        Member savedMember = order.getMember() ; // 주문자 정보

        if(StringUtils.equals(member.getEmail(), savedMember.getEmail())){
            return true ;
        }   else{
            return false ;
        }
    }

    // OrderService03) // 주문 취소 상태로 변경합니다.
    public void cancelOrder(Long orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new) ;
        order.cancelOrder();
    }

    // OrderService04) 장바구니 목록에서 체크된 상품 데이터들을 입력 받아서, 주문 1건을 생성시키는 로직을 작성합니다.
    public Long orders(List<OrderDto> orderDtoList, String email){
        Member member = this.memberRepository.findByEmail(email);

        // 주문하고자 하는 상품 리스트
        List<OrderProduct> orderProductList = new ArrayList<>() ;

        for(OrderDto bean : orderDtoList){
            Long productId = bean.getProductId() ;
            Product product = this.productRepository.findById(productId)
                            .orElseThrow(EntityNotFoundException::new) ;

            int count = bean.getCount() ;
            OrderProduct orderProduct = OrderProduct.createOrderProduct(product, count) ;
            orderProductList.add(orderProduct);
        }
        // 로그인 한 사람의 정보와 주문 상품 목록으로 주문 Entity를 생성합니다.
        Order order = Order.createOrder(member, orderProductList) ;
        this.orderRepository.save(order) ; // 주문 데이터 저장하기

        return order.getId();
    }
}
