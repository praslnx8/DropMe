package com.prasilabs.dropme.modules.rideSelect.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideFilter;

/**
 * Created by prasi on 6/6/16.
 */
public class RideFilterView
{
    public static int FILTERTYPEGENDER = 1;
    public static int FILTERTYPEVEHICLE = 2;
    public static int FILTERTYPEDEST = 3;

    public static View getView(Context context, String filterName, final int filterType, final FilterCancelCallBack filterCancelCallBack)
    {
        View view = View.inflate(context, R.layout.item_filter, null);

        TextView filterNameText = (TextView) view.findViewById(R.id.filter_name_text);
        TextView clearText = (TextView) view.findViewById(R.id.remove_text);

        filterNameText.setText(filterName);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RideFilter rideFilter = RideFilter.getInstance();
                if(filterType == FILTERTYPEGENDER)
                {
                    rideFilter.setGender(null);
                }
                else if(filterType == FILTERTYPEDEST)
                {
                    rideFilter.setDestination(null);
                    rideFilter.setDestPt(null);
                }
                else if(filterType == FILTERTYPEVEHICLE)
                {
                    rideFilter.setvType(null);
                }

                if(filterCancelCallBack != null)
                {
                    filterCancelCallBack.filterDeleted();
                }
            }
        });

        return view;
    }

    public interface FilterCancelCallBack
    {
        void filterDeleted();
    }
}
