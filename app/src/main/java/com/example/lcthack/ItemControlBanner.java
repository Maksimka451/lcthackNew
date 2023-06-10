package com.example.lcthack;

public class ItemControlBanner {

    String date;
    String kno;
    String themeofconsulting;
    String time;
    String typeofcontrol;

    public ItemControlBanner(){

    }

    public ItemControlBanner(String date, String kno, String themeofconsulting, String time, String typeofcontrol) {
        this.date = date;
        this.kno = kno;
        this.themeofconsulting = themeofconsulting;
        this.time = time;
        this.typeofcontrol = typeofcontrol;

    }

    public String getKno() {
        return kno;
    }

    public void setKno(String kno) {
        this.kno = kno;
    }

    public String getTypeofcontrol() {
        return typeofcontrol;
    }

    public void setTypeofcontrol(String typeofcontrol) {
        this.typeofcontrol = typeofcontrol;
    }

    public String getThemeofconsulting() {
        return themeofconsulting;
    }

    public void setThemeofconsulting(String themeofconsulting) {
        this.themeofconsulting = themeofconsulting;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
