package com.draw.suckhoe.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bloodPressure")
public class BloodPressure implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    int systolic; // tâm thu
    int diastolic; // tâm trương
    int pulse; // xung
    String time;

    int type;

    public BloodPressure(int id, int systolic, int diastolic, int pulse, String time, int type) {
        this.id = id;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
        this.time = time;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(systolic);
        dest.writeInt(diastolic);
        dest.writeInt(pulse);
        dest.writeString(time);
        dest.writeInt(type);
    }

    public static final Parcelable.Creator<BloodPressure> CREATOR = new Parcelable.Creator<BloodPressure>() {
        public BloodPressure createFromParcel(Parcel in) {
            return new BloodPressure(in);
        }

        public BloodPressure[] newArray(int size) {
            return new BloodPressure[size];
        }
    };

    private BloodPressure(Parcel in) {
        id = in.readInt();
        systolic = in.readInt();
        diastolic = in.readInt();
        pulse = in.readInt();
        time = in.readString();
        type = in.readInt();
    }
}
