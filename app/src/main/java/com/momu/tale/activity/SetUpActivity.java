package com.momu.tale.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.momu.tale.R;
import com.momu.tale.adapter.SetUpAdapter;
import com.momu.tale.item.PreviewItem;
import com.momu.tale.item.SetupItem;

import java.util.ArrayList;

/**
 * 설정 페이지
 * Created by songmho on 2016-09-30.
 */

public class SetUpActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SetupItem> items = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setToolbar();

        makeList();
    }

    /**
     * 리스트 생성 메소드.
     */
    private void makeList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        items.add(new SetupItem("로그인",""));
        items.add(new SetupItem("백업하기",""));
        items.add(new SetupItem("버전","Ver. 1.0.0"));
        items.add(new SetupItem("만든이",""));
        recyclerView.setAdapter(new SetUpAdapter(getApplicationContext(),items));
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ImageView imgLogo = (ImageView)toolBar.findViewById(R.id.imgLogo);
        imgLogo.setVisibility(View.GONE);
    }
}

