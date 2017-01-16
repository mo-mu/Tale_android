package com.momu.tale.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.momu.tale.R;
import com.momu.tale.activity.SavedQstDetailActivity;
import com.momu.tale.config.CConfig;
import com.momu.tale.item.PreviewItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 지난 이야기에서 쓰는 recyclerView에 대한 adapter
 * Created by songmho on 2016-10-01.
 */
public class SavedQstListAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<PreviewItem> items;

    /**
     * constructor<br>
     *
     * @param context context
     * @param items   지난이야기 ArrayLIst
     */
    public SavedQstListAdapter(Context context, ArrayList<PreviewItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PreviewItem item = items.get(position);
        Typeface typeFace1 = Typeface.createFromAsset(context.getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        Typeface typeFace2 = Typeface.createFromAsset(context.getAssets(), CConfig.FONT_YANOLJA_YACHE_REGULAR);

        ((ViewHolder) holder).txtDate.setTypeface(typeFace1);
        ((ViewHolder) holder).txtQuestion.setTypeface(typeFace2);
        ((ViewHolder) holder).txtDate.setText("" + item.getDate());
        ((ViewHolder) holder).txtQuestion.setText(item.getQuestion());
        ((ViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSaveQst = new Intent(context, SavedQstDetailActivity.class);
                gotoSaveQst.putExtra("question", item.getQuestion());
                gotoSaveQst.putExtra("questionId", item.getQuestionId());
                ((Activity)context).startActivityForResult(gotoSaveQst, CConfig.RESULT_DETAIL);
            }
        });

        for (int i = 0; i < item.getCount(); i++) {
            ((ViewHolder) holder).imgTail[i].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * ViewHolder<br>
     * item holder class
     * 멤버변수 : TextView(txtDate, txtQuestion)
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtDate) TextView txtDate;
        @BindView(R.id.txtQuestion) TextView txtQuestion;
        @BindView(R.id.container) LinearLayout container;

        ImageView[] imgTail;

        /**
         * constructor<br>
         *
         * @param itemView
         */
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            imgTail = new ImageView[6];
            for (int i = 0; i < 5; i++)
                imgTail[i] = (ImageView) itemView.findViewById(R.id.imgTail1 + i);
        }
    }
}
