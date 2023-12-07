package com.draw.suckhoe.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "titles")
public class Titles {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "title_id")
    public int titleId;
    public String titleName;
    public String imageUrl;
    public int typeId;

    public Titles() {}

    @Ignore
    public Titles(int titleId, String titleName, String imageUrl, int typeId) {
        this.titleId = titleId;
        this.titleName = titleName;
        this.imageUrl = imageUrl;
        this.typeId = typeId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
