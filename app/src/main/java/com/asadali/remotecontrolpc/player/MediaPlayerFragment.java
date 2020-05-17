package com.asadali.remotecontrolpc.player;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.asadali.remotecontrolpc.main.MainActivity;
import com.asadali.remotecontrolpc.music.MusicActivity;
import com.asadali.remotecontrolpc.models.MusicImageAvatar;
import com.asadali.remotecontrolpc.adapters.MusicImageAvatarAdapter;
import com.asadali.remotecontrolpc.R;
import com.asadali.remotecontrolpc.upload.TransferFileToServer;

import java.util.ArrayList;


public class MediaPlayerFragment extends Fragment {

    private ListView mediaPlayerListView;
    private ProgressBar avatarProgressBar;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        avatarProgressBar = rootView.findViewById(R.id.musicImageAvatarProgressBar);

        mediaPlayerListView = rootView.findViewById(R.id.mediaPlayerListView);

        new SongsList(getActivity()) {
            public void receiveData(Object result) {

                avatarProgressBar.setVisibility(View.GONE);

                ArrayList<MusicImageAvatar> songs = (ArrayList<MusicImageAvatar>) result;

                mediaPlayerListView.setAdapter(new MusicImageAvatarAdapter(getActivity(), R.layout.music_image_avatar, songs));
            }
        }.execute();

        mediaPlayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MusicImageAvatar song = (MusicImageAvatar) parent.getItemAtPosition(position);

                String fileName = song.getHeading();

                String path = song.getData();

                int duration = song.getDuration();

                duration /= 1000; // Duration in seconds

                transferFile(fileName, path, duration);
            }

        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle(getResources().getString(R.string.media_player));
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void transferFile(final String name, String path, final int duration) {

        if (MainActivity.clientSocket != null) {

            MainActivity.sendMessageToServer("FILE_TRANSFER_REQUEST");

            MainActivity.sendMessageToServer(name);

            Toast.makeText(getActivity(), "Wait for music controls.", Toast.LENGTH_LONG).show();

            new TransferFileToServer(getActivity()) {

                @Override
                public void receiveData(Object result) {

                    Intent intent = new Intent(getActivity(), MusicActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putString("MUSIC_FILE_NAME", name);

                    bundle.putInt("MUSIC_DURATION", duration);

                    intent.putExtras(bundle);

                    startActivity(intent);

                }

            }.execute(name, path);

        } else {

            Toast.makeText(getActivity(), "Not connected to computer.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity.sendMessageToServer("STOP_MUSIC");

    }

}
