package com.prasilabs.dropme.modules.rides.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.MyRideInfo;
import com.prasilabs.dropme.core.CoreAdapter;
import com.prasilabs.dropme.utils.DateUtil;

import butterknife.BindView;
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

        @BindView(R.id.time_text) TextView timeText;
        @BindView(R.id.souce_text) TextView sourceText;
        @BindView(R.id.dest_text) TextView destText;
        @BindView(R.id.status_text) TextView statusText;
        @BindView(R.id.vehicle_type_text) TextView vehicleTypeText;

        public MyRideViewHolder(View itemView)
        {
            super(itemView);

            ButterKnife.bind(itemView);
        }

        public void renderData(Context context, MyRideInfo myRideInfo)
        {
            timeText.setText(DateUtil.getRelativeTime(myRideInfo.getDate().getValue()));
            sourceText.setText(myRideInfo.getSourceLocName());
            destText.setText(myRideInfo.getDestLocName());
            if(myRideInfo.getCurrent())
            {
                statusText.setText("Progress");
                statusText.setTextColor(context.getResources().getColor(R.color.red));
            }
            else
            {

                statusText.setText("Past");
                statusText.setTextColor(context.getResources().getColor(R.color.blue));
            }
            vehicleTypeText.setText(myRideInfo.getVehicle().getType());
        }
    }
}
