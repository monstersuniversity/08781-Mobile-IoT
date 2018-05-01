package edu.cmu.ecobin;

/**
 * Created by Onedollar on 5/1/18.
 */

public class Friend {
    private String name;
    private float percent;
    private String facebookid;
    private String userid;

    Friend(String facebookid) {
        this.facebookid = facebookid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public float getPercent() {
        return percent;
    }
    public String toString() {
        return "facebookid = " + facebookid + "name = " + name + "userid = " + userid + "percent = " + String.valueOf(percent);
    }

}
