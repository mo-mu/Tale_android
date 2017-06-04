package com.momu.tale.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.momu.tale.R;
import com.momu.tale.SqliteHelper;
import com.momu.tale.config.CConfig;
import com.momu.tale.database.Answer;
import com.momu.tale.database.Questions;
import com.momu.tale.preference.AppPreference;
import com.momu.tale.utility.LogHelper;
import com.momu.tale.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * SplashActivity<br>
 * SPLASH_TIME(1500ms)만큼 존재한 후 사라짐
 * Created by songmho on 2016-09-26.
 */

public class SplashActivity extends AppCompatActivity {
    static int SPLASH_TIME = 1500;

    private ArrayList<Questions> questionList = new ArrayList<>();
    private ArrayList<Answer> answerList = new ArrayList<>();

    SqliteHelper sqliteHelper = new SqliteHelper(this, CConfig.DBNAME, null, CConfig.DBVERSION);
    public SQLiteDatabase db;

    FirebaseUser user = null;

    Context mContext;

    @BindView(R.id.txtSaying) TextView txtSaying;

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext = this;

        Fabric.with(this, new Crashlytics());

        Typeface typeFace = Typeface.createFromAsset(getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        txtSaying.setTypeface(typeFace);
        txtSaying.setText("네가 오후 네 시에 온다면\n 난 세 시부터 행복해지기 시작할거야");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getQuestionDB();
            }
        }, 500);

        //user = FirebaseAuth.getInstance().getCurrentUser(); // TODO: 2017. 3. 25.

//        MySharedPreference sharedPreference = new MySharedPreference(mContext);
//        if (user != null && sharedPreference.getIsSync()) {
//            LogHelper.e(TAG, "로그인 & 동기화 상태, 답변 db 불러옴");
//            getAnswerDB();
//        } else {
//            LogHelper.e(TAG, "로그인 안됨 혹은 동기화 안됨");
//        }
    }

    /**
     * DB 초기화하는 메소드
     */
    protected void initDatabase() {
        LogHelper.e(TAG, "initDatabase 시작");

        //임시로 질문 데이터 저장하는 코드 삽입함
        boolean isExist = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from question;", null);
            while (cursor.moveToNext())
                if (cursor.getString(1) != null) {
                    isExist = true;
                }

            LogHelper.e(TAG, "questionList 사이즈 : " + questionList.size() + " , isExist : " + isExist);
            if (!isExist) {      //Question 데이터 삽입
                for (int i = 0; i < questionList.size(); i++) {
                    LogHelper.e(TAG, i + "번째 data 삽입");
                    db.execSQL("insert into question (id, q, created_at) " +
                            "values (" + questionList.get(i).getId() + ",'" + questionList.get(i).getQ() + "','" + questionList.get(i).getCreated_at() + "');");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
            if (db != null) db.close();
        }


        if (AppPreference.loadScreenPinNumber(mContext).equals("")) {
            //메인 페이지 시작
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("fragmentName", MainActivity.MAIN_FRAGMENT_MAIN);
            startActivity(intent);
        } else {
            LogHelper.e(TAG, "잠금화면 시작!");
            Intent lockIntent = new Intent(getApplicationContext(), PinLockActivity.class);
            lockIntent.putExtra("isLockMode", true);
            lockIntent.putExtra("isStartApplication", true);
            startActivity(lockIntent);
        }
        finish();
    }

    /**
     * 서버에서 Question 받아오는 메소드
     */
    private void getQuestionDB() {
        sqliteHelper = new SqliteHelper(this, CConfig.DBNAME, null, CConfig.DBVERSION);
        db = sqliteHelper.getWritableDatabase();
        if (!Utility.isNetworkConnected(mContext)) {
            int totalQst = 0;
            Cursor cursor = null;

            try {
                cursor = db.rawQuery("select count(id) from question;", null);    //총 Question 수 가져오기
                while (cursor.moveToNext()) totalQst = cursor.getInt(0);
            } catch (Exception e) {
                LogHelper.errorStackTrace(e);
            } finally {
                if (cursor != null && !cursor.isClosed()) cursor.close();
            }

            LogHelper.e(TAG, "total question : " + totalQst);
            if(totalQst == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("앗!");
                builder.setCancelable(false);
                builder.setMessage("네트워크 연결이 필요해요.").setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).show();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (AppPreference.loadScreenPinNumber(mContext).equals("")) {
                            //메인 페이지 시작
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("fragmentName", MainActivity.MAIN_FRAGMENT_MAIN);
                            startActivity(intent);
                        } else {
                            LogHelper.e(TAG, "잠금화면 시작!");
                            Intent lockIntent = new Intent(getApplicationContext(), PinLockActivity.class);
                            lockIntent.putExtra("isLockMode", true);
                            lockIntent.putExtra("isStartApplication", true);
                            startActivity(lockIntent);
                        }
                        finish();
                    }
                }, SPLASH_TIME);

            }
            return;
        }

        LogHelper.e(TAG, "네트워크 연결됨");
        new Thread() {      //Thread안에 넣어 돌려야 받아올 수 있음.
            @Override
            public void run() {
                super.run();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("question");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LogHelper.e(TAG, "We're done loading the initial " + dataSnapshot.getChildrenCount() + " items");

                        initDatabase();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Questions question = dataSnapshot.getValue(Questions.class);
//                        LogHelper.e("q : ", question.getId() + "  q : " + question.getQ() + " createdat : " + question.getCreated_at());
                        questionList.add(question);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        Questions question =  dataSnapshot.getValue(Questions.class);
//                        LogHelper.e("2q : ",question.getId()+"  q : "+ question.getQ() +" createdat : "+question.getCreated_at());

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Questions question = dataSnapshot.getValue(Questions.class);
                        LogHelper.e("3q : ", question.getId() + "  q : " + question.getQ() + " createdat : " + question.getCreated_at());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }.start();
    }

//    /**
//     * 서버에서 Answer 받아오는 메소드
//     */
//    private void getAnswerDB() {
//        new Thread() {      //Thread안에 넣어 돌려야 받아올 수 있음.
//            @Override
//            public void run() {
//                super.run();
//                LogHelper.e(TAG, "getAnswerDB 시작");
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference().child("Answer").child(user.getUid());
//
////                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        LogHelper.e(TAG, "Answer We're done loading the initial " + dataSnapshot.getChildrenCount() + " items");
////
//////                        initDatabase();
////                    }
////
////                    @Override
////                    public void onCancelled(DatabaseError databaseError) {
////
////                    }
////                });
//                myRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        LogHelper.e(TAG, "user.getUid() : " + user.getUid());
//                        LogHelper.e(TAG, "MY answer key : " + dataSnapshot.getKey());
//                        Map<String, Answer> answerMap = (HashMap<String, Answer>) dataSnapshot.getValue();
//
//                        if(answerMap == null || answerMap.size() == 0) {
//                            LogHelper.e(TAG, "ANSWER IS NULL");
//                            return;
//                        }
//
//                        LogHelper.e(TAG, "aId : " + answerMap.get("aId") + " ,answer : " + answerMap.get("answer") +", created_at : " + answerMap.get("created_at") + ", qId : " + answerMap.get("qId"));
//
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
////                        Questions question =  dataSnapshot.getValue(Questions.class);
////                        LogHelper.e("2q : ",question.getId()+"  q : "+ question.getQ() +" createdat : "+question.getCreated_at());
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
//    }

    /**
     * InputStream받아서 String으로 바꿔주는 메소드
     *
     * @param is 서버에서 가져온 inputstream
     * @return 받아온 것을 String으로 변환
     */
    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
