package com.example.shrikrishna.coep;

/**
 * Created by Shrikrishna on 17-09-2017.
 */

public class MyL {
    String ll,user,ll2;
    boolean picked;
    String timeslot,clg;
    MyL(String ll,String ll2,String user,String timeslot,String clg)
    {
        this.ll=ll;
        this.ll2=ll2;
        this.user=user;
        this.picked=false;
        this.timeslot=timeslot;
        this.clg=clg;
    }
    MyL(String ll,String ll2,String user,boolean picked,String timeslot,String clg)
    {
        this.ll=ll;
        this.ll2=ll2;
        this.user=user;
        this.picked=picked;
        this.clg=clg;
        this.timeslot=timeslot;
    }
    public String getTimeslot() {
        return timeslot;
    }
    public String getClg() {
        return clg;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public String getLl2() {
        return ll2;
    }
    public boolean isPicked() {
        return picked;
    }
    public String getLl() {
        return ll;
    }

    public String getUser() {
        return user;
    }
}
