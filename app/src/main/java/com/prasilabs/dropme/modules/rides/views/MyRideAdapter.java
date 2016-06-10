package com.prasilabs.dropme.modules.rides.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.MyRideInfo;
import com.prasilabs.dropme.core.CoreAdapter;

import butterknife.ButterKnife;

/**
 * Created by prasi on 10/6/16.
 */
public class MyRideAdapter extends CoreAdapter<MyRideInfo, MyRideAdapter.MyRideViewHolder>
{
    private Context context;

    public MyRideAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public MyRideViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = View.inflate(context, R.layout.item_my_ride, null);

        return new MyRideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRideViewHolder holder, int position)
    {
        MyRideInfo myRideInfo = list.get(position);

        holder.renderData(context, myRideInfo);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyRideViewHolder extends RecyclerView.ViewHolder
    {

        public MyRideViewHolder(View itemView)
        {
            super(itemView);

            ButterKnife.bind(itemView);
        }

        public void renderData(Context context, MyRideInfo myRideInfo)
        {

        }
    }
}
