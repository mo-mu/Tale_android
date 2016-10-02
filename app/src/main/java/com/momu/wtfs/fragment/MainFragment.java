package com.momu.wtfs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.text.Html;
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

/**
 * Created by songmho on 2016-10-01.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    TextView txtRefresh, txtQuestion;
    LinearLayout board;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_main, container, false);
        txtQuestion = (TextView)v.findViewById(R.id.txtQuestion);
        txtRefresh = (TextView)v.findViewById(R.id.txtRefresh);
        board = (LinearLayout)v.findViewById(R.id.board);

        txtRefresh.setOnClickListener(this);
        board.setOnClickListener(this);

        txtRefresh.setText(Html.fromHtml("<u>" + "다른 질문 보여줘!" + "</u>"));   //Underbar 넣기 위해 html 태그 사용

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtRefresh){
            txtQuestion.setText("너무 어렵다ㅠㅜ");
        }
        else if(v.getId()==R.id.board){
            Fragment writeFragment=new WriteFragment();
            Bundle bundle=new Bundle();
            bundle.putString("question",txtQuestion.getText().toString());
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
