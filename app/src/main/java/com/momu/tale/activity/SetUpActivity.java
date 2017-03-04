package com.momu.tale.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.momu.tale.MySharedPreference;
import com.momu.tale.R;
import com.momu.tale.config.CConfig;
import com.momu.tale.utility.LogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 설정 페이지
 * Created by songmho on 2016-09-30.
 */

public class SetUpActivity extends AppCompatActivity {
    Context mContext;
    FirebaseUser user;
    boolean isLogined = false;

    MySharedPreference sharedPreference;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.imgLogo) ImageView imgLogo;

    @BindView(R.id.layout_login) LinearLayout layoutLogin;
    @BindView(R.id.txtLoginTitle) TextView txtLoginTitle;
    @BindView(R.id.txtLoginSub) TextView txtLoginSub;
    @BindView(R.id.txtSyncTitle) TextView txtSyncTitle;
    @BindView(R.id.txtVersionTitle) TextView txtVersionTitle;
    @BindView(R.id.txtVersionSub) TextView txtVersionSub;
    @BindView(R.id.txtAboutTitle) TextView txtAboutTitle;
    @BindView(R.id.txtAboutSub) TextView txtAboutSub;

    @BindView(R.id.schSync) Switch switchSync;

    private static final String TAG = "SetupActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        mContext = this;

        sharedPreference = new MySharedPreference(mContext);

        setToolbar();
        setTypeFace();
        setVersionName();
    }

    /**
     * 폰트 설정
     */
    void setTypeFace() {
        Typeface typeFace1 = Typeface.createFromAsset(mContext.getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        txtLoginTitle.setTypeface(typeFace1);
        txtLoginSub.setTypeface(typeFace1);
        txtSyncTitle.setTypeface(typeFace1);
        txtVersionTitle.setTypeface(typeFace1);
        txtVersionSub.setTypeface(typeFace1);
        txtAboutTitle.setTypeface(typeFace1);
        txtAboutSub.setTypeface(typeFace1);
    }

    /**
     * 동기화 및 로그인 여부 확인, 표시.
     */
    private void initSync() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
            txtLoginTitle.setText("로그아웃");
            txtLoginSub.setText(user.getEmail());
            isLogined = true;
            switchSync.setChecked(sharedPreference.getIsSync());
        } else {
            // No user is signed in
            txtLoginTitle.setText("로그인");
            txtLoginSub.setText("");
            isLogined = false;
            switchSync.setChecked(false);
        }

    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        imgLogo.setVisibility(View.GONE);
    }

    /**
     * 버전 표시
     */
    void setVersionName() {
        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            txtVersionSub.setText(i.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * OptionMenu 아이템이 선택될 때 호출 된다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        initSync();
    }

    @OnClick(R.id.schSync)
    void syncSwitchClick() {
        if (user == null) {
            Toast.makeText(mContext, "로그인 후 동기화가 가능합니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, SignUpActivity.class));
        } else {
            MySharedPreference sharedPreference = new MySharedPreference(mContext);
            sharedPreference.changeSync(!sharedPreference.getIsSync());
        }
    }

    @OnClick(R.id.layout_login)
    void layoutLoginClick() {
        if (isLogined && user != null) {
            FirebaseAuth.getInstance().signOut();
            user = FirebaseAuth.getInstance().getCurrentUser();
            isLogined = false;
            Toast.makeText(mContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

            sharedPreference.changeSync(false); //동기화 값 저장.

            initSync();

            LogHelper.e(TAG, "로그아웃 함, user : " + user);
        } else {
            startActivity(new Intent(mContext, SignInActivity.class));
        }
    }
}

