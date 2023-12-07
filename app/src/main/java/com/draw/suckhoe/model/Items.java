package com.draw.suckhoe.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Items {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "item_id")
    public int itemId;
    public String itemName;
    public String content;
    public int titleId;

    public Items(){}
    @Ignore
    public Items(int itemId, String itemName, String content, int titleId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.content = content;
        this.titleId = titleId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }
}
