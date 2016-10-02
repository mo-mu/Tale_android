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
 * Created by songmho on 2016-10-01.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    TextView txtRefresh, txtQuestion,txtAnswer;
    LinearLayout board;
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    int answer_id, question_id=9;

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

        txtRefresh.setOnClickListener(this);
        board.setOnClickListener(this);
        txtRefresh.setText(Html.fromHtml("<u>" + "다른 질문 보여줘!" + "</u>"));   //Underbar 넣기 위해 html 태그 사용

        questionSelect(question_id);      //테스트 위해 임의의 번호 넣음

        answerSelect(question_id,format.format(now).toString());
        setHasOptionsMenu(true);
        return v;
    }

    private void answerSelect(int question_id, String date) {
        SQLiteDatabase db = ((MainActivity)getActivity()).sqliteHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery("select id, a from answer where question_id="+question_id+" and created_at='"+date+"';",null);
        while (cursor.moveToNext()){
            if(cursor.getString(1)!=null) {          //답글이 있을 때
                txtAnswer.setVisibility(View.VISIBLE);
                txtRefresh.setVisibility(View.GONE);
                answer_id=cursor.getInt(0);
                txtAnswer.setText("" + cursor.getString(1));
            }
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
        while(cursor.moveToNext()){
              txtQuestion.setText(cursor.getString(1));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtRefresh){
            question_id=10;
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
