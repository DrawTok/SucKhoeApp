package com.draw.suckhoe.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class WaveWater extends View {
    private Paint wavePaint;
    private Paint textPaint;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private final int amplitude = 100;

    private float maxProgress = 2000;
    private float defProgress = 200;
    private float progress = 0f;
    private int textProgress = 0;
    private final Point startPoint = new Point();

    public WaveWater(Context context) {
        super(context);
        init();
    }

    public WaveWater(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public int getMaxProgress()
    {
        return (int) maxProgress;
    }

    public int getProgress()
    {
        return (int) progress;
    }
    public int getDefProgress()
    {
        return (int) defProgress;
    }
    public void setMaxProgress(int max)
    {
        this.maxProgress = max;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
        this.textProgress = progress;
    }

    public void setDefProgress(int def)
    {
        this.defProgress = def;
    }

    public void increaseProgress() {
        this.progress += defProgress;
        textProgress = (int) progress;
    }

    public void decreaseProgress() {
        this.progress -= defProgress;
        if (progress <= 0) {
            this.progress = 0;
        }
        textProgress = (int) progress;
    }

    private void init() {
        wavePaint = new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setStrokeWidth(1f);
        wavePaint.setColor(Color.parseColor("#2F968C"));
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setTextSize(100f);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(measureSize(400, widthMeasureSpec), measureSize(400, heightMeasureSpec));
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        startPoint.x = -screenWidth;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawWave(canvas);
        drawText(canvas);
        postInvalidateDelayed(10);
    }

    private void drawText(Canvas canvas) {
        Rect targetRect = new Rect(0, -screenHeight, screenWidth, 0);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int height = targetRect.bottom - targetRect.top;
        int baseline = targetRect.top + (height) / 5 - (fontMetrics.bottom + fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(textProgress + "ml", targetRect.centerX(), baseline, textPaint);
    }

    private void drawWave(Canvas canvas) {
        int startHeight = screenHeight / 5;
        int height = (int) (progress / maxProgress * (screenHeight - startHeight)) + startHeight;
        startPoint.y = -height;
        canvas.translate(0f, screenHeight);
        Path path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        int wave = screenWidth / 4;
        for (int i = 0; i < 4; i++) {
            int startX = startPoint.x + i * wave * 2;
            int endX = startX + 2 * wave;
            if (i % 2 == 0) {
                path.quadTo((startX + endX) / 2, startPoint.y + amplitude, endX, startPoint.y);
            } else {
                path.quadTo((startX + endX) / 2, startPoint.y - amplitude, endX, startPoint.y);
            }
        }
        path.lineTo(screenWidth, startPoint.y);
        path.lineTo(screenWidth, screenHeight);
        path.lineTo(-screenWidth, screenHeight);
        path.lineTo(-screenWidth, startPoint.y);
        path.close();
        canvas.drawPath(path, wavePaint);
        startPoint.x += 20;
        if (startPoint.x > 0) {
            startPoint.x = -screenWidth;
        }
        path.reset();
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case View.MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.EXACTLY:
                result = size;
                break;
            default:
                break;
        }
        return result;
    }
}
