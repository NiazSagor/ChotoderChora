package com.angik.chotoderchora.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.angik.chotoderchora.App.MyApp;
import com.angik.chotoderchora.Interface.PoemsLoadCallback;
import com.angik.chotoderchora.Model.Poem;
import com.angik.chotoderchora.Repository.Repository;

import java.util.List;

public class ChoraListActivityViewModel extends AndroidViewModel {

    private final Repository repository;

    private final MutableLiveData<List<Poem>> poemMutableLiveData = new MutableLiveData<>();

    public ChoraListActivityViewModel(@NonNull Application application) {
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

}
