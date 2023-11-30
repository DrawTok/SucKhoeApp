package com.draw.suckhoe.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "water")
public class Water implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    int mlWater;
    String time;
    int isGoals;
    public Water(int id, int mlWater, String time, int isGoals) {
        this.id = id;
        this.mlWater = mlWater;
        this.time = time;
        this.isGoals = isGoals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMlWater() {
        return mlWater;
    }

    public void setMlWater(int mlWater) {
        this.mlWater = mlWater;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsGoals() {
        return isGoals;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(mlWater);
        dest.writeString(time);
        dest.writeInt(isGoals);
    }

    public Water(Parcel in)
    {
        id = in.readInt();
        mlWater = in.readInt();
        time = in.readString();
        isGoals = in.readInt();
    }

    public static final Parcelable.Creator<Water> CREATOR = new Parcelable.Creator<Water>()
    {

        @Override
        public Water createFromParcel(Parcel source) {
            return new Water(source);
        }

        @Override
        public Water[] newArray(int size) {
            return new Water[size];
        }
    };
}
