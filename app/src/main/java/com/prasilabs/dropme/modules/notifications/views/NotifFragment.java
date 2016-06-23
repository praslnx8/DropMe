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
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.modules.notifications.presenters.NotifPresenter;
import com.prasilabs.dropme.services.notification.DropMeNotifCreator;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by prasi on 14/6/16.
 */
public class NotifFragment extends CoreFragment<NotifPresenter> implements NotifPresenter.GetNotificationCallBack
{
    private static final String NOT_ID_STR = "not_id";
    private static NotifFragment instance;
    @BindView(R.id.notification_list_view)
    MyRecyclerView notificationListView;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    boolean isShowLoader = false;
    private NotifAdapter notifAdapter;

    public static NotifFragment getInstance() {
        return getInstance(0);
    }

    public static NotifFragment getInstance(int notId) {
        if (instance == null) {
            instance = new NotifFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(NOT_ID_STR, notId);
        instance.setArguments(bundle);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        notifAdapter = NotifAdapter.getInstance(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            int id = bundle.getInt(NOT_ID_STR, 0);
            if (id != 0) {
                DropMeNotifCreator.cancelNotif(getContext(), id);
            }
        }

        if (!UserManager.isUserLoggedIn(getContext())) {
            getCoreActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_my_notifs, container, false));

            notificationListView.setAdapter(notifAdapter);
        }

        makeApiCall();

        return getFragmentView();
    }

    private void makeApiCall()
    {
        isShowLoader = true;
        ViewUtil.showProgressView(getContext(), topLayout, true);
        getPresenter().getNotifications();
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

        notifAdapter.clearAndAddItem(dropMeNotifsList);
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
