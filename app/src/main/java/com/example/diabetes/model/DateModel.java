package com.example.diabetes.model;

public class DateModel {
    private int date;
    private String name;

    public DateModel(int date, String name) {
        this.date = date;
        this.name = name;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
