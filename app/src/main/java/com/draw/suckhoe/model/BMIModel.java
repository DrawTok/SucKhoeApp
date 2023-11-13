package com.draw.suckhoe.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bmimodel")
public class BMIModel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    int type;
    float weight;
    float height;

    float bmi;
    String time;

    public BMIModel(int id, float weight, float height, float bmi, String time, int type) {
        this.id = id;
        this.type = type;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getWeight() {
        return weight;
    }

    public float getBmi() {
        return bmi;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(type);
        dest.writeFloat(weight);
        dest.writeFloat(height);
        dest.writeFloat(bmi);
        dest.writeString(time);
    }

    public static final Parcelable.Creator<BMIModel> CREATOR = new Parcelable.Creator<BMIModel>()
    {
        @Override
        public BMIModel createFromParcel(Parcel source) {
            return new BMIModel(source);
        }

        @Override
        public BMIModel[] newArray(int size) {
            return new BMIModel[size];
        }
    };

    public BMIModel(Parcel source) {
        id = source.readInt();
        type = source.readInt();
        weight = source.readFloat();
        height = source.readFloat();
        bmi = source.readFloat();
        time = source.readString();
    }
}
