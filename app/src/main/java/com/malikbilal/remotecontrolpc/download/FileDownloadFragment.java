package com.malikbilal.remotecontrolpc.download;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.malikbilal.remotecontrolpc.main.MainActivity;
import com.malikbilal.remotecontrolpc.R;
import com.google.android.material.button.MaterialButton;

import java.util.Stack;

import file.AvatarFile;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

// OK - 5 April 2020

public class FileDownloadFragment extends Fragment implements View.OnClickListener {

    private TextView pathTextView;
    private Stack<String> pathStack;
    private MaterialButton backButton;
    private ListView fileDownloadListView;

    public FileDownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_file_download, container, false);

        backButton = rootView.findViewById(com.malikbilal.remotecontrolpc.R.id.backButton);

        pathTextView = rootView.findViewById(com.malikbilal.remotecontrolpc.R.id.pathTextView);

        fileDownloadListView = rootView.findViewById(com.malikbilal.remotecontrolpc.R.id.fileTransferListView);

        pathStack = new Stack<>();

        pathStack.push("/");

        pathTextView.setText(pathStack.peek());

        backButton.setEnabled(false);

        backButton.setOnClickListener(this);

        getFiles();

        fileDownloadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                AvatarFile file = (AvatarFile) parent.getItemAtPosition(position);

                String path = file.getPath();

                if (file.getType().equals("folder")) {

                    pathStack.push(path);

                    String currentPath = pathStack.peek();

                    pathTextView.setText(currentPath);

                    backButton.setEnabled(true);

                    getFiles();

                } else {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        downloadFile(file.getHeading(), file.getPath());

                    } else {

                        checkForPermissionAndDownload(file.getHeading(), file.getPath());

                    }
                }
            }

        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            getActivity().setTitle(getResources().getString(R.string.file_download));

        }
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();

        if (id == com.malikbilal.remotecontrolpc.R.id.backButton) {

            pathStack.pop();

            String currentPath = pathStack.peek();

            getFiles();

            pathTextView.setText(currentPath);

            if (!currentPath.equals("/")) {

                backButton.setEnabled(true);

            } else {

                backButton.setEnabled(false);

            }
        }

    }

    private void getFiles() {

        String message = "FILE_DOWNLOAD_LIST_FILES";

        MainActivity.sendMessageToServer(message);

        message = pathStack.peek();

        MainActivity.sendMessageToServer(message);

        new GetFilesList(fileDownloadListView, getActivity()).execute(pathStack.peek());
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkForPermissionAndDownload(String name, String path) {

        if (getActivity() != null) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            } else {

                downloadFile(name, path);

            }
        }

    }

    private void downloadFile(String name, String path) {
        if (MainActivity.clientSocket != null) {

            MainActivity.sendMessageToServer("FILE_DOWNLOAD_REQUEST");

            MainActivity.sendMessageToServer(path);

            new DownloadFileFromServer(getActivity()).execute(name);

        } else {

            Toast.makeText(getActivity(), "Not connected with computer.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {

            Toast.makeText(getActivity(), "Click again to download file.", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(getActivity(), "Write permission is necessary for file download.", Toast.LENGTH_LONG).show();

        }

    }
}
