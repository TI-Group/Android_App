package io.appetizer.hci_fridge.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.appetizer.hci_fridge.Model.Barcodeinfo;
import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.View.WebviewActivity;
import io.appetizer.hci_fridge.adapter.BarcodeAdapter;
import io.appetizer.hci_fridge.adapter.DailyAdapter;
import io.appetizer.hci_fridge.adapter.FoodAdapter;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class fridgeFragment extends Fragment {
    private RecyclerView fruit;
    private RecyclerView barcodeView;
    private FoodAdapter adapter;
    private BarcodeAdapter barcodeAdapter;
    private View view;
    private Context context;
    private List<Foodinfo> foodList = new ArrayList<Foodinfo>();
    private List<Barcodeinfo> barcodeList = new ArrayList<Barcodeinfo>();
    private okhttpManager manager = new okhttpManager();
    private LayoutInflater g_inflater;
    private ViewGroup g_container;
    private Bundle g_savedInstanceState;



    // Handler
    private final static int RETURN_CHANGE_ITEM_SUCCEED= 0;
    private final static int RETURN_CHANGE_ITEM_FAILED= 1;
    private final static int RETURN_DELETE_ITEM_SUCCEED= 2;
    private final static int RETURN_DELETE_ITEM_FAILED= 3;
    private final static int RETURN_ADD_ITEM_SUCCEED= 4;
    private final static int RETURN_ADD_ITEM_FAILED= 5;
    private final static int RETURN_GET_ITEM_SUCCEED= 6;
    private final static int RETURN_GET_ITEM_FAILED= 7;

    private static final int REQUEST_CODE_SCAN = 1;


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
                    break;
                case RETURN_ADD_ITEM_SUCCEED:
                    onCreateView(g_inflater, g_container, g_savedInstanceState);
                    break;
                case RETURN_ADD_ITEM_FAILED:
                    Toast.makeText(context, "addItem failed", Toast.LENGTH_SHORT).show();
                    break;
                case RETURN_GET_ITEM_SUCCEED:
                    adapter.setList(foodList);
                    adapter.notifyDataSetChanged();
                    barcodeAdapter.setList(barcodeList);
                    barcodeAdapter.notifyDataSetChanged();
                    System.out.println("aaacccccccccccccccccccccccccccccc "+barcodeList.size()+foodList.size());
                    break;
                case RETURN_GET_ITEM_FAILED:
                    Toast.makeText(context, "getItemsItem failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        return view;
    }

    private int getItems(String userId, String token, String fridgeId){
        String url = Urlpath.getItemsUrl+"?userId="+userId+"&token="+token+"&fridgeId="+fridgeId;
        String result = manager.sendStringByPost(url, "");
        foodList.clear();
        barcodeList.clear();
        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("success");
            if(success != "true"){
                Toast.makeText(context, "getItems failed", Toast.LENGTH_SHORT).show();
                return -1;
            }
            else {
                String list = json.getString("result");
                JSONArray array = new JSONArray(list);
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(int i=0;i<array.length();i++){
                    JSONObject tmp = array.getJSONObject(i);
                    if(tmp.getString("barcode").equals("none")){
                        String rowdate = tmp.getString("putInTime");
                        rowdate = rowdate.replace("T"," ");
                        java.util.Date date = format.parse(rowdate);
                        Foodinfo food = new Foodinfo(tmp.getString("itemName"),Integer.parseInt(tmp.getString("amount")),
                                Integer.parseInt(tmp.getString("itemId")),tmp.getString("remainTime"), date, Integer.parseInt(tmp.getString("shelflife")));
                        foodList.add(food);
                    }
                    else{
                        String rowdate = tmp.getString("putInTime");
                        rowdate = rowdate.replace("T"," ");
                        java.util.Date date = format.parse(rowdate);
                        String barcode = tmp.getString("barcode");
                        String name = "a";
                        String imageUrl = "https://s.boohee.cn/house/upload_food/2018/2/10/mid_photo_url_66C79A4A8721.jpg";
                        try {
                            String barcodeurl = "https://food.boohee.com/fb/v1/foods/barcode?barcode=" + barcode + "&token=TDyqwbBFnckSpNrpmSegsBiqgL2quWmh&user_key=1573542a-8ec9-4096-978c-a8e82f0206f5&app_version=6.3.1&app_device=Android&os_version=6.0.1&phone_model=Nexus+5&channel=tencent";
                            String barresult = manager.sendStringByGet(barcodeurl);
                            JSONObject barjson = new JSONObject(barresult);
                            String info = barjson.getString("foods");
                            JSONArray bararray = new JSONArray(info);
                            for (int j = 0; j < bararray.length(); j++) {
                                JSONObject bartmp = bararray.getJSONObject(j);
                                name = bartmp.getString("name");
                                imageUrl = bartmp.getString("thumb_image_url");
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                        Barcodeinfo barcodeinfo = new Barcodeinfo(barcode,date, Integer.parseInt(tmp.getString("amount")),name, imageUrl);
                        barcodeList.add(barcodeinfo);
                    }
                }
                System.out.println("aaaaaaaaaaaaaaaaaaaaaa "+barcodeList.size()+foodList.size());
                return 0;
            }
        }catch (org.json.JSONException e){
            e.printStackTrace();
            return -1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
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
    private int deleteItem(String fridgeId, String itemName){
        String url = Urlpath.deleteItemUrl+"?fridgeId="+fridgeId+"&itemName="+itemName;
        String result = manager.sendStringByPost(url, "");
        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("success");
            if(success != "true"){
                return -1;
            }
            else return 0;
        }catch (org.json.JSONException e){
            e.printStackTrace();
            return -1;
        }

    }

    private int addItem(String fridgeId, String itemName, int amount){
        String url = Urlpath.addItemUrl+"?fridgeId="+fridgeId+"&itemName="+itemName+"&amount="+amount;
        String result = manager.sendStringByPost(url, "");
        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("success");
            Log.e("addItem",success);
            if(success != "true"){
                return -1;
            }
            else return 0;
        }catch (org.json.JSONException e){
            e.printStackTrace();
            return -1;
        }
    }

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }else{
            return;
        }
    }

    private void scan_code(){

        requestPermission();
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.colorPrimary);//设置扫描框四个角的颜色 默认为淡蓝色
        //config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+data.getStringExtra(Constant.CODED_CONTENT));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        Toolbar toolbar = getActivity().findViewById(R.id.fridgeToolBar);
        toolbar.setTitleTextColor(getActivity().getResources().getColor(R.color.white));
        context = this.getContext();
        final String userId = sharedPreferenceUtil.get(context,"hci_fridge","userid");
        final String token = sharedPreferenceUtil.get(context,"hci_fridge","token");
        final String fridgeId = sharedPreferenceUtil.get(context,"hci_fridge","current_fridge");
        toolbar.setTitle("食物");
        //导入fragment的menu文件
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.frag_fridge);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            public boolean onMenuItemClick(final MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_action:
                        final EditText itemName = new EditText(context);
                        final EditText itemNum = new EditText(context);
                        AlertDialog.Builder inputDialog =
                                new AlertDialog.Builder(context);
                        inputDialog.setIcon(R.drawable.hci_fridge);
                        inputDialog.setTitle("请输入被添加的食物").setView(itemName);
                        inputDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String name = itemName.getText().toString();
                                        final int num = 5;
                                        new Thread(new Runnable(){
                                            @Override
                                            public void run() {
                                                int result = addItem(fridgeId,name,num);
                                                if(result==0){
                                                    Message message = new Message();
                                                    message.what = RETURN_ADD_ITEM_SUCCEED;
                                                    requestHandler.sendMessage(message);
                                                }
                                                else {
                                                    Message message = new Message();
                                                    message.what = RETURN_ADD_ITEM_FAILED;
                                                    requestHandler.sendMessage(message);
                                                }

                                            }}).start();
                                    }
                                }).show();

                        break;
                    case R.id.scan_action:
                        Intent intent = new Intent(getActivity(), WebviewActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        /*
        * Url相关的要跑在线程里面，不然跑不出来
        */

        new Thread(new Runnable(){
            @Override
            public void run() {
                int result = getItems(userId,token,fridgeId);
                System.out.println("aaabbbbbbbbbbbbbbbbbbbbbbbbb "+barcodeList.size()+foodList.size());
                Message message = new Message();
                message.what = RETURN_GET_ITEM_SUCCEED;
                requestHandler.sendMessage(message);
            }}).start();



        fruit = (RecyclerView) view.findViewById(R.id.fruit);
        barcodeView = (RecyclerView) view.findViewById(R.id.barcode);
        //vegetable = (RecyclerView) view.findViewById(R.id.vegetable);


        adapter = new FoodAdapter(this.getContext(), foodList);
        barcodeAdapter = new BarcodeAdapter(this.getContext(), barcodeList);

        fruit.setLayoutManager(new GridLayoutManager(getContext(), 4));
        barcodeView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final EditText editText = new EditText(context);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(context);
                inputDialog.setIcon(R.drawable.hci_fridge);
                inputDialog.setTitle("请输入修改后的数量").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final int newnum = Integer.parseInt(editText.getText().toString());
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
                                            requestHandler.sendMessage(message);
                                        }

                                    }}).start();
                            }
                        }).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定删除？")
                        .setContentText("删除后不可恢复！")
                        .setCancelText("取消!")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance, keep widget user state, reset them if you need
                                sDialog.setTitleText("已取消")
                                        .setContentText("物品删除已取消")
                                        .setConfirmText("好的")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                final Handler deleteHandler = new Handler() {
                                    public void handleMessage(Message msg) {
                                        switch (msg.what) {
                                            case RETURN_DELETE_ITEM_SUCCEED:
                                                sDialog.setTitleText("删除")
                                                        .setContentText("物品已删除")
                                                        .setConfirmText("好的")
                                                        .showCancelButton(false)
                                                        .setCancelClickListener(null)
                                                        .setConfirmClickListener(null)
                                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                adapter.remove(position);
                                                break;
                                            case RETURN_DELETE_ITEM_FAILED:
                                                Toast.makeText(context, "deleteItem failed", Toast.LENGTH_SHORT).show();
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
                                        int result = deleteItem(fridgeId,foodList.get(position).getName());;
                                        if(result==0){
                                            Message message = new Message();
                                            message.what = RETURN_DELETE_ITEM_SUCCEED;
                                            message.arg1 = position;
                                            deleteHandler.sendMessage(message);
                                        }
                                        else {
                                            Message message = new Message();
                                            message.what = RETURN_DELETE_ITEM_FAILED;
                                            deleteHandler.sendMessage(message);
                                        }

                                    }}).start();
                            }
                        })
                        .show();

            }
        });
        fruit.setAdapter(adapter);
        barcodeView.setAdapter(barcodeAdapter);


    }
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }




}