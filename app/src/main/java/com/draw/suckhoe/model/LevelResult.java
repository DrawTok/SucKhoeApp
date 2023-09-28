package com.draw.suckhoe.model;

public class LevelResult {
    private final String nameRes;
    private final String levelRes;

    public LevelResult(String nameRes, String levelRes) {
        this.nameRes = nameRes;
        this.levelRes = levelRes;
    }

    public String getNameRes() {
        return nameRes;
    }

    public String getLevelRes() {
        return levelRes;
    }
}
