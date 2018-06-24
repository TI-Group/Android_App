package io.appetizer.hci_fridge.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.appetizer.hci_fridge.MainActivity;
import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.Model.Fridgeinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.adapter.FoodAdapter;
import io.appetizer.hci_fridge.adapter.NickAdapter;
import io.appetizer.hci_fridge.util.OnRecyclerItemClickListener;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;

/**
 * Created by user on 2018/6/24.
 */

public class Myfridges extends AppCompatActivity {
    private RecyclerView mContentRv;
    private NickAdapter adapter;
    private View view;
    public Context context;
    private okhttpManager manager = new okhttpManager();
    private List<Fridgeinfo> fridgeList = new ArrayList<Fridgeinfo>();
    private ImageView returnbtn;
    private final static int RETURN_GET_FRIDGE_SUCCEED= 0;
    private final static int RETURN_GET_FRIDGE_FAILED= 1;

    public void init(){
        Fridgeinfo fridge1 = new Fridgeinfo("1","aaaa");
        fridgeList.add(fridge1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfridge);
        context = this;
        returnbtn = (ImageView)findViewById(R.id.return_btn);
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String userId = sharedPreferenceUtil.get(context,"hci_fridge","userid");
        final String token = sharedPreferenceUtil.get(context,"hci_fridge","token");

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RETURN_GET_FRIDGE_SUCCEED:
                        mContentRv = (RecyclerView) findViewById(R.id.rv_content);
                        mContentRv.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new NickAdapter(context,fridgeList);
                        mContentRv.addOnItemTouchListener(new OnRecyclerItemClickListener(mContentRv) {
                            @Override
                            public void onItemClick(RecyclerView.ViewHolder vh) {
                                Fridgeinfo fridge = ((NickAdapter.MyViewHolder)vh).getData();
                                sharedPreferenceUtil.set(context,"hci_fridge","current_fridge",fridge.getFridegId());
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                                final Fridgeinfo fridge = ((NickAdapter.MyViewHolder)vh).getData();
                                final EditText editText = new EditText(context);
                                AlertDialog.Builder inputDialog =
                                        new AlertDialog.Builder(context);
                                inputDialog.setTitle("请输入修改后的昵称").setView(editText);
                                inputDialog.setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sharedPreferenceUtil.set(context,"bind_fridge",fridge.getFridegId(),editText.getText().toString());
                                                fridge.setNickName(editText.getText().toString());
                                                adapter.notifyDataSetChanged();
                                            }
                                        }).show();
                            }
                        });
                        /*adapter.setOnItemClickListener(new NickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {

                            }

                            @Override
                            public void onItemLongClick(View view, final int position) {
                                final EditText editText = new EditText(context);
                                AlertDialog.Builder inputDialog =
                                        new AlertDialog.Builder(context);
                                inputDialog.setTitle("请输入修改后的昵称").setView(editText);
                                inputDialog.setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sharedPreferenceUtil.set(context,"bind_fridge",fridgeList.get(position).getFridegId(),editText.getText().toString());
                                                fridgeList.get(position).setNickName(editText.getText().toString());
                                                adapter.notifyDataSetChanged();
                                            }
                                        }).show();

                            }
                        });*/
                        mContentRv.setAdapter(adapter);
                        break;
                    case RETURN_GET_FRIDGE_FAILED:
                        Toast.makeText(context, "getFridge failed", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run() {
                int result = getFridgeList(userId,token);
                if(result==0){
                    Message message = new Message();
                    message.what = RETURN_GET_FRIDGE_SUCCEED;
                    handler.sendMessage(message);
                }
                else {
                    Message message = new Message();
                    message.what = RETURN_GET_FRIDGE_FAILED;
                    handler.sendMessage(message);
                }

            }}).start();


    }
    private int getFridgeList(String userId,String token){
        String url = Urlpath.getRelationToFridgeUrl+"?userId="+userId+"&token="+token;
        String result = manager.sendStringByPost(url, "");
        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("success");
            if(success != "true"){
                Toast.makeText(context, "getfridgeList failed", Toast.LENGTH_SHORT).show();
                return -1;
            }
            else {
                String list = json.getString("result");
                JSONArray array = new JSONArray(list);
                for(int i=0;i<array.length();i++){
                    JSONObject tmp = array.getJSONObject(i);
                    Fridgeinfo fridge = new Fridgeinfo(tmp.getString("fridgeId"), sharedPreferenceUtil.get(getApplicationContext(),"bind_fridge",tmp.getString("fridgeId")));
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+fridge.getNickName());
                    fridgeList.add(fridge);
                }
                return 0;
            }
        }catch (org.json.JSONException e){
            e.printStackTrace();
            return -1;
        }
    }
}
