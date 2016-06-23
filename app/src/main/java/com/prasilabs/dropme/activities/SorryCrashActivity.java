package com.prasilabs.dropme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.prasilabs.dropme.R;
import com.prasilabs.dropme.core.CoreActivity;
import com.prasilabs.dropme.core.CorePresenter;
import com.prasilabs.dropme.modelengines.CrashReportModelEngine;

import butterknife.OnClick;

/**
 * Created by prasi on 23/6/16.
 */
public class SorryCrashActivity extends CoreActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sorry_crash);

        CrashReportModelEngine.getInstance().reportCrashIfExist();
    }

    @OnClick(R.id.try_again_btn)
    protected void tryAgain() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.close_btn)
    protected void closeClicked() {
        finish();
        System.exit(0);
    }

    @Override
    protected CorePresenter setCorePresenter() {
        return null;
    }
}
