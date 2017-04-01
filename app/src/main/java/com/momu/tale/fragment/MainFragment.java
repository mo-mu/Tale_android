package com.momu.tale.fragment;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.momu.tale.R;
import com.momu.tale.activity.MainActivity;
import com.momu.tale.activity.ModifyActivity;
import com.momu.tale.activity.SavedQstDetailActivity;
import com.momu.tale.activity.SavedQstListActivity;
import com.momu.tale.activity.SetUpActivity;
import com.momu.tale.activity.SplashActivity;
import com.momu.tale.config.CConfig;
import com.momu.tale.utility.LogHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 질문과 답글 볼 수 있는 페이지.
 * 글화면과 설정으로 이동 가능
 * Created by songmho on 2016-10-01.
 */

public class MainFragment extends Fragment {
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/ MM/ dd");
    int answerId = -1, questionId = -1;

    SQLiteDatabase db;
    Context mContext;
    private static final String TAG = "MainFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @BindView(R.id.board) LinearLayout board;
    @BindView(R.id.txtQuestion) TextView txtQuestion;
    @BindView(R.id.txtRefresh) TextView txtRefresh;
    @BindView(R.id.txtAnswer) TextView txtAnswer;
    @BindView(R.id.imgStar) ImageView imgStar;
    @BindView(R.id.btnWrite) Button btnWrite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        db = SplashActivity.sqliteHelper.getReadableDatabase();

        Typeface typeFace1 = Typeface.createFromAsset(getActivity().getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        Typeface typeFace2 = Typeface.createFromAsset(getActivity().getAssets(), CConfig.FONT_YANOLJA_YACHE_REGULAR);
        txtRefresh.setTypeface(typeFace1);
        txtAnswer.setTypeface(typeFace1);
        txtQuestion.setTypeface(typeFace2);

        initView();

        txtRefresh.setText(Html.fromHtml("<u>다른 질문 보여줘!</u>"));   //Underbar 넣기 위해 html 태그 사용

        setHasOptionsMenu(true);
        return v;
    }

    void initView() {
        if (searchTodayAsw(format.format(now))) {  //내용이 있을 경우 refresh 안보이고, answer보여야
            LogHelper.e(TAG, "오늘 작성한 답변이 있음");
            txtRefresh.setVisibility(View.GONE);
            txtAnswer.setVisibility(View.VISIBLE);
            imgStar.setVisibility(View.VISIBLE);
            questionSelect(questionId);
            answerSelect(questionId, format.format(now));
        } else {
            txtRefresh.setVisibility(View.VISIBLE);
            LogHelper.e(TAG, "오늘 작성한 답변이 없음");
            questionSelect(getQstIdRand());
        }
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
            if (cursor != null && !cursor.isClosed()) cursor.close();
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
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        answerId = -1;
        if (isAdded()) txtAnswer.setText("");
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
                    txtAnswer.setText(cursor.getString(1));

                    if (txtAnswer.getLineCount() >= 8) {
                        txtAnswer.post(new Runnable() {
                            @Override
                            public void run() {
                                if (txtAnswer.getLineCount() >= 8) {
                                    board.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //상세 보기 코드가 들어가야 하는 곳.
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
    }

    /**
     * question id를 랜덤으로 입력받아 question을 textView에 출력.
     *
     * @param id questionId
     */
    private void questionSelect(int id) {
        SQLiteDatabase db = SplashActivity.sqliteHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from question where id=" + id + ";", null);
            while (cursor.moveToNext())
                txtQuestion.setText(cursor.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
    }

    /**
     * 다른 질문 보여줘 버튼 클릭 이벤트
     */
    @OnClick(R.id.txtRefresh)
    void btnRefreshClick() {
        questionSelect(getQstIdRand());
    }

    /**
     * 글쓰기 버튼 클릭 이벤트
     */
    @OnClick(R.id.btnWrite)
    void btnWriteClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("questionId", questionId);
        bundle.putInt("answerId", answerId);
        bundle.putString("question", txtQuestion.getText().toString());
        bundle.putString("answer", txtAnswer.getText().toString());
        bundle.putBoolean("isFromMain", true);
        ((MainActivity) mContext).changeFragment(MainActivity.WRITE_FRAGMENT, bundle);
    }

    /**
     * 질문, 답변 보드(큰 레이아웃) 클릭 이벤트
     */
    @OnClick(R.id.board)
    void boardClick() {
        if (!txtAnswer.getText().toString().equals("")) { //작성된 질문이 있다면 질문 상세 페이지로 이동
            Intent gotoSaveQst = new Intent(mContext, SavedQstDetailActivity.class);
            gotoSaveQst.putExtra("question", txtQuestion.getText().toString());
            gotoSaveQst.putExtra("questionId", questionId);
            startActivityForResult(gotoSaveQst, CConfig.RESULT_DETAIL);
            ((Activity) mContext).overridePendingTransition(0, 0);
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
            startActivityForResult(new Intent(getActivity(), SavedQstListActivity.class), CConfig.RESULT_QST_LIST);
        } else if (item.getItemId() == R.id.action_setup) {
            startActivity(new Intent(getActivity(), SetUpActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.e(TAG, "onactivityresult진입" + requestCode + " , " + resultCode);
        if (requestCode == CConfig.RESULT_QST_LIST && resultCode == Activity.RESULT_OK) {
            initView();
        } else if (requestCode == CConfig.RESULT_DETAIL && resultCode == Activity.RESULT_OK) { //상세 보기 화면
            initView();
        }
    }
}
