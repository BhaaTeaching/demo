package com.helper.demo.service;

import com.amazonaws.services.appstream.model.User;
import com.helper.demo.conf.RateLimit;
import com.helper.demo.dto.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class DemoServiceImpl implements DemoService {
    private final RestClient restClient;

    @Override
    public String getService() {
        PostResponseDto body = restClient.get().uri("https://jsonplaceholder.typicode.com/posts/1").retrieve().body(PostResponseDto.class);
        assert body != null;
        return body.getBody();
    }

    @Override
    public void completableFuture() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
            return "Result 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            // Some long-running operation
            return "Result 3";
        });
        CompletableFuture.allOf(future1, future2, future3).thenRun(()->{
            log.info("After threads: {} - {} - {}", future1.join(), future2.join(), future3.join());
        });
    }

    @RateLimit(tokens = 1, minutes = 1, maxWaitMs = 2000)
    public User processUser(User user) {
        // Heavy processing logic here
        return new User();
    }

    @RateLimit(tokens = 1, minutes = 1, maxWaitMs = 50000)
    public List<User> bulkProcess(List<User> users) {
        // Bulk processing logic here
        return List.of(new User());
    }
}
