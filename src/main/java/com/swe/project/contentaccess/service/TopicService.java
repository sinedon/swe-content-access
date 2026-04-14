package com.swe.project.contentaccess.service;

import com.swe.project.contentaccess.model.Hotspot;
import com.swe.project.contentaccess.model.Topic;
import com.swe.project.contentaccess.repository.TopicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
        
import java.util.List;
import java.util.Map;

@Service
public class TopicService {

        @Autowired
        private TopicRepository topicRepository;
        

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
}