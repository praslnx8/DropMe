package com.prasilabs.dropme.modules.mobileVerification.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.core.CoreDialogFragment;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.modules.mobileVerification.presenter.MobileVerificationPresenter;
import com.prasilabs.dropme.utils.ViewUtil;
import com.prasilabs.util.ValidateUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 30/6/16.
 */
public class MobileVerificationFragment extends CoreFragment<MobileVerificationPresenter> implements MobileVerificationPresenter.OtpVerifyCallBack {
    private static final String SKIP_ENABLE_STR = "skip_enable";
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.mobile_text)
    EditText mobileText;
    @BindView(R.id.otp_text)
    EditText otpText;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.skip_btn)
    Button skipBtn;
    private String phone;
    private String otp;
    private boolean isSkipEnabled = false;

    public static CoreDialogFragment getInstance(CoreFragment parentFragment, boolean isSkipEnabled) {

        MobileVerificationFragment mobileVerificationFragment = new MobileVerificationFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(SKIP_ENABLE_STR, isSkipEnabled);
        mobileVerificationFragment.setArguments(bundle);

        CoreDialogFragment coreDialogFragment = CoreDialogFragment.showFragmentAsDialog(parentFragment, mobileVerificationFragment);

        return coreDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isSkipEnabled = getArguments().getBoolean(SKIP_ENABLE_STR, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getFragmentView() == null) {
            setFragmentView(inflater.inflate(R.layout.fragment_mobile_verification, container, false));

            VDropMeUser vDropMeUser = UserManager.getDropMeUser(getContext());
            if (vDropMeUser != null && vDropMeUser.getMobile() != null) {
                mobileText.setText(vDropMeUser.getMobile());
            }

            if (isSkipEnabled) {
                skipBtn.setVisibility(View.VISIBLE);
            } else {
                skipBtn.setVisibility(View.GONE);
            }

        }

        return getFragmentView();
    }

    @Override
    protected MobileVerificationPresenter setCorePresenter() {
        return new MobileVerificationPresenter();
    }

    @Override
    protected boolean onDialogBackPressed() {
        if (viewFlipper.getDisplayedChild() > 0) {
            nextBtn.setText("NEXT");
            viewFlipper.showPrevious();

            return false;
        }

        return super.onDialogBackPressed();
    }

    @OnClick(R.id.skip_btn)
    protected void onSkipClicked() {
        if (getCoreDialogFragment() != null) {
            getCoreDialogFragment().dismiss();
        }
    }

    @OnClick(R.id.next_btn)
    protected void onNextClicked() {
        if (viewFlipper.getDisplayedChild() == 0) {
            phone = mobileText.getText().toString();

            if (ValidateUtil.validateMobile(phone)) {
                getPresenter().sendOtp(phone);
            } else {
                mobileText.setError("Enter valid phone number");
                phone = null;
            }
        } else {
            otp = otpText.getText().toString();

            if (TextUtils.isEmpty(otp) || otp.length() != 4) {
                otpText.setError("Enter valid OTP");
            } else {
                getPresenter().verifyOtp(phone, otp);
            }
        }
    }

    @Override
    public void verified(boolean status) {
        if (status) {
            UserManager.saveAndConfirmPhoneNo(getContext(), phone);

            if (getCoreDialogFragment() != null) {
                getCoreDialogFragment().dismiss();
            }
        } else {
            ViewUtil.t(getContext(), "unabel to verify otp. Make sure correct otp");
        }
    }

    @Override
    public void otpSent(boolean status) {
        if (status) {
            viewFlipper.showNext();
            UserManager.savePhoneNo(getContext(), phone);
            nextBtn.setText("VERIFY");
        } else {
            ViewUtil.t(getContext(), "unable to sent OTP");
        }
    }
}
