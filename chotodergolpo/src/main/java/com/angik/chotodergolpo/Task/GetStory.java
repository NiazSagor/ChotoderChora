package com.angik.chotodergolpo.Task;

import androidx.annotation.NonNull;

import com.angik.chotodergolpo.Interface.StoryLoadCallback;
import com.angik.chotodergolpo.Model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetStory implements Runnable {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Story");

    private final String entry;
    private final StoryLoadCallback callback;

    public GetStory(String entry, StoryLoadCallback callback) {
        this.entry = entry;
        this.callback = callback;
    }

    private final List<Story> stories = new ArrayList<>();

    @Override
    public void run() {

        databaseReference.child(entry).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        stories.add(
                                dataSnapshot.getValue(Story.class)
                        );
                    }

                    callback.onStoryLoad(stories);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
