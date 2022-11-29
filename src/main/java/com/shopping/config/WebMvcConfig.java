package com.shopping.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// WebMvcConfigurer는 스프링의 MVC를 위하여 자바 기반의 구성 설정을 도와 주는 인터페이스입니다.
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}") // application.properties 파일의 프로터티를 읽어 옵니다.
    String uploadPath ;

    /* addResourceHandler 메소드
      Add handlers to serve static resources such as images, js, and, css files
      from specific locations under web application root, the classpath, and
      others. */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // basePath 형식으로 url을 호출할 때, uploadPath 경로를 토대로 파일을 읽어 들이겠습니다.
        String basePath = "/images/**" ;
        registry.addResourceHandler(basePath).addResourceLocations(uploadPath) ;
    }
}
