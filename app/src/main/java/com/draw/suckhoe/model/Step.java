package com.draw.suckhoe.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "step")
public class Step {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int numberStep;
    public String time;

    public Step(int id, int numberStep, String time) {
        this.id = id;
        this.numberStep = numberStep;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberStep() {
        return numberStep;
    }

    public void setNumberStep(int numberStep) {
        this.numberStep = numberStep;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
