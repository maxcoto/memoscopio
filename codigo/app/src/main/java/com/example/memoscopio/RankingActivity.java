package com.example.memoscopio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<String>();
    private ListView listView;

    public IntentFilter filter;
    private final Callback callback = new Callback();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        listView = findViewById(R.id.listView);

        configureReceiver();

        Intent intent = new Intent(RankingActivity.this, UnlamService.class);
        intent.putExtra("uri", Constants.RANKING_GET_URI);
        intent.putExtra("action", UnlamService.ACTION_RANKING_GET);
        intent.putExtra("data", new JSONObject().toString());
        intent.putExtra("method", "GET");
        startService(intent);
    }

    private void configureReceiver(){
        filter = new IntentFilter(UnlamService.ACTION_RANKING_GET);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(callback, filter);
    }

    public class Callback extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String data = intent.getStringExtra("data");
                assert data != null;
                JSONObject json = new JSONObject(data);

                String success = json.getString("success");

                if(success.equals("true")){
                    Log.e("LOGUEO RANKING", "Datos: " + data );

                    JSONArray ranking = json.getJSONArray("ranking");

                    Log.e("LOGUEO RANKING PERSON", ranking.toString() );

                    for (int i = 0; i < ranking.length(); i++) {
                        JSONObject row = ranking.getJSONObject(i);
                        String person = "" + (i+1) + ") " + row.getString("points") + " - " + row.getString("name");
                        list.add(person);
                        Log.e("LOGUEO RANKING PERSON", person );
                    }

                    adapter = new ArrayAdapter<String>(context, R.layout.sensors_listview, list);
                    listView.setAdapter(adapter);

                } else {
                    Log.e("LOGUEO RANKING FAIL", "Datos: " + data );
                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}