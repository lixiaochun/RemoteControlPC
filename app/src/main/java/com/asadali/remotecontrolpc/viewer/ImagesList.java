package com.asadali.remotecontrolpc.viewer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.asadali.remotecontrolpc.R;
import com.asadali.remotecontrolpc.reciever.CallbackReceiver;
import com.asadali.remotecontrolpc.models.MusicImageAvatar;
import com.asadali.remotecontrolpc.utility.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class ImagesList extends AsyncTask<Void, Void, ArrayList<MusicImageAvatar>> implements CallbackReceiver {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    ImagesList(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<MusicImageAvatar> doInBackground(Void... params) {
        ArrayList<MusicImageAvatar> imagesList = new ArrayList<>();
        ContentResolver imageResolver = context.getContentResolver();
        Uri imageUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor = imageResolver.query(imageUri, null, null, null, null);
        Utility utility = new Utility();
        if (imageCursor != null && imageCursor.moveToFirst()) {
            int titleColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME);
            int dataColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATA);
            int sizeColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE);
            int dateColumn = imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_TAKEN);
            do {
                String thisTitle = imageCursor.getString(titleColumn);
                String thisData = imageCursor.getString(dataColumn);
                String thisDate = imageCursor.getString(dateColumn);
                int thisSize = imageCursor.getInt(sizeColumn);//in bytes
                int icon = R.drawable.image_icon;
                String subHeading = utility.getSize(thisSize) + ", " + utility.getDate(thisDate, "dd MMM yyyy hh:mm a");
                //duration set to 0 because it is for image
                imagesList.add(new MusicImageAvatar(icon, 0, thisTitle, subHeading, thisData, "image"));
            } while (imageCursor.moveToNext());
        }
        if (imageCursor != null) {
            imageCursor.close();
        }

        Collections.sort(imagesList, new Comparator<MusicImageAvatar>() {
            public int compare(MusicImageAvatar a, MusicImageAvatar b) {
                return a.getHeading().compareTo(b.getHeading());
            }
        });
        return imagesList;
    }

    @Override
    protected void onPostExecute(ArrayList<MusicImageAvatar> imagesList) {
        receiveData(imagesList);
    }

    @Override
    public abstract void receiveData(Object result);
}