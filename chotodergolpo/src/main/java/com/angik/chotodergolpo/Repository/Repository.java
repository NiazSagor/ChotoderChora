package com.angik.chotodergolpo.Repository;

import com.angik.chotodergolpo.Interface.StoryLoadCallback;
import com.angik.chotodergolpo.Task.GetStory;

import java.util.concurrent.ExecutorService;

public class Repository {

    private final ExecutorService executorService;

    public Repository(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void getAllStories(String categoryTitle, StoryLoadCallback callback) {
        executorService.submit(new GetStory(categoryTitle, callback));
    }
}
