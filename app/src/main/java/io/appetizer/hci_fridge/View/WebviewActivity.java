package io.appetizer.hci_fridge.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONObject;

import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.util.okhttpManager;

/**
 * Created by user on 2018/8/19.
 */

public class WebviewActivity extends AppCompatActivity {
    private WebView webView;
    private okhttpManager manager = new okhttpManager();
    private final static int RETURN_SCAN_SUCCEED = 0;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_webview);
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, 175);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == 175 && resultCode == 175) {
            if (data != null) {
                final String code = data.getStringExtra("BARSCAN_OK");
                //System.out.println("BBBBBBBBBBBBBBBBBBBBBBB" + code);
                webView = (WebView) findViewById(R.id.webView);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setAllowFileAccessFromFileURLs(true);
                webView.setWebViewClient(new WebViewClient());
                final Handler Handler = new Handler() {
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case RETURN_SCAN_SUCCEED:
                                String info = (String) msg.obj;
                                webView.loadUrl("http://120.78.218.52:8080/fridge/html/food_detail.html?code=" + info);
                                break;
                            default:
                                break;
                        }
                        super.handleMessage(msg);
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url = "https://food.boohee.com/fb/v1/foods/barcode?barcode=" + code + "&token=TDyqwbBFnckSpNrpmSegsBiqgL2quWmh&user_key=1573542a-8ec9-4096-978c-a8e82f0206f5&app_version=6.3.1&app_device=Android&os_version=6.0.1&phone_model=Nexus+5&channel=tencent";
                            String result = manager.sendStringByGet(url);
                            String id = "";
                            JSONObject json = new JSONObject(result);
                            String info = json.getString("foods");
                            JSONArray array = new JSONArray(info);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject tmp = array.getJSONObject(i);
                                id = tmp.getString("code");
                            }
                            Message message = new Message();
                            message.what = RETURN_SCAN_SUCCEED;
                            message.obj = id;
                            Handler.sendMessage(message);
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        }
    }


}
