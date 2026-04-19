package com.swe.project.contentaccess.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.swe.project.contentaccess.dto.CreateTopicRequest;
import com.swe.project.contentaccess.model.Topic;
import com.swe.project.contentaccess.repository.TopicRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(String id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found: " + id));
    }

    public Topic createTopic(CreateTopicRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic title is required");
        }

        Topic topic = new Topic();
        topic.setId(generateId(request.getTitle()));
        topic.setTitle(request.getTitle().trim());
        topic.setImageUrl(request.getImageUrl());
        topic.setCategory("Custom");
        topic.setHotspots(List.of());

        return topicRepository.save(topic);
    }

    private String generateId(String title) {
        String base = title.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");

        if (base.isBlank()) {
            base = "custom-topic";
        }

        String id = base;
        int counter = 2;

        while (topicRepository.existsById(id)) {
            id = base + "-" + counter;
            counter++;
        }

        return id;
    }
}