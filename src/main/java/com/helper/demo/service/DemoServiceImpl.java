package com.helper.demo.service;

import com.helper.demo.dto.PostResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
public class DemoServiceImpl implements DemoService {
    private final RestClient restClient;

    @Override
    public String getService() {
        PostResponseDto body = restClient.get().uri("https://jsonplaceholder.typicode.com/posts/1").retrieve().body(PostResponseDto.class);
        assert body != null;
        return body.getBody();
    }
}
