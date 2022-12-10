package com.usj.smartgarbagemanagementsystem.model;

public class Bin {
    public String id;
    public double latitude;
    public double longitude;
    public int duration;
    public int lock;

    public Bin() {
    }

    public Bin(String id, double latitude, double longitude, int duration, int lock) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.duration = duration;
        this.lock = lock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public int getFillingPercentage(){
        int percentage = 0;
        if (duration <= 300) {
            percentage =100;
        } else if (duration > 300 && duration <= 400) {
            percentage =90;
        } else if (duration > 400 && duration <= 500) {
            percentage =75;
        } else if (duration > 500 && duration <= 600) {
            percentage=50;
        } else if (duration > 600 && duration <= 800) {
                percentage=25;
        } else if (duration > 800) {
                percentage=0;
        }
        return percentage;
    }
}
