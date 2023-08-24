package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter2 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		// 토큰 : cos 이걸 만들어줘야 함. id, pw 정상적으로 들어와서 로그인이 완료되면, 토큰을 만들어주고 그걸 응답을 해준다. 
		// 요청할 때마다 header에 Authorization에 value값으로 토큰을 가지고 올것임
		// 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면됨. (RSA, HS256)
		if(req.getMethod().equals("POST")) {
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			
			if("cos".equals(headerAuth)) {
				chain.doFilter(req, res);
			} else {
				System.out.println("인증 안됨");
			}
		}
		
		System.out.println("MyFilter2 doFilter.");
		chain.doFilter(req, res);
	}

}
