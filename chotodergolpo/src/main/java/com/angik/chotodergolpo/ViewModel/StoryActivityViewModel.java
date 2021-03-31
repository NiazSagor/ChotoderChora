package com.angik.chotodergolpo.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.angik.chotodergolpo.Interface.StoryLoadCallback;
import com.angik.chotodergolpo.Model.Story;
import com.angik.chotodergolpo.MyApp.MyApp;
import com.angik.chotodergolpo.Repository.Repository;

import java.util.List;

public class StoryActivityViewModel extends AndroidViewModel {

    private final Repository repository;

    private final MutableLiveData<List<Story>> allStoriesMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> selectedVideoUrl = new MutableLiveData<>();

    public StoryActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(MyApp.executorService);
    }

    public MutableLiveData<List<Story>> getAllStories(String categoryTitle) {

        repository.getAllStories(categoryTitle, new StoryLoadCallback() {
            @Override
            public void onStoryLoad(List<Story> storyList) {
                allStoriesMutableLiveData.postValue(storyList);
            }
        });

        return allStoriesMutableLiveData;
    }

    public MutableLiveData<String> getSelectedVideoUrl() {
        return selectedVideoUrl;
    }
}
