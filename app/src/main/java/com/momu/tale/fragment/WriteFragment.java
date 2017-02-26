package com.momu.tale.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.momu.tale.MySharedPreference;
import com.momu.tale.R;
import com.momu.tale.activity.MainActivity;
import com.momu.tale.activity.SplashActivity;
import com.momu.tale.config.CConfig;
import com.momu.tale.database.Answer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 질문에 답을 할 수 있는 페이지
 * 이미 답을 했다면 수정 또는 삭제 가능
 * Created by songmho on 2016-10-01.
 */

public class WriteFragment extends Fragment {
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/ MM/ dd");
    String sql;
    SQLiteDatabase db;

    Context mContext;

    boolean isLogined, isSync;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @BindView(R.id.txtQuestion) TextView txtQuestion;
    @BindView(R.id.editAnswer) EditText editAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_write, container, false);
        ButterKnife.bind(this, view);

        db = SplashActivity.sqliteHelper.getWritableDatabase();

        Typeface typeFace1 = Typeface.createFromAsset(getActivity().getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        Typeface typeFace2 = Typeface.createFromAsset(getActivity().getAssets(), CConfig.FONT_YANOLJA_YACHE_REGULAR);

        editAnswer.setTypeface(typeFace1);
        txtQuestion.setTypeface(typeFace2);
        txtQuestion.setText(getArguments().getString("question"));

        ((MainActivity) getActivity()).toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBeforeExist();
            }
        });

        if (searchTodayAsw(format.format(now)))           //답이 있을 경우(수정을 하고 싶을 경우)
            editAnswer.setText(getArguments().getString("answer"));

        setHasOptionsMenu(true);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finishFragment();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    /**
     * 오늘의 답변 있는지 확인하는 메소드
     *
     * @param today 오늘 날짜
     * @return boolean true : 있을 경우, false : 없을 경우
     */
    private boolean searchTodayAsw(String today) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from answer where created_at='" + today + "';", null);
            while (cursor.moveToNext())
                if (cursor.getString(4) != null)
                    return true;       //있으면 true
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!searchTodayAsw(format.format(now)))    //추가
            inflater.inflate(R.menu.menu_write, menu);
        else        //수정, 제거
            inflater.inflate(R.menu.menu_write_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = -1;
        Cursor cursor = null;

        switch (item.getItemId()) {
            case R.id.action_check:     //추가
                //        if (editAnswer.getText().toString().trim().equals("")) {

                //local db에 추가하는 코드
                sql = "insert into answer (question_id, user_id, a, created_at) " +
                        "values (" + getArguments().getInt("questionId") + ", 0, '" + editAnswer.getText().toString() + "', '" + format.format(now).toString() + "');";
                db.execSQL(sql);


                //현재 입력된db 가져오는 코드

                try {
                    cursor = db.rawQuery("select id from answer where question_id=" + getArguments().getInt("questionId") + " and created_at='" + format.format(now).toString() + "';", null);
                    while (cursor.moveToNext())
                        id = cursor.getInt(0);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) cursor.close();
                }

                //로그인 돼있을 경우 바로 firebase db에 저장하게 코드 짜둠
                if (isLogined && isSync) {
                    Answer answer = new Answer(id, getArguments().getInt("questionId"), editAnswer.getText().toString(), format.format(now).toString());

                    String key = myRef.push().getKey();
                    Map<String, Object> postValues = answer.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("Answer/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + id, postValues);

                    database.getReference().updateChildren(childUpdates);
                }

                finishFragment();
                Toast.makeText(mContext, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                //      }
                break;

            case R.id.action_edit:      //수정
                sql = "update answer set a = '" + editAnswer.getText().toString() + "' where id=" + getArguments().getInt("answerId") + ";";
                db.execSQL(sql);

                if (isLogined && isSync) {
                    Answer answer = new Answer(getArguments().getInt("answerId"), getArguments().getInt("questionId"), editAnswer.getText().toString(), format.format(now).toString());

                    Map<String, Object> postValues = answer.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("Answer/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + getArguments().getInt("answerId"), postValues);

                    database.getReference().updateChildren(childUpdates);
                }

                Toast.makeText(mContext, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                getActivity().setResult(Activity.RESULT_OK);
                finishFragment();
                break;

            case R.id.action_remove:    //삭제
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("잠깐만");
                builder.setMessage("글을 삭제하시겠어요?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sql = "delete from answer where id=" + getArguments().getInt("answerId") + ";";
                        db.execSQL(sql);

                        if (isLogined && isSync) {
                            myRef = myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(getArguments().getInt("answerId")));
                            myRef.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                }
                            });
                        }


                        Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().setResult(Activity.RESULT_OK);
                        finishFragment();
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

    /**
     * 종료 전에 한번 더 확인한다.
     */
    public void checkBeforeExist() {
        if (editAnswer.getText().length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("잠깐만");
            builder.setMessage("글쓰기를 취소하시겠어요?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishFragment();
                }
            }).setNegativeButton("아니요.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        } else { //키보드 내리고 MainFragment로 변경
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editAnswer.getWindowToken(), 0);
            ((MainActivity) getActivity()).changeFragment(MainActivity.MAIN_FRAGMENT_MAIN, null);
        }
    }

    /**
     * 이전 페이지 상태에 따라 현재 프래그먼트 종료 시 취해줄 액션 정해줌.
     */
    public void finishFragment() {
        if (getArguments().getBoolean("isFromMain")) { //키보드 내리고 MainFragment로 변경
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editAnswer.getWindowToken(), 0);
            ((MainActivity) getActivity()).changeFragment(MainActivity.MAIN_FRAGMENT_MAIN, null);
        } else {
            (getActivity()).finish();
        }
    }
}
