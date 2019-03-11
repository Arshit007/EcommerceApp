package com.example.arshit.ecommerceapp.Notification;

public class Sender {

    public String to;
    public Notification notification;


    public Sender() {
    }

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
