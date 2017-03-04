package com.momu.tale.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.momu.tale.MySharedPreference;
import com.momu.tale.R;
import com.momu.tale.config.CConfig;
import com.momu.tale.database.Answer;
import com.momu.tale.utility.LogHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by knulps on 2017. 1. 14..
 */

public class ModifyActivity extends AppCompatActivity {
    Context mContext;
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/ MM/ dd");
    String sql;
    SQLiteDatabase db;
    boolean isLogined, isSync;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @BindView(R.id.editAnswer) EditText editAnswer;
    @BindView(R.id.txtQuestion) TextView txtQuestion;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private static final String TAG = "ModifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        mContext = this;
        ButterKnife.bind(this);

        setToolbar();

        db = SplashActivity.sqliteHelper.getWritableDatabase();

        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        Typeface typeFace2 = Typeface.createFromAsset(getAssets(), CConfig.FONT_YANOLJA_YACHE_REGULAR);

        editAnswer.setTypeface(typeFace1);
        txtQuestion.setTypeface(typeFace2);
        txtQuestion.setText(getIntent().getStringExtra("question"));
        editAnswer.requestFocus();
        editAnswer.setText(getIntent().getStringExtra("answer"));

        MySharedPreference myShpr = new MySharedPreference(mContext);
        isSync = myShpr.getIsSync();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Answer");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            isLogined = true;
        } else {
            // No user is signed in
            isLogined = false;
        }
    }

    /**
     * 툴바를 세팅하는 메소드.
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    /**
     * 종료 전에 한번 더 확인한다.
     */
    public void checkBeforeExist() {
        if (editAnswer.getText().length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("잠깐만");
            builder.setMessage("수정을 취소하시겠어요?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setNegativeButton("아니요.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        } else { //키보드 내리고 MainFragment로 변경
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editAnswer.getWindowToken(), 0);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                checkBeforeExist();
                return false;

            case R.id.action_edit:      //수정
                LogHelper.e(TAG, "수정 id  " + getIntent().getIntExtra("answerId", -1));
                sql = "update answer set a = '" + editAnswer.getText().toString() + "' where id=" + getIntent().getIntExtra("answerId", -1) + ";";
                db.execSQL(sql);

                if (isLogined && isSync) {
                    Answer answer = new Answer(getIntent().getIntExtra("answerId", -1), getIntent().getIntExtra("questionId", -1), editAnswer.getText().toString(), format.format(now).toString());

                    Map<String, Object> postValues = answer.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("Answer/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + getIntent().getIntExtra("answerId", -1), postValues);

                    database.getReference().updateChildren(childUpdates);
                }

                Toast.makeText(mContext, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.action_remove:    //삭제
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("잠깐만");
                builder.setMessage("글을 삭제하시겠어요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sql = "delete from answer where id=" + getIntent().getIntExtra("answerId", -1) + ";";
                        db.execSQL(sql);

                        if (isLogined && isSync) {
                            myRef = myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(getIntent().getIntExtra("answerId", -1)));
                            myRef.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                }
                            });
                        }

                        Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        checkBeforeExist();
    }
}
