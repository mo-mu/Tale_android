package com.momu.wtfs.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.momu.wtfs.R;
import com.momu.wtfs.adapter.PreviewAdapter;
import com.momu.wtfs.item.PreviewItem;

import java.util.ArrayList;

/**
 * PreviewActivity<br>
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

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolBar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setToolbar(toolBar);

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


    /**
     * setToolBar.<br>
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setLogo(R.drawable.fox_small_profile);
        getSupportActionBar().setTitle("");
    }
}
