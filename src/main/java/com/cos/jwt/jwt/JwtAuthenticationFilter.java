package com.cos.jwt.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		try {
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨
			// 데이터베이스에 있는 username과 password가 일치한다
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			// 로그인이 되었다는 뜻.
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println(principalDetails.getUser().getUsername()); // 로그인이 정상적으로 되었다는 뜻
			
			// authentication 객체가 session 영역에 저장을 해야 하고 그 방법이 return 해주면됨
			// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는것
			// 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 단지 권한 처리 때문에 session을 넣어주는것
			// 다만 여기서 JWT 토큰을 만들 필요는 없는게 이 다음에 successfulAuthentication이 실행되므로 거기서 만드는것이 좋음
			return authentication;
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 2. 정상인지 로그인 시도 함. authenticationManager로 로그인 시도를 하면, 
		// PrincipalDetailsService가 호출됨. loadUserByUsername() 함수 실행됨
		
		
		// 3. PrincipalDetails를 세션에 담고 (담는 이유: 권한 관리를위해서)
		
		
		// 4. JWT 토큰을 만들어서 응답해주면 됨.
		
		return null;
	}
	
	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행되므로
	// 여기서 JWT 토큰을 만들어서 request 요청한 사용자에게 response 해주면됨
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 실행됨. 인증 완료!");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		// HASH 암호 방식
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername()) // 토큰 이름
				.withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 만료 시간 설정
				.withClaim("id", principalDetails.getUser().getId()) 
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("cos"));
		
		response.addHeader("Authorization", new StringBuilder().append("Bearer").append(" ").append(jwtToken).toString());
	}
}
