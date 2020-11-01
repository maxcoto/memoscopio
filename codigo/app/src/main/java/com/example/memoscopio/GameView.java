package com.example.memoscopio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.View;

import java.util.ArrayList;

@SuppressLint("ViewConstructor")
public class GameView extends View {

    // diametro de la pelotita azul
    private static final int DIAMETER = 50;
    // cantidad de pelitas amarillas
    private static final int AMOUNT = 10;

    // estados del juego
    public enum State {
        STARTING,
        PLAYING,
        PAUSED,
        FINISHED
    }

    private State state;

    // pelotita azul
    private final Bubble bubble;
    // pelotitas amarillas
    private final ArrayList<Memo> memos = new ArrayList<>();

    private final GameActivity context;

    private long time;
    private int penalty = 0;

    private final Paint paintMessage;
    private final Paint paintCountdown;
    private String countdown = "";
    private String message1 = "";
    private String message2 = "";
    private String message3 = "";

    public GameView(GameActivity context, int width, int height) {
        super(context);
        this.context = context;

        // calcula el centro de la pantalla
        int x = (width - DIAMETER)/2;
        int y = (height/2) - DIAMETER;

        // inicializa la pelotita azul
        bubble = new Bubble(x, y, width, height, DIAMETER);

        //inicializa las pelotitas amarillas
        for(int i=0; i<AMOUNT; i++){
            memos.add( new Memo(x, y) );
        }

        // inicializa los mesajes que apareceran en pantalla
        paintMessage = new Paint();
        paintCountdown = new Paint();
        paintMessage.setTextSize(40);
        paintCountdown.setTextSize(200);

        start();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // dibuja la pelotita azul
        bubble.draw(canvas);

        // esconde las pelotitas amarillas solo si el estado es PLAYING
        boolean show = state != State.PLAYING;
        for( Memo memo : memos ){
            memo.draw(canvas, show);
        }

        // dibuja los mensajes de texto en pantalla
        canvas.drawText(countdown, 20, 170, paintCountdown);
        canvas.drawText(message1, 20, 100, paintMessage);
        canvas.drawText(message2, 20, 180, paintMessage);
        canvas.drawText(message3, 20, 260, paintMessage);
    }


    // mueve la pelotita azul y chequea si se encontraron todas las amarillas
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
                setState(State.PAUSED);
                penalty += 10;
            }
        } else {
            if(state == State.PAUSED){
                setState(State.PLAYING);
            }
        }
    }

    // chequea si se encontraron todas las pelotitas amarillas
    // termina el juego en caso positivo
    private void check(){
        Rect rect = bubble.getBounds();

        int found = 0;
        for( Memo memo : memos ){
            memo.check(rect);
            found += memo.found ? 1 : 0;
        }

        if( found == AMOUNT ) {
            this.finish();
        }
    }

    // comienza el juego, delay de 6 segundos para memorizar
    private void start(){
        setState(State.STARTING);

        new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                countdown = "" + (millisUntilFinished / 1000);
            }
            public void onFinish() {
                countdown = "";
                time = System.currentTimeMillis();
                setState(State.PLAYING);
            }
        }.start();
    }

    // finaliza el juego, muestra el tiempo, la penalidad
    // envia los datos al servidor propio
    // vuelve al menu luego de 5 segundos
    @SuppressLint("DefaultLocale")
    private void finish(){
        if(state == State.FINISHED) return;

        setState(State.FINISHED);

        double elapsed = ((System.currentTimeMillis() - time)/1000.0) + penalty;
        String elapsedString = String.format("%.3f", elapsed);
        String penaltyString = String.format("%d", penalty);

        message1 = "Muy bien, los encontraste!";
        message2 = "Tu tiempo fue: " + elapsedString + "s\n" ;
        message3 += "Penalidad: " + penaltyString + "s";

        context.sendScore(elapsedString);

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                Intent intent = new Intent(context, MenuActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        }.start();
    }

    // metodo para cambiar el estado
    // tambien lo envía al servidor de la catedra
    private void setState(State newState){
        this.state = newState;
        context.sendEvent(newState);
    }
}