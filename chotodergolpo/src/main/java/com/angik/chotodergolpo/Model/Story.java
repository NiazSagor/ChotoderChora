package com.angik.chotodergolpo.Model;

public class Story {

    private String storyTitle,
            storyVideoUrl;

    public Story() {
    }

    public Story(String storyTitle, String storyVideoUrl) {
        this.storyTitle = storyTitle;
        this.storyVideoUrl = storyVideoUrl;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public String getStoryVideoUrl() {
        return storyVideoUrl;
    }
}
