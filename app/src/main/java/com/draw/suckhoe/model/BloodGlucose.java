package com.draw.suckhoe.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bloodGlucose")
public class BloodGlucose {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "levelBGlucose")
    float levelBGlucose;
    String time;

    public BloodGlucose(int id, float levelBGlucose, String time) {
        this.id = id;
        this.levelBGlucose = levelBGlucose;
        this.time = time;
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

    public void setLevelBGlucose(float levelBGlucose) {
        this.levelBGlucose = levelBGlucose;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
