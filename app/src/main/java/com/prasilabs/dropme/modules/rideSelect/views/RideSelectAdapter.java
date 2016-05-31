package com.prasilabs.dropme.modules.rideSelect.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.core.CoreAdapter;

/**
 * Created by prasi on 1/6/16.
 */
public class RideSelectAdapter<RideDetail, RideSelectViewHolder> extends CoreAdapter
{
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class RideSelectViewHolder extends RecyclerView.ViewHolder
    {
        public RideSelectViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
