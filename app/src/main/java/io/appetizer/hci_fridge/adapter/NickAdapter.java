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

import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;


/**
 * Created by user on 2018/5/31.
 */

public class NickAdapter extends RecyclerView.Adapter<NickAdapter.ItemViewHolder> {

    private List<String> data;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private Context acontext;

    public NickAdapter(Context context, List<String> objects) {
        this.data = objects;
        mInflater = LayoutInflater.from(context);
        acontext = context;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {
        String fridgeId = data.get(i);
        itemViewHolder.fridgeName.setText("AAAAAAAAAA");
        if(mOnItemClickListener != null) {
            /**
             * 这里加了判断，itemViewHolder.itemView.hasOnClickListeners()
             * 目的是减少对象的创建，如果已经为view设置了click监听事件,就不用重复设置了
             * 不然每次调用onBindViewHolder方法，都会创建两个监听事件对象，增加了内存的开销
             */
            if(!itemViewHolder.itemView.hasOnClickListeners()) {
                Log.e("ListAdapter", "setOnClickListener");
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = itemViewHolder.getPosition();
                        mOnItemClickListener.onItemClick(v, pos);
                    }
                });
                itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = itemViewHolder.getPosition();
                        mOnItemClickListener.onItemLongClick(v, pos);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        /**
         * 使用RecyclerView，ViewHolder是可以复用的。这根使用ListView的VIewHolder复用是一样的
         * ViewHolder创建的个数好像是可见item的个数+3
         */
        Log.e("ListAdapter", "onCreateViewHolder");
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(
                R.layout.item_nickname, viewGroup, false));
        return holder;
    }


    /*public void add(int position, Foodinfo value) {
        if(position > data.size()) {
            position = data.size();
        }
        if(position < 0) {
            position = 0;
        }
        data.add(position, value);
        notifyItemInserted(position);
    }

    public String remove(int position) {
        if(position > data.size()-1) {
            return null;
        }
        Foodinfo value = data.remove(position);
        notifyItemRemoved(position);
        return value.toString();
    }*/


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 处理item的点击事件和长按事件
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView fridgeName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            fridgeName = (TextView) itemView.findViewById(R.id.fridgeName);
        }
    }

}