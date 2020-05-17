package com.asadali.remotecontrolpc.connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asadali.remotecontrolpc.main.MainActivity;
import com.asadali.remotecontrolpc.R;
import com.asadali.remotecontrolpc.server.Server;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// OK - 5 April 2020

public class ConnectFragment extends Fragment implements OnTaskCompleted {

    private MaterialButton connectButton;
    private SharedPreferences sharedPreferences;
    private TextInputLayout ipAddressInput, portNumberInput;
    private TextInputEditText ipAddressEditText, portNumberEditText;


    public ConnectFragment() {

        // Required empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);

        ipAddressEditText = rootView.findViewById(R.id.ipAddress);
        portNumberEditText = rootView.findViewById(R.id.portNumber);

        ipAddressInput = rootView.findViewById(R.id.ipLayout);
        portNumberInput = rootView.findViewById(R.id.portLayout);

        connectButton = rootView.findViewById(R.id.connectButton);

        ipAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ipAddressInput.setError(null);
            }
        });
        portNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                portNumberInput.setError(null);
            }
        });

        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences("lastConnectionDetails", Context.MODE_PRIVATE);
        }

        String[] lastConnectionDetails = getLastConnectionDetails();
        ipAddressEditText.setText(lastConnectionDetails[0]);
        portNumberEditText.setText(lastConnectionDetails[1]);

        if (MainActivity.clientSocket != null) {

            connectButton.setText(R.string.computer_connected);

            connectButton.setEnabled(false);

            ipAddressInput.setEnabled(false);

            portNumberInput.setEnabled(false);

        }

        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeConnection();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            getActivity().setTitle(getResources().getString(R.string.connect));

        }
    }


    private void makeConnection() {

        if (ipAddressEditText.getText() != null && portNumberEditText.getText() != null) {

            String ipAddress = ipAddressEditText.getText().toString().trim();
            String portNumber = portNumberEditText.getText().toString().trim();

            if (ipAddress.isEmpty()) {

                ipAddressInput.setError("Ip Address is required.");

            } else if (!(Patterns.IP_ADDRESS.matcher(ipAddress).matches())) {

                ipAddressInput.setError("Ip Address is invalid.");

            } else if (portNumber.isEmpty()) {

                portNumberInput.setError("Port Number is required.");

            } else if (portNumber.length() != 4 || !(portNumber.matches(".*\\d.*")) || !(Integer.parseInt(portNumber) > 1023)) {

                portNumberInput.setError("Port Number is invalid.");

            } else {

                setLastConnectionDetails(new String[]{ipAddress, portNumber});

                connectButton.setText(R.string.making_connection);

                connectButton.setEnabled(false);

                // Calling the Async Task to Make Connection with Computer.

                new MakeConnection(this, ipAddress, portNumber).execute();

            }
        }
    }

    // Method for getting last connection info in shared preferences.

    @NonNull
    private String[] getLastConnectionDetails() {

        String[] arr = new String[2];
        arr[0] = sharedPreferences.getString("lastConnectedIP", "");
        arr[1] = sharedPreferences.getString("lastConnectedPort", "3000");

        return arr;
    }

    // Method for storing last connection info in shared preferences.

    private void setLastConnectionDetails(@NonNull String[] arr) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastConnectedIP", arr[0]);
        editor.putString("lastConnectedPort", arr[1]);
        editor.apply();
    }


    @Override
    public void onTaskCompleted(final int portNumber) {

        if (MainActivity.clientSocket == null) {

            Toast.makeText(getActivity(), "Server is not listening.", Toast.LENGTH_SHORT).show();

            connectButton.setText(R.string.computer_not_connected);

            connectButton.setEnabled(true);

        } else {

            connectButton.setText(R.string.computer_connected);

            ipAddressInput.setEnabled(false);

            portNumberInput.setEnabled(false);

            new Thread(new Runnable() {

                public void run() {

                    new Server(getActivity()).startServer(portNumber);

                }
            }).start();
        }

    }
}
