package io.appetizer.hci_fridge.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.youtu.Youtu;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.android.Intents;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.appetizer.hci_fridge.MainActivity;
import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.View.Myfridges;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    public static final String APP_ID = "10136384";
    public static final String SECRET_ID = "AKID06ACILoIE6tsofBb6WrAQcjSFxeIKRuL";
    public static final String SECRET_KEY = "Y4VuJuSxPl3XyKxdHsNqmmquwunO6lQS";
    public static final String USER_ID = "1823997989";

    private static final String TAG = "MeFragment";
    private okhttpManager manager = new okhttpManager();
    // scan 2d code
    private LinearLayout scan_code_button,personal_home_btn,upload_portrait_button;
    private CircleImageView portrait_view;
    private static final int REQUEST_CODE_SCAN = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final int RETURN_SETRELATION_ITEM_SUCCEED = 3;
    private static final int RETURN_SETRELATION_ITEM_FAILED = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_me, container, false);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = getActivity().findViewById(R.id.meToolBar);
        toolbar.setTitleTextColor(getActivity().getResources().getColor(R.color.white));
        toolbar.setTitle("我的");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        requestPermission();

        // portrait
        setPortrait_view();
        upload_portrait_button = getActivity().findViewById(R.id.upload_portrait_btn);
        scan_code_button = getActivity().findViewById(R.id.scan_code_btn);
        personal_home_btn = getActivity().findViewById(R.id.personal_home_btn);

        personal_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Myfridges.class);
                startActivity(intent);
            }
        });


        scan_code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File fileDir = getContext().getFilesDir();
                File myPortrait = new File(fileDir,"portrait");
                if (!myPortrait.exists()) {
                    Toast.makeText(getActivity(), "请先上传头像", Toast.LENGTH_SHORT).show();
                    return;
                }
                scan_code();
            }
        });

        // upload portrait
        upload_portrait_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Fragment> list = getFragmentManager().getFragments();
                MultiImageSelector.create(getContext())
                        .showCamera(false) // 是否显示相机. 默认为显示
                        .single() // 单选模式
                        .start(getFragmentManager().getFragments().get(0), REQUEST_IMAGE);
            }
        });
    }

    private void setPortrait_view(){
        portrait_view = getActivity().findViewById(R.id.portrait_view);
        File fileDir = getContext().getFilesDir();
        File myPortrait = new File(fileDir, "portrait");
        long n = myPortrait.length();
        if (!myPortrait.exists()){
            Log.d(TAG, "setPortrait_view: portrait file not exist");
            return;
        }
            
        Glide.with(getContext())
                .load(myPortrait)
                .asBitmap()
                .centerCrop()
                .skipMemoryCache(true)   // ignore cache
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // do not put in cache
                .placeholder(R.drawable.two_div_code)
                .into(portrait_view);
    }


    private void scan_code(){
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

    // build a connection between user's face and the fridge with id fridge_id
    private void upload_portrait(final String fridge_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File fileDir = getContext().getFilesDir();
                File myPortrait = new File(fileDir,"portrait");
                Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY, Youtu.API_YOUTU_END_POINT,USER_ID);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("hci_fridge",MODE_PRIVATE);
                String user_id = sharedPreferences.getString("userid", "");
                List<String> group = new ArrayList<>();
                group.add(fridge_id);
                try {
                    JSONObject object = faceYoutu.NewPerson(myPortrait.getPath(), user_id, group);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d(TAG, "run: upload success");
            }
        }).start();
    }

    private int setRelationToFridge(String fridgeId, String userId, String token){
        String url = Urlpath.setRelationToFridgeUrl+"?userId="+userId+"&fridgeId="+fridgeId+"&token="+token;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                final String content = data.getStringExtra(Constant.CODED_CONTENT);
                final EditText editText = new EditText(getContext());
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(getContext());
                inputDialog.setTitle("请输入冰箱的昵称").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferenceUtil.set(getContext(),"bind_fridge",content,editText.toString());
                            }
                        }).show();
                sharedPreferenceUtil.set(getContext(),"hci_fridge","current_fridge",content);
                final String userId = "2";
                final String token = sharedPreferenceUtil.get(getContext(),"hci_fridge","token");


                final Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case RETURN_SETRELATION_ITEM_SUCCEED:
                                upload_portrait(content);
                                break;
                            case RETURN_SETRELATION_ITEM_FAILED:
                                Toast.makeText(getContext(), "setrelation failed", Toast.LENGTH_SHORT).show();
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
                        int result = setRelationToFridge(content,userId,token);
                        if(result==0){
                            Message message = new Message();
                            message.what = RETURN_SETRELATION_ITEM_SUCCEED;
                            handler.sendMessage(message);
                        }
                        else {
                            Message message = new Message();
                            message.what = RETURN_SETRELATION_ITEM_FAILED;
                            handler.sendMessage(message);
                        }

                    }}).start();
                // build new connection

            }
        }

        // get portrait
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // get return file path list
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                File fileDir = getContext().getFilesDir();
                File myPortrait = new File(fileDir,"portrait");
                // delete old
                if (!myPortrait.exists()) {
                    myPortrait.delete();
                }
                // save new
                myPortrait = new File(fileDir,"portrait");
                File select_file = new File(path.get(0));
                // copy
                try {
                    myPortrait.createNewFile();
                    FileInputStream ins = new FileInputStream(select_file);
                    FileOutputStream out = new FileOutputStream(myPortrait);
                    byte[] b = new byte[1024];
                    int n = 0;
                    while ((n = ins.read(b)) != -1) {
                        out.write(b, 0, n);
                    }

                    ins.close();
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }


    }


}
