package com.momu.tale.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.messaging.FirebaseMessaging;
import com.momu.tale.R;
import com.momu.tale.fragment.MainFragment;
import com.momu.tale.fragment.WriteFragment;

/**
 * 메인 화면 페이지
 */
public class MainActivity extends AppCompatActivity {
    public Toolbar toolBar;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    String currentFragmentName;
    Fragment currentFragment;


    Context mContext;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic("android");
        mContext = this;
        setToolBar();

        changeToMainFragment(null, getIntent().getStringExtra("fragmentName"));
    }

    /**
     * MainActivity내 Fragment들을 변경시켜주는 메소드(Fragment 내에서도 사용 됨)
     *
     * @param fragment
     */
    public void changeToMainFragment(Fragment fragment, String fragmentName) {
        Log.e(TAG, "changeToMainFragment 진입");
        currentFragmentName = fragmentName;
        if (currentFragmentName == null || currentFragmentName.equals("first")) {        //처음 프레그먼트 설정(splash에서 넘어올 경우)
            currentFragment = new MainFragment();
            currentFragmentName = "MainFragment";
        } else if (currentFragmentName.equals("savedQst")) {        //saveQst Activity에서 넘어올 경우
            Fragment writeFragment = new WriteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("questionId", getIntent().getIntExtra("questionId", -1));
            bundle.putInt("answerId", getIntent().getIntExtra("answerId", -1));
            bundle.putString("question", getIntent().getStringExtra("question"));
            bundle.putString("answer", getIntent().getStringExtra("answer"));
            bundle.putBoolean("isMain", false);
            writeFragment.setArguments(bundle);

            currentFragmentName = "WriteFragment";
            currentFragment = writeFragment;
        } else {               //나머지 경우 (Fragment 간의 공유 등)
            currentFragment = fragment;
        }

        fragmentManager = getSupportFragmentManager();
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
     * OptionMenu 아이템이 선택될 때 호출 된다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if (getCurrentFragmentName().equals("WriteFragment")) {
                ((WriteFragment) currentFragment).checkBeforeExist();
            } else if(currentFragmentName == null || currentFragmentName.equals("MainFragment")){
                super.onBackPressed();
            } else {
                changeToMainFragment();
            }
        }
        return false;
    }



    @Override
    public void onBackPressed() {
        //글 작성중이면 다이얼로그를 띄워서 한번 더 물어본다.
        if (getCurrentFragmentName().equals("WriteFragment")) {
            ((WriteFragment) currentFragment).checkBeforeExist();
        } else if(currentFragmentName == null || currentFragmentName.equals("MainFragment")){
            super.onBackPressed();
        } else {
            changeToMainFragment();
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

    /**
     * 현재 프레그먼트를 MainFragment로 변경하는 메소드
     */
    public void changeToMainFragment() {
        Fragment recvFragment = new MainFragment();     //공통적으로 MainFragment로 전환시킴
        changeToMainFragment(recvFragment, "MainFragment");
    }
}
