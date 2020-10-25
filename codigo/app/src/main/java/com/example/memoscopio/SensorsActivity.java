package com.example.memoscopio;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SensorsActivity extends AppCompatActivity implements SensorEventListener  {
    private SensorManager sensorManager;

    private TextView acelerometro;
    private TextView proximidad;
    private ListView listView;

    private Button saveButton;

    private String x;
    private String y;
    private String z;
    private String p;

    private int index;

    private SharedPreferences preferences;
    private DecimalFormat format;

    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        listView = findViewById(R.id.listView);
        acelerometro = findViewById(R.id.acelerometro);
        proximidad = findViewById(R.id.proximidad);
        saveButton  = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(saveHandler);

        format = new DecimalFormat("#.##");
        preferences = getSharedPreferences("sensors", MODE_PRIVATE);
        index = preferences.getInt(Constants.INDEX_PREFERENCE, 0);

        for(int i=0; i<=index; i++){
            String str = preferences.getString(Constants.STORE_PREFERENCE + i, "");
            list.add(str);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.sensors_listview, list);
        listView.setAdapter(adapter);
    }

    private View.OnClickListener saveHandler = (_v) -> {
        String str = "SNAPSHOT -> " + x + ", " + y + ", " + z + ", " + p;
        list.add(str);
        index++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.STORE_PREFERENCE + index, str);
        editor.putInt(Constants.INDEX_PREFERENCE, index);
        editor.commit();
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt = "";

        synchronized (this) {
            switch (event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    txt += "acelerometro:\n";
                    x = "x: " + format.format(event.values[0]);
                    y = "y: " + format.format(event.values[1]);
                    z = "z: " + format.format(event.values[2]);
                    txt += x + "\n" + y + "\n" + z + "\n";
                    acelerometro.setText(txt);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    txt += "proximidad:\n";
                    p = "p: " + format.format(event.values[0]);
                    txt += p + "\n";
                    proximidad.setText(txt);
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
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void stopSensors(){
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

}