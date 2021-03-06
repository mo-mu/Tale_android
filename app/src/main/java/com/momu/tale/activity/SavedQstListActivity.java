package com.momu.tale.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.momu.tale.R;
import com.momu.tale.SqliteHelper;
import com.momu.tale.adapter.SavedQstListAdapter;
import com.momu.tale.config.CConfig;
import com.momu.tale.item.SavedQstListItem;
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
    ArrayList<SavedQstListItem> items = new ArrayList<>();
    Context mContext;
    SqliteHelper sqliteHelper = new SqliteHelper(this, CConfig.DBNAME, null, CConfig.DBVERSION);
    SQLiteDatabase db;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.imgLogo) ImageView imgLogo;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.layout_empty) LinearLayout layoutEmpty;
    @BindView(R.id.txt_empty) TextView txtEmpty;

    private static final String TAG = "SavedQstListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogHelper.e(TAG, "onCreate 진입");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        mContext = this;

        db = sqliteHelper.getReadableDatabase();
        Typeface typeFace = Typeface.createFromAsset(getAssets(), CConfig.FONT_YANOLJA_YACHE_REGULAR);
        txtEmpty.setTypeface(typeFace);

        setToolbar();
        initList();
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    /**
     * 리스트를 초기화하는 메소드
     */
    private void initList() {
        items.clear();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT count(answer.a), answer.created_at, question.q, question.id  FROM question, answer WHERE question.id=answer.question_id GROUP BY question.q ORDER BY answer.created_at DESC;", null);
            while (cursor.moveToNext()) {
                SavedQstListItem item = new SavedQstListItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
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

        //리스트가 없을 경우 우는 여우 보여줌
        if (items.size() == 0) {
            imgLogo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            imgLogo.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
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
            setResult(RESULT_OK);
        }
    }
}
