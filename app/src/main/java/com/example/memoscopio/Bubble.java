package com.example.memoscopio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class Bubble {

    private static final double SPEED = 2.0;
    private final int diameter;

    public ShapeDrawable bubble;

    private final int maxLeft;
    private final int maxTop;
    private final int maxRight;
    private final int maxBottom;

    private int x;
    private int y;

    public Bubble(int x, int y, int width, int height, int diameter){
        this.x = x;
        this.y = y;
        this.diameter = diameter;

        this.maxLeft = 0;
        this.maxTop = 0;
        this.maxRight = width - diameter;
        this.maxBottom = height - diameter;

        bubble = new ShapeDrawable(new OvalShape());
        bubble.setBounds(x, y, x + diameter, y + diameter);
        bubble.getPaint().setColor(Color.BLUE);
    }

    public void draw(Canvas canvas){
        bubble.draw(canvas);
    }

    public Rect getBounds(){
        return bubble.getBounds();
    }

    protected void move(float f, float g) {
        if(x < maxLeft   && f > 0) f=0;
        if(x > maxRight  && f < 0) f=0;
        if(y < maxTop    && g < 0) g=0;
        if(y > maxBottom && g > 0) g=0;

        x = (int) (x - f * SPEED);
        y = (int) (y + g * SPEED);
        bubble.setBounds(x, y, x + diameter, y + diameter);
    }
}
