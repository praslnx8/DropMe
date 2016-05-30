package com.prasilabs.dropme.modules.rideSelect.views;

import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideSelectPresenter;

/**
 * Created by prasi on 30/5/16.
 */
public class RideSelectFragment extends CoreFragment<RideSelectPresenter>
{
    private static RideSelectFragment instance;

    public static RideSelectFragment getInstance()
    {
        if(instance == null)
        {
            instance = new RideSelectFragment();
        }
        return instance;
    }

    private RideSelectPresenter rideSelectPresenter = RideSelectPresenter.newInstance();


    @Override
    protected RideSelectPresenter setCorePresenter()
    {
        return rideSelectPresenter;
    }
}
