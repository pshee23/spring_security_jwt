package com.cos.jwt.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// login 요청해서 username, password 전송(post)하면 UsernamePasswordAuthenticationFilter가 동작함
// formLogin을 disable 했기 때문에 login 접속이 안되어서 따로 설정해야됨
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter called");
		
		// 1. username, password 받아서
		
		// 2. 정상인지 로그인 시도 함. authenticationManager로 로그인 시도를 하면, 
		// PrincipalDetailsService가 호출됨. loadUserByUsername() 함수 실행됨
		
		
		// 3. PrincipalDetails를 세션에 담고 (담는 이유: 권한 관리를위해서)
		
		
		// 4. JWT 토큰을 만들어서 응답해주면 됨.
		
		return super.attemptAuthentication(request, response);
	}
}
