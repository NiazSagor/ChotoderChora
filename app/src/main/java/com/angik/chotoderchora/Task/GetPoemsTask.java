package com.angik.chotoderchora.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angik.chotoderchora.Interface.PoemsLoadCallback;
import com.angik.chotoderchora.Model.Poem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetPoemsTask implements Runnable {

    private final DatabaseReference databaseReferencePoems = FirebaseDatabase.getInstance().getReference("Poem").child("Title");

    private PoemsLoadCallback callback;

    private final List<Poem> allPoems = new ArrayList<>();

    public GetPoemsTask(PoemsLoadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {

        databaseReferencePoems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        allPoems.add(
                                dataSnapshot.getValue(Poem.class)
                        );
                    }

                    callback.onPoemsLoadCallback(allPoems);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
