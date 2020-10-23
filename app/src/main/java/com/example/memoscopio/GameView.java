package com.example.memoscopio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.View;

public class GameView extends View {

    private final int diameter = 200;

    private boolean starting = true;
    private boolean helping = false;
    private boolean finished = false;

    private Bubble bubble;
    private Memo memo1;
    private Memo memo2;

    private Paint paint;
    private Context context;
    private long time;

    String message = "5";

    public GameView(Context context, int width, int height) {
        super(context);
        this.context = context;

        int x = (width-diameter)/2;
        int y = (height/2) - diameter;

        bubble = new Bubble(x, y, width, height, diameter);
        memo1 = new Memo(x, y);
        memo2 = new Memo(x, y);

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                message = "" + (millisUntilFinished / 1000);
                invalidate();
            }
            public void onFinish() {
                message = "";
                time = System.currentTimeMillis();
                starting = false;
            }
        }.start();


        paint = new Paint();
        paint.setTextSize(70);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bubble.draw(canvas);

        memo1.draw(canvas, starting);
        memo2.draw(canvas, starting);

        canvas.drawText(message, 20, 70, paint);
    }

    protected void move(float f, float g) {
        if(starting) return;
        this.check();
        bubble.move(f, g);
        invalidate();
    }

    protected void help(boolean stop) {
        //starting = stop;
    }

    private void check(){
        Rect rect = bubble.getBounds();
        memo1.check(rect);
        memo2.check(rect);

        if( memo1.found && memo2.found ) {
            this.finish();
        }
    }

    private void finish(){
        this.starting = true;

        long elapsed = System.currentTimeMillis() - time;
        message = "Muy bien! Tu tiempo fue: " + (elapsed/1000) + "s";
        invalidate();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {};
            public void onFinish() {
                Intent intent = new Intent(context, MenuActivity.class);
                context.startActivity(intent);
            }
        }.start();
    }
}