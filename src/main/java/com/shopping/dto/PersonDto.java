package com.shopping.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//PersonDto 클래스를 이용하여 thymeleaf 코드를 작성해 보세요.
@Getter @Setter
public class PersonDto {
    private Long id ;
    private String name ;
    private Integer age ;
    private String address ;
    private String gender;
    private LocalDateTime regTime ;
}
