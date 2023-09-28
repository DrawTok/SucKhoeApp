package com.draw.suckhoe.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weightBMI")
public class Weight {
    @PrimaryKey(autoGenerate = true)
    int id;
    int age;
    float weight;
    float height;
    String time;

    public Weight(int id, int age, float weight, float height, String time) {
        this.id = id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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
}
