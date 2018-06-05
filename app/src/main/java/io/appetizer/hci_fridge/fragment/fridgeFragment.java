package io.appetizer.hci_fridge.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.adapter.FoodAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class fridgeFragment extends Fragment {

    private List<Foodinfo> foodList = new ArrayList<Foodinfo>();
    public fridgeFragment() {
        // Required empty public constructor
    }


    private void initFood(){
        Foodinfo apple = new Foodinfo("apple",2.0, R.drawable.ic_tab1_s);
        Foodinfo banana = new Foodinfo("banana",2.0,R.drawable.ic_tab2_s);
        Foodinfo orange = new Foodinfo("orange",2.0,R.drawable.ic_tab3_s);
        Foodinfo pear = new Foodinfo("pear",2.0,R.drawable.ic_tab4_s);
        Foodinfo peach = new Foodinfo("peach",2.0,R.drawable.ic_tab_menu_selected);
        foodList.add(apple);
        foodList.add(banana);
        foodList.add(orange);
        foodList.add(peach);
        foodList.add(pear);
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        initFood();
        FoodAdapter adapter = new FoodAdapter(fridgeFragment.this.getActivity(),R.layout.fridge_item,foodList);
        flist = (ListView) view.findViewById(R.id.foodlist);
        flist.setAdapter(adapter);
        return view;
    }*/
    private SwipeRefreshLayout mRefreshSrl;
    private RecyclerView mContentRv;
    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        initFood();
        mContentRv = (RecyclerView) view.findViewById(R.id.rv_content);
        mContentRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mContentRv.setHasFixedSize(true);
        mContentRv.setAdapter(new FoodAdapter(foodList));
        return view;
    }



}
