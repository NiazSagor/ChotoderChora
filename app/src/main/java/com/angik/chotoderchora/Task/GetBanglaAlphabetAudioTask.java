package com.angik.chotoderchora.Task;

import androidx.annotation.NonNull;

import com.angik.chotoderchora.Interface.BanglaAlphabetAudioLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetBanglaAlphabetAudioTask implements Runnable {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bangla Alphabet/Audio");

    private BanglaAlphabetAudioLoadCallback callback;
    private String query;

    private String audioUrl;

    public GetBanglaAlphabetAudioTask(BanglaAlphabetAudioLoadCallback callback, String query) {
        this.callback = callback;
        this.query= query;
    }

    @Override
    public void run() {
        databaseReference.child(query).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    audioUrl = snapshot.getValue(String.class);
                }

                callback.onAudioLoadCallback(audioUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
