package com.prasilabs.dropme.core;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.customs.FragmentNavigator;


/**
 * Created by prasi on 8/2/16.
 */
public class CoreDialogFragment extends DialogFragment
{
    private static final String TAG = CoreDialogFragment.class.getSimpleName();

    private LinearLayout linearLayout;
    private CoreFragment coreFragment;

    private View view;
    private boolean isRenderNeeded = false;


    public static CoreDialogFragment showFragmentAsFullScreen(CoreActivity coreActivity, CoreFragment coreFragment)
    {
        FragmentManager fm = coreActivity.getSupportFragmentManager();
        CoreDialogFragment coreDialogFragment = new CoreDialogFragment();
        coreDialogFragment.setCoreFragment(coreFragment);
        coreDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.fullScreen_Dialog);
        coreDialogFragment.show(fm, coreDialogFragment.getClass().getSimpleName());

        return coreDialogFragment;
    }

    public static CoreDialogFragment showFragmentAsFullScreen(CoreFragment parentFragment, CoreFragment coreFragment) {
        FragmentManager fm = parentFragment.getChildFragmentManager();
        CoreDialogFragment coreDialogFragment = new CoreDialogFragment();
        coreDialogFragment.setCoreFragment(coreFragment);
        coreDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.fullScreen_Dialog);
        coreDialogFragment.show(fm, coreDialogFragment.getClass().getSimpleName());

        return coreDialogFragment;
    }

    public static CoreDialogFragment showFragmentAsDialog(CoreActivity coreActivity, CoreFragment coreFragment) {
        FragmentManager fm = coreActivity.getSupportFragmentManager();
        CoreDialogFragment coreDialogFragment = new CoreDialogFragment();
        coreDialogFragment.setCoreFragment(coreFragment);
        coreDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.centre_Dialog);
        coreDialogFragment.show(fm, coreDialogFragment.getClass().getSimpleName());

        return coreDialogFragment;
    }

    public static CoreDialogFragment showFragmentAsDialog(CoreFragment parentFragment, CoreFragment coreFragment) {
        FragmentManager fm = parentFragment.getChildFragmentManager();
        CoreDialogFragment coreDialogFragment = new CoreDialogFragment();
        coreDialogFragment.setCoreFragment(coreFragment);
        coreDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.centre_Dialog);
        coreDialogFragment.show(fm, coreDialogFragment.getClass().getSimpleName());

        return coreDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {

        }
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL;

        if(view == null)
        {
            isRenderNeeded = true;
            view = inflater.inflate(R.layout.dialog_linear_layout, container, false);

            linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if(isRenderNeeded)
        {
            FragmentNavigator.placeFragment(this, coreFragment, linearLayout.getId());
        }

        this.getDialog().setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    return !coreFragment.onDialogBackPressed();
                }
                return false;
            }
        });
    }

    public void setCoreFragment(CoreFragment coreFragment)
    {
        this.coreFragment = coreFragment;
    }
}
