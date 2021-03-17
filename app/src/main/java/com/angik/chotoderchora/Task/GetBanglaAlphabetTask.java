package com.angik.chotoderchora.Task;

import androidx.annotation.NonNull;

import com.angik.chotoderchora.Interface.BanglaAlphabetLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetBanglaAlphabetTask implements Runnable {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bangla Alphabet/Image");

    private final BanglaAlphabetLoadCallback callback;
    private List<String> allAlphabets;

    public GetBanglaAlphabetTask(BanglaAlphabetLoadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        allAlphabets = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        allAlphabets.add(
                                dataSnapshot.getValue(String.class)
                        );
                    }

                    callback.onBanglaAlphabetLoadCallback(allAlphabets);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
