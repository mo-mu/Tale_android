package com.momu.wtfs.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.momu.wtfs.R;
import com.momu.wtfs.activity.MainActivity;
import com.momu.wtfs.activity.PreviewActivity;
import com.momu.wtfs.activity.SetUpActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MainFragment<br>
 *     질문과 답글 볼 수 있는 페이지.
 *     글화면과 설정으로 이동 가능
 * Created by songmho on 2016-10-01.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    TextView txtRefresh, txtQuestion,txtAnswer;
    LinearLayout board;
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    int answer_id, question_id;

    SQLiteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_main, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).toolBar.setLogo(null);

        txtQuestion = (TextView)v.findViewById(R.id.txtQuestion);
        txtRefresh = (TextView)v.findViewById(R.id.txtRefresh);
        txtAnswer = (TextView)v.findViewById(R.id.txtAnswer);
        board = (LinearLayout)v.findViewById(R.id.board);

        db = ((MainActivity)getActivity()).sqliteHelper.getReadableDatabase();

        txtRefresh.setOnClickListener(this);
        board.setOnClickListener(this);

        if(searchTodayAsw(format.format(now).toString())){  //내용이 있을 경우 refresh 안보이고, answer보여야
            txtRefresh.setVisibility(View.GONE);
            txtAnswer.setVisibility(View.VISIBLE);
        }
        else
            question_id=1;      //임의의 번호가 들어가야 하는 부분. 우선적으로 1 넣음

        questionSelect(question_id);      //테스트 위해 임의의 번호 넣음
        answerSelect(question_id,format.format(now).toString());

        txtRefresh.setText(Html.fromHtml("<u>" + "다른 질문 보여줘!" + "</u>"));   //Underbar 넣기 위해 html 태그 사용

        setHasOptionsMenu(true);
        return v;
    }

    /**
     * searchTodayAsw<br>
     *     오늘의 답변 있는지 확인하는 메소드
     *     question_id를 query문에서 받아 변경해준다.
     * @param today 오늘 날짜
     * @return boolean true : 있을 경우, false : 없을 경우
     */
    private boolean searchTodayAsw(String today) {
        Cursor cursor =db.rawQuery("select * from answer where created_at='"+today+"';",null);
        while(cursor.moveToNext())
            if(cursor.getString(4)!=null){
                question_id=cursor.getInt(1);   //question_id query받음
                return true;}       //있으면 true
        return false;
    }

    /**
     * answerSelect<br>
     *     오늘의 답 찾는 메소드
     * @param question_id
     * @param date
     */
    private void answerSelect(int question_id, String date) {
        Cursor cursor =db.rawQuery("select id, a from answer where question_id="+question_id+" and created_at='"+date+"';",null);
        while (cursor.moveToNext())
            if(cursor.getString(1)!=null) {          //답글이 있을 때
                answer_id=cursor.getInt(0);
                txtAnswer.setText("" + cursor.getString(1));
            }
    }

    /**
     * questionSelect<br>
     * question id를 랜덤으로 입력받아 question을 textView에 출력.
     * @param id question_id
     */
    private void questionSelect(int id) {
        SQLiteDatabase db = ((MainActivity)getActivity()).sqliteHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("select * from question where id="+id+";",null);
        while(cursor.moveToNext())
              txtQuestion.setText(cursor.getString(1));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtRefresh){
            question_id=2;
            questionSelect(question_id);      //테스트 위해 임의의 번호 넣음
        }
        else if(v.getId()==R.id.board){
            Fragment writeFragment=new WriteFragment();
            Bundle bundle=new Bundle();
            bundle.putInt("question_id",question_id);
            bundle.putInt("answer_id",answer_id);
            bundle.putString("question",txtQuestion.getText().toString());
            bundle.putString("answer",txtAnswer.getText().toString());
            writeFragment.setArguments(bundle);

            ((MainActivity)getActivity()).changeFragment(writeFragment);
         }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_preview){
            startActivity(new Intent(getActivity(),PreviewActivity.class));
        }

        else if(item.getItemId()==R.id.action_setup){
            startActivity(new Intent(getActivity(),SetUpActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
