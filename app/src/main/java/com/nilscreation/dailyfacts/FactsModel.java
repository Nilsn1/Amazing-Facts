package com.nilscreation.dailyfacts;

public class FactsModel {

    String poster, category, title, text;


    public FactsModel(String poster, String category, String title, String text) {

        this.poster = poster;
        this.category = category;
        this.title = title;
        this.text = text;
    }

    public String getPoster() {
        return poster;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
