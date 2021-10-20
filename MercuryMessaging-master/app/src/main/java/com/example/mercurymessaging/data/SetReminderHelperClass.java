package com.example.mercurymessaging.data;

import java.util.List;

public class SetReminderHelperClass {
    String message;
    String time;
    String date;
    String currentTime;



    String currentDate;
    List<String> recepients;
    List<String> services;

    public SetReminderHelperClass(String message, String time, String date,
                                  List<String> recepients, List<String> services, String currentDate, String currentTime) {
        this.message = message;
        this.time = time;
        this.date = date;
        this.recepients = recepients;
        this.services = services;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getReceipnts() {
        return recepients;
    }

    public void setReceipnts(List<String> recepients) {
        this.recepients = recepients;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
