package com.prasilabs.dropme.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


/**
 * Created by prasi on 6/2/16.
 */
public abstract class CoreActivity<T extends CorePresenter> extends AppCompatActivity
{
    private static final String TAG = CoreActivity.class.getSimpleName();
    private T corePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        corePresenter = setCorePresenter();
        if(corePresenter != null)
        {
            corePresenter.onCreate();
        }
    }

    protected abstract T setCorePresenter();

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
        if(corePresenter != null)
        {
            corePresenter.onDestroy();
        }
    }

    protected T getPresenter()
    {
        return corePresenter;
    }
}
