package com.angik.chotoderchora.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.angik.chotoderchora.App.MyApp;
import com.angik.chotoderchora.Interface.PoemAudioLoadCallback;
import com.angik.chotoderchora.Interface.PoemsLoadCallback;
import com.angik.chotoderchora.Model.Poem;
import com.angik.chotoderchora.Repository.Repository;

import java.util.List;

public class ChoraActivityViewModel extends AndroidViewModel {

    private final Repository repository;

    private final MutableLiveData<String> poemAudioLinkMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Poem>> poemMutableLiveData = new MutableLiveData<>();

    public ChoraActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(MyApp.executorService);
    }

    public MutableLiveData<List<Poem>> getPoemMutableLiveData(){

        repository.getAllPoems(new PoemsLoadCallback() {
            @Override
            public void onPoemsLoadCallback(List<Poem> allPoems) {
                poemMutableLiveData.postValue(allPoems);
            }
        });

        return poemMutableLiveData;
    }

    public MutableLiveData<String> getPoemAudioLinkMutableLiveData(String poemCode) {

        repository.getPoemAudio(new PoemAudioLoadCallback() {
            @Override
            public void onPoemAudioLoadCallback(String audioLink) {
                poemAudioLinkMutableLiveData.postValue(audioLink);
            }
        }, poemCode);

        return poemAudioLinkMutableLiveData;
    }

}
