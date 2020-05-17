package com.malikbilal.remotecontrolpc.screen;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.malikbilal.remotecontrolpc.main.MainActivity;
import com.malikbilal.remotecontrolpc.R;

import java.util.Timer;
import java.util.TimerTask;

public class LiveScreenFragment extends Fragment {

    private Timer timer;

    private ImageView screenshotImageView;

    private int xCord, yCord, initX, initY;

    private long currentPressTime, lastPressTime;

    private boolean mouseMoved = false, multiTouch = false;

    private int screenshotImageViewX, screenshotImageViewY;

    public LiveScreenFragment() {

        // Required empty public constructor

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_live_screen, container, false);

        screenshotImageView = rootView.findViewById(R.id.screenshotImageView);

        ViewTreeObserver vto = screenshotImageView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                screenshotImageViewX = screenshotImageView.getHeight();

                screenshotImageViewY = screenshotImageView.getWidth();

                ViewTreeObserver obs = screenshotImageView.getViewTreeObserver();

                obs.removeOnGlobalLayoutListener(this);

            }
        });

        screenshotImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (MainActivity.clientSocket != null) {

                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:

                            xCord = screenshotImageViewX - (int) event.getY();

                            yCord = (int) event.getX();

                            initX = xCord;

                            initY = yCord;

                            MainActivity.sendMessageToServer("MOUSE_MOVE_LIVE");

                            MainActivity.sendMessageToServer((float) xCord / screenshotImageViewX);

                            MainActivity.sendMessageToServer((float) yCord / screenshotImageViewY);

                            mouseMoved = false;

                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (!multiTouch) {

                                xCord = screenshotImageViewX - (int) event.getY();

                                yCord = (int) event.getX();

                                if ((xCord - initX) != 0 && (yCord - initY) != 0) {

                                    initX = xCord;

                                    initY = yCord;

                                    MainActivity.sendMessageToServer("MOUSE_MOVE_LIVE");

                                    MainActivity.sendMessageToServer((float) xCord / screenshotImageViewX);

                                    MainActivity.sendMessageToServer((float) yCord / screenshotImageViewY);

                                    mouseMoved = true;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:

                            currentPressTime = System.currentTimeMillis();

                            long interval = currentPressTime - lastPressTime;

                            if (interval >= 500 && !mouseMoved) {

                                MainActivity.sendMessageToServer("LEFT_CLICK");

                                delayedUpdateScreenshot();

                            }
                            lastPressTime = currentPressTime;
                            break;
                    }
                }
                return true;
            }
        });
        timer = new Timer();
        updateScreenshot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            getActivity().setTitle(getResources().getString(R.string.screen_share));

        }

    }

    @SuppressLint("StaticFieldLeak")
    private void updateScreenshot() {

        if (MainActivity.clientSocket != null) {

            MainActivity.sendMessageToServer("SCREENSHOT_REQUEST");

            new UpdateScreenshot() {
                @Override
                public void receiveData(Object result) {

                    String path = (String) result;

                    Bitmap bitmap = BitmapFactory.decodeFile(path);

                    Matrix matrix = new Matrix();

                    matrix.postRotate(-90);

                    try {

                        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        screenshotImageView.setImageBitmap(rotated);

                    } catch (Exception e) {

                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    }
                }
            }.execute();
        }
    }

    private void delayedUpdateScreenshot() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateScreenshot();
            }
        }, 500);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        timer.cancel();

        timer.purge();

    }

}
