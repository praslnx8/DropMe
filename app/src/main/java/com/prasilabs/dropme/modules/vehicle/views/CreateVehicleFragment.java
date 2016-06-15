package com.prasilabs.dropme.modules.vehicle.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.modules.vehicle.presenters.CreateVehiclePresenter;

/**
 * Created by prasi on 15/6/16.
 */
public class CreateVehicleFragment extends CoreFragment<CreateVehiclePresenter> {
    private static CreateVehicleFragment instance;

    public static CreateVehicleFragment getInstance() {
        if (instance == null) {
            instance = new CreateVehicleFragment();
        }
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getFragmentView() == null) {
            //setFragmentView(inflater.inflate(R.layout.fragment_create_vehicle, container, false));


        }

        return getFragmentView();
    }

    @Override
    protected CreateVehiclePresenter setCorePresenter() {
        return null;
    }
}
