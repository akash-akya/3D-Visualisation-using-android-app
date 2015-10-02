package com.akash.net.Model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by akash on 1/10/15.
 */
public class Sensors implements SensorEventListener{
    private HashMap<Sensor,ArrayList<Float>> mSensorList;
    private ArrayList<Sensor> mAvailableSensorList;
    private SensorManager mSensorManager;

    public Sensors(ArrayList<Sensor> availableSensorList, SensorManager sensorManager)
    {
        mAvailableSensorList = availableSensorList;
        mSensorList = new HashMap<>();
        mSensorManager = sensorManager;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        ArrayList<Float> values = new ArrayList<>();
        for (Float value : event.values)
        {
            values.add(value);
        }
        mSensorList.put(event.sensor, values);
    }

    public String getSensorData(Sensor s)
    {
        String data = "";
        for (Float value : mSensorList.get(s))
        {
            data += value;
        }
        return data;
    }

    public String getAllSensorData()
    {
        String allData = "";
        Set set = mSensorList.entrySet();
        for (Object sensor : set) {
            allData += getSensorData((Sensor)sensor);
        }
        return  allData;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerListener() {
//        Set set = mAvailableSensorList.entrySet();
        for (Sensor aSet : mAvailableSensorList) {
            this.mSensorList.put((Sensor)aSet, new ArrayList<Float>());
            mSensorManager.registerListener(this, (Sensor)aSet, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }
}
