package com.zzu.luanchuan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.luanchuan.R;
import com.zzu.luanchuan.beans.EvaluateItem;

import java.util.List;

@SuppressWarnings("all")
public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.ViewHolder> {
    private List<EvaluateItem> evaluate_item_list;

    public EvaluateAdapter(List<EvaluateItem> evaluate_item_list) {
        this.evaluate_item_list = evaluate_item_list;
    }

    @Override
    public EvaluateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evaluate_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(EvaluateAdapter.ViewHolder holder, int position) {
        EvaluateItem evi = evaluate_item_list.get(position);
        holder.t1.setText("haha");
        holder.t2.setText("hehe");

    }

    @Override
    public int getItemCount() {
        return evaluate_item_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1;
        TextView t2;

        public ViewHolder(View itemView) {

            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.nick_name);
            t2 = (TextView) itemView.findViewById(R.id.time_stamp);
        }
    }
}
