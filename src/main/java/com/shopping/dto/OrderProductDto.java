package com.shopping.dto;

import com.shopping.entity.OrderProduct;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter // 주문 상품 1개에 대한 정보를 표현하는 dto 클래스
public class OrderProductDto { // OrderProductDto01
    private String name ; // 상품명
    private int count ; // 주문 수량
    private int orderPrice ; // 주문 금액
    private String imageUrl ; // 상품 이미지

    public OrderProductDto(OrderProduct orderProduct, String imageUrl) { // OrderProductDto01
        this.name = orderProduct.getProduct().getName() ;
        this.count = orderProduct.getCount() ;
        this.orderPrice = orderProduct.getOrderPrice() ;
        this.imageUrl = imageUrl ;
    }
}
