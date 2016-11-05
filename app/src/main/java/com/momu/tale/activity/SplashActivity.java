package com.momu.tale.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.tale.R;

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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },SPLASH_TIME);
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
