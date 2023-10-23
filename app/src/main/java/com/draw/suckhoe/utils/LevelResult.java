package com.draw.suckhoe.utils;

public class LevelResult {
    private final String nameRes;
    private final String levelRes;
    private final int type;

    public LevelResult(String nameRes, String levelRes, int type) {
        this.nameRes = nameRes;
        this.levelRes = levelRes;
        this.type = type;
    }

    public String getNameRes() {
        return nameRes;
    }

    public int getType() {
        return type;
    }

    public String getLevelRes() {
        return levelRes;
    }
}
