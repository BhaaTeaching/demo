package com.example.componenttest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

@SpringBootTest
class ComponentTestApplicationTests {

	private RestClient restClient = RestClient.create();

	@Test
	void contextLoads() {
		String body = restClient.get().uri("http://localhost:8081/demo").retrieve().body(String.class);
        assert body != null;
        Assertions.assertFalse(body.isEmpty());
	}

}
