package com.zzu.luanchuan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goyourfly.multi_picture.MultiPictureView;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.activity.BigImage;
import com.zzu.luanchuan.beans.EvaluateItem;
import com.zzu.luanchuan.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.ViewHolder> {
    private List<EvaluateItem> evaluate_item_list;
    private Activity mActivity;

    public EvaluateAdapter(List<EvaluateItem> evaluate_item_list,Activity mActivity) {
        this.evaluate_item_list = evaluate_item_list;
        this.mActivity = mActivity;
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

        holder.mMultiPictureView.setList(evi.getEvaluate_img_uris());
        holder.evaluate_content.setText(evi.getEvaluate_content());
        holder.mMultiPictureView.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(View view, int index, ArrayList<Uri> uris) {
                MyToast.showToast(view.getContext(), uris.get(index).toString() + "=======" + index);
                Intent intent = new Intent(mActivity, BigImage.class);
                intent.putExtra("uris", uris);//非必须
                intent.putExtra("position", index);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);//必须
                intent.putExtra("locationY", location[1]);//必须

                intent.putExtra("width", view.getWidth());//必须
                intent.putExtra("height", view.getHeight());//必须
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return evaluate_item_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t1;
        TextView t2;
        TextView evaluate_content;
        MultiPictureView mMultiPictureView;

        public ViewHolder(View itemView) {

            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.nick_name);
            t2 = (TextView) itemView.findViewById(R.id.time_stamp);
            evaluate_content = (TextView) itemView.findViewById(R.id.evaluate_content);
            mMultiPictureView = (MultiPictureView) itemView.findViewById(R.id.evaluate_imgs);

        }
    }
}
