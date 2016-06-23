package com.prasilabs.dropme.modules.rideSelect.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.activities.GenericActivity;
import com.prasilabs.dropme.backend.dropMeApi.model.RideDetail;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.FlowLayout;
import com.prasilabs.dropme.customs.MyRecyclerView;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideFilter;
import com.prasilabs.dropme.modules.rideSelect.presenters.RideSelectPresenter;
import com.prasilabs.dropme.utils.DialogUtils;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 14/6/16.
 */
public class RideSelectFragment extends CoreFragment<RideSelectPresenter> implements RideSelectPresenter.RenderViewCallBack, RideSelectPresenter.GetRidesCallBack
{

    private static final String TAG = RideSelectFragment.class.getSimpleName();
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.rider_list)
    MyRecyclerView ridersListView;
    @BindView(R.id.empty_layout)
    LinearLayout emptyJokerLayout;
    @BindView(R.id.flow_layout)
    FlowLayout filterShowLayout;

    private RideSelectAdapter rideSelectAdapter;

    public static RideSelectFragment newInstance()
    {
        RideSelectFragment rideSelectFragment = new RideSelectFragment();

        return rideSelectFragment;
    }

    @Override
    protected RideSelectPresenter setCorePresenter() {
        return new RideSelectPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        rideSelectAdapter = RideSelectAdapter.getInstance(getPresenter(), getCoreActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_selct_ride, container, false));


            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getCoreActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            autocompleteFragment.setHint("Search Destination");
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place)
                {
                    ConsoleLog.i(TAG, "Place: " + place.getName());

                    ViewUtil.showProgressView(getContext(), topLayout, true);
                    getPresenter().filterByDestination(String.valueOf(place.getName()), place.getLatLng(), RideSelectFragment.this);
                }

                @Override
                public void onError(Status status)
                {
                    ConsoleLog.i(TAG, "An error occurred: " + status);
                }
            });

            ridersListView.setAdapter(rideSelectAdapter);

            ViewUtil.showProgressView(getContext(), topLayout, true);
            getPresenter().getRideDetailList(RideSelectFragment.this);

        }

        return getFragmentView();
    }

    @Override
    public void getRides(List<RideDetail> rideDetailList)
    {
        ConsoleLog.i(TAG, "ridelist came for the views ");
        ViewUtil.hideProgressView(getContext(), topLayout);
        if(rideDetailList != null && rideDetailList.size() > 0)
        {
            ConsoleLog.i(TAG, "ride list size is : " + rideDetailList.size());
            rideSelectAdapter.clearAndAddItem(rideDetailList);
            ridersListView.setVisibility(View.VISIBLE);
            emptyJokerLayout.setVisibility(View.GONE);
        }
        else
        {
            emptyJokerLayout.setVisibility(View.VISIBLE);
            ridersListView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.filter_btn)
    protected void onFilterClicked()
    {
        DialogUtils.showRideSelectFilter(getContext(), new DialogUtils.FilterDialogCallBack() {
            @Override
            public void filterApplied()
            {
                ViewUtil.showProgressView(getContext(), topLayout, true);
                getPresenter().filter(RideSelectFragment.this);
            }
        });
    }

    @OnClick(R.id.create_alert_btn)
    protected void gotoCreateAlert()
    {
        GenericActivity.openAlertCreate(getContext());
        getCoreActivity().finish();
    }

    @Override
    public void renderFilterData()
    {
        filterShowLayout.removeAllViews();

        RideFilter rideFilter = RideFilter.getInstance();

        List<View> rideFilterView = rideFilter.getRideFilterViews(getContext(), new RideFilterView.FilterCancelCallBack()
        {
            @Override
            public void filterDeleted()
            {
                ViewUtil.showProgressView(getContext(), topLayout, true);
                getPresenter().filter(RideSelectFragment.this);
            }
        });

        for(View view : rideFilterView)
        {
            filterShowLayout.addView(view);
        }

    }
}
