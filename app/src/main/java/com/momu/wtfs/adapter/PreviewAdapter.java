package com.momu.wtfs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.momu.wtfs.R;
import com.momu.wtfs.item.PreviewItem;

import java.util.ArrayList;

/**
 * PreviewAdapter<br>
 *     지난 이야기에서 쓰는 recyclerView에 대한 adapter
 * Created by songmho on 2016-10-01.
 */
public class PreviewAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<PreviewItem> items;

    /**
     * constructor<br>
     * @param context context
     * @param items 지난이야기 ArrayLIst
     */
    public PreviewAdapter(Context context, ArrayList<PreviewItem> items) {
        this.context=context;
        this.items=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PreviewItem item = items.get(position);
        ((viewHolder)holder).txtDate.setText(item.getDate());
        ((viewHolder)holder).txtQuestion.setText(item.getQuestion());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * viewHolder<br>
     *     item holder class
     *     멤버변수 : TextView(txtDate, txtQuestion)
     */
    public class viewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtQuestion;

        /**
         * constructor<br>
         * @param itemView
         */
        public viewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
            txtQuestion = (TextView)itemView.findViewById(R.id.txtQuestion);
        }
    }
}
