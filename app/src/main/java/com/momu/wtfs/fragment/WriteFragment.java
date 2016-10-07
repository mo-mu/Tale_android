package com.momu.wtfs.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.wtfs.R;
import com.momu.wtfs.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * WriteFragment<br>
 *     질문에 답을 할 수 있는 페이지
 *     이미 답을 했다면 수정 또는 삭제 가능
 * Created by songmho on 2016-10-01.
 */

public class WriteFragment extends Fragment {
    TextView txtQuestion;
    EditText editAnswer;

    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/ MM/ dd");
    String sql;
    SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.fragment_write,container,false);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).toolBar.setLogo(R.drawable.fox_toolbar);

        txtQuestion = (TextView)v.findViewById(R.id.txtQuestion);
        editAnswer = (EditText)v.findViewById(R.id.editAnswer);
        txtQuestion.setText(getArguments().getString("question"));

        db = ((MainActivity)getActivity()).sqliteHelper.getWritableDatabase();

        if(searchTodayAsw(format.format(now).toString()))           //답이 있을 경우(수정을 하고 싶을 경우)
            editAnswer.setText(getArguments().getString("answer").toString());

        setHasOptionsMenu(true);
        return v;
    }

    /**
     * searchTodayAsw<br>
     *     오늘의 답변 있는지 확인하는 메소드
     * @param today 오늘 날짜
     * @return boolean true : 있을 경우, false : 없을 경우
     */
    private boolean searchTodayAsw(String today) {
        Cursor cursor =db.rawQuery("select * from answer where created_at='"+today+"';",null);
        while(cursor.moveToNext())
            if(cursor.getString(4)!=null)
                return true;       //있으면 true
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
        if(!searchTodayAsw(format.format(now).toString()))    //추가
            inflater.inflate(R.menu.menu_write, menu);
        else        //수정, 제거
            inflater.inflate(R.menu.menu_write_edit,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment recvFragment = new MainFragment();     //공통적으로 MainFragment로 전환시킴
        ((MainActivity)getActivity()).changeFragment(recvFragment);

        if(item.getItemId()==R.id.action_check){    //추가
            sql = "insert into answer (questionId, user_id, a, created_at) " +
                    "values (" + getArguments().getInt("questionId") + ", 0, '" + editAnswer.getText().toString() + "', '" + format.format(now).toString() + "');";
            db.execSQL(sql);
            Toast.makeText(getActivity().getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
        }

        else if(item.getItemId()==R.id.action_edit){        //수정
            sql = "update answer set a = '"+editAnswer.getText().toString()+"' where id="+getArguments().getInt("answerId")+";";
            db.execSQL(sql);
            Toast.makeText(getActivity().getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.action_remove){      //삭제
            sql = "delete from answer where id="+getArguments().getInt("answerId")+";";
            db.execSQL(sql);
            Toast.makeText(getActivity().getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
