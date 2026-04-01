package com.sp.app.config;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/*
  - 자동으로 만들어지는 테이블(hotel_vector)의 구조
    컬럼명		: 타입		: 설명
    id			: UUID		: 문서의 고유 식별자
    content		: TEXT		: 실제 호텔 정보 텍스트
    metadata    : JSONB     : 섹션 정보, 키워드, 카테고리
    embedding :	VECTOR(768) : Gemini 모델(768차원)로 변환된 벡터 값
    
   - DBeaver 에서 확인
     SELECT * FROM hotel_vector;
*/

@Configuration
@RequiredArgsConstructor
public class FaqLoader {
	private  final VectorStore vectorStore;
	private final JdbcClient jdbcClient;
	
	@Value("classpath:data.txt")
	private Resource resource;
	
	@PostConstruct
	public void init() throws Exception {
		Integer count = jdbcClient.sql("select count(*) from tripan_vector")
					.query(Integer.class)
					.single();
		
		// System.out.println("PG VectorStore 에 저장된 데이터 갯수 = " + count);
		
		if(count == 0){
			List<Document> documents = Files.lines(resource.getFile().toPath())
						.map(Document::new)
						.collect(Collectors.toList());
			
			TextSplitter textSplitter = new TokenTextSplitter();
			
			for(Document document : documents) {
				List<Document> splitteddocs = textSplitter.split(document);
                // System.out.println("before adding document: " + document.getFormattedContent());
                
				vectorStore.add(splitteddocs); // 임베딩해서 저장
				// System.out.println("Added document: " + document.getFormattedContent());
                
                Thread.sleep(1000);
			}
            
            // System.out.println("Application is ready to Serve the Requests");
		}
	}
	
}
