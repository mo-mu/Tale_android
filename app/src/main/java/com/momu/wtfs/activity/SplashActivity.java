package com.momu.wtfs.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.momu.wtfs.R;

/**
 * SPLASH_TIME(1500ms)만큼 존재한 후 사라짐
 * Created by songmho on 2016-09-26.
 */

public class SplashActivity extends AppCompatActivity {
    static int SPLASH_TIME = 1500;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView txtSaying = (TextView) findViewById(R.id.txtSaying);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/SeoulNamsanCL.ttf");
        txtSaying.setTypeface(typeFace);
        txtSaying.setText("네가 오후 네 시에 온다면\n 난 세 시부터 행복해지기 시작할거야");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_TIME);
    }
}
