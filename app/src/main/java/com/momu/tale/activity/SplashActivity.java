package com.momu.tale.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.tale.R;
import com.momu.tale.SqliteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

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


    public static SqliteHelper sqliteHelper;
    private final String DBNAME = "wtfs.db";
    private final int DBVERSION = 1;
    public SQLiteDatabase db;

    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

    java.net.URL url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtSaying = (TextView)findViewById(R.id.txtSaying);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/SeoulNamsanCL.ttf");
        txtSaying.setTypeface(typeFace);
        txtSaying.setText("네가 오후 네 시에 온다면\n 난 세 시부터 행복해지기 시작할거야");

        getQuestionDB();
        initDatabase(); //DB 초기화


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.putExtra("fragmentName","first");
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME);
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

    /**
     * 서버에서 Question 받아오는 메소드
     */
    private void getQuestionDB() {
        new Thread() {      //Thread안에 넣어 돌려야 받아올 수 있음.
            @Override
            public void run() {
                super.run();

                try {
                    url = new URL(URL_STRING);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setReadTimeout(DATARETRIVAL_TIMEOUT);

                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    JSONObject object = new JSONObject(getStringFromInputStream(in));

                    JSONArray questionArray  = (JSONArray) object.get("questions");   //Object형에서 Array형으로 변경
                    for(int i=0; i<questionArray.length(); i++){
                        //배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
                        JSONObject obj = (JSONObject) questionArray.get(i);

                        //JSON name으로 추출
                        Log.e("id",""+obj.get("id"));
                        Log.e("question",""+obj.get("q"));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }

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
