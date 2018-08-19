package io.appetizer.hci_fridge.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.appetizer.hci_fridge.Model.Dailyinfo;
import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.adapter.DailyAdapter;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends Fragment {


    private List<Dailyinfo> foodList = new ArrayList<Dailyinfo>();
    private Context context;
    private okhttpManager manager = new okhttpManager();
    public DiscoveryFragment() {
        // Required empty public constructor
    }


    private void getItems(String userId, String token, String fridgeId){
        String url = Urlpath.getDailyItemsUrl+"?userId="+userId;
        String result = manager.sendStringByPost(url, "");
        foodList.clear();
        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("success");
            if(success != "true"){
                Toast.makeText(context, "getItems failed", Toast.LENGTH_SHORT).show();
            }
            else {
                String list = json.getString("result");
                JSONArray array = new JSONArray(list);
                for(int i=0;i<array.length();i++){
                    JSONObject tmp = array.getJSONObject(i);
                    Dailyinfo food = new Dailyinfo(tmp.getString("itemName"),Integer.parseInt(tmp.getString("amount")),Integer.parseInt(tmp.getString("itemId")),tmp.getString("time"),Integer.parseInt(tmp.getString("fridgeId")),Integer.parseInt(tmp.getString("userId")));
                    foodList.add(food);
                }

            }
        }catch (org.json.JSONException e){
            e.printStackTrace();
        }

    }

    private void initFood(){
        context = this.getContext();
        final String userId = sharedPreferenceUtil.get(context,"hci_fridge","userid");
        final String token = sharedPreferenceUtil.get(context,"hci_fridge","token");
        final String fridgeId = "1";
        /*
        * Url相关的要跑在线程里面，不然跑不出来
        */

        new Thread(new Runnable(){
            @Override
            public void run() {
                getItems(userId,token,fridgeId);
            }}).start();
    }

    /* @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.frag_fridge, container, false);
         initFood();
         DailyAdapter adapter = new DailyAdapter(fridgeFragment.this.getActivity(),R.layout.fridge_item,foodList);
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
        view= inflater.inflate(R.layout.fragment_discovery, container, false);
        initFood();
        mContentRv = (RecyclerView) view.findViewById(R.id.rv_content);
        mContentRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mContentRv.setHasFixedSize(true);
        mContentRv.setAdapter(new DailyAdapter(foodList));
        return view;
    }
}
