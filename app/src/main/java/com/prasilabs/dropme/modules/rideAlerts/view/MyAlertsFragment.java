package com.prasilabs.dropme.modules.rideAlerts.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.activities.GenericActivity;
import com.prasilabs.dropme.backend.dropMeApi.model.RideAlertIo;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MyRecyclerView;
import com.prasilabs.dropme.modules.rideAlerts.presenters.MyAlertsPresenter;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 13/6/16.
 */
public class MyAlertsFragment extends CoreFragment<MyAlertsPresenter> implements MyAlertsPresenter.AlertCallBack
{
    private static MyAlertsFragment instance;
    @BindView(R.id.recycler_view)
    MyRecyclerView alertListView;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    private boolean isLoading;
    private MyAlertsAdapter myAlertsAdapter;

    public static MyAlertsFragment getInstance()
    {
        if(instance == null)
        {
            instance = new MyAlertsFragment();
        }

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        myAlertsAdapter = MyAlertsAdapter.getInstance(getContext(), getPresenter());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_my_alerts, container, false));

            alertListView.setAdapter(myAlertsAdapter);
        }

        makeApiCall(0,false);

        return getFragmentView();
    }

    public void makeApiCall(int skip, boolean isRefresh)
    {

        if(skip == 0)
        {
            isLoading = true;
            ViewUtil.showProgressView(getContext(), topLayout, true);
        }
        getPresenter().getAlerts(0, 10, false);
    }

    @Override
    protected MyAlertsPresenter setCorePresenter()
    {
        return new MyAlertsPresenter(this);
    }

    @Override
    public void getAlertList(int skip, List<RideAlertIo> rideAlertIoList)
    {
        if(isLoading)
        {
            ViewUtil.hideProgressView(getContext(), topLayout);
            isLoading = false;
        }

        if(skip == 0)
        {
            myAlertsAdapter.clearAndAddItem(rideAlertIoList);

            alertListView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        else
        {
            myAlertsAdapter.addListItem(rideAlertIoList);
        }
    }

    @OnClick(R.id.create_alert_btn)
    protected void createAlert()
    {
        GenericActivity.openAlertCreate(getContext());
    }

    @Override
    public void showEmpty(int skip)
    {
        if(isLoading)
        {
            ViewUtil.hideProgressView(getContext(), topLayout);
            isLoading = false;
        }

        if(skip == 0)
        {
            isLoading = false;
            alertListView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }
}
