package com.prasilabs.dropme.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.customs.FragmentNavigator;
import com.prasilabs.dropme.modules.profile.views.ProfileFragment;

import butterknife.BindView;

/**
 * Created by prasi on 2/7/16.
 */
public class GenericCollapsingActivity extends CoreActivity {
    private static final String REQUEST_FOR = "requestFor";
    private static final String ID_STR = "id";

    private static final int MY_PROFILE = 1;

    @BindView(R.id.container)
    LinearLayout container;

    public static void openMyProfile(Context context, long userId) {
        Intent intent = new Intent(context, GenericCollapsingActivity.class);
        intent.putExtra(REQUEST_FOR, MY_PROFILE);
        intent.putExtra(ID_STR, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generic);

        int requestFor = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestFor = bundle.getInt(REQUEST_FOR);
        }

        if (requestFor == MY_PROFILE) {
            long userID = bundle.getLong(ID_STR);
            FragmentNavigator.navigateToFragment(this, ProfileFragment.newInstance(userID), false, container.getId());
        }
    }

    @Override
    protected CorePresenter setCorePresenter() {
        return null;
    }
}