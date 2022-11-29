package com.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {
    @Bean // AuditorAware 구현체를 Bean으로 등록할께요.
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl() ;
    }
}
