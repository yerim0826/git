package com.shopping.controller;

import com.shopping.dto.MemberFormDto;
import com.shopping.entity.Member;
import com.shopping.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor // for MemberController02
@RequestMapping(value = "/members")
public class MemberController {
    private final String urlPrefix = "/member" ; // MemberController01

    @GetMapping(value = "/new")
    public String insertForm(Model model){ // MemberController01
        System.out.println(this.getClass() + " get called");
        model.addAttribute("memberFormDto", new MemberFormDto());
        return urlPrefix + "/meInsertForm" ;
    }

    // for MemberController02
    private final MemberService memberService ;
    private final PasswordEncoder passEncoder ;

    // @Valid는 command 객체에 유효성 검사를 진행해 줍니다.
    // BindingResult는 유효성 검사에 문제가 있으면, 해당 정보가 들어 있습니다.
    @PostMapping(value = "/new")
    public String insertForm(@Valid MemberFormDto dto, BindingResult error, Model model){ // MemberController02
        System.out.println(this.getClass() + " post called");
        if(error.hasErrors()){ // 유효성 검사를 충족하지 못하면 다시 가입 페이지로 이동
            return urlPrefix + "/meInsertForm" ;
        }

        try{
            Member member = Member.createMember(dto, passEncoder) ;
            this.memberService.saveMember(member) ;

        }catch (IllegalStateException err){
            model.addAttribute("errorMessage", err.getMessage());
            return urlPrefix + "/meInsertForm" ;
        }

        // 메인 페이지로 이동
        return "redirect:/" ; // response.sendRedirect() ;
    }

    // MemberController03
    // form 태그와 SecurityConfig 파일에 정의 되어 있습니다.
    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm" ;
    }

    // MemberController03
    // SecurityConfig 파일에 정의 되어 있습니다.
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        // "loginErrorMsg" 내용은 memberLoginForm.html 파일 내에 구현되어 있습니다.
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀 번호를 확인해 주세요.") ;
        return "/member/memberLoginForm" ;
    }

}
