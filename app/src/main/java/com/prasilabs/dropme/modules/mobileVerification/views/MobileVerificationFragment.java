package com.prasilabs.dropme.modules.mobileVerification.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.VDropMeUser;
import com.prasilabs.dropme.constants.PermisionConstant;
import com.prasilabs.dropme.core.CoreDialogFragment;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.modules.mobileVerification.presenter.MobileVerificationPresenter;
import com.prasilabs.dropme.services.sms.SmsVerificationReciever;
import com.prasilabs.dropme.utils.ViewUtil;
import com.prasilabs.util.ValidateUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 30/6/16.
 */
public class MobileVerificationFragment extends CoreFragment<MobileVerificationPresenter> implements MobileVerificationPresenter.OtpVerifyCallBack {
    private static final String SKIP_ENABLE_STR = "skip_enable";

    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
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
        return new MobileVerificationPresenter(this);
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
                if (ActivityCompat.checkSelfPermission(getCoreActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    getSmsRecievePermisison();
                } else {
                    ViewUtil.showProgressView(getContext(), mainLayout, true);
                    getPresenter().sendOtp(phone);
                }
            } else {
                mobileText.setError("Enter valid phone number");
                phone = null;
            }
        } else {
            otp = otpText.getText().toString();

            if (TextUtils.isEmpty(otp) || otp.length() != 4) {
                otpText.setError("Enter valid OTP");
            } else {
                ViewUtil.showProgressView(getContext(), mainLayout, true);
                getPresenter().verifyOtp(otp);
            }
        }
    }

    @Override
    public void verified(boolean status) {
        ViewUtil.hideProgressView(getContext(), mainLayout);
        if (status) {
            UserManager.saveAndConfirmPhoneNo(getContext(), phone);

            if (getCoreDialogFragment() != null) {
                getCoreDialogFragment().dismiss();
            }
        } else {
            ViewUtil.t(getContext(), "unabel to verify otp. Make sure correct otp");
        }
    }

    private void getSmsRecievePermisison() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getCoreActivity(), Manifest.permission.READ_SMS)) {
            Snackbar.make(getFragmentView(), "Require access to read otp.",
                    Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.READ_SMS}, PermisionConstant.READ_SMS);
                        }
                    })
                    .show();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, PermisionConstant.READ_SMS);
        }
    }

    private void sendOtp() {
        SmsVerificationReciever.setSmsVerificationReciever(getCoreActivity());
        ViewUtil.showProgressView(getContext(), mainLayout, true);
        getPresenter().sendOtp(phone);
    }

    @Override
    public void otpSent(boolean status) {
        ViewUtil.hideProgressView(getContext(), mainLayout);
        if (status) {
            viewFlipper.showNext();
            UserManager.savePhoneNo(getContext(), phone);
            nextBtn.setText("VERIFY");
        } else {
            ViewUtil.t(getContext(), "unable to sent OTP");
        }
    }

    @Override
    public void otpRecieved(String tempOTp) {
        otpText.setText(otp);
        nextBtn.performClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermisionConstant.READ_SMS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendOtp();
            } else {
                sendOtp();
                Snackbar.make(getFragmentView(), "No permision. enter code manually", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SmsVerificationReciever.unRegisterSmsVerificationReciever(getCoreActivity());
    }
}