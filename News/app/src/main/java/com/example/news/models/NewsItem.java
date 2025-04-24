package com.example.news.models;

public class NewsItem {

    private String title;
    private String imageUrl;
    private String description;
    private String sourceUrl;

    public NewsItem(String title, String imageUrl, String description, String sourceUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
