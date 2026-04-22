package com.swe.project.contentaccess.service;

import com.swe.project.contentaccess.dto.CreateTopicRequest;
import com.swe.project.contentaccess.dto.HotspotRequest;
import com.swe.project.contentaccess.model.Hotspot;
import com.swe.project.contentaccess.model.Topic;
import com.swe.project.contentaccess.repository.TopicRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final Path imageUploadDir = Paths.get("uploads/images");
    private final Path audioUploadDir = Paths.get("uploads/audio");

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(String id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found: " + id));
    }

    public Topic createTopic(CreateTopicRequest request) {
        if (request == null || request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic title is required");
        }

        String id = (request.getId() != null && !request.getId().isBlank())
                ? request.getId().trim()
                : generateId(request.getTitle());

        List<Hotspot> hotspots = new ArrayList<>();

        if (request.getHotspots() != null) {
            for (HotspotRequest hotspotRequest : request.getHotspots()) {
                validateHotspotRequest(hotspotRequest);

                Hotspot hotspot = new Hotspot(
                        hotspotRequest.getLabel().trim(),
                        hotspotRequest.getImageUrl(),
                        hotspotRequest.getXPercent(),
                        hotspotRequest.getYPercent(),
                        safeTranslations(hotspotRequest.getTranslations()),
                        safeAudioUrls(hotspotRequest.getAudioUrls())
                );

                hotspots.add(hotspot);
            }
        }

        Topic topic = new Topic(
                id,
                request.getTitle().trim(),
                request.getImageUrl(),
                (request.getCategory() == null || request.getCategory().isBlank())
                        ? "Custom"
                        : request.getCategory().trim(),
                hotspots
        );

        return topicRepository.save(topic);
    }

    public Topic addHotspot(String topicId, HotspotRequest request) {
        validateHotspotRequest(request);

        Topic topic = getTopicById(topicId);
        List<Hotspot> hotspots = topic.getHotspots() == null
                ? new ArrayList<>()
                : new ArrayList<>(topic.getHotspots());

        for (Hotspot existing : hotspots) {
            if (existing.getLabel() != null && existing.getLabel().equalsIgnoreCase(request.getLabel().trim())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A hotspot with that label already exists");
            }
        }

        Hotspot hotspot = new Hotspot(
                request.getLabel().trim(),
                request.getImageUrl(),
                request.getXPercent(),
                request.getYPercent(),
                safeTranslations(request.getTranslations()),
                safeAudioUrls(request.getAudioUrls())
        );

        hotspots.add(hotspot);
        topic.setHotspots(hotspots);
        return topicRepository.save(topic);
    }

    public Topic updateHotspot(String topicId, String label, HotspotRequest request) {
        validateHotspotRequest(request);

        Topic topic = getTopicById(topicId);
        List<Hotspot> hotspots = topic.getHotspots() == null
                ? new ArrayList<>()
                : new ArrayList<>(topic.getHotspots());

        Hotspot hotspotToUpdate = null;

        for (Hotspot hotspot : hotspots) {
            if (hotspot.getLabel() != null && hotspot.getLabel().equalsIgnoreCase(label)) {
                hotspotToUpdate = hotspot;
                break;
            }
        }

        if (hotspotToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotspot not found: " + label);
        }

        String newLabel = request.getLabel().trim();

        for (Hotspot hotspot : hotspots) {
            if (hotspot != hotspotToUpdate
                    && hotspot.getLabel() != null
                    && hotspot.getLabel().equalsIgnoreCase(newLabel)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A hotspot with that label already exists");
            }
        }

        hotspotToUpdate.setLabel(newLabel);
        hotspotToUpdate.setImageUrl(request.getImageUrl());
        hotspotToUpdate.setXPercent(request.getXPercent());
        hotspotToUpdate.setYPercent(request.getYPercent());
        hotspotToUpdate.setTranslations(safeTranslations(request.getTranslations()));
        hotspotToUpdate.setAudioUrls(safeAudioUrls(request.getAudioUrls()));

        topic.setHotspots(hotspots);
        return topicRepository.save(topic);
    }

    public Topic deleteHotspot(String topicId, int index) {
        Topic topic = getTopicById(topicId);
        List<Hotspot> hotspots = topic.getHotspots() == null
                ? new ArrayList<>()
                : new ArrayList<>(topic.getHotspots());

        if (index < 0 || index >= hotspots.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotspot index not found: " + index);
        }

        hotspots.remove(index);
        topic.setHotspots(hotspots);
        return topicRepository.save(topic);
    }

    public void deleteTopic(String id) {
        if (!topicRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found: " + id);
        }

        topicRepository.deleteById(id);
    }

    public String storeImage(String topicId, MultipartFile file) {
        getTopicById(topicId);
        return storeFile(file, imageUploadDir, List.of("jpg", "jpeg", "png", "webp"), "/topics/media/images/");
    }

    public String storeAudio(String topicId, MultipartFile file) {
        getTopicById(topicId);
        return storeFile(file, audioUploadDir, List.of("mp3"), "/topics/media/audio/");
    }

    public Resource loadImage(String filename) {
        return loadFile(imageUploadDir, filename, "Image file");
    }

    public Resource loadAudio(String filename) {
        return loadFile(audioUploadDir, filename, "Audio file");
    }

    private void validateHotspotRequest(HotspotRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hotspot request is required");
        }

        if (request.getLabel() == null || request.getLabel().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hotspot label is required");
        }

        if (request.getXPercent() < 0 || request.getXPercent() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "xPercent must be between 0 and 100");
        }

        if (request.getYPercent() < 0 || request.getYPercent() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "yPercent must be between 0 and 100");
        }
    }

    private Map<String, String> safeTranslations(Map<String, String> translations) {
        return translations == null ? Map.of() : translations;
    }

    private Map<String, String> safeAudioUrls(Map<String, String> audioUrls) {
        return audioUrls == null ? Map.of() : audioUrls;
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

    private String storeFile(MultipartFile file,
                             Path uploadDir,
                             List<String> allowedExtensions,
                             String publicPathPrefix) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A file is required");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is required");
        }

        String extension = getExtension(originalFilename);
        if (!allowedExtensions.contains(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type");
        }

        try {
            Files.createDirectories(uploadDir);

            String cleanedName = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
            String storedFilename = UUID.randomUUID() + "-" + cleanedName;

            Path basePath = uploadDir.toAbsolutePath().normalize();
            Path destination = uploadDir.resolve(storedFilename).normalize().toAbsolutePath();

            if (!destination.startsWith(basePath)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
            }

            file.transferTo(destination.toFile());
            return publicPathPrefix + storedFilename;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save uploaded file");
        }
    }

    private Resource loadFile(Path uploadDir, String filename, String label) {
        try {
            Path basePath = uploadDir.toAbsolutePath().normalize();
            Path filePath = uploadDir.resolve(filename).normalize().toAbsolutePath();

            if (!filePath.startsWith(basePath)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
            }

            if (!Files.exists(filePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, label + " not found");
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, label + " not readable");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not load uploaded file");
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}