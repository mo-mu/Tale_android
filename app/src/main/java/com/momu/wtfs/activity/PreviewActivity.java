package com.momu.wtfs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.momu.wtfs.R;
import com.momu.wtfs.adapter.PreviewAdapter;
import com.momu.wtfs.item.PreviewItem;

import java.util.ArrayList;

/**
 *  PreviewActivity<br>
 *      지난이야기 보여주는 페이지.
 * Created by songmho on 2016-09-30.
 */

public class PreviewActivity extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;
    ArrayList<PreviewItem> items=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Toolbar toolBar =(Toolbar)findViewById(R.id.toolBar);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        setToolbar(toolBar);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        PreviewItem item=new PreviewItem(3,"2016/ 10/ 04","testtest");
        items.add(item);
        items.add(item);
        items.add(item);

        recyclerView.setAdapter(new PreviewAdapter(getApplicationContext(),items));
    }

    /**
     * setToolBar.<br>
     *  툴바를 세팅하는 메소드.
     */
    private void setToolbar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setLogo(R.drawable.fox_toolbar);
        getSupportActionBar().setTitle("");
    }
}
