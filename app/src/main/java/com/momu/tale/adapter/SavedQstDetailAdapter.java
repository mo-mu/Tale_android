package com.momu.tale.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.momu.tale.R;
import com.momu.tale.activity.MainActivity;
import com.momu.tale.activity.ModifyActivity;
import com.momu.tale.config.CConfig;
import com.momu.tale.item.SavedQstDetailItem;
import com.momu.tale.utility.LogHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 각 질문마다 저장된 글 목록
 * Created by songmho on 2016-10-15.
 */
public class SavedQstDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<SavedQstDetailItem> items;

    private static final String TAG = "SavedQstDetailAdapter";

    public SavedQstDetailAdapter(Context context, ArrayList<SavedQstDetailItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SavedQstDetailItem item = items.get(position);
        Typeface typeFace1 = Typeface.createFromAsset(context.getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        Typeface typeFace2 = Typeface.createFromAsset(context.getAssets(), CConfig.FONT_YANOLJA_YACHE_REGULAR);

        ((ViewHolder) holder).txtDate.setTypeface(typeFace1);
        ((ViewHolder) holder).txtAnswer.setTypeface(typeFace1);
        ((ViewHolder) holder).txtQuestion.setTypeface(typeFace2);
        if (position == 0) {
            ((ViewHolder) holder).txtQuestion.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).txtQuestion.setText(item.getQuestion());
        }
        ((ViewHolder) holder).txtDate.setText(item.getDate());
        ((ViewHolder) holder).txtAnswer.setText(item.getAnswer());
        ((ViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.e(TAG, "AnswerId : " + item.getAnswerId());
                Intent gotoSaveQst = new Intent(context, ModifyActivity.class);
                gotoSaveQst.putExtra("question", item.getQuestion());
                gotoSaveQst.putExtra("questionId", item.getQuestionId());
                gotoSaveQst.putExtra("answer", item.getAnswer());
                gotoSaveQst.putExtra("answerId", item.getAnswerId());
                gotoSaveQst.putExtra("fragmentName", MainActivity.MAIN_FRAGMENT_SAVE_QST);
                ((Activity)context).startActivityForResult(gotoSaveQst, CConfig.RESULT_MODIFY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.container) LinearLayout container;
        @BindView(R.id.txtQuestion) TextView txtQuestion;
        @BindView(R.id.txtDate) TextView txtDate;
        @BindView(R.id.txtAnswer) TextView txtAnswer;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
