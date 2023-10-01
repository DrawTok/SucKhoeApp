package com.draw.suckhoe.view.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.draw.suckhoe.R;

import java.util.ArrayList;
import java.util.List;

public class DotWithTextView extends View {

    private Paint dotPaint;
    private Paint textPaint;
    private List<DotInfo> dotInfoList;

    public DotWithTextView(Context context) {
        super(context);
        init();
    }

    public DotWithTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotWithTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dotPaint = new Paint();
        dotPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        //textPaint.setTextAlign(Paint.Align.CENTER); // Canh giữa văn bản

        dotInfoList = new ArrayList<>();
        dotInfoList.add(new DotInfo(getContext().getString(R.string.bPressure_low), ContextCompat.getColor(getContext(), R.color.blue_4th)));
        dotInfoList.add(new DotInfo(getContext().getString(R.string.bPressure_normal),ContextCompat.getColor(getContext(), R.color.green)));
        dotInfoList.add(new DotInfo(getContext().getString(R.string.bPressure_high), ContextCompat.getColor(getContext(), R.color.yellow_primary)));
        dotInfoList.add(new DotInfo(getContext().getString(R.string.bPressure_stage), ContextCompat.getColor(getContext(), R.color.orange_primary)));
        dotInfoList.add(new DotInfo(getContext().getString(R.string.bPressure_stage), ContextCompat.getColor(getContext(), R.color.orange_secondary)));
        dotInfoList.add(new DotInfo(getContext().getString(R.string.bPressure_stage), ContextCompat.getColor(getContext(), R.color.red_primary)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = 50; // Điểm bắt đầu từ bên trái
        int currentX = startX;
        int currentY = 0;

        for (DotInfo dotInfo : dotInfoList) {
            // Tính toán tọa độ của văn bản
            Rect bounds = new Rect();
            textPaint.getTextBounds(dotInfo.getLabel(), 0, dotInfo.getLabel().length(), bounds);
            float textWidth = bounds.width();
            float textX = currentX + textWidth / 2 + 10; // Cách chấm một khoảng
            float textY = currentY + textPaint.getTextSize() / 2 + bounds.height() / 2;

            // Tính toán tọa độ của chấm
            float x = currentX;
            float y = textY; // Sử dụng tọa độ y của văn bản

            // Vẽ chấm
            dotPaint.setColor(dotInfo.getColor());
            canvas.drawCircle(x, y, textPaint.getTextSize() / 2, dotPaint);

            // Kiểm tra xem label có bị ẩn không
            if (textX + textWidth > getWidth()) {
                // Nếu label bị ẩn, di chuyển xuống dòng
                currentX = startX;
                currentY += textPaint.getTextSize() + 10;
                textX = textWidth / 2 + 10; // Cập nhật tọa độ x
                textY = currentY + bounds.height() / 2;
            }

            // Vẽ văn bản bên cạnh chấm
            canvas.drawText(dotInfo.getLabel(), textX, textY, textPaint);

            // Di chuyển đến vị trí của dot và label tiếp theo
            currentX += textWidth; // Cách các dot và label một khoảng
        }
    }




    private static class DotInfo {
        private final String label;
        private final int color;

        DotInfo(String label, int color) {
            this.label = label;
            this.color = color;
        }

        String getLabel() {
            return label;
        }

        int getColor() {
            return color;
        }
    }
}
