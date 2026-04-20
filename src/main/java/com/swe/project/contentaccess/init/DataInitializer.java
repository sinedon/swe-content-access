package com.swe.project.contentaccess.init;

import com.swe.project.contentaccess.repository.TopicRepository;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final TopicRepository topicRepository;

    public DataInitializer(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @jakarta.annotation.PostConstruct
    public void init() {

        System.out.println("\n[INIT] Checking Mongo topic collection...");
        long count = topicRepository.count();

        System.out.println("[INIT] Current topic count: " + count);

        if (count == 0) {

            System.out.println("[INIT] Seeding database...");

            topicRepository.saveAll(SeedData.topics());

            System.out.println("[INIT] Seeding complete. New count: " + topicRepository.count());
        } else {
            System.out.println("[INIT] Database already seeded. Skipping.");
        }
    }
}