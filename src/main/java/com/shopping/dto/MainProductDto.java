package com.shopping.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainProductDto { // MainProductDto01
    private Long id ;
    private String name ;
    private String description ;
    private String imageUrl ;
    private Integer price ;

    // projection이란 테이블의 특정한 컬럼 정보들을 조회하는 동작을 의미합니다.
    // 결과 값을 Dto로 만들고자 할 때, 이 어노테이션을 사용하면 됩니다.
    @QueryProjection
    public MainProductDto(Long id, String name, String description, String imageUrl, Integer price) { // MainProductDto01
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }
}
