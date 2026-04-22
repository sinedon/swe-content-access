package com.swe.project.contentaccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.project.contentaccess.controller.TopicController;
import com.swe.project.contentaccess.dto.CreateTopicRequest;
import com.swe.project.contentaccess.dto.HotspotRequest;
import com.swe.project.contentaccess.model.Hotspot;
import com.swe.project.contentaccess.model.Topic;
import com.swe.project.contentaccess.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TopicController.class)
class TopicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TopicService topicService;

    @Test
    void getAllTopics_returnsTopics() throws Exception {
        Topic topic = buildTopic("classroom", "Classroom", List.of());

        given(topicService.getAllTopics()).willReturn(List.of(topic));

        mockMvc.perform(get("/topics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("classroom"))
                .andExpect(jsonPath("$[0].title").value("Classroom"));
    }

    @Test
    void getTopicById_returnsTopic() throws Exception {
        Topic topic = buildTopic("classroom", "Classroom", List.of());

        given(topicService.getTopicById("classroom")).willReturn(topic);

        mockMvc.perform(get("/topics/classroom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("classroom"))
                .andExpect(jsonPath("$.title").value("Classroom"));
    }

    @Test
    void getTopicById_returns404_whenMissing() throws Exception {
        given(topicService.getTopicById("missing"))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        mockMvc.perform(get("/topics/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTopic_returnsCreatedTopic() throws Exception {
        Topic savedTopic = buildTopic("german-classroom", "German Classroom", List.of());

        given(topicService.createTopic(any(CreateTopicRequest.class))).willReturn(savedTopic);

        CreateTopicRequest request = new CreateTopicRequest();
        request.setId("german-classroom");
        request.setTitle("German Classroom");
        request.setImageUrl("/topics/images/classroom.jpg");
        request.setCategory("German");
        request.setHotspots(List.of());

        mockMvc.perform(post("/topics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("german-classroom"))
                .andExpect(jsonPath("$.title").value("German Classroom"));
    }

    @Test
    void addHotspot_returnsUpdatedTopic() throws Exception {
        Hotspot hotspot = buildHotspot(
                "Pen",
                "/topics/media/images/pen.png",
                42.5,
                51.0,
                Map.of("Spanish", "boligrafo"),
                Map.of("Spanish", "/topics/media/audio/pen.mp3")
        );

        Topic topicAfter = buildTopic("classroom", "Classroom", List.of(hotspot));

        given(topicService.addHotspot(eq("classroom"), any(HotspotRequest.class))).willReturn(topicAfter);

        HotspotRequest request = new HotspotRequest();
        request.setLabel("Pen");
        request.setImageUrl("/topics/media/images/pen.png");
        request.setXPercent(42.5);
        request.setYPercent(51.0);
        request.setTranslations(Map.of("Spanish", "boligrafo"));
        request.setAudioUrls(Map.of("Spanish", "/topics/media/audio/pen.mp3"));

        mockMvc.perform(post("/topics/classroom/hotspots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotspots[0].label").value("Pen"));
    }

    @Test
    void updateHotspot_returnsUpdatedTopic() throws Exception {
        Hotspot updatedHotspot = buildHotspot(
                "Pen",
                "/topics/media/images/new-pen.png",
                30.0,
                40.0,
                Map.of("Spanish", "boligrafo"),
                Map.of("Spanish", "/topics/media/audio/new-pen.mp3")
        );

        Topic updatedTopic = buildTopic("classroom", "Classroom", List.of(updatedHotspot));

        given(topicService.updateHotspot(eq("classroom"), eq("Pen"), any(HotspotRequest.class)))
                .willReturn(updatedTopic);

        HotspotRequest request = new HotspotRequest();
        request.setLabel("Pen");
        request.setImageUrl("/topics/media/images/new-pen.png");
        request.setXPercent(30.0);
        request.setYPercent(40.0);
        request.setTranslations(Map.of("Spanish", "boligrafo"));
        request.setAudioUrls(Map.of("Spanish", "/topics/media/audio/new-pen.mp3"));

        mockMvc.perform(put("/topics/classroom/hotspots/Pen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotspots[0].imageUrl").value("/topics/media/images/new-pen.png"));
    }

    @Test
    void deleteHotspot_returnsUpdatedTopic() throws Exception {
        Topic topicAfterDelete = buildTopic("classroom", "Classroom", List.of());

        given(topicService.deleteHotspot("classroom", 0)).willReturn(topicAfterDelete);

        mockMvc.perform(delete("/topics/classroom/hotspots/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotspots").isEmpty());
    }

    @Test
    void deleteTopic_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/topics/classroom"))
                .andExpect(status().isNoContent());

        verify(topicService).deleteTopic("classroom");
    }

    @Test
    void uploadImage_returnsImagePath() throws Exception {
        given(topicService.storeImage(eq("classroom"), any(org.springframework.web.multipart.MultipartFile.class)))
                .willReturn("/topics/media/images/test-image.png");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.png",
                MediaType.IMAGE_PNG_VALUE,
                "image-data".getBytes()
        );

        mockMvc.perform(multipart("/topics/classroom/images").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value("/topics/media/images/test-image.png"));
    }

    @Test
    void uploadAudio_returnsAudioPath() throws Exception {
        given(topicService.storeAudio(eq("classroom"), any(org.springframework.web.multipart.MultipartFile.class)))
                .willReturn("/topics/media/audio/test-audio.mp3");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-audio.mp3",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "audio-data".getBytes()
        );

        mockMvc.perform(multipart("/topics/classroom/audio").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value("/topics/media/audio/test-audio.mp3"));
    }

    private Topic buildTopic(String id, String title, List<Hotspot> hotspots) {
        return new Topic(
                id,
                title,
                "/topics/media/images/classroom.jpg",
                "Custom",
                hotspots
        );
    }

    private Hotspot buildHotspot(
            String label,
            String imageUrl,
            double xPercent,
            double yPercent,
            Map<String, String> translations,
            Map<String, String> audioUrls
    ) {
        return new Hotspot(
                label,
                imageUrl,
                xPercent,
                yPercent,
                translations,
                audioUrls
        );
    }
}