package com.shopping.repository;

import com.shopping.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// MemberRepository01
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일을 이용하여 회원 검색을 합니다.
    // 회원 가입시 중복 체크를 위한 용도로 사용될 예정입니다.
    Member findByEmail(String email) ;
}
