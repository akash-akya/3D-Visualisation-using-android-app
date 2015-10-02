package com.akash.net.Model;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by akash on 1/10/15.
 */
public class Sensors implements SensorEventListener{
    private HashMap<Integer,ArrayList<Float>> mSensorList;
    private ArrayList<Integer> mAvailableSensorList;
    private SensorManager mSensorManager;

    public Sensors(ArrayList<Integer> availableSensorList, SensorManager sensorManager)
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
        mSensorList.put(event.sensor.getType(), values);
    }

    public String getSensorData(int type)
    {
        String data = "";
        for (Float value : mSensorList.get(type))
        {
            data += " " + value;
        }
        return data;
    }

    public String getAllSensorData()
    {
        String allData = "";
        for (Map.Entry<Integer, ArrayList<Float>> entry : mSensorList.entrySet())
        {
            allData += entry.getKey()+ " - " + entry.getValue();
        }

        return  allData;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerListener() {
//        Set set = mAvailableSensorList.entrySet();
        for (int aSet : mAvailableSensorList) {
            this.mSensorList.put(aSet, new ArrayList<Float>());
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(aSet), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }
}
