package com.swe.project.contentaccess.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.swe.project.contentaccess.dto.CreateTopicRequest;
import com.swe.project.contentaccess.model.Hotspot;
import com.swe.project.contentaccess.model.Topic;
import com.swe.project.contentaccess.repository.TopicRepository;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

        private final List<Topic> topics = List.of(
                new Topic(
                        "fruits",
                        "Fruits",
                        "/images/fruits.jpg",
                        "Food",
                        List.of(
                                new Hotspot(
                                        "Apple",
                                        "/images/apple.jpg",
                                        10.0,
                                        53.0,
                                        Map.of("Spanish", "Manzana"),
                                        Map.of("Spanish", "/audio/spanish_apple.mp3")
                                ),
                                new Hotspot(
                                        "Orange",
                                        "/images/orange.jpg",
                                        24.0,
                                        23.0,
                                        Map.of("Spanish", "Naranja"),
                                        Map.of("Spanish", "/audio/spanish_orange.mp3")
                                ),
                                new Hotspot(
                                        "Kiwi",
                                        "/images/kiwi.jpg",
                                        67.0,
                                        35.0,
                                        Map.of("Spanish", "Kiwi"),
                                        Map.of("Spanish", "/audio/spanish_kiwi.mp3")
                                ),
                                new Hotspot(
                                        "Raspberry",
                                        "/images/raspberry.jpg",
                                        81.0,
                                        67.0,
                                        Map.of("Spanish", "Frambuesa"),
                                        Map.of("Spanish", "/audio/spanish_raspberry.mp3")
                                )
                        )
                ),
                new Topic(
                        "vegetables",
                        "Vegetables",
                        "/images/vegetables.jpg",
                        "Food",
                        List.of(
                                new Hotspot(
                                        "Carrot",
                                        "/images/carrot.jpg",
                                        52.0,
                                        37.0,
                                        Map.of("Spanish", "Zanahoria"),
                                        Map.of()
                                ),
                                new Hotspot(
                                        "Garlic",
                                        "/images/garlic.jpg",
                                        14.0,
                                        75.0,
                                        Map.of("Spanish", "Ajo"),
                                        Map.of()
                                ),
                                new Hotspot(
                                        "Potato",
                                        "/images/potato.jpg",
                                        82.0,
                                        52.0,
                                        Map.of("Spanish", "Papa"),
                                        Map.of()
                                )
                        )
                )
        );

    public void start() {
        System.out.println("\n\n\n Number of topics in repository: " + topicRepository.count());
        if (topicRepository.count() == 0) {
            System.out.println("\n printing topics to be saved: " + topics + "\n\n\n");
            topicRepository.saveAll(topics);
        }
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