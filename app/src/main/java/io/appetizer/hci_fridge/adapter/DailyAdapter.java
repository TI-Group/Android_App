package io.appetizer.hci_fridge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.appetizer.hci_fridge.Model.Dailyinfo;
import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;


/**
 * Created by user on 2018/5/31.
 */

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    private List<Dailyinfo> data;
    public DailyAdapter(List<Dailyinfo> objects) {
        this.data = objects;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dailyfood_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Dailyinfo food = data.get(position);
        viewHolder.foodName.setText(food.getName());
        viewHolder.foodNum.setText(food.getNum()+"");
        viewHolder.fridgeID.setText(food.getFridgeID());
        viewHolder.time.setText(food.getTime());
        viewHolder.foodImage.setImageResource(food.getItemID());
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return data.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView foodImage;
        public TextView foodName;
        public TextView foodNum;
        public TextView fridgeID;
        public TextView time;
        public ViewHolder(View view){
            super(view);
            foodImage = (ImageView) view.findViewById(R.id.icon);
            foodName = (TextView) view.findViewById(R.id.label);
            foodNum = (TextView) view.findViewById(R.id.num);
            fridgeID = (TextView) view.findViewById(R.id.fridgeId);
            time = (TextView) view.findViewById(R.id.time);
        }
    }

}