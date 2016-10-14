package com.momu.wtfs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.momu.wtfs.R;
import com.momu.wtfs.item.SavedQstItem;

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
        SavedQstItem item = items.get(position);
        if(position==0) {
            ((ViewHolder) holder).txtQuestion.setVisibility(View.VISIBLE);
            ((ViewHolder)holder).txtQuestion.setText(item.getQuestion());
        }
        ((ViewHolder)holder).txtDate.setText(item.getDate());
        ((ViewHolder)holder).txtAnswer.setText(item.getAnswer());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtQuestion, txtDate, txtAnswer;
        public ViewHolder(View itemView) {
            super(itemView);
            txtQuestion = (TextView)itemView.findViewById(R.id.txtQuestion);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
            txtAnswer = (TextView)itemView.findViewById(R.id.txtAnswer);
        }
    }
}
