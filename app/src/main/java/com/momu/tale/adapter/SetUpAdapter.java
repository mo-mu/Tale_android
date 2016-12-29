package com.momu.tale.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.tale.R;
import com.momu.tale.item.SetupItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by songm on 2016-12-30.
 */
public class SetUpAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<SetupItem> items;
    public SetUpAdapter(Context context, ArrayList<SetupItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setup,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SetupItem item = items.get(position);

        Typeface typeFace1 = Typeface.createFromAsset(context.getAssets(), "fonts/SeoulNamsanCL.ttf");
        ((ViewHolder) holder).txtTitle.setTypeface(typeFace1);
        ((ViewHolder) holder).txtSub.setTypeface(typeFace1);

        ((ViewHolder) holder).txtTitle.setText(item.getTitle());
        ((ViewHolder) holder).txtSub.setText(item.getSub());
        ((ViewHolder)holder).container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.container) LinearLayout container;
        @BindView(R.id.txtTitle) TextView txtTitle;
        @BindView(R.id.txtSub) TextView txtSub;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}