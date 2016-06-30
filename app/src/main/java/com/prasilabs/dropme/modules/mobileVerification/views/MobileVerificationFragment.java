package com.prasilabs.dropme.modules.mobileVerification.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.modules.mobileVerification.presenter.MobileVerificationPresenter;

import butterknife.BindView;

/**
 * Created by prasi on 30/6/16.
 */
public class MobileVerificationFragment extends CoreFragment<MobileVerificationPresenter> {

    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.skip_btn)
    Button skipBtn;

    public static MobileVerificationFragment getInstance() {
        MobileVerificationFragment mobileVerificationFragment = new MobileVerificationFragment();
        return mobileVerificationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getFragmentView() == null) {
            setFragmentView(inflater.inflate(R.layout.fragment_mobile_verification, container, false));


        }

        return getFragmentView();
    }

    @Override
    protected MobileVerificationPresenter setCorePresenter() {
        return new MobileVerificationPresenter();
    }
}
