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
		// STATELESS: Session 기반의 인증기반을 사용하지 않음
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		// Spring Security Custom Filter Load - Form '인증'에 대해서 사용
		.addFilterBefore(new CorsConfig().corsFilter(), UsernamePasswordAuthenticationFilter.class) // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
		.formLogin().disable() // form 기반의 로그인에 대해 비 활성화하며 커스텀으로 구성한 필터를 사용한다.
		.httpBasic().disable() // 로그인 시, 서버는 client에게 세션ID 할당하고, client는 해당 ID를 쿠키에 저장(Session). 
							   // - 하지만 서버가 여러대인 경우 각 서버마다 로그인 세션 저장 공간이 따로 생기게 됨. 
							   // - 쿠키는 동일 도메인일때에만 작동. header cookie를 수정해야 되는데 이러면 서버에서 거부
							   // header의 Authorization에 ID, PW를 담아 보내는 방법도 있음(httpBasic). 확장성은 좋으나 ID,PW가 노출됨
							   // - https로 해서 보내면 숨겨짐. 
							   // Authorization에 Token을 담아 보내는 방법도 있음(Bearer)(JWT). Token의 유효시간을 설정해야지 좋음
		.authorizeRequests()
		.antMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
		.antMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
		.antMatchers("/api/v1/admin/**").hasRole("ADMIN");
		return http.build();
	}
}
