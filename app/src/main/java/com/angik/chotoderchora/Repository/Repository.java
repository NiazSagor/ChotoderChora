package com.angik.chotoderchora.Repository;

import com.angik.chotoderchora.Interface.BanglaAlphabetAudioLoadCallback;
import com.angik.chotoderchora.Interface.BanglaAlphabetLoadCallback;
import com.angik.chotoderchora.Interface.PoemAudioLoadCallback;
import com.angik.chotoderchora.Interface.PoemsLoadCallback;
import com.angik.chotoderchora.Task.GetAudioLinkTask;
import com.angik.chotoderchora.Task.GetBanglaAlphabetAudioTask;
import com.angik.chotoderchora.Task.GetBanglaAlphabetTask;
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

    public void getBanglaAlphabets(BanglaAlphabetLoadCallback callback){
        executorService.submit(new GetBanglaAlphabetTask(callback));
    }

    public void getBanglaAlphabetAudio(BanglaAlphabetAudioLoadCallback callback, String query){
        executorService.submit(new GetBanglaAlphabetAudioTask(callback, query));
    }
}
