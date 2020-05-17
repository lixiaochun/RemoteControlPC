package com.asadali.remotecontrolpc.main;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.asadali.remotecontrolpc.sender.MessageToServer;
import com.asadali.remotecontrolpc.R;
import com.asadali.remotecontrolpc.connect.ConnectFragment;
import com.asadali.remotecontrolpc.download.FileDownloadFragment;
import com.asadali.remotecontrolpc.keyboard.KeyboardFragment;
import com.asadali.remotecontrolpc.screen.LiveScreenFragment;
import com.asadali.remotecontrolpc.player.MediaPlayerFragment;
import com.asadali.remotecontrolpc.server.Server;
import com.asadali.remotecontrolpc.power.PowerFragment;
import com.asadali.remotecontrolpc.touchpad.TouchpadFragment;
import com.asadali.remotecontrolpc.upload.FileTransferFragment;
import com.asadali.remotecontrolpc.viewer.ImageViewerFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static android.Manifest.permission;

// OK - 5 April 2020

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Socket clientSocket = null;
    public static ObjectInputStream objectInputStream = null;
    public static ObjectOutputStream objectOutputStream = null;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Selecting connect fragment by default
        replaceFragment(R.id.nav_connect);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitleTextAppearance(this, R.style.ToolbarView);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkForPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(MainActivity.this, "Read permission is necessary to transfer.", Toast.LENGTH_LONG).show();

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        replaceFragment(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void replaceFragment(int id) {
        Fragment fragment = null;

        if (id == R.id.nav_connect) {

            fragment = new ConnectFragment();

        } else if (id == R.id.nav_touchpad) {

            fragment = new TouchpadFragment();

        } else if (id == R.id.nav_keyboard) {

            fragment = new KeyboardFragment();

        } else if (id == R.id.nav_file_transfer) {

            fragment = new FileTransferFragment();

        } else if (id == R.id.nav_file_download) {

            fragment = new FileDownloadFragment();

        } else if (id == R.id.nav_image_viewer) {

            fragment = new ImageViewerFragment();

        } else if (id == R.id.nav_media_player) {

            fragment = new MediaPlayerFragment();

        } else if (id == R.id.nav_power_setting) {

            fragment = new PowerFragment();

        } else if (id == R.id.nav_live_screen) {

            fragment = new LiveScreenFragment();

        }
        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
    }

    protected void onDestroy() {
        super.onDestroy();

        try {

            if (MainActivity.clientSocket != null) {

                MainActivity.clientSocket.close();
            }
            if (MainActivity.objectOutputStream != null) {

                MainActivity.objectOutputStream.close();
            }
            if (MainActivity.objectInputStream != null) {

                MainActivity.objectInputStream.close();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        Server.closeServer();
    }

    // This method is called from fragments to send message to server (Desktop)
    public static void sendMessageToServer(String message) {
        if (MainActivity.clientSocket != null) {
            new MessageToServer().execute(String.valueOf(message), "STRING");
        }
    }

    public static void sendMessageToServer(int message) {
        if (MainActivity.clientSocket != null) {
            new MessageToServer().execute(String.valueOf(message), "INT");
        }
    }

    public static void socketException() {
        if (MainActivity.clientSocket != null) {
            try {
                MainActivity.clientSocket.close();
                MainActivity.objectOutputStream.close();
                MainActivity.clientSocket = null;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void sendMessageToServer(float message) {
        if (MainActivity.clientSocket != null) {
            new MessageToServer().execute(String.valueOf(message), "FLOAT");
        }
    }

    public static void sendMessageToServer(long message) {
        if (MainActivity.clientSocket != null) {
            new MessageToServer().execute(String.valueOf(message), "LONG");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Click again to Download.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Failed to Download.", Toast.LENGTH_SHORT).show();
                }
            }
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Click again to Download.", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "File Transfer will not Work.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
