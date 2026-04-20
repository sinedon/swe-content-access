package com.swe.project.contentaccess;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.swe.project.contentaccess.repository.TopicRepository;

@SpringBootTest
@ActiveProfiles("test")
class ContentAccessApplicationTests {

	@MockBean
    private TopicRepository topicRepository;
	@Test
	void contextLoads() {
	}

}
