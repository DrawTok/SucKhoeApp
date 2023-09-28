package com.draw.suckhoe.view.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;

import com.draw.suckhoe.R;

public class CustomSeekBar extends AppCompatSeekBar {

    private Paint paint;
    private int[] segmentColors;

    public CustomSeekBar(Context context) {
        super(context);
        init(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // Lấy màu từ tệp color.xml
        segmentColors = new int[]{
                ContextCompat.getColor(context, R.color.green),
                ContextCompat.getColor(context, R.color.blue_4th),
                ContextCompat.getColor(context, R.color.yellow_primary),
                ContextCompat.getColor(context, R.color.orange_primary),
                ContextCompat.getColor(context, R.color.orange_secondary),
                ContextCompat.getColor(context, R.color.red_primary)
        };
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int numSegments = segmentColors.length;
        int segmentWidth = getWidth() / numSegments;

        // Draw each segment with a different color
        for (int i = 0; i < numSegments; i++) {
            int segmentColor = segmentColors[i];
            paint.setColor(segmentColor);
            int startX = i * segmentWidth;
            int endX = (i + 1) * segmentWidth;
            canvas.drawRect(startX, 0, endX, getHeight(), paint);
        }
    }
}
