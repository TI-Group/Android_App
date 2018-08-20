package io.appetizer.hci_fridge.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.zxing.Result;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONObject;

import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;
import io.appetizer.hci_fridge.util.sharedPreferenceUtil;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by user on 2018/8/19.
 */

public class ScanActivity extends AppCompatActivity{
    private ZXingScannerView mScannerView;
    public static final String BARSCAN_OK = "BARSCAN_OK";
    private Context context;
    private okhttpManager manager = new okhttpManager();
    private final static int RETURN_GRT_ITEM_SUCCEED= 0;
    private final static int QUIT= 1;

    private Handler Handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RETURN_GRT_ITEM_SUCCEED:
                    finish();
                    break;
                case QUIT:
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private ZXingScannerView.ResultHandler mResultHandler = new ZXingScannerView.ResultHandler() {
        @Override
        public void handleResult(final Result result) {
            //Toast.makeText(getApplicationContext(), "内容:" + result.getText() + ",格式=" + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
            if(result!=null){
                Intent data = new Intent();
                data.putExtra(BARSCAN_OK, result.getText());
                setResult(175, data);
                final String fridgeId = sharedPreferenceUtil.get(context,"hci_fridge","current_fridge");
                AlertDialog.Builder checkDialog =
                        new AlertDialog.Builder(context);
                checkDialog.setIcon(R.drawable.hci_fridge);
                checkDialog.setTitle("将食物放入冰箱？");
                checkDialog.setMessage("你要点击哪一个按钮呢?");
                checkDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String barcode = result.getText();
                                final int num = 1;
                                new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        int a = addItemWithBarcode(fridgeId,barcode,num);
                                        if(a==0){
                                            Message message = new Message();
                                            message.what = RETURN_GRT_ITEM_SUCCEED;
                                            Handler.sendMessage(message);
                                        }
                                    }}).start();
                            }
                        })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Message message = new Message();
                                        message.what = QUIT;
                                        Handler.sendMessage(message);
                                    }
                                })
                        .show();
            }
            else{
                mScannerView.resumeCameraPreview(mResultHandler); //重新开始扫码。
            }
        }
    };

    private int addItemWithBarcode(String fridgeId, String barcode, int amount){
        String url = Urlpath.additemWithBarcodeUrl+"?fridgeId="+fridgeId+"&barcode="+barcode+"&amount="+amount;
        String result = manager.sendStringByPost(url, "");
        Log.e("addItemWithBarcode",result);
        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("success");
            Log.e("addItemWithBarcode",success);
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
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(mResultHandler);
        context = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }



}
