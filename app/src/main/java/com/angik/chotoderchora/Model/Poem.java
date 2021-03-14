package com.angik.chotoderchora.Model;

public class Poem {

    String poemCode,
            poemImage,
            poemTitle;

    public Poem() {
    }

    public Poem(String poemCode, String poemImage, String poemTitle) {
        this.poemCode = poemCode;
        this.poemImage = poemImage;
        this.poemTitle = poemTitle;
    }

    public String getPoemCode() {
        return poemCode;
    }

    public String getPoemImage() {
        return poemImage;
    }

    public String getPoemTitle() {
        return poemTitle;
    }
}
