package com.draw.suckhoe.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "water")
public class Water {
    @PrimaryKey(autoGenerate = true)
    int id;
    int mlWater;
    String time;

    public Water(int id, int mlWater, String time) {
        this.id = id;
        this.mlWater = mlWater;
        this.time = time;
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
}
