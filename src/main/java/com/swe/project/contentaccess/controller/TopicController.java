package com.swe.project.contentaccess.controller;

import com.swe.project.contentaccess.dto.CreateTopicRequest;
import com.swe.project.contentaccess.dto.HotspotRequest;
import com.swe.project.contentaccess.model.Topic;
import com.swe.project.contentaccess.service.TopicService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @PostMapping
    public Topic createTopic(@RequestBody CreateTopicRequest request) {
        return topicService.createTopic(request);
    }

    @PostMapping("/{id}/hotspots")
    public Topic addHotspot(@PathVariable String id, @RequestBody HotspotRequest request) {
        return topicService.addHotspot(id, request);
    }

    @PutMapping("/{id}/hotspots/{label}")
    public Topic updateHotspot(@PathVariable String id,
                               @PathVariable String label,
                               @RequestBody HotspotRequest request) {
        return topicService.updateHotspot(id, label, request);
    }

    @DeleteMapping("/{id}/hotspots/{index}")
    public Topic deleteHotspot(@PathVariable String id, @PathVariable int index) {
        return topicService.deleteHotspot(id, index);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(@PathVariable String id,
                                                           @RequestPart("file") MultipartFile file) {
        String path = topicService.storeImage(id, file);
        return ResponseEntity.ok(Map.of("path", path));
    }

    @PostMapping(value = "/{id}/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadAudio(@PathVariable String id,
                                                           @RequestPart("file") MultipartFile file) {
        String path = topicService.storeAudio(id, file);
        return ResponseEntity.ok(Map.of("path", path));
    }

    @GetMapping("/media/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource resource = topicService.loadImage(filename);
        return ResponseEntity.ok()
                .contentType(detectImageType(filename))
                .body(resource);
    }

    @GetMapping("/media/audio/{filename:.+}")
    public ResponseEntity<Resource> getAudio(@PathVariable String filename) {
        Resource resource = topicService.loadAudio(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }

    private MediaType detectImageType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        }
        return MediaType.IMAGE_JPEG;
    }
}
