package com.akash.net.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.akash.net.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;


public class DataTransfer extends ActionBarActivity implements SensorEventListener {

    private String   msg;
    private String   address;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private TextView mOrientationTextView;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address = getIntent().getExtras().getString("ADDR", "");
        Log.w("NETAPP", "address : " + address);

        mOrientationTextView = (TextView) findViewById(R.id.orientation_data);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }


    protected void onResume() {
        super.onResume();
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);

    }


    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);

            new Thread( new Runnable() {
                @Override
                public void run() {

                    final int UDP_SERVER_PORT = 23456;
                    DatagramSocket ds = null;
                    int[] val  = new int[3];
                    val[0] = (int) (mOrientation[0]*180.0f/3.14f);
                    val[1] = (int) (mOrientation[1]*180.0f/3.14f);
                    val[2] = (int) (mOrientation[2]*180.0f/3.14f);

                    if (!address.isEmpty()) {
                        msg = String.format("%5d%5d%5d", val[0], val[1], val[2]);
//                    String textViewText = String.format("A : %10f \nP : %10f \nR : %10f\n", mOrientation[0], mOrientation[1], mOrientation[2]);
//                    mOrientationTextView.setText(textViewText);
                        Log.w("NETAPP", msg);
                        try {
                            ds = new DatagramSocket();
                            InetAddress serverAddr = InetAddress.getByName(address);
                            DatagramPacket dp;
                            dp = new DatagramPacket(msg.getBytes(), msg.length(),
                                    serverAddr, UDP_SERVER_PORT);
                            ds.send(dp);
//            Log.w("NetworkApp", "Data sent to "+address+" Post: "+UDP_SERVER_PORT);
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (ds != null) {
                                ds.close();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }



}
