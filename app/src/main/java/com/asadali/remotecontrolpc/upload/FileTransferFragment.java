package com.asadali.remotecontrolpc.upload;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asadali.remotecontrolpc.main.MainActivity;
import com.asadali.remotecontrolpc.R;
import com.asadali.remotecontrolpc.utility.Utility;
import com.google.android.material.button.MaterialButton;

import java.io.File;

import file.AvatarFile;

// OK - 5 April 2020

public class FileTransferFragment extends Fragment implements View.OnClickListener {

    private TextView pathTextView;
    private File currentDirectory;
    private MaterialButton backButton;
    private String currentPath, rootPath;
    private ListView fileTransferListView;

    public FileTransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_file_transfer, container, false);

        backButton = rootView.findViewById(com.asadali.remotecontrolpc.R.id.backButton);

        pathTextView = rootView.findViewById(com.asadali.remotecontrolpc.R.id.pathTextView);

        fileTransferListView = rootView.findViewById(com.asadali.remotecontrolpc.R.id.fileTransferListView);

        currentPath = new Utility().getStoragePath();

        rootPath = currentPath;

        currentDirectory = new File(currentPath);

        backButton.setEnabled(false);

        backButton.setOnClickListener(this);

        new GetFilesList(fileTransferListView, getActivity()).execute(currentPath);

        fileTransferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                AvatarFile file = (AvatarFile) parent.getItemAtPosition(position);

                String path = file.getPath();

                File tempDirectoryOrFile = new File(path);

                if (tempDirectoryOrFile.isDirectory()) {

                    File[] tempArray = tempDirectoryOrFile.listFiles();

                    //to avoid crash when 0 item
                    if (tempArray != null && tempArray.length > 0) {

                        backButton.setEnabled(true);

                        currentPath = path;

                        currentDirectory = tempDirectoryOrFile;

                        pathTextView.setText(currentPath);

                        new GetFilesList(fileTransferListView, getActivity()).execute(currentPath);
                    }
                } else {

                    transferFile(file.getHeading(), file.getPath());

                }
            }

        });

        pathTextView.setText(currentPath);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            getActivity().setTitle(getResources().getString(R.string.file_transfer));

        }
    }

    @Override
    public void onClick(@NonNull View v) {

        int id = v.getId();

        if (id == com.asadali.remotecontrolpc.R.id.backButton) {

            currentPath = currentDirectory.getParent();

            if (currentPath != null) {

                currentDirectory = new File(currentPath);

                new GetFilesList(fileTransferListView, getActivity()).execute(currentPath);

                pathTextView.setText(currentPath);

                if (!currentPath.equals(rootPath)) {

                    backButton.setEnabled(true);

                } else {

                    backButton.setEnabled(false);

                }
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void transferFile(String name, String path) {

        if (MainActivity.clientSocket != null) {

            MainActivity.sendMessageToServer("FILE_TRANSFER_REQUEST");

            MainActivity.sendMessageToServer(name);

            new TransferFileToServer(getActivity()) {
                @Override
                public void receiveData(Object result) {

                }

            }.execute(name, path);

        } else {

            Toast.makeText(getActivity(), "Not connected to computer.", Toast.LENGTH_LONG).show();

        }
    }

}
