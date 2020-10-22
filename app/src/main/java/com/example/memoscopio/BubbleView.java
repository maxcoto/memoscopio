package com.example.memoscopio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class BubbleView extends View {

    private int x;
    private int y;
    private int width;
    private int height;
    private int diameter = 100;

    private int maxLeft;
    private int maxTop;
    private int maxRight;
    private int maxBottom;

    private ShapeDrawable bubble;

    public BubbleView(Context context, int width, int height)
    {
        super(context);

        this.width = width;
        this.height = height;
        this.x = (width-diameter)/2;
        this.y = (height/2) - diameter;

        this.maxLeft = diameter;
        this.maxTop = diameter;
        this.maxRight = width - diameter;
        this.maxBottom = height - diameter;

        bubble = new ShapeDrawable(new OvalShape());
        bubble.setBounds(x, y, x + diameter, y + diameter);
        bubble.getPaint().setColor(Color.BLUE);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bubble.draw(canvas);
    }

    protected void move(float f, float g) {
        if(x < maxLeft && f < 0) return;
        if(x > maxRight && f > 0) return;
        if(y < maxTop && g < 0) return;
        if(y > maxBottom && g > 0) return;

        x = (int) (x + f);
        y = (int) (y + g);
        bubble.setBounds(x, y, x + diameter, y + diameter);
        invalidate();
    }
}