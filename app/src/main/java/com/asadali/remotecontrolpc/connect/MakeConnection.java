package com.asadali.remotecontrolpc.connect;

import android.os.AsyncTask;

import com.asadali.remotecontrolpc.main.MainActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

// OK - 5 April 2020

class MakeConnection extends AsyncTask<Void, Void, Socket> {

    private OnTaskCompleted listener;

    private int portNumber;
    private String ipAddress;

    MakeConnection(OnTaskCompleted listener, String ipAddress, String portNumber) {

        this.listener = listener;
        this.ipAddress = ipAddress;
        this.portNumber = Integer.parseInt(portNumber);
    }

    @Override
    protected Socket doInBackground(Void... params) {


        Socket clientSocket;

        try {

            SocketAddress socketAddress = new InetSocketAddress(ipAddress, portNumber);

            clientSocket = new Socket();

            clientSocket.connect(socketAddress, 3000);

            MainActivity.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            MainActivity.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (Exception e) {

            e.printStackTrace();

            clientSocket = null;

        }

        return clientSocket;
    }

    protected void onPostExecute(Socket clientSocket) {

        MainActivity.clientSocket = clientSocket;

        listener.onTaskCompleted(portNumber);
    }
}

