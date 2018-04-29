package edu.cmu.ecobin;

/**
 * Created by Onedollar on 4/25/18.
 */

public class User {
    private static User user;
    private static String userID;
    private static String userEmail;
    private static String userName;
    private static String facebookID;
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