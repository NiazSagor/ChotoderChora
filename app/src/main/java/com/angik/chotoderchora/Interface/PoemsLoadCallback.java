package com.angik.chotoderchora.Interface;

import com.angik.chotoderchora.Model.Poem;

import java.util.List;

public interface PoemsLoadCallback {

    void onPoemsLoadCallback(List<Poem> allPoems);

}
