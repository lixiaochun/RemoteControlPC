package com.asadali.remotecontrolpc.touchpad;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asadali.remotecontrolpc.main.MainActivity;
import com.asadali.remotecontrolpc.R;
import com.google.android.material.button.MaterialButton;

// OK - 5 April 2020

public class TouchpadFragment extends Fragment {

    private int initX, initY, disX, disY;
    private boolean mouseMoved = false, multiTouch = false;

    public TouchpadFragment() {
        // Required empty public constructor
    }


    // Suppress Warning onTouchListener

    @SuppressLint("ClickableViewAccessibility")

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_touchpad, container, false);

        MaterialButton leftClickButton = rootView.findViewById(R.id.leftClickButton);

        MaterialButton rightClickButton = rootView.findViewById(R.id.rightClickButton);

        LinearLayout touchpadLayout = rootView.findViewById(R.id.touchpad_surface);

        leftClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLeftClick();
            }
        });

        rightClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateRightClick();
            }
        });

        touchpadLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainActivity.clientSocket != null) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:

                            initX = (int) event.getX();
                            initY = (int) event.getY();
                            mouseMoved = false;

                            break;

                        case MotionEvent.ACTION_MOVE:

                            if (!multiTouch) {
                                disX = (int) event.getX() - initX;
                                disY = (int) event.getY() - initY;

                                initX = (int) event.getX();
                                initY = (int) event.getY();
                                if (disX != 0 || disY != 0) {

                                    MainActivity.sendMessageToServer("MOUSE_MOVE");

                                    MainActivity.sendMessageToServer(disX);

                                    MainActivity.sendMessageToServer(disY);

                                    mouseMoved = true;
                                }

                            } else {

                                disY = (int) event.getY() - initY;
                                disY = disY / 2;
                                initY = (int) event.getY();
                                if (disY != 0) {
                                    MainActivity.sendMessageToServer("MOUSE_WHEEL");
                                    MainActivity.sendMessageToServer(disY);
                                    mouseMoved = true;
                                }
                            }
                            break;

                        case MotionEvent.ACTION_CANCEL:

                        case MotionEvent.ACTION_UP:

                            //consider a tap only if user did not move mouse after ACTION_DOWN
                            if (!mouseMoved) {
                                MainActivity.sendMessageToServer("LEFT_CLICK");
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            initY = (int) event.getY();
                            mouseMoved = false;
                            multiTouch = true;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            if (!mouseMoved) {

                                MainActivity.sendMessageToServer("LEFT_CLICK");
                            }
                            multiTouch = false;
                            break;
                    }
                }
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            getActivity().setTitle(getResources().getString(R.string.touchpad));

        }
    }

    private void simulateLeftClick() {
        String message = "LEFT_CLICK";
        MainActivity.sendMessageToServer(message);
    }

    private void simulateRightClick() {
        String message = "RIGHT_CLICK";
        MainActivity.sendMessageToServer(message);
    }

}
