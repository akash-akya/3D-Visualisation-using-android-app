package com.akash.net.Networking;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by akash on 9/24/2015.
 */
public class UdpClient implements Runnable {

    final int UDP_SERVER_PORT = 12345;

    public void run(String address, String msg) {
//        String udpMsg = "hello world from UDP client " + UDP_SERVER_PORT;
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName(address);
            DatagramPacket dp;
            dp = new DatagramPacket(msg.getBytes(), msg.length(),
                    serverAddr, UDP_SERVER_PORT);
            ds.send(dp);
            Log.w("NetworkApp", "Data sent to "+address+" Post: "+UDP_SERVER_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }catch (UnknownHostException e) {
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


    @Override
    public void run() {

    }
}
