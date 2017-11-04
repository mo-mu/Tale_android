package com.momu.tale.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.momu.tale.R;
import com.momu.tale.fragment.MainFragment;
import com.momu.tale.fragment.WriteFragment;
import com.momu.tale.utility.LogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 메인 화면 페이지
 */
public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    String currentFragmentName;
    Fragment currentFragment;

    Context mContext;

    public static final String WRITE_FRAGMENT = "WriteFragment";
    public static final String MAIN_FRAGMENT_MAIN = "MainFragmentMain"; //처음 진입시 Splash에서 넘어온 경우
    public static final String MAIN_FRAGMENT_SAVE_QST = "MainFragmentSaveQst";  //SaveQstActivity에서 넘어온 경우

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.imgLogo) ImageView imgLogo;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseMessaging.getInstance().subscribeToTopic("all");
//        FirebaseMessaging.getInstance().subscribeToTopic("android");
        mContext = this;
        ButterKnife.bind(this);

        changeFragment(getIntent().getStringExtra("fragmentName"), null);
    }

    /**
     * Fragment 변경 처리.
     *
     * @param fragmentName Fragment 이름
     * @param bundle       번들 데이터
     */
    public void changeFragment(String fragmentName, Bundle bundle) {
        LogHelper.e(TAG, fragmentName + "으로 changeFragment 시도");
        switch (fragmentName) {
            case MAIN_FRAGMENT_MAIN:
                currentFragment = new MainFragment();
                setCurrentFragmentName("MainFragment");
                break;

            case MAIN_FRAGMENT_SAVE_QST:
                currentFragment = new WriteFragment();
                setCurrentFragmentName("WriteFragment");

                bundle = new Bundle();
                bundle.putInt("questionId", getIntent().getIntExtra("questionId", -1));
                bundle.putInt("answerId", getIntent().getIntExtra("answerId", -1));
                bundle.putString("question", getIntent().getStringExtra("question"));
                bundle.putString("answer", getIntent().getStringExtra("answer"));
                bundle.putBoolean("isFromMain", false);
                currentFragment.setArguments(bundle);
                break;

            case WRITE_FRAGMENT:
                currentFragment = new WriteFragment();
                setCurrentFragmentName("WriteFragment");
                break;
        }

        if (bundle != null) {
            currentFragment.setArguments(bundle);
        }

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, currentFragment);
        transaction.commit();

        setToolBar(fragmentName);
    }

    /**
     * 툴바를 설정하는 메소드.
     */
    private void setToolBar(String currentFragmentName) {
        switch (currentFragmentName) {
            case MAIN_FRAGMENT_MAIN:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                imgLogo.setVisibility(View.GONE);
                break;

            case MAIN_FRAGMENT_SAVE_QST:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                imgLogo.setVisibility(View.VISIBLE);
                break;

            case WRITE_FRAGMENT:
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                imgLogo.setVisibility(View.VISIBLE);
                break;
        }
        toolbar.setTitle("");     //title은 와이어프레임에 맞춰 없게 지정
        setSupportActionBar(toolbar);
    }

    /**
     * OptionMenu 아이템이 선택될 때 호출 된다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            switch (getCurrentFragmentName()) {
                case "WriteFragment":
                    ((WriteFragment) currentFragment).checkBeforeExist();
                    break;

                case "MainFragment":
                    super.onBackPressed();
                    break;

                default:
                    changeFragment("MainFragment", null);
                    break;
            }
        }
        return false;
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    /**
     * 백버튼 클릭 이벤트
     */
    @Override
    public void onBackPressed() {
        LogHelper.e(TAG, "onbackpressed currentFragmentName : " + getCurrentFragmentName());
        switch (getCurrentFragmentName()) {
            //글쓰기,수정 화면
            case "WriteFragment":
                ((WriteFragment) currentFragment).checkBeforeExist();   //글 작성중이면 다이얼로그를 띄워서 한번 더 물어본다.
                break;

            //메인 화면
            case "MainFragment":
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return;
                } else {
                    Toast.makeText(getBaseContext(), R.string.main_back_clicked, Toast.LENGTH_SHORT).show();
                }

                mBackPressed = System.currentTimeMillis();
                break;

            default:
                changeFragment("MainFragment", null);
                break;
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
     * 현재 MainActivity에 붙어있는 Fragment 이름을 설정한다.
     *
     * @param fragmentName 이름
     */
    public void setCurrentFragmentName(String fragmentName) {
        this.currentFragmentName = fragmentName;
    }
}
