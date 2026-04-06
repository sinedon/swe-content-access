package com.swe.project.contentaccess.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/topics")
public class TopicController {

    public TopicController() {
        System.out.println(">>> TopicController CREATED <<<");
    }
    
    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @GetMapping("/{id}")
    public Map<String, String> getTopic(@PathVariable String id) {
        return Map.of(
                "id", id,
                "name", "Test Topic",
                "status", "content-access working"
        );
    }
}