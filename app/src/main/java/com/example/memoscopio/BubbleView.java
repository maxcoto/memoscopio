package com.example.memoscopio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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
    private int diameter = 200;
    private double speed = 2.0;

    private int maxLeft;
    private int maxTop;
    private int maxRight;
    private int maxBottom;

    private boolean starting = true;

    private ShapeDrawable bubble;

    private Memo memo1;
    private Memo memo2;

    private ArrayList<ShapeDrawable> rects;

    private Paint paint;

    String secondsRemaining = "5";
    String distance = "false";

    public BubbleView(Context context, int width, int height) {
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

        memo1 = new Memo(x, y);
        memo2 = new Memo(x, y);

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

        memo1.draw(canvas, starting);
        memo2.draw(canvas, starting);

        canvas.drawText(secondsRemaining, 10, 100, paint);
        canvas.drawText(distance, 10, 200, paint);
    }

    protected void move(float f, float g) {
        if(starting) return;
        this.check();

        if(x < maxLeft   && f > 0) f=0;
        if(x > maxRight  && f < 0) f=0;
        if(y < maxTop    && g < 0) g=0;
        if(y > maxBottom && g > 0) g=0;

        x = (int) (x - f * speed);
        y = (int) (y + g * speed);
        bubble.setBounds(x, y, x + diameter, y + diameter);
        invalidate();
    }

    private void check(){
        Rect rect = bubble.getBounds();
        memo1.check(rect);
        memo2.check(rect);

        if( memo1.found && memo2.found ) starting = true;
    }
}