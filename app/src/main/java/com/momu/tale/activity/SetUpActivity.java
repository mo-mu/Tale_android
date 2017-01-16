package com.momu.tale.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.momu.tale.R;
import com.momu.tale.adapter.SetUpAdapter;
import com.momu.tale.item.SetupItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 설정 페이지
 * Created by songmho on 2016-09-30.
 */

public class SetUpActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SetupItem> items = new ArrayList<>();

    Context mContext;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private static final String TAG = "SetupActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        mContext = this;
        setToolbar();
        makeList();
    }

    /**
     * 리스트 생성 메소드.
     */
    private void makeList() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        items.add(new SetupItem("로그인",""));
        items.add(new SetupItem("백업하기",""));
        items.add(new SetupItem("버전","Ver. 1.0.0"));
        items.add(new SetupItem("만든이","MOMU"));
        recyclerView.setAdapter(new SetUpAdapter(mContext,items));
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ImageView imgLogo = (ImageView)toolbar.findViewById(R.id.imgLogo);
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
}

