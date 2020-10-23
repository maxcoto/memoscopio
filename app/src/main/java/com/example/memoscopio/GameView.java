package com.example.memoscopio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.View;

public class GameView extends View {

    private enum State {
        STARTING,
        PLAYING,
        PAUSED,
        FINISHED
    }

    private State state = State.STARTING;

    private final int diameter = 200;
    private Bubble bubble;
    private Memo memo1;
    private Memo memo2;

    private Paint paint;
    private Context context;

    private long time;
    private int penalty = 0;
    private String message1 = "5";
    private String message2 = "";
    private String message3 = "";

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
                message1 = "" + (millisUntilFinished / 1000);
                invalidate();
            }
            public void onFinish() {
                message1 = "";
                time = System.currentTimeMillis();
                state = State.PLAYING;
            }
        }.start();


        paint = new Paint();
        paint.setTextSize(70);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bubble.draw(canvas);

        boolean showMemos = state != State.PLAYING;
        memo1.draw(canvas, showMemos);
        memo2.draw(canvas, showMemos);

        canvas.drawText(message1, 20, 80, paint);
        canvas.drawText(message2, 20, 150, paint);
        canvas.drawText(message3, 20, 230, paint);
    }

    protected void move(float f, float g) {
        if(state == State.PLAYING) {
            this.check();
            bubble.move(f, g);
        }
        invalidate();
    }

    protected void help(boolean pause) {
        if(pause){
            if(state == State.PLAYING){
                state = State.PAUSED;
                penalty += 10;
            }
        } else {
            if(state == State.PAUSED){
                state = State.PLAYING;
            }
        }
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
        state = State.FINISHED;

        double elapsed = ((System.currentTimeMillis() - time)/1000.0) + penalty;
        String elapsedString = String.format("%.3f", elapsed);
        String penaltyString = String.format("%d", penalty);

        message1 = "Muy bien, los encontraste!";
        message2 = "Tu tiempo fue: " + elapsedString + "s\n" ;
        message3 += "Penalidad: " + penaltyString + "s";

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {};
            public void onFinish() {
                Intent intent = new Intent(context, MenuActivity.class);
                context.startActivity(intent);
            }
        }.start();
    }
}