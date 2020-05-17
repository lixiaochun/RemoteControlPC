package com.asadali.remotecontrolpc.power;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.asadali.remotecontrolpc.main.MainActivity;
import com.asadali.remotecontrolpc.R;
import com.google.android.material.button.MaterialButton;

// OK - 5 April 2020

public class PowerFragment extends Fragment implements View.OnClickListener {

    private DialogInterface.OnClickListener dialogClickListener;

    private AlertDialog.Builder builder;

    private String action;

    private String title;

    public PowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_power, container, false);

        MaterialButton shutdownButton = rootView.findViewById(R.id.shutdownButton);

        MaterialButton restartButton = rootView.findViewById(R.id.restartButton);

        MaterialButton sleepButton = rootView.findViewById(R.id.sleepButton);

        MaterialButton lockButton = rootView.findViewById(R.id.lockButton);

        shutdownButton.setOnClickListener(this);

        restartButton.setOnClickListener(this);

        sleepButton.setOnClickListener(this);

        lockButton.setOnClickListener(this);

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        sendActionToServer(action.toUpperCase());
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }

            }
        };

        if (getActivity() != null) {
            builder = new AlertDialog.Builder(getActivity());
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            getActivity().setTitle(getString(R.string.power_option));

        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.shutdownButton:

                action = "Shutdown_PC";
                title = "Shutdown your Computer ?";
                break;

            case R.id.restartButton:

                action = "Restart_PC";
                title = "Restart your Computer ?";
                break;

            case R.id.sleepButton:

                action = "Sleep_PC";
                title = "Sleep your Computer ?";
                break;

            case R.id.lockButton:
                action = "Lock_PC";
                title = "Lock your Computer ?";
                break;
        }
        showConfirmDialog();

    }

    private void showConfirmDialog() {
        builder.setTitle("Do you want to " + title)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    private void sendActionToServer(String action) {
        MainActivity.sendMessageToServer(action);
    }

}
