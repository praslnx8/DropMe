package com.prasilabs.dropme.modules.rideAlerts.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.core.CoreAdapter;

import butterknife.ButterKnife;

/**
 * Created by prasi on 13/6/16.
 */
public class MyAlertsAdapter extends CoreAdapter<RideAlertIo, MyAlertsAdapter.MyAlertsViewHolder>
{
    private Context context;

    private static MyAlertsAdapter instance;

    private MyAlertsAdapter(){}

    public static MyAlertsAdapter getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new MyAlertsAdapter();
        }

        instance.context = context;

        return instance;
    }

    @Override
    public MyAlertsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = View.inflate(context, R.layout.item_my_alert, null);

        return new MyAlertsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAlertsViewHolder holder, int position)
    {
        holder.renderData(context, list.get(position));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyAlertsViewHolder extends RecyclerView.ViewHolder
    {

        public MyAlertsViewHolder(View itemView)
        {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void renderData(Context context, RideAlertIo rideAlertIo)
        {

        }
    }
}
