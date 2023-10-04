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
    int isSwitchOn;

    public Reminder(int idReminder, String nameReminder, String timeReminder, String dayReminder, int isSwitchOn) {
        this.idReminder = idReminder;
        this.nameReminder = nameReminder;
        this.timeReminder = timeReminder;
        this.dayReminder = dayReminder;
        this.isSwitchOn = isSwitchOn;
    }

    public String getNameReminder() {
        return nameReminder;
    }

    public int getIdReminder() {return idReminder;}
    public String getTimeReminder() {return timeReminder;}
    public String getDayReminder() {return dayReminder;}
    public int getIsSwitchOn() {return isSwitchOn;}

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
        isSwitchOn = in.readInt();
    }
}
