package edu.cmu.ecobin;

import android.graphics.Bitmap;

import java.util.Comparator;

/**
 * Created by Onedollar on 5/1/18.
 */

public class Friend implements Comparable<Friend>{
    private String name;
    private float percent;
    private String facebookid;
    private String userid;
    private Bitmap pic;

    Friend(String facebookid) {
        this.facebookid = facebookid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getFacebookid() {
        return facebookid;
    }

    public void setFacebookid(String facebookid) {
        this.facebookid = facebookid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public float getPercent() {
        return percent;
    }
    public String toString() {
        return "facebookid = " + facebookid + " name = " + name + " userid = " + userid + " percent = " + String.valueOf(percent) + "pic = " + pic;
    }

    @Override
    public int compareTo(Friend o) {
        if (this.getPercent() == o.getPercent()) {
            return 0;
        } else {
            return this.getPercent() > o.getPercent() ? -1 : 1;
        }
    }
}