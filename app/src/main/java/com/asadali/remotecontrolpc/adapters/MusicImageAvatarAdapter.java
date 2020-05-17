package com.asadali.remotecontrolpc.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.asadali.remotecontrolpc.R;
import com.asadali.remotecontrolpc.models.MusicImageAvatar;
import com.asadali.remotecontrolpc.utility.Utility;

import java.util.ArrayList;

public class MusicImageAvatarAdapter extends ArrayAdapter<MusicImageAvatar> {

    private Context context;
    private int layoutResourceID;
    private ArrayList<MusicImageAvatar> objects;

    public MusicImageAvatarAdapter(Context context, int layoutResourceID, ArrayList<MusicImageAvatar> objects) {

        super(context, layoutResourceID, objects);

        this.context = context;

        this.layoutResourceID = layoutResourceID;

        this.objects = objects;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        MusicImageAvatarHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = NavigationDrawerItemAdapter.scanForActivity(context).getLayoutInflater();

            row = inflater.inflate(layoutResourceID, parent, false);

            holder = new MusicImageAvatarHolder();

            holder.icon = row.findViewById(R.id.avatarImageView);

            holder.avatarHeading = row.findViewById(R.id.avatarHeadingTextView);

            holder.avatarSubheading = row.findViewById(R.id.avatarSubheadingTextView);

            row.setTag(holder);

        } else {

            holder = (MusicImageAvatarHolder) row.getTag();

        }
        MusicImageAvatar item = objects.get(position);

        Bitmap bitmap;

        Utility utility = new Utility();

        if (item.getType().equals("image")) {

            bitmap = utility.decodeImageFile(item.getData());

            holder.icon.setImageBitmap(bitmap);

        } else {

            bitmap = utility.getAlbumArt(item.getIcon(), context);

            if (bitmap != null) {

                holder.icon.setImageBitmap(bitmap);

            } else {

                holder.icon.setImageResource(R.drawable.audio_icon);
            }
        }

        holder.avatarHeading.setText(item.getHeading());

        holder.avatarSubheading.setText(item.getSubheading());

        return row;
    }

    static class MusicImageAvatarHolder {
        ImageView icon;
        TextView avatarHeading, avatarSubheading;
    }
}
