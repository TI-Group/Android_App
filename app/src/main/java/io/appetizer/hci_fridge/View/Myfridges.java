package io.appetizer.hci_fridge.View;

import android.content.Context;
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
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.adapter.NickAdapter;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;

/**
 * Created by user on 2018/6/24.
 */

public class Myfridges extends AppCompatActivity {
    private SwipeRefreshLayout mRefreshSrl;
    private RecyclerView mContentRv;
    private NickAdapter adapter;
    private View view;
    private android.support.v7.widget.Toolbar toolbar;
    public Context context;
    private okhttpManager manager = new okhttpManager();
    private List<String> fridgeList = new ArrayList<String>();
    private final static int RETURN_GET_FRIDGE_SUCCEED= 0;
    private final static int RETURN_GET_FRIDGE_FAILED= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfridge);

        final String userId = "2";
        final String token = "a";

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RETURN_GET_FRIDGE_SUCCEED:
                        mContentRv = (RecyclerView) findViewById(R.id.rv_content);
                        mContentRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mContentRv.setHasFixedSize(true);
                        adapter = new NickAdapter(getApplicationContext(),fridgeList);
                        adapter.setOnItemClickListener(new NickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
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
                    fridgeList.add(tmp.getString("fridgeId"));
                }
                return 0;
            }
        }catch (org.json.JSONException e){
            e.printStackTrace();
            return -1;
        }
    }
}
