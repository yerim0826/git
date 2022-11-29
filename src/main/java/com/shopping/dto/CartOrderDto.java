package com.shopping.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto { // CartOrderDto01
    private Long cartProductId ;

    // 사용자가 장바구니 목록에서 동시에 여러 개를 주문할 수 있으므로, 리스트 컬렉션으로 선언합니다.
    private List<CartOrderDto> cartOrderDtoList ;
}
