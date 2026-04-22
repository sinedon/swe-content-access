package com.swe.project.contentaccess.dto;

import java.util.List;

public class CreateTopicRequest {

    private String id;
    private String title;
    private String imageUrl;
    private String category;
    private List<HotspotRequest> hotspots;

    public CreateTopicRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<HotspotRequest> getHotspots() {
        return hotspots;
    }

    public void setHotspots(List<HotspotRequest> hotspots) {
        this.hotspots = hotspots;
    }
}