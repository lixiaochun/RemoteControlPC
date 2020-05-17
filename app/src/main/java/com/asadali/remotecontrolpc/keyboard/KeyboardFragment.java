package com.asadali.remotecontrolpc.keyboard;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asadali.remotecontrolpc.main.MainActivity;
import com.asadali.remotecontrolpc.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

// OK - 5 April 2020

public class KeyboardFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, TextWatcher {

    private TextInputEditText typeHereEditText;
    private String previousText = "";

    public KeyboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_keyboard, container, false);

        initialization(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle(getResources().getString(R.string.keyboard));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialization(@NonNull View rootView) {
        typeHereEditText = rootView.findViewById(com.asadali.remotecontrolpc.R.id.typeHereEditText);
        MaterialButton ctrlButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.ctrlButton);
        MaterialButton altButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.altButton);
        MaterialButton shiftButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.shiftButton);
        MaterialButton enterButton = rootView.findViewById(R.id.enterButton);
        MaterialButton tabButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.tabButton);
        MaterialButton escButton = rootView.findViewById(R.id.escButton);
        MaterialButton printScrButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.printScrButton);
        MaterialButton backspaceButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.backspaceButton);
        MaterialButton deleteButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.deleteButton);
        MaterialButton clearTextButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.clearTextButton);
        MaterialButton nButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.nButton);
        MaterialButton tButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.tButton);
        MaterialButton wButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.wButton);
        MaterialButton rButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.rButton);
        MaterialButton fButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.fButton);
        MaterialButton zButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.zButton);
        MaterialButton cButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.cButton);
        MaterialButton xButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.xButton);
        MaterialButton vButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.vButton);
        MaterialButton aButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.aButton);
        MaterialButton oButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.oButton);
        MaterialButton sButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.sButton);
        MaterialButton ctrlAltTButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.ctrlAltTButton);
        MaterialButton ctrlZButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.ctrlZButton);
        MaterialButton altF4Button = rootView.findViewById(com.asadali.remotecontrolpc.R.id.altF4Button);
        ctrlButton.setOnTouchListener(this);
        altButton.setOnTouchListener(this);
        shiftButton.setOnTouchListener(this);
        backspaceButton.setOnClickListener(this);
        enterButton.setOnClickListener(this);
        tabButton.setOnClickListener(this);
        escButton.setOnClickListener(this);
        printScrButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        clearTextButton.setOnClickListener(this);
        nButton.setOnClickListener(this);
        tButton.setOnClickListener(this);
        wButton.setOnClickListener(this);
        rButton.setOnClickListener(this);
        fButton.setOnClickListener(this);
        zButton.setOnClickListener(this);
        cButton.setOnClickListener(this);
        xButton.setOnClickListener(this);
        vButton.setOnClickListener(this);
        aButton.setOnClickListener(this);
        oButton.setOnClickListener(this);
        sButton.setOnClickListener(this);
        ctrlAltTButton.setOnClickListener(this);
        ctrlZButton.setOnClickListener(this);
        altF4Button.setOnClickListener(this);
        typeHereEditText.addTextChangedListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, @NonNull MotionEvent event) {

        String action = "KEY_PRESS";

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            action = "KEY_PRESS";

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            action = "KEY_RELEASE";

        }

        int keyCode = 17;//dummy initialization

        switch (v.getId()) {

            case com.asadali.remotecontrolpc.R.id.ctrlButton:
                keyCode = 17;
                break;
            case com.asadali.remotecontrolpc.R.id.altButton:
                keyCode = 18;
                break;
            case com.asadali.remotecontrolpc.R.id.shiftButton:
                keyCode = 16;
                break;
        }

        sendKeyCodeToServer(action, keyCode);
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == com.asadali.remotecontrolpc.R.id.clearTextButton) {

            typeHereEditText.setText("");

        } else if (id == com.asadali.remotecontrolpc.R.id.ctrlAltTButton || id == com.asadali.remotecontrolpc.R.id.ctrlZButton || id == com.asadali.remotecontrolpc.R.id.altF4Button) {

            switch (id) {
                case com.asadali.remotecontrolpc.R.id.ctrlAltTButton:

                    MainActivity.sendMessageToServer("CTRL_ALT_T");
                    break;
                case com.asadali.remotecontrolpc.R.id.ctrlZButton:

                    MainActivity.sendMessageToServer("CTRL_Z");
                    break;
                case com.asadali.remotecontrolpc.R.id.altF4Button:

                    MainActivity.sendMessageToServer("ALT_F4");
                    break;
            }
        } else {

            int keyCode = 17;
            String action = "TYPE_KEY";
            switch (id) {
                case com.asadali.remotecontrolpc.R.id.enterButton:
                    keyCode = 10;
                    break;
                case com.asadali.remotecontrolpc.R.id.tabButton:
                    keyCode = 9;
                    break;
                case com.asadali.remotecontrolpc.R.id.escButton:
                    keyCode = 27;
                    break;
                case com.asadali.remotecontrolpc.R.id.printScrButton:
                    keyCode = 154;
                    break;
                case com.asadali.remotecontrolpc.R.id.deleteButton:
                    keyCode = 127;
                    break;
                case com.asadali.remotecontrolpc.R.id.nButton:
                    keyCode = 78;
                    break;
                case com.asadali.remotecontrolpc.R.id.tButton:
                    keyCode = 84;
                    break;
                case com.asadali.remotecontrolpc.R.id.wButton:
                    keyCode = 87;
                    break;
                case com.asadali.remotecontrolpc.R.id.rButton:
                    keyCode = 82;
                    break;
                case com.asadali.remotecontrolpc.R.id.fButton:
                    keyCode = 70;
                    break;
                case com.asadali.remotecontrolpc.R.id.zButton:
                    keyCode = 90;
                    break;
                case com.asadali.remotecontrolpc.R.id.cButton:
                    keyCode = 67;
                    break;
                case com.asadali.remotecontrolpc.R.id.xButton:
                    keyCode = 88;
                    break;
                case com.asadali.remotecontrolpc.R.id.vButton:
                    keyCode = 86;
                    break;
                case com.asadali.remotecontrolpc.R.id.aButton:
                    keyCode = 65;
                    break;
                case com.asadali.remotecontrolpc.R.id.oButton:
                    keyCode = 79;
                    break;
                case com.asadali.remotecontrolpc.R.id.sButton:
                    keyCode = 83;
                    break;
                case com.asadali.remotecontrolpc.R.id.backspaceButton:
                    keyCode = 8;
                    break;
            }
            sendKeyCodeToServer(action, keyCode);
        }

    }

    private void sendKeyCodeToServer(String action, int keyCode) {
        MainActivity.sendMessageToServer(action);
        MainActivity.sendMessageToServer(keyCode);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        char ch = newCharacter(s, previousText);
        if (ch == 0) {
            return;
        }
        MainActivity.sendMessageToServer("TYPE_CHARACTER");
        MainActivity.sendMessageToServer(Character.toString(ch));
        previousText = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private char newCharacter(@NonNull CharSequence currentText, @NonNull CharSequence previousText) {
        char ch = 0;
        int currentTextLength = currentText.length();
        int previousTextLength = previousText.length();
        int difference = currentTextLength - previousTextLength;
        if (currentTextLength > previousTextLength) {
            if (1 == difference) {
                ch = currentText.charAt(previousTextLength);
            }
        } else if (currentTextLength < previousTextLength) {
            if (-1 == difference) {
                ch = '\b';
            } else {
                ch = ' ';
            }
        }
        return ch;
    }

}

