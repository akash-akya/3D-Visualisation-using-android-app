package com.akash.net.Activity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akash.net.Model.Sensors;
import com.akash.net.Network.ScheduledSender;
import com.akash.net.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DataTransfer extends ActionBarActivity implements Runnable {
    private Sensors mSensors;
    private ArrayList<Integer> mSensorList;
    private String mAdress;
    private Long mInterval;
    private  Timer timer = new Timer(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout selectedSensors = (LinearLayout) findViewById(R.id.sensor_data);

        Bundle extras = getIntent().getExtras();
        mAdress = extras.getString("ADDR", "");
        mSensorList = extras.getIntegerArrayList("SENSORS");
        mInterval = extras.getLong("TIME", 250);

        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensors = new Sensors(mSensorList, mSensorManager);

        for (int sensor : mSensorList){
            Sensor s = mSensorManager.getDefaultSensor(sensor);
            TextView sensorHeading = new TextView(this);
            sensorHeading.setText(s.getName());
            sensorHeading.setTextAppearance(this,R.style.TextAppearance_AppCompat_Title);
            sensorHeading.setPadding(0,15,0,0);
            sensorHeading.setAllCaps(true);
            selectedSensors.addView(sensorHeading);

            TextView sensorText = new TextView(this);
            sensorText.setText("");
            sensorText.setId(sensor);
            sensorText.setPadding(5,5,5,5);
            selectedSensors.addView(sensorText);
        }
        Log.w("CUBOID", "address : " + mAdress);
    }

    protected void onResume()
    {
        super.onResume();
        mSensors.registerListener();
        TimerTask timerTask = new ScheduledSender(mAdress, this , mSensors);
        timer.schedule(timerTask, 0, mInterval);
    }

    protected void onPause() {
        super.onPause();
        mSensors.unregisterListener();
        timer.cancel();
    }

    @Override
    public void run() {
        for (int sensor : mSensorList){
            TextView tv = ((TextView) findViewById(sensor));
            String values = mSensors.getSensorData(sensor);
            tv.setText(values);
            Log.w("CUBOID", values);
        }

    }
}
