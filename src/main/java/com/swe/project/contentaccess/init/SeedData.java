package com.swe.project.contentaccess.init;

import com.swe.project.contentaccess.model.Hotspot;
import com.swe.project.contentaccess.model.Topic;

import java.util.List;
import java.util.Map;

public class SeedData {
    public static List<Topic> topics() {
        return List.of(
                new Topic(
                        "fruits",
                        "Fruits",
                        "/images/fruits.jpg",
                        "Food",
                        List.of(
                                new Hotspot(
                                        "Apple",
                                        "/images/apple.jpg",
                                        10.0,
                                        53.0,
                                        Map.of("Spanish", "Manzana"),
                                        Map.of("Spanish", "/audio/spanish_apple.mp3")
                                ),
                                new Hotspot(
                                        "Orange",
                                        "/images/orange.jpg",
                                        24.0,
                                        23.0,
                                        Map.of("Spanish", "Naranja"),
                                        Map.of("Spanish", "/audio/spanish_orange.mp3")
                                ),
                                new Hotspot(
                                        "Kiwi",
                                        "/images/kiwi.jpg",
                                        67.0,
                                        35.0,
                                        Map.of("Spanish", "Kiwi"),
                                        Map.of("Spanish", "/audio/spanish_kiwi.mp3")
                                ),
                                new Hotspot(
                                        "Raspberry",
                                        "/images/raspberry.jpg",
                                        81.0,
                                        67.0,
                                        Map.of("Spanish", "Frambuesa"),
                                        Map.of("Spanish", "/audio/spanish_raspberry.mp3")
                                )
                        )
                ),
                new Topic(
                        "vegetables",
                        "Vegetables",
                        "/images/vegetables.jpg",
                        "Food",
                        List.of(
                                new Hotspot(
                                        "Carrot",
                                        "/images/carrot.jpg",
                                        52.0,
                                        37.0,
                                        Map.of("Spanish", "Zanahoria"),
                                        Map.of("Spanish", "/audio/spanish_carrot.mp3")
                                ),
                                new Hotspot(
                                        "Garlic",
                                        "/images/garlic.jpg",
                                        14.0,
                                        75.0,
                                        Map.of("Spanish", "Ajo"),
                                        Map.of("Spanish", "/audio/spanish_garlic.mp3")
                                ),
                                new Hotspot(
                                        "Potato",
                                        "/images/potato.jpg",
                                        82.0,
                                        52.0,
                                        Map.of("Spanish", "Papa"),
                                        Map.of("Spanish", "/audio/spanish_potato.mp3")
                                )
                        )
                ),
                new Topic(
                        "classroom",
                        "Classroom",
                        "/images/classroom.jpg",
                        "School",
                        List.of(
                                new Hotspot(
                                        "Paperclip",
                                        "/images/paperclip.jpg",
                                        63.0,
                                        61.0,
                                        Map.of("German", "Büroklammer"),
                                        Map.of("German", "/audio/paperclip.mp3")
                                ),
                                new Hotspot(
                                        "Colored Pencil",
                                        "/images/colored_pencil.jpg",
                                        36.0,
                                        17.0,
                                        Map.of("German", "Buntstift"),
                                        Map.of("German", "/audio/colored_pencil.mp3")
                                ),
                                new Hotspot(
                                        "Notebook",
                                        "/images/notebook.jpg",
                                        75.0,
                                        12.0,
                                        Map.of("German", "Notizbuch"),
                                        Map.of("German", "/audio/notebook.mp3")
                                ),
                                new Hotspot(
                                        "Pencil Sharpener",
                                        "/images/pencil_sharpener.jpg",
                                        33.0,
                                        55.0,
                                        Map.of("German", "Spitzer"),
                                        Map.of("German", "/audio/pencil_sharpener.mp3")
                                ),
                                new Hotspot(
                                        "Pen",
                                        "/images/pen.jpg",
                                        86.0,
                                        48.0,
                                        Map.of("German", "Stift"),
                                        Map.of("German", "/audio/pen.mp3")
                                ),
                                new Hotspot(
                                        "Eraser",
                                        "/images/eraser.jpg",
                                        28.0,
                                        68.0,
                                        Map.of("German", "Radiergummi"),
                                        Map.of("German", "/audio/eraser.mp3")
                                )
                        )
                )
        );
    }
}
