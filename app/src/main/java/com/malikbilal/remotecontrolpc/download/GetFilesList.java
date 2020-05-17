package com.malikbilal.remotecontrolpc.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import com.malikbilal.remotecontrolpc.adapters.AvatarFileAdapter;

import java.util.ArrayList;

import file.AvatarFile;

class GetFilesList extends GetFilesListFromServer {

    @SuppressLint("StaticFieldLeak")
    private ListView fileDownloadListView;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    GetFilesList(ListView fileDownloadListView, Context context) {

        this.fileDownloadListView = fileDownloadListView;

        this.context = context;
    }


    @Override
    public void receiveData(Object result) {

        if (result != null) {

            fileDownloadListView.setAdapter(new AvatarFileAdapter(context, com.malikbilal.remotecontrolpc.R.layout.music_image_avatar, (ArrayList<AvatarFile>) result));

        } else {

            Toast.makeText(context, "Not connected to computer.", Toast.LENGTH_LONG).show();

        }

    }

}
