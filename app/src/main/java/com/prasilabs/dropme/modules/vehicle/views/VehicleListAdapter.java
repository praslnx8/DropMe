package com.prasilabs.dropme.modules.vehicle.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.backend.dropMeApi.model.VVehicle;
import com.prasilabs.dropme.core.CoreAdapter;

/**
 * Created by prasi on 28/5/16.
 */
public class VehicleListAdapter extends CoreAdapter<VVehicle, RecyclerView.ViewHolder>
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
    public int getItemViewType(int position)
    {
        if(position == list.size() -1)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size() + 1;
    }

    class VehicleListViewHolder extends RecyclerView.ViewHolder
    {

        public VehicleListViewHolder(View itemView) {
            super(itemView);
        }
    }

    class AddVehicleViewHolder extends RecyclerView.ViewHolder
    {

        public AddVehicleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
