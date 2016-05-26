package com.prasilabs.dropme.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


/**
 * Created by prasi on 6/2/16.
 */
public abstract class CoreActivity extends AppCompatActivity
{
    private static final String TAG = CoreActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
