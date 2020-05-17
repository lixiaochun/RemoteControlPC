package com.asadali.remotecontrolpc.upload;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.asadali.remotecontrolpc.reciever.CallbackReceiver;
import com.asadali.remotecontrolpc.R;
import com.asadali.remotecontrolpc.utility.Utility;

import java.io.File;
import java.util.ArrayList;

import file.AvatarFile;

// OK - 5 April 2020

public abstract class FilesList extends AsyncTask<String, Void, ArrayList<AvatarFile>> implements CallbackReceiver {

    @Override
    protected ArrayList<AvatarFile> doInBackground(@NonNull String... params) {

        String path = params[0];

        ArrayList<AvatarFile> myFiles = new ArrayList<>();

        Utility utility = new Utility();

        File file = new File(path);

        File[] files = file.listFiles();

        if (files != null && files.length > 0) {

            for (File value : files) {

                String avatarHeading = value.getName();

                long lastModified = value.lastModified();

                String lastModifiedDate = utility.getDate(lastModified, "dd MMM yyyy hh:mm a");

                int icon = R.drawable.folder_icon;

                String itemsOrSize, filePath, type;
                if (value.isDirectory()) {

                    type = "folder";

                    File[] tempArray = value.listFiles();

                    if (tempArray != null) {

                        itemsOrSize = tempArray.length + " items";

                    } else {
                        itemsOrSize = 0 + " item";
                    }
                } else {
                    itemsOrSize = utility.getSize(value.length());
                    type = "file";
                    if (avatarHeading.length() > 3) {
                        String extension = avatarHeading.substring(avatarHeading.length() - 3).toLowerCase();
                        switch (extension) {
                            case "jpg":
                            case "jpeg":
                            case "png":
                            case "svg":
                                type = "image";
                                break;
                            case "mp3":
                                type = "mp3";
                                break;
                            case "pdf":
                                type = "pdf";
                                break;
                        }
                    }
                }

                filePath = value.getAbsolutePath();

                String subHeading = itemsOrSize + " " + lastModifiedDate;

                AvatarFile avatarFile = new AvatarFile(icon, avatarHeading, subHeading, filePath, type);

                myFiles.add(avatarFile);
            }
        }

        return myFiles;
    }

    protected void onPostExecute(ArrayList<AvatarFile> myFiles) {
        receiveData(myFiles);
    }

    @Override
    public abstract void receiveData(Object result);

}
