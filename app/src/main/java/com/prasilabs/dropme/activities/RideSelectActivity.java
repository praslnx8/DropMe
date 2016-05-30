package com.prasilabs.dropme.activities;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.customs.FragmentNavigator;
import com.prasilabs.dropme.modules.rideSelect.views.RideSelectFragment;

import butterknife.BindView;

public class RideSelectActivity extends CoreActivity
{
    @BindView(R.id.container)
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_select);

        FragmentNavigator.navigateToFragment(this, RideSelectFragment.getInstance(), false, mainLayout.getId());
    }
}
