package com.malikbilal.remotecontrolpc.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.malikbilal.remotecontrolpc.R;
import com.malikbilal.remotecontrolpc.utility.Utility;

import java.util.ArrayList;

import file.AvatarFile;

public class AvatarFileAdapter extends ArrayAdapter<AvatarFile> {

    private Context context;

    private int layoutResourceID;

    private ArrayList<AvatarFile> objects;

    public AvatarFileAdapter(Context context, int layoutResourceID, ArrayList<AvatarFile> objects) {

        super(context, layoutResourceID, objects);

        this.context = context;

        this.objects = objects;

        this.layoutResourceID = layoutResourceID;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        AvatarFileHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = NavigationDrawerItemAdapter.scanForActivity(context).getLayoutInflater();

            row = inflater.inflate(layoutResourceID, parent, false);

            holder = new AvatarFileHolder();

            holder.icon = row.findViewById(R.id.avatarImageView);

            holder.avatarHeading = row.findViewById(R.id.avatarHeadingTextView);

            holder.avatarSubheading = row.findViewById(R.id.avatarSubheadingTextView);

            row.setTag(holder);

        } else {

            holder = (AvatarFileHolder) row.getTag();

        }

        AvatarFile item = objects.get(position);

        Bitmap bitmap;

        Utility utility = new Utility();

        String type = item.getType();

        switch (type) {

            case "image":
                //files from server has icon -1
                if (item.getIcon() != -1) {

                    bitmap = utility.decodeImageFile(item.getPath());

                    holder.icon.setImageBitmap(bitmap);

                } else {

                    holder.icon.setImageResource(R.drawable.image_icon);

                }
                break;

            case "mp3":

                holder.icon.setImageResource(R.drawable.audio_icon);

                break;

            case "pdf":

                holder.icon.setImageResource(R.drawable.pdf_icon);

                break;
            case "file":

                holder.icon.setImageResource(R.drawable.file_icon);

                break;

            case "folder":

                holder.icon.setImageResource(R.drawable.folder_icon);

                break;
        }

        holder.avatarHeading.setText(item.getHeading());

        holder.avatarSubheading.setText(item.getSubheading());

        return row;
    }

    static class AvatarFileHolder {

        ImageView icon;

        TextView avatarHeading, avatarSubheading;

    }
}
