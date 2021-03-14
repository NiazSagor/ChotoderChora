package com.angik.chotoderchora.Repository;

import com.angik.chotoderchora.Interface.PoemAudioLoadCallback;
import com.angik.chotoderchora.Interface.PoemsLoadCallback;
import com.angik.chotoderchora.Task.GetAudioLinkTask;
import com.angik.chotoderchora.Task.GetPoemsTask;

import java.util.concurrent.ExecutorService;

public class Repository {

    private final ExecutorService executorService;

    public Repository(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void getAllPoems(PoemsLoadCallback callback){
        executorService.submit(new GetPoemsTask(callback));
    }

    public void getPoemAudio(PoemAudioLoadCallback callback, String poemCode){
        executorService.submit(new GetAudioLinkTask(callback, poemCode));
    }
}
