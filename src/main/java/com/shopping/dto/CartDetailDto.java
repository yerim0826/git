package com.shopping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartDetailDto { // CartDetailDto01
    private Long cartProductId ; // 장바구니 상품 아이디
    private String name ; // 상품명
    private int price ; // 금액
    private int count ; // 구매 수량
    private String imageUrl ; // 상품 이미지

    public CartDetailDto(Long cartProductId, String name, int price, int count, String imageUrl) { // CartDetailDto01
        this.cartProductId = cartProductId;
        this.name = name;
        this.price = price;
        this.count = count;
        this.imageUrl = imageUrl;
    }
}
