package com.momu.wtfs.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.momu.wtfs.R;
import com.momu.wtfs.fragment.MainFragment;

/**
 * Main Activity.<br>
 *     메인 화면 페이지
 */
public class MainActivity extends AppCompatActivity {

    Toolbar toolBar;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolBar();

        Fragment fragment=new MainFragment();       //처음 나올 Fragment 설정(MainFragment로)
        changeFragment(fragment);
    }

    /**
     * ChangeFragment.<br>
     *     MainActivity내 Fragment들을 변경시켜주는 메소드(Fragment 내에서도 사용 됨)
     * @param fragmentName
     */
    public void changeFragment(Fragment fragmentName){
        fragmentManager=getSupportFragmentManager();
        transaction= fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragmentName);
        transaction.commit();
    }

    /**
     * setToolBar.<br>
     *  툴바를 세팅하는 메소드.
     */
    private void setToolBar() {
        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");     //title은 와이어프레임에 맞춰 없게 지정
    }

}
