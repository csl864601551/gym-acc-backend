package com.ztl.gym.weixin.config;


/**
 * code come from:https://github.com/geektime-geekbang/geektime-spring-family/blob/master/Chapter%207/advanced-resttemplate-demo/src/main/java/geektime/spring/springbucks/customer/CustomerServiceApplication.java
 * @author DigitalSonic
 */
//@Configuration
public class RestTemplateConfig {
//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		//return builder.build();
//		return builder
//				.setConnectTimeout(Duration.ofMillis(100))
//				.setReadTimeout(Duration.ofMillis(500))
//				.requestFactory(this::requestFactory)
//				.build();
//	}
//
//	@Bean
//	public HttpComponentsClientHttpRequestFactory requestFactory() {
//		PoolingHttpClientConnectionManager connectionManager =
//				new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
//		connectionManager.setMaxTotal(200);
//		connectionManager.setDefaultMaxPerRoute(20);
//
//		CloseableHttpClient httpClient = HttpClients.custom()
//				.setConnectionManager(connectionManager)
//				.evictIdleConnections(30, TimeUnit.SECONDS)
//				.disableAutomaticRetries()
//				// 有 Keep-Alive 认里面的值，没有的话永久有效
//				//.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
//				// 换成自定义的
//				.setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
//				.build();
//
//		HttpComponentsClientHttpRequestFactory requestFactory =
//				new HttpComponentsClientHttpRequestFactory(httpClient);
//
//		return requestFactory;
//	}
}
