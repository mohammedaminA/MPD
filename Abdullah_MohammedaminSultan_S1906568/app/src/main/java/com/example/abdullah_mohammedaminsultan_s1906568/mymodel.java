package com.example.abdullah_mohammedaminsultan_s1906568;

public class mymodel {
    String title, description, date;

    public mymodel(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public mymodel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
