package com.malikbilal.remotecontrolpc.viewer;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.malikbilal.remotecontrolpc.main.MainActivity;
import com.malikbilal.remotecontrolpc.models.MusicImageAvatar;
import com.malikbilal.remotecontrolpc.adapters.MusicImageAvatarAdapter;
import com.malikbilal.remotecontrolpc.R;
import com.malikbilal.remotecontrolpc.upload.TransferFileToServer;

import java.util.ArrayList;


public class ImageViewerFragment extends Fragment {

    private ListView mediaPlayerListView;
    private ProgressBar avatarProgressBar;

    public ImageViewerFragment() {
        // Required empty public constructor
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_image_viewer, container, false);

        avatarProgressBar = rootView.findViewById(R.id.musicImageAvatarProgressBar);

        mediaPlayerListView = rootView.findViewById(R.id.mediaPlayerListView);

        new ImagesList(getActivity()) {

            public void receiveData(Object result) {

                avatarProgressBar.setVisibility(View.GONE);

                ArrayList<MusicImageAvatar> images = (ArrayList<MusicImageAvatar>) result;

                mediaPlayerListView.setAdapter(new MusicImageAvatarAdapter(getActivity(), R.layout.music_image_avatar, images));

            }
        }.execute();

        mediaPlayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                MusicImageAvatar image = (MusicImageAvatar) parent.getItemAtPosition(position);

                String fileName = image.getHeading();

                String path = image.getData();

                transferFile(fileName, path);

            }

        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            getActivity().setTitle(getResources().getString(R.string.image_viewer));

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void transferFile(final String name, String path) {

        if (MainActivity.clientSocket != null) {

            MainActivity.sendMessageToServer("FILE_TRANSFER_REQUEST");

            MainActivity.sendMessageToServer(name);

            new TransferFileToServer(getActivity()) {

                @Override
                public void receiveData(Object result) {

                    MainActivity.sendMessageToServer("SHOW_IMAGE");

                    MainActivity.sendMessageToServer(name);

                }

            }.execute(name, path);

        } else {

            Toast.makeText(getActivity(), "Not connected to computer", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity.sendMessageToServer("CLOSE_IMAGE_VIEWER");

    }

}
