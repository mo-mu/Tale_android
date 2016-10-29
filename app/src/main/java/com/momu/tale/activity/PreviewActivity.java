package com.momu.tale.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.momu.tale.R;
import com.momu.tale.adapter.PreviewAdapter;
import com.momu.tale.item.PreviewItem;

import java.util.ArrayList;

/**
 * 지난이야기 보여주는 페이지.
 * Created by songmho on 2016-09-30.
 */
public class PreviewActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PreviewItem> items = new ArrayList<>();

    SQLiteDatabase db = MainActivity.sqliteHelper.getReadableDatabase();

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
        getSupportActionBar().setTitle("");
    }

    /**
     * 리스트를 초기화하는 메소드
     */
    private void initList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT count(answer.a), answer.created_at, question.q, question.id  FROM question, answer WHERE question.id=answer.question_id GROUP BY question.q ORDER BY answer.created_at DESC;", null);
            while (cursor.moveToNext()) {
                PreviewItem item = new PreviewItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        recyclerView.setAdapter(new PreviewAdapter(getApplicationContext(), items));
    }
}
