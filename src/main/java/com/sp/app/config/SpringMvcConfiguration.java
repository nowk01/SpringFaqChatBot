package com.sp.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
- CORS(Cross-Origin Resource Sharing)
  브라우저에서 다른 출처(Origin) 의 서버로 요청을 보낼 때 발생하는 보안 정책
- Origin = 프로토콜 + 도메인 + 포트
- 브라우저는 Same-Origin Policy (동일 출처 정책) 를 기본
- 특정 컨트롤러에서 설정
  @CrossOrigin(origins = "http://localhost:5173")
  @RestController
  @RequestMapping("/api")
  public class TestController {
      :
*/

@Configuration
public class SpringMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins("http://localhost:9090", "http://localhost") 
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
	}
}
