package com.sp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 루트 컨트롤러
@Controller
public class RouteController {
	@GetMapping("/hotel/chatBot")
	public String chatForm() {
		return "hotel/main";
	}
}
