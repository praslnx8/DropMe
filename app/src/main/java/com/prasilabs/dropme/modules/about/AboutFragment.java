package com.prasilabs.dropme.modules.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.core.CorePresenter;

/**
 * Created by prasi on 14/6/16.
 */
public class AboutFragment extends CoreFragment
{
    private static AboutFragment instance;

    public static AboutFragment getInstance()
    {
        if(instance == null)
        {
            instance = new AboutFragment();
        }

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
          if(getFragmentView() == null)
          {
                setFragmentView(inflater.inflate(R.layout.fragment_about, container, false));
          }

        return getFragmentView();
    }

    @Override
    protected CorePresenter setCorePresenter()
    {
        return null;
    }
}
