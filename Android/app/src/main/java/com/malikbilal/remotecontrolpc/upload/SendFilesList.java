package com.malikbilal.remotecontrolpc.upload;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

import file.AvatarFile;

public class SendFilesList {

    public void sendList(final ArrayList<AvatarFile> myFiles, final ObjectOutputStream out) {
        new Thread() {
            public void run() {
                try {
                    out.writeObject(myFiles);
                    out.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
