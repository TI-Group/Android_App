package io.appetizer.hci_fridge.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.adapter.FoodAdapter;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class fridgeFragment extends Fragment {
    private RecyclerView fruit,vegetable;
    private FoodAdapter adapter;
    private View view;
    private Context context;
    private List<Foodinfo> foodList = new ArrayList<Foodinfo>();
    private okhttpManager manager = new okhttpManager();

    // Handler
    private final static int RETURN_CHANGE_ITEM_SUCCEED= 0;
    private final static int RETURN_CHANGE_ITEM_FAILED= 1;
    private Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RETURN_CHANGE_ITEM_SUCCEED:
                    int position = msg.arg1;
                    int newnum = msg.arg2;
                    foodList.get(position).setNum(newnum);
                    adapter.notifyDataSetChanged();
                    break;
                case RETURN_CHANGE_ITEM_FAILED:
                    Toast.makeText(context, "changeItem failed", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        context = this.getContext();
        final String userId = "2";
        final String token = "a";
        final String fridgeId = "1";
        /*
        * Url相关的要跑在线程里面，不然跑不出来
        */

        new Thread(new Runnable(){
            @Override
            public void run() {
                getItems(userId,token,fridgeId);
            }}).start();



        fruit = (RecyclerView) view.findViewById(R.id.fruit);
        vegetable = (RecyclerView) view.findViewById(R.id.vegetable);


        adapter = new FoodAdapter(this.getContext(), foodList);

        fruit.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                View promptsView = inflater.inflate(R.layout.dialog,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextResult);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        final int newnum = Integer.parseInt(userInput.getText().toString());
                                        final Foodinfo food = foodList.get(position);
                                        new Thread(new Runnable(){
                                            @Override
                                            public void run() {
                                                int result = changeItem(food.getItemID(),newnum,userId, token, fridgeId);
                                                if(result==0){
                                                    Message message = new Message();
                                                    message.what = RETURN_CHANGE_ITEM_SUCCEED;
                                                    message.arg1 = position;
                                                    message.arg2 = (int)newnum;
                                                    requestHandler.sendMessage(message);
                                                }
                                                else {
                                                    Message message = new Message();
                                                    message.what = RETURN_CHANGE_ITEM_FAILED;
                                                    message.arg1 = position;
                                                    message.arg2 = newnum;
                                                    requestHandler.sendMessage(message);
                                                }

                                            }}).start();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                adapter.remove(position); //remove the item

            }
        });
        fruit.setAdapter(adapter);
        vegetable.setLayoutManager(new GridLayoutManager(getContext(), 4));
        vegetable.setAdapter(adapter);
        return view;
    }
    private List<Integer> createData() {
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                data.add(R.mipmap.chat_normal);
            } else {
                data.add(R.mipmap.chat_press);
            }
        }
        return data;
    }

    private void getItems(String userId, String token, String fridgeId){
        String url = Urlpath.getItemsUrl+"?userId="+userId+"&token="+token+"&fridgeId="+fridgeId;
        String result = manager.sendStringByPost(url, "");
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
                    Foodinfo food = new Foodinfo(tmp.getString("itemName"),Integer.parseInt(tmp.getString("amount")),Integer.parseInt(tmp.getString("itemId")),tmp.getString("remainTime"));
                    foodList.add(food);
                }

            }
        }catch (org.json.JSONException e){
            e.printStackTrace();
        }

    }
    private int changeItem(int itemId, int amount,String userId, String token, String fridgeId){
        String url = Urlpath.changeItemUrl+"?userId="+userId+"&token="+token+"&fridgeId="+fridgeId+"&itemId="+itemId+"&amount="+amount;
        String result = manager.sendStringByPost(url, "");
        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("success");
            if(success != "true"){
                //Toast.makeText(context, "changeItem failed", Toast.LENGTH_SHORT).show();
                return -1;
            }
            else return 0;
        }catch (org.json.JSONException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.support.v7.widget.Toolbar toolbar = getActivity().findViewById(R.id.fridgeToolBar);
        toolbar.setTitleTextColor(getActivity().getResources().getColor(R.color.white));
        toolbar.setTitle("食物");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }



}