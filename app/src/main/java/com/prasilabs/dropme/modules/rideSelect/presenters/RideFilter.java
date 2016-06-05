package com.prasilabs.dropme.modules.rideSelect.presenters;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.prasilabs.dropme.modules.rideSelect.views.RideFilterView;
import com.prasilabs.enums.Gender;
import com.prasilabs.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 6/6/16.
 */
public class RideFilter
{
    private static RideFilter instance;

    public static RideFilter getInstance()
    {
        if(instance == null)
        {
            instance = new RideFilter();
        }

        return instance;
    }

    private RideFilter(){}

    private LatLng destPt;
    private String destination;
    private Gender gender;
    private VehicleType vType;

    public LatLng getDestPt() {
        return destPt;
    }

    public void setDestPt(LatLng destPt) {
        this.destPt = destPt;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public VehicleType getvType() {
        return vType;
    }

    public void setvType(VehicleType vType) {
        this.vType = vType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<View> getRideFilterViews(Context context, RideFilterView.FilterCancelCallBack filterCancelCallBack)
    {
        List<View> viewList = new ArrayList<>();

        if(getvType() != null)
        {
            View view = RideFilterView.getView(context, getvType().name(), RideFilterView.FILTERTYPEVEHICLE, filterCancelCallBack);
            viewList.add(view);
        }

        if(getDestination() != null)
        {
            View view = RideFilterView.getView(context, getDestination(), RideFilterView.FILTERTYPEDEST, filterCancelCallBack);
            viewList.add(view);
        }

        if(getGender() != null)
        {
            View view = RideFilterView.getView(context, getGender().name(), RideFilterView.FILTERTYPEGENDER, filterCancelCallBack);
            viewList.add(view);
        }

        return viewList;
    }
}
