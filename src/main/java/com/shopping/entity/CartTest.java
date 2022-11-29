package com.shopping.entity;

import com.shopping.dto.MemberFormDto;
import com.shopping.repository.CartRepository;
import com.shopping.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class CartTest { // CartTest01
    @Autowired
    MemberRepository memberRepository ; // CartTest01

    @Autowired
    CartRepository cartRepository ; // CartTest01

    @PersistenceContext // CartTest01
    EntityManager em ; // 엔터티 관리자

    @Test
    @DisplayName("장바구니 회원 엔터티 매핑 조회 테스트")
    public void findCartAndMemberTest(){ // CartTest01
        Member member = this.createMember() ;
        this.memberRepository.save(member) ;
        Cart cart = new Cart() ;
        cart.setMember(member);
        this.cartRepository.save(cart) ;

        em.flush(); // PersistenceContext의 내용을 데이터 베에스에 저장합니다.
        em.clear(); // PersistenceContext의 내용을 모두 비워 줍니다.

        Cart savedCart = this.cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new) ;

        System.out.println("savedCart Information");
        this.ShowCartInfo(savedCart);

        // 회원의 아이디 비교
        Assertions.assertEquals(savedCart.getMember().getId(), member.getId());
    }

    @Autowired // in SecurityConfig.java file
    PasswordEncoder passEncoder ; // CartTest01

    private Member createMember() { // CartTest01
        MemberFormDto dto = new MemberFormDto();
        dto.setEmail("abcd@naver.com");
        dto.setPassword("1234");
        dto.setName("김만식");
        dto.setAddress("금천구 가산동");
        return Member.createMember(dto, passEncoder) ;
    }

    private void ShowCartInfo(Cart savedCart){ // CartTest01
        System.out.println("카트 아이디 : " + savedCart.getId());
        System.out.println("회원 아이디 : " + savedCart.getMember().getId());
        System.out.println("이름 : " + savedCart.getMember().getName());
        System.out.println("이메일 : " + savedCart.getMember().getEmail());
        System.out.println("주소 : " + savedCart.getMember().getAddress());
        System.out.println("비번 : " + savedCart.getMember().getPassword());
        System.out.println("Role : " + savedCart.getMember().getRole());
    }

}
