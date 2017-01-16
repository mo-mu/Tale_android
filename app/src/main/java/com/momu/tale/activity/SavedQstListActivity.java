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

import com.momu.tale.R;
import com.momu.tale.adapter.SavedQstListAdapter;
import com.momu.tale.config.CConfig;
import com.momu.tale.item.PreviewItem;
import com.momu.tale.utility.LogHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 지난이야기 리스트 보여주는 페이지.
 * Created by songmho on 2016-09-30.
 */
public class SavedQstListActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PreviewItem> items = new ArrayList<>();
    Context mContext;
    SQLiteDatabase db = SplashActivity.sqliteHelper.getReadableDatabase();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private static final String TAG = "SavedQstListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogHelper.e(TAG, "onCreate 진입");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        mContext = this;

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
     * 리스트를 초기화하는 메소드
     */
    private void initList() {
        if(items != null) {
            items.clear();
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT count(answer.a), answer.created_at, question.q, question.id  FROM question, answer WHERE question.id=answer.question_id GROUP BY question.q ORDER BY answer.created_at DESC;", null);
            while (cursor.moveToNext()) {
                PreviewItem item = new PreviewItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                items.add(item);
            }
        } catch (Exception e) {
            LogHelper.errorStackTrace(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        recyclerView.setAdapter(new SavedQstListAdapter(mContext, items));
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
     * 백버튼 선택 이벤트
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.e(TAG, "onactivityresult진입" + requestCode + " , " + resultCode);
        if (requestCode == CConfig.RESULT_DETAIL && resultCode == RESULT_OK) {
            initList();
        }
    }
}
