package com.shopping.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

// 상품 상세 페이지에서 넘겨 지는 상품의 아이디와 주문 수량을 처리해주는 dto 클래스
@Getter @Setter
public class OrderDto { // OrderDto01
    @NotNull(message = "상품 아이디는 필수 입력 사항입니다.")
    private Long productId ;

    @Min(value = 1, message = "최소 주문 수량은 1개입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개입니다.")
    private int count ;
}
