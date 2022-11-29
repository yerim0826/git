package com.shopping.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class ProductDto {
    private Long id ;
    private String name ;
    private int price ;
    private String description ;
    private String sellStatCd ;
    private LocalDateTime regDate ;
    private LocalDateTime updateDate ;
}
