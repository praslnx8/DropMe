package com.prasilabs.dropme.modules.vehicle.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MyRecyclerView;
import com.prasilabs.dropme.modules.vehicle.presenters.VehiclePresenter;

import butterknife.BindView;

/**
 * Created by prasi on 28/5/16.
 */
public class VehicleListFragment extends CoreFragment<VehiclePresenter>
{
    @BindView(R.id.vehicle_list)
    MyRecyclerView vehicleList;

    @Override
    protected VehiclePresenter setCorePresenter()
    {
        return new VehiclePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_vehicle_list, container, false));
        }

        return getFragmentView();
    }
}
