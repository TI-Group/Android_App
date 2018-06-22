package io.appetizer.hci_fridge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.View.RoundedRectProgressBar;


/**
 * Created by user on 2018/5/31.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener mOnItemClickListener;


    private List<Foodinfo> data;

    public FoodAdapter(Context context, List<Foodinfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.fridge_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Foodinfo food = data.get(position);
        holder.itemName.setText(food.getName());
        holder.itemNum.setText(food.getNum()+"");
        int percent = 50;
        holder.itemTime.setProgress(percent);
        holder.itemImage.setImageResource(R.drawable.ic_tab1_s);
        //if (position % 2 == 0) {
        //    holder.itemImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        //} else {
        //    holder.itemImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        //}
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
        Foodinfo value = data.remove(position);
        notifyItemRemoved(position);
        return value.toString();
    }

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

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;
        private TextView itemName;
        private TextView itemNum;
        private RoundedRectProgressBar itemTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            this.itemName = (TextView) itemView.findViewById(R.id.itemName);
            this.itemNum = (TextView) itemView.findViewById(R.id.itemNum);
            this.itemTime = (RoundedRectProgressBar) itemView.findViewById(R.id.itemTime);
        }
    }

}