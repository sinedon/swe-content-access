package com.swe.project.contentaccess.controller;

import com.swe.project.contentaccess.model.Topic;
import com.swe.project.contentaccess.service.TopicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    @GetMapping("/{id}")
    public Topic getTopic(@PathVariable String id) {
        return topicService.getTopicById(id);
    }

    /*
    CRUD on topic and hotspot documents in MongoDB. Business verbs:
    POST /topics, GET /topics (table of contents), GET /topics/{id} (with embedded
    hotspots), POST /topics/{id}/hotspots, PUT /topics/{id}/hotspots/{label}, DELETE
    /topics/{id}
    */
}