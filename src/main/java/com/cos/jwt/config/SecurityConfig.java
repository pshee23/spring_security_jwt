package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 서버에 인증정보를 저장하지 않기에 csrf를 사용하지 않는다.
		http.csrf().disable();
		// Session 기반의 인증기반을 사용하지 않고 추후 JWT를 이용하여서 인증 예정
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		// Spring Security Custom Filter Load - Form '인증'에 대해서 사용
		.addFilterBefore(new CorsConfig().corsFilter(), UsernamePasswordAuthenticationFilter.class) // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
		.formLogin().disable() // form 기반의 로그인에 대해 비 활성화하며 커스텀으로 구성한 필터를 사용한다.
		.httpBasic().disable()
		.authorizeRequests()
		.antMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
		.antMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
		.antMatchers("/api/v1/admin/**").hasRole("ADMIN");
		return http.build();
	}
}
