package com.sp.app.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class ChatBotService {
	private final ChatClient chatClient;
	private final VectorStore vectorStore;
	
	public ChatBotService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		this.chatClient = chatClientBuilder.build();
		this.vectorStore = vectorStore;
	}
	
	public Flux<String> generateAnswer(String question) {
		try {
	    	// Fetch similar movies using vector store
	        List<Document> results = vectorStore.similaritySearch(
	        		SearchRequest.builder()
	        		.query(question)
	        		.similarityThreshold(0.5)
	        		.topK(1)
	        		.build()
	        );
	        System.out.println(results);

	        String template = """
	        		당신은 어느 호텔 직원입니다. 문맥에 따라 고객의 질문에 정중하게 답변해 주십시오. 
	        		컨텍스트가 질문에 대답할 수 없는 경우 '모르겠습니다'라고 대답하세요.
	                               
	        		컨텍스트:
	        		{context}
	        		질문: 
	        		{question}
	                 
	        		답변:
	        	""";

	        return chatClient.prompt()
	        		.user(promptUserSpec -> promptUserSpec.text(template)
	        				.param("context", results)
	        				.param("question", question))
	        		.stream()
	        		.content();
	        
		} catch (Exception e) {
			return Flux.error(e);
		}
		
	}
}
