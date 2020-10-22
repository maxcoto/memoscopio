package com.example.memoscopio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class MyView extends View
{

    private int x;
    private int y;
    private int r = 100;
    private Paint paint;
    private Canvas grid;

    public MyView(Context context, int width, int height)
    {
        super(context);

        x = width/2;
        y = height/2;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(x, y, r, paint);
        invalidate();
    }
}