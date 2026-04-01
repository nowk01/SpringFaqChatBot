package com.sp.app.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sp.app.service.ChatBotService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatBotRestController {
	private final ChatBotService chatBotService;
	
    @GetMapping("/api/question")
	public Flux<String> handleChat(@RequestParam("question") String question, Model model) throws Exception {
    	return chatBotService.generateAnswer(question);
	}
}
