package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  //내 서버가 응답할때 json 을 자바스크립에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*"); //모든 ip에 응답을 허용
        config.addAllowedHeader("*"); //모든 header에 응답을 허용
        config.addAllowedMethod("*"); // 모든 POST, GET , DELETE PATCH... 모든 요청 허용
        source.registerCorsConfiguration("/api/**", config);   // 해당 URL 들은 이 config 를 수행한다
        return new CorsFilter(source);
    }

}