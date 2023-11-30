package com.draw.suckhoe.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder")
public class Reminder implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    int idReminder;
    String nameReminder;
    String timeReminder;
    String dayReminder;

    int isMonday;
    int isTuesday;
    int isWednesday;
    int isThursday;
    int isFriday;
    int isSaturday;
    int isSunday;
    int isSwitchOn;

    public Reminder(int idReminder, String nameReminder, String timeReminder,
                    String dayReminder, int isMonday, int isTuesday, int isWednesday,
                    int isThursday, int isFriday, int isSaturday, int isSunday, int isSwitchOn) {
        this.idReminder = idReminder;
        this.nameReminder = nameReminder;
        this.timeReminder = timeReminder;
        this.dayReminder = dayReminder;
        this.isMonday = isMonday;
        this.isTuesday = isTuesday;
        this.isWednesday = isWednesday;
        this.isThursday = isThursday;
        this.isFriday = isFriday;
        this.isSaturday = isSaturday;
        this.isSunday = isSunday;
        this.isSwitchOn = isSwitchOn;
    }

    public int getIdReminder() {
        return idReminder;
    }

    public String getNameReminder() {
        return nameReminder;
    }

    public String getTimeReminder() {
        return timeReminder;
    }

    public String getDayReminder() {
        return dayReminder;
    }

    public int getIsMonday() {
        return isMonday;
    }

    public int getIsTuesday() {
        return isTuesday;
    }

    public int getIsWednesday() {
        return isWednesday;
    }

    public int getIsThursday() {
        return isThursday;
    }

    public int getIsFriday() {
        return isFriday;
    }

    public int getIsSaturday() {
        return isSaturday;
    }

    public int getIsSunday() {
        return isSunday;
    }

    public int getIsSwitchOn() {
        return isSwitchOn;
    }

    public void setIsSwitchOn(int isSwitchOn)
    {
        this.isSwitchOn = isSwitchOn;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idReminder);
        dest.writeString(nameReminder);
        dest.writeString(timeReminder);
        dest.writeString(dayReminder);
        dest.writeInt(isMonday);
        dest.writeInt(isThursday);
        dest.writeInt(isWednesday);
        dest.writeInt(isFriday);
        dest.writeInt(isFriday);
        dest.writeInt(isSaturday);
        dest.writeInt(isSunday);
        dest.writeInt(isSwitchOn);
    }

    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>()
    {

        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
    private Reminder(Parcel in)
    {
        idReminder = in.readInt();
        nameReminder = in.readString();
        timeReminder = in.readString();
        dayReminder = in.readString();
        isMonday = in.readInt();
        isTuesday = in.readInt();
        isWednesday = in.readInt();
        isThursday = in.readInt();
        isFriday = in.readInt();
        isSaturday = in.readInt();
        isSunday = in.readInt();
        isSwitchOn = in.readInt();
    }
}
