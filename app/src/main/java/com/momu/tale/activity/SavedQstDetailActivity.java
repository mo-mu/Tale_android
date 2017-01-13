package com.momu.tale.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.momu.tale.R;
import com.momu.tale.adapter.SavedQstDetailAdapter;
import com.momu.tale.config.CConfig;
import com.momu.tale.item.SavedQstItem;
import com.momu.tale.utility.LogHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 지난 이야기 상세 화면
 * Created by songmho on 2016-10-14.
 */

public class SavedQstDetailActivity extends AppCompatActivity {
    LinearLayoutManager layoutManager;
    ArrayList<SavedQstItem> items = new ArrayList<>();
    SQLiteDatabase db = SplashActivity.sqliteHelper.getReadableDatabase();
    Context mContext;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private static final String TAG = "SavedQstDetailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogHelper.e(TAG, "onCreate 진입");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = this;
        ButterKnife.bind(this);

        setToolbar();
        initList();
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    /**
     * recyclerView 레이아웃 및 리스트 데이터를 세팅하는 메소드
     */
    private void initList() {
        recyclerView.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        Cursor cursor = null;
        try {
            Intent getIntent = getIntent();
            LogHelper.e("SavedQstDetailActivity", "question id : " + getIntent.getIntExtra("questionId", -1));
            cursor = db.rawQuery("SELECT created_at, a, id FROM answer WHERE question_id = " + getIntent.getIntExtra("questionId", -1) + ";", null);
            while (cursor != null && cursor.moveToNext()) {
                SavedQstItem item = new SavedQstItem(getIntent.getStringExtra("question"), cursor.getString(0), cursor.getString(1), cursor.getInt(2), getIntent.getIntExtra("questionId", -1));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        recyclerView.setAdapter(new SavedQstDetailAdapter(mContext, items));
        recyclerView.setVisibility(View.VISIBLE);
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

    /**
     * 백버튼 클릭 이벤트
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.e(TAG, "onactivityresult진입" + requestCode + " , " + resultCode);
        if(requestCode == CConfig.RESULT_MODIFY && resultCode == RESULT_OK) {
            initList();
        }
    }
}
