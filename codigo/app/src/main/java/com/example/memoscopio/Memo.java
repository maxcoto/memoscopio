package com.example.memoscopio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import java.util.Random;

public class Memo {

    private final static int DIAMETER = 100;

    public ShapeDrawable bubble;
    public boolean found;

    public Memo(int x, int y){

        found = false;
        int offsetX = new Random().nextInt(x * 2 + 1) - x;
        int offsetY = new Random().nextInt(y * 2 + 1) - y;
        bubble = new ShapeDrawable(new OvalShape());
        bubble.setBounds(x  + offsetX, y + offsetY, x + DIAMETER + offsetX, y + DIAMETER + offsetY);
        bubble.getPaint().setColor(Color.YELLOW);

    }

    public void draw(Canvas canvas, boolean show){
        if(show || found){
            bubble.draw(canvas);
        }
    }

    public void check(Rect rect){
        if(rect.intersect(bubble.getBounds())){
            found = true;
        }
    }
}
