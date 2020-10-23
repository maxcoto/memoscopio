package com.example.memoscopio;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import java.util.Random;

public class Memo {

    private int diameter = 100;

    public ShapeDrawable bubble;
    public boolean found = false;

    public Memo(int x, int y){

        int offsetX = new Random().nextInt(x * 2 + 1) - x;
        int offsetY = new Random().nextInt(y * 2 + 1) - y;
        bubble = new ShapeDrawable(new OvalShape());
        bubble.setBounds(x  + offsetX, y + offsetY, x + diameter + offsetX, y + diameter + offsetY);
        bubble.getPaint().setColor(Color.YELLOW);

    }
}
