package com.draw.suckhoe.utils;

import android.content.Context;

import com.draw.suckhoe.R;
import com.draw.suckhoe.model.BloodGlucose;
import com.draw.suckhoe.model.BloodPressure;

public class ViewColorRenderer {
    public LevelResult renderColorView(BloodPressure bloodPressure, Context context) {
        int type = bloodPressure.getType();
        String name, about;
        int color;

        if (type == 1) {
            name = context.getString(R.string.bPressure_low);
            about = context.getString(R.string.bPressure_level_low);
            color = R.color.blue_4th;
        } else if (type == 2) {
            name = context.getString(R.string.bPressure_normal);
            about = context.getString(R.string.bPressure_level_normal);
            color = R.color.green;
        } else if (type == 3) {
            name = context.getString(R.string.bPressure_high);
            about = context.getString(R.string.bPressure_level_high);
            color = R.color.yellow_primary;
        } else if (type == 4) {
            name = context.getString(R.string.bPressure_stage_1);
            about = context.getString(R.string.bPressure_level_stage1);
            color = R.color.orange_primary;
        } else if (type == 5) {
            name = context.getString(R.string.bPressure_stage_2);
            about = context.getString(R.string.bPressure_level_stage2);
            color = R.color.orange_secondary;
        } else {
            name = context.getString(R.string.bPressure_stage_3);
            about = context.getString(R.string.bPressure_level_stage3);
            color = R.color.red_primary;
        }

        return new LevelResult(name, about, color);
    }

    public LevelResult renderColorView(BloodGlucose glucose, Context context) {
        int type = glucose.getType();
        String name, about;
        int color;

        if (type == 1) {
            name = context.getString(R.string.bGlucose_low);
            about = context.getString(R.string.bGlucose_level_low);
            color = R.color.blue_4th;
        } else if (type == 2) {
            name = context.getString(R.string.bGlucose_normal);
            about = context.getString(R.string.bGlucose_level_normal);
            color = R.color.green;
        } else if (type == 3) {
            name = context.getString(R.string.bGlucose_preDiabetes);
            about = context.getString(R.string.bGlucose_level_preDiabetes);
            color = R.color.yellow_primary;
        } else {
            name = context.getString(R.string.bGlucose_diabetes);
            about = context.getString(R.string.bGlucose_level_diabetes);
            color = R.color.red_primary;
        }

        return new LevelResult(name, about, color);
    }
}
