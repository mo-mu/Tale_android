package com.momu.tale.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.momu.tale.MySharedPreference;
import com.momu.tale.R;
import com.momu.tale.config.CConfig;
import com.momu.tale.preference.AppPreference;
import com.momu.tale.utility.LogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 설정 페이지
 * Created by songmho on 2016-09-30.
 */

public class SetUpActivity extends AppCompatActivity {
    Context mContext;
    FirebaseUser user;
    boolean isLogined = false;

    MySharedPreference sharedPreference;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.imgLogo) ImageView imgLogo;

    @BindView(R.id.layout_login) LinearLayout layoutLogin;
    @BindView(R.id.txtLoginTitle) TextView txtLoginTitle;
    @BindView(R.id.txtLoginSub) TextView txtLoginSub;
    @BindView(R.id.txtSyncTitle) TextView txtSyncTitle;
    @BindView(R.id.txtVersionTitle) TextView txtVersionTitle;
    @BindView(R.id.txtVersionSub) TextView txtVersionSub;
    @BindView(R.id.txtAboutTitle) TextView txtAboutTitle;
    @BindView(R.id.txtAboutSub) TextView txtAboutSub;

    @BindView(R.id.schSync) Switch switchSync;
    @BindView(R.id.switch_lock) SwitchCompat switchLock;
    @BindView(R.id.btn_lock_change) LinearLayout btnLockChange;
    @BindView(R.id.txt_change_lock_password) TextView txtLockChange;
    @BindView(R.id.txt_lock_head) TextView txtLockHead;
    @BindView(R.id.txt_warning) TextView txtWarning;

    private static final int RESULT_SIGN_IN = 11;

    private static final String TAG = "SetupActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        mContext = this;

        sharedPreference = new MySharedPreference(mContext);

        setToolbar();
        setTypeFace();
        setVersionName();
    }

    /**
     * 폰트 설정
     */
    void setTypeFace() {
        Typeface typeFace1 = Typeface.createFromAsset(mContext.getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        txtLoginTitle.setTypeface(typeFace1);
        txtLoginSub.setTypeface(typeFace1);
        txtSyncTitle.setTypeface(typeFace1);
        txtVersionTitle.setTypeface(typeFace1);
        txtVersionSub.setTypeface(typeFace1);
        txtAboutTitle.setTypeface(typeFace1);
        txtAboutSub.setTypeface(typeFace1);
        txtLockChange.setTypeface(typeFace1);
        txtLockHead.setTypeface(typeFace1);
        txtWarning.setTypeface(typeFace1);
    }

    /**
     * 동기화 및 로그인 여부 확인, 표시.
     */
    private void initSync() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
            txtLoginTitle.setText(R.string.logout);
            txtLoginSub.setText(user.getEmail());
            isLogined = true;
            switchSync.setChecked(sharedPreference.getIsSync());
        } else {
            // No user is signed in
            txtLoginTitle.setText(R.string.login);
            txtLoginSub.setText("");
            isLogined = false;
            switchSync.setChecked(false);
        }
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.setup);
        imgLogo.setVisibility(View.GONE);
    }

    /**
     * 버전 표시
     */
    void setVersionName() {
        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            txtVersionSub.setText(i.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * OptionMenu 아이템이 선택될 때 호출 된다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        initSync();
        initPinLayout();
    }

    /**
     * 화면 잠금 레이아웃 초기화
     */
    void initPinLayout() {
        if (AppPreference.loadScreenPinNumber(mContext).equals("")) {   //비밀번호가 설정되어 있지 않은 경우
            switchLock.setChecked(false);
            btnLockChange.setVisibility(View.GONE);
        } else {    //비밀번호가 설정된 경우
            switchLock.setChecked(true);
            btnLockChange.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.schSync)
    void syncSwitchClick() {
        if (user == null) {
            Toast.makeText(mContext, R.string.check_sync, Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(mContext, SignUpActivity.class));
        } else {
            MySharedPreference sharedPreference = new MySharedPreference(mContext);
            sharedPreference.changeSync(!sharedPreference.getIsSync());

            if (sharedPreference.getIsSync()) {
                //동기화 시작
                startSyncProcess();
            }
        }
    }

    /**
     * 잠금 설정 버튼 클릭 이벤트
     */
    @OnClick({R.id.btn_lock_setting, R.id.switch_lock})
    void btnLockSettingClick() {
        if (AppPreference.loadScreenPinNumber(mContext).equals("")) {   //암호 설정
            Intent lockIntent = new Intent(mContext, PinLockActivity.class);
            startActivity(lockIntent);

        } else {    //암호 해제
            Toast.makeText(mContext, R.string.unlocked, Toast.LENGTH_SHORT).show();
            AppPreference.saveScreenPinNumber(mContext, "");
            initPinLayout();
        }
    }

    /**
     * 암호 변경
     */
    @OnClick(R.id.btn_lock_change)
    void btnLockChangeClick() {
        Intent lockIntent = new Intent(mContext, PinLockActivity.class);
        startActivity(lockIntent);
    }


    @OnClick(R.id.layout_login)
    void layoutLoginClick() {
        if (isLogined && user != null) {
            FirebaseAuth.getInstance().signOut();
            user = FirebaseAuth.getInstance().getCurrentUser();
            isLogined = false;
            Toast.makeText(mContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

            sharedPreference.changeSync(false); //동기화 값 저장.

            initSync();

            LogHelper.e(TAG, "로그아웃 함, user : " + user);
        } else {
            Toast.makeText(mContext, "hihihi", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(mContext, SignInActivity.class), RESULT_SIGN_IN);
        }
    }

    /**
     * 서버로 Answer 업로드
     */
    void startSyncProcess() {
        //업로드 끝난 후 다운로드 받기
        getAnswerDB();
    }

    /**
     * 서버에서 Answer 받아오는 메소드
     */
    private void getAnswerDB() {
//        LogHelper.e(TAG, "start get answer db from server");
//        final ArrayList<Answer> answerList = new ArrayList<>();
//
//        new Thread() {      //Thread안에 넣어 돌려야 받아올 수 있음.
//            @Override
//            public void run() {
//                super.run();
//                LogHelper.e(TAG, "getAnswerDB 시작");
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference().child("Answer").child(user.getUid());
//
//                myRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        LogHelper.e(TAG, "user.getUid() : " + user.getUid());
//                        LogHelper.e(TAG, "MY answer key : " + dataSnapshot.getKey());
//                        Map<String, Answer> answerMap = (Map<String, Answer>) dataSnapshot.getValue();
//
//                        if (answerMap == null || answerMap.size() == 0) {
//                            LogHelper.e(TAG, "ANSWER IS NULL");
//                            return;
//                        } else {
//                            LogHelper.e(TAG, "ANSWERMAP SIZE : " + answerMap.size());
//                        }
//
//                        LogHelper.e(TAG, "aId : " + answerMap.get("aId") + " ,answer : " + answerMap.get("answer") + ", created_at : " + answerMap.get("created_at") + ", qId : " + answerMap.get("qId"));
//
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                        Answer answer = dataSnapshot.child(user.getUid()).getValue(Answer.class);
//                        LogHelper.e("qid : ", answer.getqId() + "  aid : " + answer.getaId() + ", answer : " + answer.getAnswer() + " createdat : " + answer.getCreated_at());
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//
//                });
//            }
//        }.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SIGN_IN:
                if (resultCode == RESULT_OK)
                    getAnswerDB();
                break;
        }
    }
}

