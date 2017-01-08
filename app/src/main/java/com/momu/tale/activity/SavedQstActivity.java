package com.momu.tale.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.momu.tale.R;
import com.momu.tale.adapter.SavedQstAdapter;
import com.momu.tale.item.SavedQstItem;

import java.util.ArrayList;

/**
 * Created by songmho on 2016-10-14.
 */

public class SavedQstActivity extends AppCompatActivity {
    LinearLayoutManager layoutManager;
    ArrayList<SavedQstItem> items = new ArrayList<>();
    SQLiteDatabase db = SplashActivity.sqliteHelper.getReadableDatabase();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setToolbar();
        initList();
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   //     getSupportActionBar().setLogo(R.drawable.fox_small_profile);
        getSupportActionBar().setTitle("");
    }

    /**
     * recyclerView 레이아웃 및 리스트 데이터를 세팅하는 메소드
     */
    private void initList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Intent getIntent = getIntent();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        Cursor cursor = null;
        try {
            Log.e("SavedQstActivity", "question id : " + getIntent.getIntExtra("questionId", -1));
            cursor = db.rawQuery("SELECT created_at, a, id FROM answer WHERE question_id = " + getIntent.getIntExtra("questionId", -1) + ";", null);
            while (cursor != null && cursor.moveToNext()) {
                SavedQstItem item = new SavedQstItem(getIntent.getStringExtra("question"), cursor.getString(0), cursor.getString(1), cursor.getInt(2),cursor.getInt(getIntent.getIntExtra("questionId", -1)));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        recyclerView.setAdapter(new SavedQstAdapter(getApplicationContext(), items));
    }
}
