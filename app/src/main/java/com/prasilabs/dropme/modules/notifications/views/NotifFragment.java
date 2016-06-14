package com.prasilabs.dropme.modules.notifications.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.customs.MyRecyclerView;
import com.prasilabs.dropme.db.dbPojos.DropMeNotifs;
import com.prasilabs.dropme.modules.notifications.presenters.NotifPresenter;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by prasi on 14/6/16.
 */
public class NotifFragment extends CoreFragment<NotifPresenter> implements NotifPresenter.GetNotificationCallBack
{
    private static NotifFragment instance;

    public static NotifFragment getInstance()
    {
        if(instance == null)
        {
            instance = new NotifFragment();
        }

        return instance;
    }

    @BindView(R.id.notification_list_view)
    MyRecyclerView notificationListView;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    boolean isShowLoader = false;

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
            setFragmentView(inflater.inflate(R.layout.fragment_my_notifs, container, false));
        }

        getPresenter().getNotifications();

        return getFragmentView();
    }

    @Override
    protected NotifPresenter setCorePresenter()
    {
        return new NotifPresenter(this);
    }

    @Override
    public void getNotifications(List<DropMeNotifs> dropMeNotifsList)
    {
        if(isShowLoader)
        {
            isShowLoader = false;
            ViewUtil.hideProgressView(getContext(), topLayout);
        }

        emptyLayout.setVisibility(View.GONE);
        notificationListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty()
    {
        if(isShowLoader)
        {
            isShowLoader = false;
            ViewUtil.hideProgressView(getContext(), topLayout);
        }

        emptyLayout.setVisibility(View.VISIBLE);
        notificationListView.setVisibility(View.GONE);
    }
}
