package com.momu.tale.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.tale.R;
import com.momu.tale.activity.MainActivity;
import com.momu.tale.activity.SavedQstActivity;
import com.momu.tale.item.SavedQstItem;

import java.util.ArrayList;

/**
 * Created by songmho on 2016-10-15.
 */
public class SavedQstAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<SavedQstItem> items;

    public SavedQstAdapter(Context context, ArrayList<SavedQstItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SavedQstItem item = items.get(position);
        Typeface typeFace1 = Typeface.createFromAsset(context.getAssets(), "fonts/SeoulNamsanCL.ttf");
        Typeface typeFace2 = Typeface.createFromAsset(context.getAssets(), "fonts/YanoljaYacheRegular.ttf");

        ((ViewHolder)holder).txtDate.setTypeface(typeFace1);
        ((ViewHolder)holder).txtAnswer.setTypeface(typeFace1);
        ((ViewHolder)holder).txtQuestion.setTypeface(typeFace2);
        if(position==0) {
            ((ViewHolder) holder).txtQuestion.setVisibility(View.VISIBLE);
            ((ViewHolder)holder).txtQuestion.setText(item.getQuestion());
        }
        ((ViewHolder)holder).txtDate.setText(item.getDate());
        ((ViewHolder)holder).txtAnswer.setText(item.getAnswer());
        ((ViewHolder)holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSaveQst = new Intent(context.getApplicationContext(), MainActivity.class);
                gotoSaveQst.putExtra("question", item.getQuestion());
                gotoSaveQst.putExtra("questionId", item.getQuestionId());
                gotoSaveQst.putExtra("answer", item.getAnswer());
                gotoSaveQst.putExtra("answerId", item.getAnswerId());
                gotoSaveQst.putExtra("fragmentName","savedQst");
                gotoSaveQst.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(gotoSaveQst);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout container;
        TextView txtQuestion, txtDate, txtAnswer;
        public ViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout)itemView.findViewById(R.id.container);
            txtQuestion = (TextView)itemView.findViewById(R.id.txtQuestion);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
            txtAnswer = (TextView)itemView.findViewById(R.id.txtAnswer);
        }
    }
}
