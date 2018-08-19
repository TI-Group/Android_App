package io.appetizer.hci_fridge.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.Result;
import com.yzq.zxinglibrary.common.Constant;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by user on 2018/8/19.
 */

public class ScanActivity extends AppCompatActivity{
    private ZXingScannerView mScannerView;
    public static final String BARSCAN_OK = "BARSCAN_OK";


    private ZXingScannerView.ResultHandler mResultHandler = new ZXingScannerView.ResultHandler() {
        @Override
        public void handleResult(Result result) {
            //Toast.makeText(getApplicationContext(), "内容:" + result.getText() + ",格式=" + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "+result.getText());
            if(result!=null){
                Intent data = new Intent();
                data.putExtra(BARSCAN_OK, result.getText());
                setResult(175, data);
                finish();

            }

            mScannerView.resumeCameraPreview(mResultHandler); //重新开始扫码。
        }
    };

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(mResultHandler);
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
