package com.tripfortrip.tripfortrip._01_MyJourney;


public class Journey {
    int pic;
    String title;
    String info;

    public Journey() {
    }

    public Journey(int pic, String title, String info) {
        this.pic = pic;
        this.title = title;
        this.info = info;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
