package com.tripfortrip.tripfortrip._01_MyJourney;


import java.io.Serializable;

public class JourneyBean implements Serializable{
    private  int id;
    private int pic;
    private String title;
    private String dateStart;
    private String dateEnd;
    public JourneyBean() {
    }

    public JourneyBean(int pic, String title, String dateStart,String dateEnd) {
        this.pic = pic;
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd=dateEnd;
    }

    public JourneyBean(int id, int pic, String title, String dateStart, String dateEnd) {
        this.id = id;
        this.pic = pic;
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
