package org.techtown.ThreeMate;



public class User {
    private String userUID;
    private String userName;
    private String userProfile;
    private String userEmail;
    private String bornDate;
    private String gender;
    private String bodyLength;
    private String bodyWeight;


    public User(){
    }



    public User(String userUID, String userName, String  userProfile, String userEmail, String bornDate, String gender, String bodyLength, String bodyWeight){
        this.userUID = userUID;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userEmail = userEmail;
        this.bornDate = bornDate;
        this.gender = gender;
        this.bodyLength = bodyLength;
        this.bodyWeight = bodyWeight;
    }

    public String getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(String bodyLength) {
        this.bodyLength = bodyLength;
    }

    public String getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(String bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
