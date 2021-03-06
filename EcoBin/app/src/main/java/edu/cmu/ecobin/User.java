package edu.cmu.ecobin;

import android.graphics.Bitmap;

/**
 * Created by Onedollar on 4/25/18.
 */

public class User {
    private static User user;
    private static String userID;
    private static String userEmail;
    private static String userName;
    private static String facebookID;
    private static float percent;
    private static Bitmap pic;
    private User(){};
    public static User getInstance(){
        if (user == null){
            user = new User();
        }
        return user;
    }

    public void setUserID(String userID){
        User u = User.getInstance();
        u.userID = userID;
    }

    public void setPic(Bitmap pic){
        User u = User.getInstance();
        u.pic = pic;
    }
    public void setPercent(float percent){
        User u = User.getInstance();
        u.percent = percent;
    }
    public void setUserEmail(String email){
        User u = User.getInstance();
        u.userEmail = email;
    }
    public void setUserName(String name){
        User u = User.getInstance();
        u.userName = name;
    }
    public void setFacebookID(String facebookID){
        User u = User.getInstance();
        u.facebookID = facebookID;
    }

    public String getUserID(){
        User u = User.getInstance();
        return u.userID;
    }
    public Bitmap getPic(){
        User u = User.getInstance();
        return u.pic;
    }
    public float getPercent(){
        User u = User.getInstance();
        return u.percent;
    }
    public String getUserEmail(){
        User u = User.getInstance();
        return u.userEmail;
    }
    public String getUserName(){
        User u = User.getInstance();
        return u.userName;
    }
    public String getFacebookID(){
        User u = User.getInstance();
        return u.facebookID;
    }

    public void clearUserID(){
        User u = User.getInstance();
        u = null;
    }

}