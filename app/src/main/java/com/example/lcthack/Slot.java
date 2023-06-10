package com.example.lcthack;

public class Slot {
    private String department;
    private String date;
    private String time;

    public Slot(String department, String date, String time) {
        this.department = department;
        this.date = date;
        this.time = time;
    }

    public String getDepartment() {
        return department;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}