package com.draw.suckhoe.model;

public class Reminder {
    String nameReminder;
    String timeReminder;
    String dayReminder;
    int isSwitchOn;

    public Reminder(String nameReminder, String timeReminder, String dayReminder, int isSwitchOn) {
        this.nameReminder = nameReminder;
        this.timeReminder = timeReminder;
        this.dayReminder = dayReminder;
        this.isSwitchOn = isSwitchOn;
    }

    public String getNameReminder() {
        return nameReminder;
    }

    public void setNameReminder(String nameReminder) {
        this.nameReminder = nameReminder;
    }

    public String getTimeReminder() {
        return timeReminder;
    }

    public void setTimeReminder(String timeReminder) {
        this.timeReminder = timeReminder;
    }

    public String getDayReminder() {
        return dayReminder;
    }

    public void setDayReminder(String dayReminder) {
        this.dayReminder = dayReminder;
    }

    public int getIsSwitchOn() {
        return isSwitchOn;
    }

    public void setIsSwitchOn(int isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
    }
}
