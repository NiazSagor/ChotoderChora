package com.angik.chotodergolpo.Interface;

import com.angik.chotodergolpo.Model.Story;

import java.util.List;

public interface StoryLoadCallback {
    void onStoryLoad(List<Story> storyList);
}
