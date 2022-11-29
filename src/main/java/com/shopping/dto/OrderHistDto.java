package com.shopping.dto;

import com.shopping.constant.OrderStatus;
import com.shopping.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderHistDto { // OrderHistDto01
    private Long orderId ; // 주문 아이디(송장 번호)
    private String orderDate ; // 주문 일자
    private OrderStatus orderStatus ; // 주문 상태

    // 이번에 주문할 때 구매한 상품 정보들 // OrderHistDto01
    private List<OrderProductDto> orderProductDtoList = new ArrayList<>();

    public void addOrderProductDto(OrderProductDto dto){ // OrderHistDto01
        orderProductDtoList.add(dto) ;
    }

    public OrderHistDto(Order order) { // OrderHistDto01
        this.orderId = order.getId() ;

       String pattern = "yyyy-MM-dd HH:mm" ; // 주문 일자 보여 주는 형식 지정정
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern) ;
       this.orderDate = order.getOrderDate().format(formatter) ;
       this.orderStatus = order.getOrderStatus() ;
    }
}
