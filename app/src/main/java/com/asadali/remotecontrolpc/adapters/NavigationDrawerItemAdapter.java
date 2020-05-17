package com.asadali.remotecontrolpc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.asadali.remotecontrolpc.models.NavigationDrawerItem;

public class NavigationDrawerItemAdapter extends ArrayAdapter<NavigationDrawerItem> {
    private Context context;
    private int layoutResourceID;
    private NavigationDrawerItem[] objects;

    public NavigationDrawerItemAdapter(Context context, int layoutResourceID, NavigationDrawerItem[] objects) {
        super(context, layoutResourceID, objects);
        this.context = context;
        this.layoutResourceID = layoutResourceID;
        this.objects = objects;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        NavigationDrawerItemHolder holder;

        if (row == null) {

            LayoutInflater inflater = (scanForActivity(context)).getLayoutInflater();

            row = inflater.inflate(layoutResourceID, parent, false);

            holder = new NavigationDrawerItemHolder();

            row.setTag(holder);

        } else {

            holder = (NavigationDrawerItemHolder) row.getTag();

        }
        NavigationDrawerItem item = objects[position];

        holder.textTitle.setText(item.getTitle());

        holder.imgIcon.setImageResource(item.getIcon());

        return row;
    }

    static class NavigationDrawerItemHolder {
        ImageView imgIcon;
        TextView textTitle;
    }

    static Activity scanForActivity(Context context) {

        if (context == null)

            return null;

        else if (context instanceof Activity)

            return (Activity) context;

        else if (context instanceof ContextWrapper)

            return scanForActivity(((ContextWrapper) context).getBaseContext());

        return null;
    }
}
