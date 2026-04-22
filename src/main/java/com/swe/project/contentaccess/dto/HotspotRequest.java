package com.swe.project.contentaccess.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class HotspotRequest {

    @JsonProperty("label")
    private String label;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("xPercent")
    private double xPercent;

    @JsonProperty("yPercent")
    private double yPercent;

    @JsonProperty("translations")
    private Map<String, String> translations;

    @JsonProperty("audioUrls")
    private Map<String, String> audioUrls;

    public HotspotRequest() {
    }

    public HotspotRequest(String label,
                          String imageUrl,
                          double xPercent,
                          double yPercent,
                          Map<String, String> translations,
                          Map<String, String> audioUrls) {
        this.label = label;
        this.imageUrl = imageUrl;
        this.xPercent = xPercent;
        this.yPercent = yPercent;
        this.translations = translations;
        this.audioUrls = audioUrls;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getXPercent() {
        return xPercent;
    }

    public void setXPercent(double xPercent) {
        this.xPercent = xPercent;
    }

    public double getYPercent() {
        return yPercent;
    }

    public void setYPercent(double yPercent) {
        this.yPercent = yPercent;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public Map<String, String> getAudioUrls() {
        return audioUrls;
    }

    public void setAudioUrls(Map<String, String> audioUrls) {
        this.audioUrls = audioUrls;
    }
}