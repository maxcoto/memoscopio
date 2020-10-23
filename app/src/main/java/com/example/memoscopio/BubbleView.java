package com.example.memoscopio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

public class BubbleView extends View {

    private int x;
    private int y;
    private int width;
    private int height;
    private int diameter = 50;
    private double speed = 2.0;

    private int maxLeft;
    private int maxTop;
    private int maxRight;
    private int maxBottom;

    private boolean starting = true;
    private boolean found = false;

    private ShapeDrawable bubble;

    private ShapeDrawable memo;

    private ArrayList<ShapeDrawable> rects;

    private Paint paint;

    String secondsRemaining = "5";

    public BubbleView(Context context, int width, int height)
    {
        super(context);

        this.width = width;
        this.height = height;
        this.x = (width-diameter)/2;
        this.y = (height/2) - diameter;

        this.maxLeft = 0;
        this.maxTop = 0;
        this.maxRight = width - diameter;
        this.maxBottom = height - diameter;

        bubble = new ShapeDrawable(new OvalShape());
        bubble.setBounds(x, y, x + diameter, y + diameter);
        bubble.getPaint().setColor(Color.BLUE);

        int offset = 200;
        memo = new ShapeDrawable(new OvalShape());
        memo.setBounds(x-offset, y-offset, x + diameter - offset, y + diameter - offset);
        memo.getPaint().setColor(Color.YELLOW);


        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                secondsRemaining = "" + (millisUntilFinished / 1000);
                invalidate();
            }
            public void onFinish() {
                secondsRemaining = "";
                starting = false;
            }
        }.start();


        paint = new Paint();
        paint.setTextSize(100);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bubble.draw(canvas);
        if(found || starting) {
            memo.draw(canvas);
        }

        canvas.drawText(secondsRemaining, 10, 50, paint);
    }

    protected void move(float f, float g) {
        if(starting) return;
        if(x < maxLeft && f > 0) return;
        if(x > maxRight && f < 0) return;
        if(y < maxTop && g < 0) return;
        if(y > maxBottom && g > 0) return;

        x = (int) (x - f * speed);
        y = (int) (y + g * speed);
        bubble.setBounds(x, y, x + diameter, y + diameter);
        invalidate();
    }
}