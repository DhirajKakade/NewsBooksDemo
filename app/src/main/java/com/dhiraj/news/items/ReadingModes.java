package com.dhiraj.news.items;

public class ReadingModes{
    public boolean text;
    public boolean image;

    public ReadingModes() {
    }

    public ReadingModes(boolean text, boolean image) {
        this.text = text;
        this.image = image;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }
}
