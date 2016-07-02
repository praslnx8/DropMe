package com.prasilabs.dropme.modules.profile.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.prasilabs.dropme.R;
import com.prasilabs.dropme.backend.dropMeApi.model.DropMeUserDetail;
import com.prasilabs.dropme.constants.PermisionConstant;
import com.prasilabs.dropme.core.CoreFragment;
import com.prasilabs.dropme.managers.UserManager;
import com.prasilabs.dropme.modules.profile.presenters.ProfilePresenter;
import com.prasilabs.dropme.utils.DateUtil;
import com.prasilabs.dropme.utils.ViewUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by prasi on 2/7/16.
 */
public class ProfileFragment extends CoreFragment<ProfilePresenter> implements ProfilePresenter.GetProfileCallBack {
    private static final String USER_ID_STR = "user_id";
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.user_image_view)
    ImageView userImage;
    @BindView(R.id.age_text)
    TextView ageText;
    @BindView(R.id.gender_text)
    TextView genderText;
    @BindView(R.id.phone_text)
    TextView phoneText;
    @BindView(R.id.ride_count_text)
    TextView rideCountText;
    @BindView(R.id.member_date_text)
    TextView memberDateText;
    @BindView(R.id.message_text)
    TextView messageText;
    @BindView(R.id.fab)
    FloatingActionButton fabButton;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    private long userId;
    private ProgressDialog progressDialog;

    private DropMeUserDetail userProfile;

    public static ProfileFragment newInstance(long userId) {
        ProfileFragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(USER_ID_STR, userId);
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.userId = getArguments().getLong(USER_ID_STR, 0);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getFragmentView() == null) {
            setFragmentView(inflater.inflate(R.layout.fragment_my_profile, container, false));

            Toolbar toolbar = (Toolbar) getFragmentView().findViewById(R.id.toolbar);

            getCoreActivity().setSupportActionBar(toolbar);
            if (getCoreActivity().getSupportActionBar() != null) {
                getCoreActivity().getSupportActionBar().setHomeButtonEnabled(true);
                getCoreActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCoreActivity().finish();
                }
            });

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            getPRofile();
        }

        return getFragmentView();
    }

    private void getPRofile() {
        progressDialog.show();
        getPresenter().getProfile(userId, this);
    }

    @Override
    protected ProfilePresenter setCorePresenter() {
        return new ProfilePresenter();
    }

    @Override
    public void getUserDetail(DropMeUserDetail dropMeUserDetail) {
        if (dropMeUserDetail != null) {
            this.userProfile = dropMeUserDetail;

            ViewUtil.renderImage(userImage, dropMeUserDetail.getPicture(), false);
            collapsingToolbarLayout.setTitle(dropMeUserDetail.getName());
            collapsingToolbarLayout.setTitleEnabled(true);

            genderText.setText(dropMeUserDetail.getGender());
            DateTime dateTime = dropMeUserDetail.getDob();
            if (dateTime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateTime.getValue());
                int year = calendar.get(Calendar.YEAR);
                int nowYear = Calendar.getInstance().get(Calendar.YEAR);
                int age = nowYear - year;
                ageText.setText(String.valueOf(age));
            } else {
                ageText.setText("N/A");
            }
            if (dropMeUserDetail.getMobile() != null && dropMeUserDetail.getMobileVerified()) {
                phoneText.setText(dropMeUserDetail.getMobile());
            } else {
                phoneText.setText("N/A");
            }
            rideCountText.setText(String.valueOf(dropMeUserDetail.getRideCount()));
            memberDateText.setText(DateUtil.getRelativeTime(dropMeUserDetail.getCreated().getValue()));
            if (TextUtils.isEmpty(dropMeUserDetail.getMessage())) {
                messageText.setText("Hi!, Lets share ride via DropMe");
            } else {
                messageText.setText(dropMeUserDetail.getMessage());
            }

            if (dropMeUserDetail.getId() == UserManager.getUserId(getContext())) {
                fabButton.setVisibility(View.VISIBLE);
                fabButton.setImageResource(R.drawable.ic_edit_white_24dp);
            } else if (dropMeUserDetail.getMobile() != null && dropMeUserDetail.getMobileVerified()) {
                fabButton.setVisibility(View.VISIBLE);
                fabButton.setImageResource(R.drawable.ic_call_white_24dp);
            } else {
                fabButton.setVisibility(View.GONE);
            }
        } else {
            ViewUtil.t(getContext(), "unable to get user detail");
        }
        progressDialog.dismiss();
    }

    @OnClick(R.id.fab)
    protected void onFabClicked() {
        long myUserId = UserManager.getUserId(getContext());
        if (userId == myUserId) {

        } else {
            callUSer();
        }
    }

    private void callUSer() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            getCallPermission();
        } else {
            Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(userProfile.getMobile()));
            try {
                getContext().startActivity(in);
            } catch (Exception ex) {
                ViewUtil.t(getContext(), "You cannot make phone calls in this phone");
            }
        }
    }

    private void getCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getCoreActivity(), Manifest.permission.CALL_PHONE)) {
            Snackbar.make(getCoreActivity().getCurrentFocus(), "Require permission to call.",
                    Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View view) {
                            requestPermissions(
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    PermisionConstant.REQUEST_CALL_NO);
                        }
                    })
                    .show();
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PermisionConstant.REQUEST_CALL_NO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermisionConstant.REQUEST_CALL_NO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callUSer();
            } else {
                Snackbar.make(getFragmentView(), "No permision. cannot call", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
