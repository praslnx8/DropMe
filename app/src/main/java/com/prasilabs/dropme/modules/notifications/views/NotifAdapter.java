package com.prasilabs.dropme.modules.notifications.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prasilabs.constants.PushMessageJobType;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreAdapter;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;
import com.prasilabs.gcmData.LocationSharePush;
import com.prasilabs.gcmData.RideInfoPush;

/**
 * Created by prasi on 14/6/16.
 */
public class NotifAdapter extends CoreAdapter<DropMeNotifs, RecyclerView.ViewHolder>
{
    private static final int VIEW_TYPE_ALERT = 1;
    private static final int VIEW_TYPE_LOC_SHARE = 2;

    private Context context;
    private static NotifAdapter instance;

    public static NotifAdapter getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new NotifAdapter();
        }
        instance.context = context;

        return instance;
    }

    private NotifAdapter(){}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(viewType == VIEW_TYPE_ALERT)
        {
            View view = View.inflate(context, R.layout.item_notif_alert, null);

            return new AlertViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_LOC_SHARE)
        {
            View view = View.inflate(context, R.layout.item_notif_loc_share, null);

            return new LocationShareViewHolder(view);
        }

        return new JunkDataViewHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof AlertViewHolder)
        {

        }
        else if(holder instanceof LocationShareViewHolder)
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        DropMeNotifs dropMeNotifs = list.get(position);
        if(dropMeNotifs.getJobType().equals(PushMessageJobType.RIDE_FOUND_ALERT_STR))
        {
            return VIEW_TYPE_ALERT;
        }
        else if(dropMeNotifs.getJobType().equals(PushMessageJobType.LOCATION_SHARE_STR))
        {
            return VIEW_TYPE_LOC_SHARE;
        }

        return 0;
    }

    public class AlertViewHolder extends RecyclerView.ViewHolder
    {


        public AlertViewHolder(View itemView)
        {
            super(itemView);
        }

        public void renderAlert(RideInfoPush rideInfoPush)
        {

        }
    }

    public class LocationShareViewHolder extends RecyclerView.ViewHolder
    {

        public LocationShareViewHolder(View itemView)
        {
            super(itemView);
        }

        public void renderData(LocationSharePush locationSharePush)
        {

        }
    }

    public class JunkDataViewHolder extends RecyclerView.ViewHolder
    {

        public JunkDataViewHolder(View itemView) {
            super(itemView);
        }
    }
}
