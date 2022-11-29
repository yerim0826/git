package com.shopping.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

// AuditorAware) 감사(Audit) 정보를 캡쳐하기 위하여 JPA가 지원하는 인터페이스
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // Authentication) 현재 인증 받은 사용자의 인증 정보가 들어 있는 인터페이스
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "" ;
        if(authentication != null){
            userId = authentication.getName() ;
        }
        return Optional.of(userId) ;
    }
}
