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
	        	    당신은 종합 여행 및 숙소 예약 플랫폼 'Tripan'의 친절하고 전문적인 고객 지원 챗봇입니다. 
	        	    아래에 제공된 [컨텍스트] 정보만을 바탕으로 사용자의 [질문]에 정중하고 알기 쉽게 답변해 주십시오.
	        	    
	        	    답변 시 다음 규칙을 엄격하게 지켜주세요:
	        	    1. 질문에 대한 답이 [컨텍스트]에 없다면, 절대 정보를 지어내지 말고 "죄송합니다. 제공된 정보에서는 해당 내용을 찾을 수 없습니다. Tripan 고객센터로 문의해 주시면 감사하겠습니다."라고 대답하세요.
	        	    2. 답변은 명확하고 간결하게 작성하며, 사용자가 이해하기 쉽도록 안내하세요.
	        	                       
	        	    [컨텍스트]:
	        	    {context}
	        	    
	        	    [질문]: 
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
