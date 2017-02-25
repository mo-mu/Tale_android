package com.momu.tale.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.momu.tale.MySharedPreference;
import com.momu.tale.R;
import com.momu.tale.adapter.SetUpAdapter;
import com.momu.tale.item.SetupItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 설정 페이지
 * Created by songmho on 2016-09-30.
 */

public class SetUpActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SetupItem> items = new ArrayList<>();
    SetUpAdapter setupAdapter;
    Context mContext;
    FirebaseUser user;
    boolean isLogined = false;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.imgLogo) ImageView imgLogo;

    private static final String TAG = "SetupActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        mContext = this;
        setToolbar();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 리스트 생성 메소드.
     */
    private void makeList() {
        if (items != null) items.clear();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
            try {
                items.add(new SetupItem("로그아웃", user.getEmail()));
            } catch (NullPointerException e) {
                e.printStackTrace();
                items.add(new SetupItem("로그아웃", ""));
            }
            isLogined = true;
        } else {
            // No user is signed in
            items.add(new SetupItem("로그인", ""));
            isLogined = false;
        }

        String version = "0.0.0";
        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        MySharedPreference shpr = new MySharedPreference(mContext);
        items.add(new SetupItem("동기화", shpr.getIsSync()));
        items.add(new SetupItem("버전", "Ver. " + version));
        items.add(new SetupItem("만든이", "MOMU"));

        if (setupAdapter == null) {
            setupAdapter = new SetUpAdapter(mContext, items, isLogined);
            recyclerView.setAdapter(setupAdapter);
        } else {
            setupAdapter.notifyDataSetChanged();
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
        makeList();
    }
}

