package com.prasilabs.dropme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.customs.FragmentNavigator;
import com.prasilabs.dropme.debug.ConsoleLog;
import com.prasilabs.dropme.modules.rideAlerts.view.MyAlertsFragment;
import com.prasilabs.dropme.modules.rides.views.MyRidesFragment;

import butterknife.BindView;

/**
 * Created by prasi on 10/6/16.
 */
public class GenericActivity extends CoreActivity
{
    private static final String REQUEST_FOR = "requestFor";
    private static final int MY_RIDE = 1;
    private static final int MY_ALERT = 2;
    private static final int MY_NOTIFS = 3;
    private static final String TAG = GenericActivity.class.getSimpleName();

    public static void openMyRide(Context context)
    {
        Intent intent = new Intent(context, GenericActivity.class);
        intent.putExtra(REQUEST_FOR, MY_RIDE);
        context.startActivity(intent);
    }

    public static void openMyAlert(Context context)
    {
        Intent intent = new Intent(context, GenericActivity.class);
        intent.putExtra(REQUEST_FOR, MY_ALERT);
        context.startActivity(intent);
    }

    public static void openNotification(Context context)
    {
        Intent intent = new Intent(context, GenericActivity.class);
        intent.putExtra(REQUEST_FOR, MY_NOTIFS);
        context.startActivity(intent);
    }

    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generic);

        int requestFor = 0;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            requestFor = bundle.getInt(REQUEST_FOR);
        }

        if(requestFor == MY_RIDE)
        {
            FragmentNavigator.navigateToFragment(this, MyRidesFragment.getInstance(), false, container.getId());
            if(getSupportActionBar() != null)
            {
                getSupportActionBar().setTitle("My Rides");
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
        else if(requestFor == MY_ALERT)
        {
            FragmentNavigator.navigateToFragment(this, MyAlertsFragment.getInstance(), false, container.getId());
            if(getSupportActionBar() != null)
            {
                getSupportActionBar().setTitle("My Alerts");
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
        else
        {
            ConsoleLog.s(TAG, "inappropriate call");
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected CorePresenter setCorePresenter()
    {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
