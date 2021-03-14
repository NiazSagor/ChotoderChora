package com.angik.chotoderchora.Task;

import androidx.annotation.NonNull;

import com.angik.chotoderchora.Interface.PoemAudioLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetAudioLinkTask implements Runnable {

    private final DatabaseReference databaseReferenceAudio = FirebaseDatabase.getInstance().getReference("Poem")
            .child("Audio");


    private String poemAudioLink;
    private final String poemCode;

    private PoemAudioLoadCallback callback;

    public GetAudioLinkTask(PoemAudioLoadCallback callback, String poemCode) {
        this.callback = callback;
        this.poemCode = poemCode;
    }

    @Override
    public void run() {
        databaseReferenceAudio
                .child(poemCode)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            poemAudioLink = snapshot.getValue(String.class);
                            callback.onPoemAudioLoadCallback(poemAudioLink);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
