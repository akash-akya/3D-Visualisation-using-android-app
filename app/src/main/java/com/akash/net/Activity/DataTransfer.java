package com.akash.net.Activity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.akash.net.Model.Sensors;
import com.akash.net.Network.ScheduledSender;
import com.akash.net.Network.ScheduledSenderInterface;
import com.akash.net.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DataTransfer extends ActionBarActivity implements ScheduledSenderInterface {

    private Sensors mSensors;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        String address = extras.getString("ADDR", "");
        ArrayList<Integer> s = extras.getIntegerArrayList("SENSORS");
        ArrayList<Sensor> sensor = new ArrayList<>();
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        for (int sen : s)
        {
            sensor.add(mSensorManager.getDefaultSensor(sen));
        }
        mSensors = new Sensors(sensor, mSensorManager);
        scheduledExecutorService.scheduleAtFixedRate(new ScheduledSender(address, this, mSensors),
                0, 10, TimeUnit.MILLISECONDS);

        Log.w("CUBOID", "address : " + address);
        tv = (TextView) findViewById(R.id.orientation_data);
    }

    protected void onResume()
    {
        super.onResume();
        mSensors.registerListener();
    }

    protected void onPause() {
        super.onPause();
        mSensors.unregisterListener();
    }

    @Override
    public String preRun() {
        return mSensors.getAllSensorData();
    }

    @Override
    public void postRun(String string) {
        tv.setText(string);
    }

}
