package com.draw.suckhoe.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bloodGlucose")
public class BloodGlucose implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "levelBGlucose")
    float levelBGlucose;
    String time;
    int type;

    public BloodGlucose(int id, float levelBGlucose, String time, int type) {
        this.id = id;
        this.levelBGlucose = levelBGlucose;
        this.time = time;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLevelBGlucose() {
        return levelBGlucose;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(levelBGlucose);
        dest.writeString(time);
        dest.writeInt(type);
    }

    public static final Parcelable.Creator<BloodGlucose> CREATOR = new Parcelable.Creator<BloodGlucose>() {
        public BloodGlucose createFromParcel(Parcel in) {
            return new BloodGlucose(in);
        }

        public BloodGlucose[] newArray(int size) {
            return new BloodGlucose[size];
        }
    };

    private BloodGlucose(Parcel in) {
        id = in.readInt();
        levelBGlucose = in.readFloat();
        time = in.readString();
        type = in.readInt();
    }
}
