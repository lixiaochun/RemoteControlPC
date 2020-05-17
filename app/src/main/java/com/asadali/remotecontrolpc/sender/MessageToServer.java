package com.asadali.remotecontrolpc.sender;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.asadali.remotecontrolpc.main.MainActivity;

// OK - 5 April 2020

public class MessageToServer extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(@NonNull String... params) {
        String message = params[0];
        String code = params[1];

        // message may be int, float, long or string
        int intMessage;
        float floatMessage;
        long longMessage;
        System.out.println(message + ", " + code);
        switch (code) {
            case "STRING":
                try {
                    MainActivity.objectOutputStream.writeObject(message);
                    MainActivity.objectOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.socketException();
                }
                break;
            case "INT":
                try {
                    intMessage = Integer.parseInt(message);
                    MainActivity.objectOutputStream.writeObject(intMessage);
                    MainActivity.objectOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.socketException();
                }
                break;
            case "FLOAT":
                try {
                    floatMessage = Float.parseFloat(message);
                    MainActivity.objectOutputStream.writeObject(floatMessage);
                    MainActivity.objectOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.socketException();
                }
                break;
            case "LONG":
                try {
                    longMessage = Long.parseLong(message);
                    MainActivity.objectOutputStream.writeObject(longMessage);
                    MainActivity.objectOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.socketException();
                }
                break;
        }
        return null;
    }
}
