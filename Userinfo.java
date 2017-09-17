package com.example.shrikrishna.coep;

/**
 * Created by Shrikrishna on 17-09-2017.
 */

public class Userinfo {
    String email;
    String collage;
    String timeslot;
    boolean vehicle;
    Userinfo(String email ,String collage,String timeslot,boolean vehicle)
    {
        this.email=email;
        this.collage=collage;
        this.timeslot=timeslot;
        this.vehicle=vehicle;
    }

    public boolean isVehicle() {
        return vehicle;
    }

    public String getCollage() {
        return collage;
    }

    public String getEmail() {
        return email;
    }

    public String getTimeslot() {
        return timeslot;
    }
}
