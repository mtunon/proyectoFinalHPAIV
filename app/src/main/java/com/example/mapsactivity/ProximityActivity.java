package com.example.mapsactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ProximityActivity extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        final TextView textView = findViewById(R.id.textView);

        final int[] seg = {0};
        final int[] min = {0};
        final String[] text = new String[1];

        Button cerrar = findViewById(R.id.cerrar);
        Button mapa = findViewById(R.id.mapa);

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantalla3 = new Intent(ProximityActivity.this, MapsActivity.class);
                startActivity(pantalla3);
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                if (seg[0] == 60){
                    min[0]++;
                    seg[0] = 0;
                }

                text[0] = "";
                if (min[0] < 10) {
                    text[0] = "0"+min[0];
                }else{
                    text[0] = ""+min[0];
                }

                if(seg[0] < 10){
                    text[0] = text[0] +":0"+seg[0];
                }else{
                    text[0] = text[0] +":"+seg[0];
                }
                textView.setText(""+text[0]);
                seg[0]++;
            }
        }, 1000, 1000);

        if (sensor==null)
            finish();
        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0]<sensor.getMaximumRange()){
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }else{
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        start();
    }
    public void start(){
        sensorManager.registerListener(sensorEventListener,sensor,2000*1000);
    }
    public void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause() {
        stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        start();
        super.onResume();
    }
}