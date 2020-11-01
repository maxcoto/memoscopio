package com.example.memoscopio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import java.util.Random;

public class Memo {

    // diametro de pelotita amarilla configurable
    private final static int DIAMETER = 60;

    public ShapeDrawable bubble;
    public boolean found;

    // inicializa la pelotita amarilla en una posicion aleatoria de la pantalla
    public Memo(int x, int y){
        found = false;
        int offsetX = new Random().nextInt(x * 2 + 1) - x;
        int offsetY = new Random().nextInt(y * 2 + 1) - y;
        bubble = new ShapeDrawable(new OvalShape());
        bubble.setBounds(x  + offsetX, y + offsetY, x + DIAMETER + offsetX, y + DIAMETER + offsetY);
        bubble.getPaint().setColor(Color.YELLOW);
    }

    // dibuja la pelotita amarilla si se ha encontrado o si se la fuerza mediante el parametro show
    // "show" se usa para forzar ver la pelotita si el estado no es PLAYING.
    public void draw(Canvas canvas, boolean show){
        if(show || found){
            bubble.draw(canvas);
        }
    }

    // setea en encontrada a la pelotita amarilla si se intersecta con la azul
    public void check(Rect rect){
        if(rect.intersect(bubble.getBounds())){
            found = true;
        }
    }
}
