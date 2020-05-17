package com.malikbilal.remotecontrolpc.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ListView;

import com.malikbilal.remotecontrolpc.adapters.AvatarFileAdapter;

import java.util.ArrayList;

import file.AvatarFile;

class GetFilesList extends FilesList {

    @SuppressLint("StaticFieldLeak")
    private ListView fileTransferListView;

    @SuppressLint("StaticFieldLeak")
    private Context context;

    GetFilesList(ListView fileTransferListView, Context context) {

        this.fileTransferListView = fileTransferListView;
        this.context = context;
    }

    @Override
    public void receiveData(Object result) {

        ArrayList<AvatarFile> filesInFolder = (ArrayList<AvatarFile>) result;

        fileTransferListView.setAdapter(new AvatarFileAdapter(context, com.malikbilal.remotecontrolpc.R.layout.music_image_avatar, filesInFolder));

    }

}
