package com.prasilabs.dropme.modules.rides.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.activities.RideCreateActivity;
import com.prasilabs.dropme.backend.dropMeApi.model.MyRideInfo;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MyRecyclerView;
import com.prasilabs.dropme.modules.rides.presenter.MyRidesPresenter;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 10/6/16.
 */
public class MyRidesFragment extends CoreFragment<MyRidesPresenter> implements MyRidesPresenter.MyRideListCallBack
{
    private MyRidesPresenter myRidesPresenter = new MyRidesPresenter(this);
    private boolean isShowLoading;

    private static MyRidesFragment instance;

    public static MyRidesFragment getInstance()
    {
        if(instance == null)
        {
            instance = new MyRidesFragment();
        }

        return instance;
    }

    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.create_ride_btn)
    Button createRideBtn;
    @BindView(R.id.my_ride_list_view)
    MyRecyclerView myRideListView;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    private MyRideAdapter myRideAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        myRideAdapter = new MyRideAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_my_rides, container, false));

            myRideListView.setAdapter(myRideAdapter);

            myRideListView.handleScroll(new MyRecyclerView.HandlePaginationScroll() {
                @Override
                public void onScroll(int pageSize, int skip)
                {
                    makeApiCall(skip);
                }
            });
        }

        makeApiCall(0);

        return getFragmentView();
    }

    private void makeApiCall(int skip)
    {
        ViewUtil.showProgressView(getContext(), topLayout, true);
        isShowLoading = true;
        myRidesPresenter.getMyRides(false, skip);
    }

    @OnClick(R.id.create_ride_btn)
    protected void onCreateClicked()
    {
        Intent intent = new Intent(getContext(), RideCreateActivity.class);
        startActivity(intent);
        getCoreActivity().finish();
    }

    @Override
    protected MyRidesPresenter setCorePresenter()
    {
        return myRidesPresenter;
    }

    @Override
    public void getMyRideList(int skip, List<MyRideInfo> myRideInfoList)
    {
        if(skip == 0)
        {
            myRideAdapter.clearAndAddItem(myRideInfoList);
        }
        else
        {
            myRideAdapter.addListItem(myRideInfoList);
        }

        if(isShowLoading)
        {
            ViewUtil.hideProgressView(getContext(), topLayout);
            isShowLoading = false;
        }

        myRideListView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void getMyRideIsEmpty()
    {
        if(isShowLoading)
        {
            ViewUtil.hideProgressView(getContext(), topLayout);
            isShowLoading = false;
        }

        myRideListView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }
}
