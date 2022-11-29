package com.shopping.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

// cf) VO(Value Object), Java Bean
// DTO(Data Transfer Object)
// 회원 가입 화면(<form> 태그)에서 넘겨지는(tranfer) 파라미터를 저장할 Dto 클래스
@Getter @Setter
public class MemberFormDto {
    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name ;

    @NotEmpty(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 메일 형식이 아닙니다.")
    private String email ;

    @NotEmpty(message = "비밀 번호는 필수 입력 사항입니다.")
    @Length(min = 8, max = 16, message = "비밀 번호는 8자리 이상 16자리 이하로 입력해 주세요.")
    private String password ;

    @NotEmpty(message = "주소는 필수 입력 사항입니다.")
    private String address ;
}
