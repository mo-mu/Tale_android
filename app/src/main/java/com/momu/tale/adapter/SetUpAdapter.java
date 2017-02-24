package com.momu.tale.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.momu.tale.MySharedPreference;
import com.momu.tale.R;
import com.momu.tale.activity.SignInActivity;
import com.momu.tale.activity.SignUpActivity;
import com.momu.tale.config.CConfig;
import com.momu.tale.item.SetupItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by songm on 2016-12-30.
 */
public class SetUpAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<SetupItem> items;
    private boolean isLogined;
    MySharedPreference myShpr;

    public SetUpAdapter(Context context, ArrayList<SetupItem> items,boolean isLogined) {
        this.context = context;
        this.items = items;
        this.isLogined = isLogined;

        myShpr = new MySharedPreference(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setup, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SetupItem item = items.get(position);


        Typeface typeFace1 = Typeface.createFromAsset(context.getAssets(), CConfig.FONT_SEOUL_NAMSAN_CL);
        ((ViewHolder) holder).txtTitle.setTypeface(typeFace1);
        ((ViewHolder) holder).txtSub.setTypeface(typeFace1);

        ((ViewHolder) holder).txtTitle.setText(item.getTitle());

        if(position!=1) {
            ((ViewHolder) holder).txtSub.setText(item.getSub());
            ((ViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getTitle().equals("로그인")) {
                        context.startActivity(new Intent(context, SignInActivity.class));
                    }
                    else if(item.getTitle().equals("로그아웃")){
                        FirebaseAuth.getInstance().signOut();
                        myShpr.changeSync(false);
                        Toast.makeText(context,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            ((ViewHolder)holder).schSync.setVisibility(View.VISIBLE);
            ((ViewHolder)holder).schSync.setChecked(item.getIsSync());
            ((ViewHolder)holder).schSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(!isLogined) {
                            context.startActivity(new Intent(context, SignUpActivity.class));
                        }
                        else{
                            myShpr.changeSync(true);
                            ((ViewHolder)holder).schSync.setChecked(true);
                            Toast.makeText(context,"current statue : "+myShpr.getIsSync(),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        myShpr.changeSync(false);
                        ((ViewHolder)holder).schSync.setChecked(false);
                        Toast.makeText(context,"current statue : "+myShpr.getIsSync(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.container) LinearLayout container;
        @BindView(R.id.txtTitle) TextView txtTitle;
        @BindView(R.id.txtSub) TextView txtSub;
        @BindView(R.id.schSync) Switch schSync;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}