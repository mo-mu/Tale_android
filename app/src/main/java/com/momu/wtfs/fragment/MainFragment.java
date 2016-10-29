package com.momu.wtfs.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.momu.wtfs.R;
import com.momu.wtfs.activity.MainActivity;
import com.momu.wtfs.activity.PreviewActivity;
import com.momu.wtfs.activity.SetUpActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 질문과 답글 볼 수 있는 페이지.
 * 글화면과 설정으로 이동 가능
 * Created by songmho on 2016-10-01.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    TextView txtRefresh, txtQuestion, txtAnswer;
    ImageView imgStar;
    LinearLayout board;
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/ MM/ dd");
    int answerId, questionId = -1;

    SQLiteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_main, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ImageView logo = (ImageView) ((((MainActivity)getActivity()).toolBar)).findViewById(R.id.imgLogo);
        logo.setVisibility(View.GONE);

        txtQuestion = (TextView) v.findViewById(R.id.txtQuestion);
        txtRefresh = (TextView) v.findViewById(R.id.txtRefresh);
        txtAnswer = (TextView) v.findViewById(R.id.txtAnswer);
        board = (LinearLayout) v.findViewById(R.id.board);
        imgStar = (ImageView) v.findViewById(R.id.imgStar);

        db = MainActivity.sqliteHelper.getReadableDatabase();

        Typeface typeFace1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SeoulNamsanCL.ttf");
        Typeface typeFace2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/YanoljaYacheRegular.ttf");
        txtRefresh.setTypeface(typeFace1);
        txtAnswer.setTypeface(typeFace1);
        txtQuestion.setTypeface(typeFace2);

        txtRefresh.setOnClickListener(this);
        board.setOnClickListener(this);

        if (searchTodayAsw(format.format(now))) {  //내용이 있을 경우 refresh 안보이고, answer보여야
            txtRefresh.setVisibility(View.GONE);
            txtAnswer.setVisibility(View.VISIBLE);
            imgStar.setVisibility(View.VISIBLE);
            questionSelect(questionId);
            answerSelect(questionId, format.format(now));
        } else {
            questionSelect(getQstIdRand());
        }


        txtRefresh.setText(Html.fromHtml("<u>" + "다른 질문 보여줘!" + "</u>"));   //Underbar 넣기 위해 html 태그 사용

        setHasOptionsMenu(true);
        return v;
    }


    /**
     * 랜덤으로 Question id를 생성해주는 메소드
     * 현재 나와있는 질문만 중복시키지 않고 반환시킴
     *
     * @return int Question id를 랜덤으로 뿌려줌
     */
    private int getQstIdRand() {
        int returnQstId, totalQst = -1;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(id) from question;", null);    //총 Question 수 가져오기
            while (cursor.moveToNext())
                totalQst = cursor.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        Random random = new Random();
        do returnQstId = random.nextInt(totalQst);
        while (questionId == (returnQstId + 1));

        questionId = returnQstId + 1;
        return returnQstId + 1;
    }

    /**
     * 오늘의 답변 있는지 확인하는 메소드
     * question_id를 query문에서 받아 변경해준다.
     *
     * @param today 오늘 날짜
     * @return boolean true : 있을 경우, false : 없을 경우
     */
    private boolean searchTodayAsw(String today) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select question_id, a from answer where created_at='" + today + "';", null);
            while (cursor.moveToNext())
                if (cursor.getString(1) != null) {
                    questionId = cursor.getInt(0);   //questionId query받음
                    return true;
                }       //있으면 true

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    /**
     * 오늘의 답 찾는 메소드
     *
     * @param question_id
     * @param date
     */
    private void answerSelect(int question_id, String date) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select id, a from answer where question_id=" + question_id + " and created_at='" + date + "';", null);
            while (cursor.moveToNext())
                if (cursor.getString(1) != null) {          //답글이 있을 때
                    answerId = cursor.getInt(0);
                    txtAnswer.setText("" + cursor.getString(1));
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    /**
     * question id를 랜덤으로 입력받아 question을 textView에 출력.
     *
     * @param id questionId
     */
    private void questionSelect(int id) {
        SQLiteDatabase db = MainActivity.sqliteHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from question where id=" + id + ";", null);
            while (cursor.moveToNext())
                txtQuestion.setText(cursor.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtRefresh) {
            questionSelect(getQstIdRand());
        } else if (v.getId() == R.id.board) {
            Fragment writeFragment = new WriteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("questionId", questionId);
            bundle.putInt("answerId", answerId);
            bundle.putString("question", txtQuestion.getText().toString());
            bundle.putString("answer", txtAnswer.getText().toString());
            writeFragment.setArguments(bundle);

            ((MainActivity) getActivity()).changeFragment(writeFragment, "WriteFragment");
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
        if (item.getItemId() == R.id.action_preview) {
            startActivity(new Intent(getActivity(), PreviewActivity.class));
        } else if (item.getItemId() == R.id.action_setup) {
            startActivity(new Intent(getActivity(), SetUpActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
