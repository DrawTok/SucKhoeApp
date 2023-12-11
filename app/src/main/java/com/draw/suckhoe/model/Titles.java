package com.draw.suckhoe.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "titles")
public class Titles implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(titleId);
        dest.writeString(titleName);
        dest.writeString(imageUrl);
        dest.writeInt(typeId);
    }

    public static final Parcelable.Creator<Titles> CREATOR = new Parcelable.Creator<Titles>() {
        public Titles createFromParcel(Parcel in) {
            return new Titles(in);
        }

        public Titles[] newArray(int size) {
            return new Titles[size];
        }
    };

    private Titles(Parcel in) {
        titleId = in.readInt();
        titleName = in.readString();
        imageUrl = in.readString();
        typeId = in.readInt();
    }
}
