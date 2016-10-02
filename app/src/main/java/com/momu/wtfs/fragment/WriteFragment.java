package com.momu.wtfs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.momu.wtfs.R;
import com.momu.wtfs.activity.MainActivity;

/**
 * Created by songmho on 2016-10-01.
 */

public class WriteFragment extends Fragment {
    TextView txtQuestion;
    EditText editAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.fragment_write,container,false);
        txtQuestion = (TextView)v.findViewById(R.id.txtQuestion);
        editAnswer = (EditText)v.findViewById(R.id.editAnswer);
        txtQuestion.setText(getArguments().getString("question"));

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_write, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_check){
            Fragment recvFragment = new MainFragment();
            ((MainActivity)getActivity()).changeFragment(recvFragment);
        }
        return super.onOptionsItemSelected(item);
    }
}
