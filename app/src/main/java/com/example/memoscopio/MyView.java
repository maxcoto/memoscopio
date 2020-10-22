package com.example.memoscopio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyView extends View
{

    private int x = 150;
    private int y = 150;
    private int r = 50;
    private Paint paint;
    private Canvas grid;

    private boolean grafica = true;

    public MyView(Context context)
    {
        super(context);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (grafica) {
            canvas.drawCircle(x, y, r, paint);
            grafica = false;
        }
    }
}