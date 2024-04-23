package com.helper.demo.controllers;

import com.helper.demo.service.DemoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private final DemoService demoService;
    private final RedisTemplate<String, Object> redisTemplate;

    public DemoController(DemoService demoService,  RedisTemplate<String, Object> template) {
        this.demoService = demoService;
        this.redisTemplate = template;
    }

    @GetMapping
    public String getDemo() {
        return demoService.getService();
    }

    @GetMapping("/set/{key}/{value}")
    public String setValue(@PathVariable String key, @PathVariable String value) {
        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();
        ops.set(key, value);
        return "Set";
    }

    @GetMapping("/get/{key}")
    public Object getValue(@PathVariable String key) {
        Object o = this.redisTemplate.opsForValue().get(key);
        return o;
    }

}
