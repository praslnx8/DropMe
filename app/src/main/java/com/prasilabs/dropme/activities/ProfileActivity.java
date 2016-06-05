package com.prasilabs.dropme.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.modules.profile.presenters.ProfilePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileActivity extends CoreActivity<ProfilePresenter>
{
    ProfilePresenter profilePresenter = new ProfilePresenter();

    @BindView(R.id.user_image)
    ImageView userImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    protected ProfilePresenter setCorePresenter()
    {
        return profilePresenter;
    }

    @OnClick(R.id.fab)
    protected void fabButtonClicked()
    {

    }
}
