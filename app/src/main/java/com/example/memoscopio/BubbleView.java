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
    private int diameter = 200;
    private double speed = 2.0;

    private int maxLeft;
    private int maxTop;
    private int maxRight;
    private int maxBottom;

    private boolean starting = true;

    private ShapeDrawable bubble;

    private ShapeDrawable memo1;
    private ShapeDrawable memo2;
    private boolean found1 = false;
    private boolean found2 = false;

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


        int offsetX = new Random().nextInt(x * 2 + 1) - x;
        int offsetY = new Random().nextInt(y * 2 + 1) - y;
        memo1 = new ShapeDrawable(new OvalShape());
        memo1.setBounds(x  + offsetX, y + offsetY, x + diameter + offsetX, y + diameter + offsetY);
        memo1.getPaint().setColor(Color.YELLOW);

        offsetX = new Random().nextInt(x * 2 + 1) - x;
        offsetY = new Random().nextInt(y * 2 + 1) - y;
        memo2 = new ShapeDrawable(new OvalShape());
        memo2.setBounds(x + offsetX, y + offsetY, x + diameter + offsetX, y + diameter + offsetY);
        memo2.getPaint().setColor(Color.YELLOW);


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
        if(starting) {
            memo1.draw(canvas);
            memo2.draw(canvas);
        } else {
            if(found1){
                memo1.draw(canvas);
            }
            if(found2) {
                memo2.draw(canvas);
            }
        }


        canvas.drawText(secondsRemaining, 10, 100, paint);
        canvas.drawText(distance, 10, 200, paint);
    }

    protected void move(float f, float g) {
        if(starting) return;
        if(x < maxLeft && f > 0) return;
        if(x > maxRight && f < 0) return;
        if(y < maxTop && g < 0) return;
        if(y > maxBottom && g > 0) return;

        if(bubble.getBounds().intersect(memo1.getBounds())){
            distance = "true";
            found1 = true;
        } else {
            distance = "false";
        }

        if(bubble.getBounds().intersect(memo2.getBounds())){
            distance = "true";
            found2 = true;
        } else {
            distance = "false";
        }

        if( found1 && found2 ) starting = true;

        x = (int) (x - f * speed);
        y = (int) (y + g * speed);
        bubble.setBounds(x, y, x + diameter, y + diameter);
        invalidate();
    }
}