package com.example.arshit.ecommerceapp.Model;

public class User {

    private String id;
    private String username;
    private String profilePic;
    private int Contact;

    public User(String id, String username, String profilePic, int contact) {
        this.id = id;
        this.username = username;
        this.profilePic = profilePic;
        this.Contact = contact;
    }

    public User()
    {

    }

    public int getContact() {
        return Contact;
    }

    public void setContact(int contact) {
        this.Contact = contact;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() { return profilePic; }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}
