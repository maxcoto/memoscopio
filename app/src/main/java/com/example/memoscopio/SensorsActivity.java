package com.example.memoscopio;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener  {

    private SensorManager sensorManager;

    private TextView acelerometro;
    private TextView giroscopio;
    private TextView proximidad;
    private TextView detecta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        acelerometro = findViewById(R.id.acelerometro);
        giroscopio = findViewById(R.id.giroscopio);
        proximidad = findViewById(R.id.proximidad);
        detecta = findViewById(R.id.detecta);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt = "";

        synchronized (this) {

            Log.d("sensor", event.sensor.getName());
            Log.d("sensor 2 id", String.valueOf(this.getTaskId()));

            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    txt += "acelerometro:\n";
                    txt += "x: " + event.values[0] + "\n";
                    txt += "y: " + event.values[1] + "\n";
                    txt += "z: " + event.values[2] + "\n";
                    acelerometro.setText(txt);

                    if(event.values[0] > 25 || event.values[1] > 25 || event.values[2] > 25){
                        Toast.makeText(SensorsActivity.this, "shake that ass baby", Toast.LENGTH_LONG).show();
                    }
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    txt += "giroscopio:\n";
                    txt += "x: " + event.values[0] + "\n";
                    txt += "y: " + event.values[1] + "\n";
                    txt += "z: " + event.values[2] + "\n";
                    giroscopio.setText(txt);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    txt += "proximidad:\n";
                    txt += event.values[0] + "\n";
                    proximidad.setText(txt);

                    if(event.values[0] == 0){
                        detecta.setText("proximidad detectada");
                    } else {
                        detecta.setText("-");
                    }
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSensors();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startSensors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensors();
    }

    protected void startSensors(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void stopSensors(){
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
    }

}