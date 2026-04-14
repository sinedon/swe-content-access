package com.swe.project.contentaccess.repository;

import com.swe.project.contentaccess.model.Topic;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {

}
