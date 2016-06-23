package com.prasilabs.dropme.modules.rideSelect.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.core.CoreAdapter;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideSelectPresenter;
import com.prasilabs.dropme.services.location.DropMeLocatioListener;
import com.prasilabs.dropme.utils.DateUtil;
import com.prasilabs.dropme.utils.DialogUtils;
import com.prasilabs.dropme.utils.LocationUtils;
import com.prasilabs.dropme.utils.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prasi on 1/6/16.
 */
public class RideSelectAdapter extends CoreAdapter<RideDetail, RideSelectAdapter.RideSelectViewHolder>
{
    public static RideSelectAdapter instance;
    public CoreActivity coreActivity;
    private RideSelectPresenter rideSelectPresenter;

    private RideSelectAdapter(CoreActivity coreActivity) {
        this.coreActivity = coreActivity;
    }

    public static RideSelectAdapter getInstance(RideSelectPresenter rideSelectPresenter, CoreActivity coreActivity)
    {
        if (instance == null || instance.coreActivity == null)
        {
            instance = new RideSelectAdapter(coreActivity);
        }
        instance.rideSelectPresenter = rideSelectPresenter;
        return instance;
    }

    @Override
    public RideSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(coreActivity).inflate(R.layout.item_ride_select, parent, false);

        return new RideSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RideSelectViewHolder holder, int position)
    {
        RideDetail rideDetail = list.get(position);
        holder.render(coreActivity, rideDetail);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class RideSelectViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.main_layout)
        LinearLayout mainLayout;
        @BindView(R.id.distance_text)
        TextView distanceText;
        @BindView(R.id.user_image)
        ImageView userImage;
        @BindView(R.id.name_text)
        TextView nameText;
        @BindView(R.id.gender_text)
        TextView genderText;
        @BindView(R.id.dest_text)
        TextView destText;
        @BindView(R.id.vehicle_type_text)
        TextView vehicleTypeText;
        @BindView(R.id.start_text)
        TextView startText;


        public RideSelectViewHolder(View itemView)
        {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void render(final CoreActivity coreActivity, final RideDetail rideDetail)
        {
            ViewUtil.renderImage(userImage, rideDetail.getOwnerPicture(), true);
            LatLng currentLatLng = DropMeLocatioListener.getLatLng(coreActivity);
            String distancestr = LocationUtils.formatDistanceBetween(currentLatLng, LocationUtils.convertToLatLng(rideDetail.getCurrentLatLng()));
            if(distancestr != null)
            {
                distanceText.setText(distancestr);
            }
            else
            {
                distanceText.setText("N/A");
            }
            nameText.setText(rideDetail.getOwnerName());
            genderText.setText(rideDetail.getGender());
            destText.setText(rideDetail.getDestLoc());
            vehicleTypeText.setText(rideDetail.getVehicleType());
            if(rideDetail.getStartDate() != null)
            {
                startText.setText(DateUtil.getRelativeTime(rideDetail.getStartDate().getValue()));
            }
            else
            {
                startText.setText("Now");
            }

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    DialogUtils.showSelectRideMenu(coreActivity, rideDetail, new DialogUtils.ShareLocCallBack() {
                        @Override
                        public void shareTo(long recieverId) {
                            if (rideSelectPresenter != null) {
                                rideSelectPresenter.shareLocation(recieverId, new RideSelectPresenter.ShareLocationCallBack() {
                                    @Override
                                    public void locationShared() {
                                        ViewUtil.t(coreActivity, "Your location is shared");
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }
}
