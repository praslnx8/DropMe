package com.prasilabs.dropme.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by prasi on 6/2/16.
 * All fragment should extend this class
 */
public abstract class CoreFragment<T extends CorePresenter> extends Fragment
{
    private static final String TAG = CoreFragment.class.getSimpleName();
    private View mFragmentView;
    private Context context;
    private CoreActivity coreActivity;
    private T corePresenter;

    public View getFragmentView()
    {
        return mFragmentView;
    }

    public void setFragmentView(View fragmentView)
    {
        this.mFragmentView = fragmentView;
        ButterKnife.bind(this, fragmentView);
    }

    protected abstract T setCorePresenter();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        corePresenter = setCorePresenter();
        if(corePresenter != null)
        {
            corePresenter.onCreate();
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(corePresenter != null)
        {
            corePresenter.onDestroy();
        }
    }

    protected boolean onDialogBackPressed()
    {
        return true;
    }

    public CoreActivity getCoreActivity()
    {
        return coreActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.coreActivity = (CoreActivity) activity;
    }

    @Override
    public Context getContext()
    {
        return context;
    }

    protected T  getPresenter()
    {
        return corePresenter;
    }
}
