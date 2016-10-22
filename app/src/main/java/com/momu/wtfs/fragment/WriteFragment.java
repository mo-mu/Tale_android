package com.momu.wtfs.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.wtfs.R;
import com.momu.wtfs.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 질문에 답을 할 수 있는 페이지
 * 이미 답을 했다면 수정 또는 삭제 가능
 * Created by songmho on 2016-10-01.
 */

public class WriteFragment extends Fragment {
    TextView txtQuestion;
    EditText editAnswer;

    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/ MM/ dd");
    String sql;
    SQLiteDatabase db;

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_write, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView logo = (ImageView) ((((MainActivity)getActivity()).toolBar)).findViewById(R.id.imgLogo);
        logo.setVisibility(View.VISIBLE);

        txtQuestion = (TextView) v.findViewById(R.id.txtQuestion);
        editAnswer = (EditText) v.findViewById(R.id.editAnswer);

        db = MainActivity.sqliteHelper.getWritableDatabase();

        Typeface typeFace1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SeoulNamsanCL.ttf");
        Typeface typeFace2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/YanoljaYacheRegular.ttf");

        editAnswer.setTypeface(typeFace1);
        txtQuestion.setTypeface(typeFace2);
        txtQuestion.setText(getArguments().getString("question"));

        ((MainActivity) getActivity()).toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editAnswer.getText().length()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("잠깐만");
                    builder.setMessage("글쓰기를 취소하시겠어요?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            changeFragment();
                        }
                    }).setNegativeButton("아니요.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }else
                    changeFragment();
            }
        });

        if (searchTodayAsw(format.format(now)))           //답이 있을 경우(수정을 하고 싶을 경우)
            editAnswer.setText(getArguments().getString("answer"));

        setHasOptionsMenu(true);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    changeFragment();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    /**
     * 오늘의 답변 있는지 확인하는 메소드
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
            if (cursor != null) cursor.close();
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
        switch (item.getItemId()){
              case R.id.action_check:     //추가
                  sql = "insert into answer (question_id, user_id, a, created_at) " +
                        "values (" + getArguments().getInt("questionId") + ", 0, '" + editAnswer.getText().toString() + "', '" + format.format(now).toString() + "');";
                  db.execSQL(sql);
                  changeFragment();
                  Toast.makeText(mContext, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                  break;

            case R.id.action_edit:      //수정
                sql = "update answer set a = '" + editAnswer.getText().toString() + "' where id=" + getArguments().getInt("answerId") + ";";
                db.execSQL(sql);
                changeFragment();
                Toast.makeText(mContext, "수정되었습니다.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        changeFragment();
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
     * 현재 프레그먼트를 MainFragment로 변경하는 메소드
     */
    private void changeFragment() {
        Fragment recvFragment = new MainFragment();     //공통적으로 MainFragment로 전환시킴
        ((MainActivity) getActivity()).changeFragment(recvFragment);
        InputMethodManager imm= (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editAnswer.getWindowToken(), 0);
    }
}
