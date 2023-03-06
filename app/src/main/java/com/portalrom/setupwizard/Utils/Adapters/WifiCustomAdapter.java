package com.portalrom.setupwizard.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.portalrom.setupwizard.R;
import com.portalrom.setupwizard.Utils.DataModel.WifiDataModel;

import java.util.ArrayList;

public class WifiCustomAdapter extends ArrayAdapter<WifiDataModel>
        implements View.OnClickListener{

    private ArrayList<WifiDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView wifiLevel;
        ImageView wifiPassword;
        TextView wifiName;
        TextView wifiState;
    }

    public WifiCustomAdapter(ArrayList<WifiDataModel> data, Context context) {
        super(context, R.layout.activity_listview_wifi, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        WifiDataModel dataModel=(WifiDataModel)object;

        /*
        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WifiDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_listview_wifi, parent, false);
            viewHolder.wifiLevel = (ImageView) convertView.findViewById(R.id.wifiLogo);
            viewHolder.wifiPassword = (ImageView) convertView.findViewById(R.id.lockLogo);
            viewHolder.wifiName = (TextView) convertView.findViewById(R.id.wifiText);
            viewHolder.wifiState = (TextView) convertView.findViewById(R.id.wifiConnectionState);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.wifiName.setText(dataModel.getName());
        viewHolder.wifiState.setText(dataModel.getState());
        viewHolder.wifiLevel.setBackgroundResource(getDrawableByLevel(dataModel.getLevel()));
        viewHolder.wifiPassword.setBackgroundResource(getDrawableByLock(dataModel.getLock()));

        // Return the completed view to render on screen
        return convertView;
    }

    private int getDrawableByLock(boolean lock) {

        Integer drawable = 0;
        if(lock)
            drawable = R.drawable.ic_lock;
        else
            drawable = R.drawable.ic_unlock;


        return  drawable;
    }

    private int getDrawableByLevel(Integer level) {

        Integer drawable = 0;
        if(level >= -50)
            drawable = R.drawable.ic_wifi_excelent;
        else if(level <-50 && level >= -60)
            drawable = R.drawable.ic_wifi_good;
        else if(level <-60 && level >= -70)
            drawable = R.drawable.ic_wifi_fair;
        else if(level <-70)
            drawable = R.drawable.ic_wifi_weak;

        return  drawable;
    }
}