package com.zzu.luanchuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zzu.luanchuan.beans.Evaluate_item;

import java.util.List;

@SuppressWarnings("all")
public class Evaluate_adapter extends RecyclerView.Adapter <Evaluate_adapter.ViewHolder>{
    private List<Evaluate_item> Evaluate_item_list;
    @Override
    public Evaluate_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(Evaluate_adapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
