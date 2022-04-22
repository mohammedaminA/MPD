package com.example.abdullah_mohammedaminsultan_s1906568;


public class RssFeedModel {

    public String title;
    public String link;
    public String description;
    public String date;
    public String lon;
    public String overstrt, overend;
    public String lat;
    public String road;

    public RssFeedModel() { }

    public RssFeedModel(String title, String link, String description, String date, String lon, String lat, String road, String overstrt, String overend) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
        this.lon = lon;
        this.overstrt = overstrt;
        this.overend = overend;
        this.lat = lat;
        this.road = road;
    }

}
