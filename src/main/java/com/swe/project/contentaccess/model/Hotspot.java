package com.swe.project.contentaccess.model;

import lombok.Data;
import java.util.Map;

public class Hotspot {
    private String label;
    private Map<String, String> translations;
    private Map<String, String> audioUrls;

}