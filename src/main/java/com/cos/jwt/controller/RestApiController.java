package com.cos.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RestApiController {
	
	@GetMapping("home")
	public String home() {
		return "<h1>home<h1>";
	}
	
	@GetMapping("user")
	public String user() {
		return "<h1>user</h1>";
	}
	
	@GetMapping("manager")
	public String manager() {
		return "<h1>manager</h1>";
	}
	
	@PostMapping("token")
	public String token() {
		return "<h1>token</h1>";
	}
}
