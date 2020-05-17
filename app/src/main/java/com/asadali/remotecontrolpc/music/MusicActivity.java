package com.asadali.remotecontrolpc.music;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.asadali.remotecontrolpc.main.MainActivity;
import com.asadali.remotecontrolpc.R;
import com.google.android.material.button.MaterialButton;

// OK - 5 April 2020

public class MusicActivity extends AppCompatActivity {

    Handler handler;
    int progress, duration;
    Runnable updateProgressRunnable;
    private boolean isPaused = false;
    MaterialButton pauseOrResumeButton;
    SeekBar volumeSeekBar, musicProgressSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        pauseOrResumeButton = findViewById(R.id.pauseOrResumeButton);
        musicProgressSeekBar = findViewById(R.id.musicProgressSeekBar);

        progress = 1;

        handler = new Handler();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            duration = bundle.getInt("MUSIC_DURATION");

            String name = bundle.getString("MUSIC_FILE_NAME");

            playMusic(name);

            volumeSeekBar.setMax(10);

            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    float volume = (float) progress / 10;
                    setVolume(volume);

                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                    // TODO Auto-generated method stub

                }

            });

            musicProgressSeekBar.setMax(duration);

            musicProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar arg0, int p, boolean fromUser) {
                    if (fromUser) {
                        slideMusic(p);
                        progress = p;
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

            });

            updateProgressRunnable = new Runnable() {

                @Override
                public void run() {
                    updateProgress();

                }

            };

            updateProgress();
        }
    }


    private void playMusic(String name) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("PLAY_MUSIC");
            MainActivity.sendMessageToServer(name);
        }
    }

    private void slideMusic(int duration) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("SLIDE_MUSIC");
            MainActivity.sendMessageToServer(duration);
        }
    }

    public void pauseOrResume(View view) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("PAUSE_OR_RESUME_MUSIC");
            String buttonText = pauseOrResumeButton.getText().toString().toLowerCase();
            if (buttonText.equals("pause")) {
                pauseOrResumeButton.setText(R.string.resume_music);
                isPaused = true;
            } else {
                pauseOrResumeButton.setText(R.string.pause_music);
                isPaused = false;
                updateProgress();
            }
        }
    }

    private void setVolume(float volume) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("SET_VOLUME_MUSIC");
            MainActivity.sendMessageToServer(volume);
        }
    }

    private void updateProgress() {
        if (!isPaused) {
            handler.removeCallbacks(updateProgressRunnable);
            musicProgressSeekBar.setProgress(progress);
            progress++;
            handler.postDelayed(updateProgressRunnable, 1000);
        }
    }
}
