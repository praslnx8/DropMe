package com.prasilabs.dropme.customs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.core.CoreFragment;


/**
 * Created by prasi on 7/2/16.
 * Helper for navigating fragment or replacing fragment
 */
public class FragmentNavigator
{
    public static void navigateToFragment(CoreActivity coreActivity, CoreFragment coreFragment, boolean addToBackStack)
    {
        navigateToFragment(coreActivity, coreFragment, addToBackStack, 0);
    }

    public static void navigateToFragment(CoreActivity coreActivity, CoreFragment coreFragment, boolean addToBackStack, int viewId)
    {
        FragmentManager fragmentManager = coreActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);

        if (!coreFragment.isAdded())
        {
            fragmentTransaction.replace(viewId, coreFragment, coreFragment.getClass().getSimpleName());
            if (addToBackStack)
            {
                fragmentTransaction.addToBackStack(null);
            }
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public static void placeFragment(Fragment parentFragment, CoreFragment coreFragment, int viewId)
    {
        FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!coreFragment.isAdded())
        {
            fragmentTransaction.replace(viewId, coreFragment, coreFragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public static void placeFragment(CoreActivity coreActivity, CoreFragment coreFragment, int viewId)
    {
        FragmentManager fragmentManager = coreActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!coreFragment.isAdded())
        {
            fragmentTransaction.replace(viewId, coreFragment, coreFragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

}
