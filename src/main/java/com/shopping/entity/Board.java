package com.shopping.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class Board {
    private Integer no;

    @NotBlank(message = "작성자를 반드시 입력해 주세요.")
    private String writer;

    @NotBlank(message = "글 제목을 반드시 입력해 주세요.")
    private String subject;

    @NotBlank(message = "글 내용을을 반드시 입력 주세요.")
    private String description;
}
