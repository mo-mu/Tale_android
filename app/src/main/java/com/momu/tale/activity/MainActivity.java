package com.momu.tale.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.momu.tale.R;
import com.momu.tale.SqliteHelper;
import com.momu.tale.fragment.MainFragment;
import com.momu.tale.fragment.WriteFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 메인 화면 페이지
 */
public class MainActivity extends AppCompatActivity {
    public Toolbar toolBar;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    String currentFragmentName;
    Fragment currentFragment;

    public static SqliteHelper sqliteHelper;
    private final String DBNAME = "wtfs.db";
    private final int DBVERSION = 1;
    public SQLiteDatabase db;

    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        setToolBar();

        currentFragment = new MainFragment();       //처음 나올 Fragment 설정(MainFragment로)
        changeFragment(currentFragment, "MainFragment");
        currentFragmentName = "MainFragment";

        initDatabase(); //DB 초기화
    }

    /**
     * MainActivity내 Fragment들을 변경시켜주는 메소드(Fragment 내에서도 사용 됨)
     *
     * @param fragment
     */
    public void changeFragment(Fragment fragment, String fragmentName) {
        currentFragmentName = fragmentName;
        fragmentManager = getSupportFragmentManager();
        currentFragment = fragment;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, currentFragment);
        transaction.commit();
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolBar() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolBar.setTitle("");     //title은 와이어프레임에 맞춰 없게 지정
        setSupportActionBar(toolBar);
    }


    /**
     * DB 초기화하는 메소드
     */
    protected void initDatabase() {
        sqliteHelper = new SqliteHelper(this, DBNAME, null, DBVERSION);
        db = sqliteHelper.getWritableDatabase();

        //임시로 질문 데이터 저장하는 코드 삽입함
        boolean isExist = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from question;", null);
            while (cursor.moveToNext())
                if (cursor.getString(1) != null)
                    isExist = true;

            if (!isExist) {      //Question 데이터 생성(임의로)
                String sql = "insert into question (id, q, created_at) " +
                        "values (1,'당신에게 가장 기억에 남는 여행지는 어디인가요?','" + format.format(now) + "');";
                db.execSQL(sql);
                sql = "insert into question (id, q, created_at) " +
                        "values (2,'당신에게 가장 기억에 남는 사람은 누구인가요?','" + format.format(now) + "');";
                db.execSQL(sql);
                sql = "insert into question (id, q, created_at) " +
                        "values (3,'당신이 가장 좋아하는 동물은 무엇인가요?','" + format.format(now) + "');";
                db.execSQL(sql);
                sql = "insert into question (id, q, created_at) " +
                        "values (4,'당신이 가장 좋아하는 음식은 무엇인가요?','" + format.format(now) + "');";
                db.execSQL(sql);
                sql = "insert into question (id, q, created_at) " +
                        "values (5,'당신에게 가장 좋았던 기억은 무엇인가요?','" + format.format(now) + "');";
                db.execSQL(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        //글 작성중이면 다이얼로그를 띄워서 한번 더 물어본다.
        if (getCurrentFragmentName().equals("WriteFragment")) {
            ((WriteFragment)currentFragment).checkBeforeExist();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 현재 MainActivity에 붙어있는 Fragment 이름을 리턴한다
     *
     * @return Fragment 이름
     */
    public String getCurrentFragmentName() {
        return currentFragmentName;
    }
}
