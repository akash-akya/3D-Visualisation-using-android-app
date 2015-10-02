package com.akash.net.Network;

import android.hardware.Sensor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.akash.net.Model.Sensors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;

/**
 * Created by akash on 1/10/15.
 */
public class ScheduledSender extends TimerTask{

    private Sensors mSensors;
    private String mAddress;
    private Runnable mPostUIRunnable;

    public ScheduledSender(String address,  Runnable r, Sensors sensors ){
        mAddress = address;
        mPostUIRunnable = r;
        mSensors = sensors;
    }

    @Override
    public void run() {
        Handler handler = new Handler(Looper.getMainLooper());
        String msg = mSensors.getAllSensorData();
        Log.w("APP", msg);
        handler.post(mPostUIRunnable);

        if (!mAddress.isEmpty()) {
            send(msg);
        }
    }

    void send(String msg)
    {
        final int UDP_SERVER_PORT = 23456;
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(mAddress);
            DatagramPacket dp;
            dp = new DatagramPacket(msg.getBytes(), msg.length(),
                    serverAddr, UDP_SERVER_PORT);
            ds.send(dp);
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
