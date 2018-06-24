package io.appetizer.hci_fridge.adapter;

/**
 * Created by user on 2018/6/24.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


import io.appetizer.hci_fridge.Model.Fridgeinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.View.RoundedRectProgressBar;
import io.appetizer.hci_fridge.util.itemImageUtil;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;


/**
 * Created by user on 2018/5/31.
 */

public class NickAdapter extends RecyclerView.Adapter<NickAdapter.MyViewHolder> {

    private Context context;
    private NickAdapter.OnItemClickListener mOnItemClickListener;


    private List<Fridgeinfo> data;

    public NickAdapter(Context context, List<Fridgeinfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public NickAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NickAdapter.MyViewHolder holder = new NickAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_nickname, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final NickAdapter.MyViewHolder holder, int position) {
        Fridgeinfo fridge = data.get(position);
        holder.fridgeName.setText(fridge.getNickName());

        if(mOnItemClickListener != null) {
            /**
             * 这里加了判断，itemViewHolder.itemView.hasOnClickListeners()
             * 目的是减少对象的创建，如果已经为view设置了click监听事件,就不用重复设置了
             * 不然每次调用onBindViewHolder方法，都会创建两个监听事件对象，增加了内存的开销
             */
            if(!holder.itemView.hasOnClickListeners()) {
                Log.e("ListAdapter", "setOnClickListener");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getPosition();
                        mOnItemClickListener.onItemClick(v, pos);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getPosition();
                        mOnItemClickListener.onItemLongClick(v, pos);
                        return true;
                    }
                });
            }
        }
    }



    @Override
    public int getItemCount() {
        return this.data.size();
    }

    /**
     * 移除指定位置元素
     * @param position
     * @return
     */
    public String remove(int position) {
        if(position > data.size()-1) {
            return null;
        }
        Fridgeinfo value = data.remove(position);
        notifyItemRemoved(position);
        return value.toString();
    }

    public void setOnItemClickListener(NickAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 处理item的点击事件和长按事件
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView fridgeName;

        public MyViewHolder(View itemView) {
            super(itemView);
            fridgeName = (TextView) itemView.findViewById(R.id.fridgeName);
        }
    }


}