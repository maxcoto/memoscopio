package com.example.memoscopio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {

    private final int diameter = 70;
    private final int amount = 1;

    private enum State {
        STARTING,
        PLAYING,
        PAUSED,
        FINISHED
    }

    private State state;

    private Bubble bubble;
    private ArrayList<Memo> memos;

    private Context context;

    private long time;
    private int penalty = 0;

    private Paint paintMessage;
    private Paint paintCountdown;
    private String countdown = "";
    private String message1 = "";
    private String message2 = "";
    private String message3 = "";

    public GameView(Context context, int width, int height) {
        super(context);
        this.context = context;

        int x = (width-diameter)/2;
        int y = (height/2) - diameter;

        bubble = new Bubble(x, y, width, height, diameter);

        memos = new ArrayList<Memo>();
        for(int i=0; i<amount; i++){
            memos.add( new Memo(x, y) );
        }

        paintMessage = new Paint();
        paintCountdown = new Paint();
        paintMessage.setTextSize(70);
        paintCountdown.setTextSize(200);

        start();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bubble.draw(canvas);

        boolean show = state != State.PLAYING;

        for( Memo memo : memos ){
            memo.draw(canvas, show);
        }

        canvas.drawText(countdown, 20, 170, paintCountdown);
        canvas.drawText(message1, 20, 100, paintMessage);
        canvas.drawText(message2, 20, 180, paintMessage);
        canvas.drawText(message3, 20, 260, paintMessage);
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

        int found = 0;
        for( Memo memo : memos ){
            memo.check(rect);
            found += memo.found ? 1 : 0;
        }

        if( found == amount ) {
            this.finish();
        }
    }

    private void start(){
        state = State.STARTING;

        new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                countdown = "" + (millisUntilFinished / 1000);
            }
            public void onFinish() {
                countdown = "";
                time = System.currentTimeMillis();
                state = State.PLAYING;
            }
        }.start();
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