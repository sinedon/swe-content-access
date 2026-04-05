package com.swe.project.contentaccess.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;

@Document(collection = "topics")
public class Topic {
    @Id
    private String id;
    private String title;
    private String imageUrl;
    private String category;
    private List<Hotspot> hotspots;

}
