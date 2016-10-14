package com.momu.wtfs.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.momu.wtfs.R;
import com.momu.wtfs.adapter.SavedQstAdapter;
import com.momu.wtfs.item.SavedQstItem;

import java.util.ArrayList;

/**
 * Created by songmho on 2016-10-14.
 */

public class SavedQstActivity extends AppCompatActivity {

    LinearLayoutManager layoutManager;
    ArrayList<SavedQstItem> items = new ArrayList<>();
    SQLiteDatabase db= MainActivity.sqliteHelper.getReadableDatabase();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolBar =(Toolbar)findViewById(R.id.toolBar);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        Intent getIntent = getIntent();

        setToolbar(toolBar);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        Cursor cursor =db.rawQuery("SELECT created_at, a, id  FROM answer WHERE question_id="+getIntent.getIntExtra("questionId",-1)+";",null);
        while (cursor.moveToNext()){
            SavedQstItem item=new SavedQstItem(getIntent.getStringExtra("question"),cursor.getString(0),cursor.getString(1),cursor.getInt(2));
            items.add(item);
        }
        recyclerView.setAdapter(new SavedQstAdapter(getApplicationContext(),items));
    }

    /**
     * setToolBar.<br>
     *  툴바를 세팅하는 메소드.
     */
    private void setToolbar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setLogo(R.drawable.fox_small_profile);
        getSupportActionBar().setTitle("");
    }
}
