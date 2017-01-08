package com.momu.tale.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.momu.tale.R;
import com.momu.tale.SqliteHelper;
import com.momu.tale.database.Questions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * SplashActivity<br>
 *     SPLASH_TIME(1500ms)만큼 존재한 후 사라짐
 * Created by songmho on 2016-09-26.
 */

public class SplashActivity extends AppCompatActivity {
    static int SPLASH_TIME = 1500;
    TextView txtSaying;
    static String URL_STRING =  "http://52.78.172.143/api/v1/questions";    //question있는 주소
    static int CONNECTION_TIMEOUT = 3000;
    static int DATARETRIVAL_TIMEOUT = 3000;

    private ArrayList<Questions> questionList = new ArrayList<>();

    public static SqliteHelper sqliteHelper;
    private final String DBNAME = "wtfs.db";
    private final int DBVERSION = 1;
    public SQLiteDatabase db;

    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

    java.net.URL url;

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtSaying = (TextView)findViewById(R.id.txtSaying);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/SeoulNamsanCL.ttf");
        txtSaying.setTypeface(typeFace);
        txtSaying.setText("네가 오후 네 시에 온다면\n 난 세 시부터 행복해지기 시작할거야");

        getQuestionDB();

//  todo Firebase에서 불러온 다음에 메인페이지를 띄우게 변경하였다.
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//                intent.putExtra("fragmentName","first");
//                startActivity(intent);
//                finish();
//            }
//        },SPLASH_TIME);
    }

    /**
     * DB 초기화하는 메소드
     */
    protected void initDatabase() {
        Log.e(TAG, "initDatabase 시작");
        sqliteHelper = new SqliteHelper(this, DBNAME, null, DBVERSION);
        db = sqliteHelper.getWritableDatabase();

        //임시로 질문 데이터 저장하는 코드 삽입함
        boolean isExist = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from question;", null);
            while (cursor.moveToNext())
                if (cursor.getString(1) != null) {
                    isExist = true;
                }

            Log.e(TAG, "questionList 사이즈 : " + questionList.size() + " , isExist : " + isExist);
            if (!isExist) {      //Question 데이터 삽입
                for(int i = 0; i < questionList.size(); i++) {
                    Log.e(TAG, i+"번째 data 삽입");
                    db.execSQL("insert into question (id, q, created_at) " +
                            "values ("+ questionList.get(i).getId() +",'"+ questionList.get(i).getQ()+"','" + questionList.get(i).getCreated_at() + "');");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if(db != null) db.close();
        }

        //메인 페이지 시작
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        intent.putExtra("fragmentName","first");
        startActivity(intent);
        finish();
    }

    /**
     * 서버에서 Question 받아오는 메소드
     */
    private void getQuestionDB() {
        new Thread() {      //Thread안에 넣어 돌려야 받아올 수 있음.
            @Override
            public void run() {
                super.run();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(TAG, "We're done loading the initial "+dataSnapshot.getChildrenCount()+" items");

                        initDatabase();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Questions question =  dataSnapshot.getValue(Questions.class);
                        Log.e("q : ",question.getId()+"  q : "+ question.getQ() +" createdat : "+question.getCreated_at());
                        questionList.add(question);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        Questions question =  dataSnapshot.getValue(Questions.class);
//                        Log.e("2q : ",question.getId()+"  q : "+ question.getQ() +" createdat : "+question.getCreated_at());

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Questions question =  dataSnapshot.getValue(Questions.class);
                        Log.e("3q : ",question.getId()+"  q : "+ question.getQ() +" createdat : "+question.getCreated_at());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }.start();
    }

    /**
     * InputStream받아서 String으로 바꿔주는 메소드
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
