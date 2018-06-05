package io.appetizer.hci_fridge.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.appetizer.hci_fridge.R;


/**
 * Created by Administrator on 2018/5/7.
 */

public class InputMessageCodeActivity extends AppCompatActivity {
    private TextView next_step_btn,message_code_edittext;
    private boolean flag;
    private String phoneNum;
    private ImageView returnbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_message_code);
        a();
    }

    protected void a(){
        flag = false;
        phoneNum = (String)getIntent().getStringExtra("phoneNum");
        sendCode("86",phoneNum);
        message_code_edittext = (TextView)findViewById(R.id.message_code_edittext) ;
        next_step_btn = (TextView)findViewById(R.id.next_step_btn);
        returnbtn = (ImageView)findViewById(R.id.return_btn);
        next_step_btn.setEnabled(true);
        next_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = message_code_edittext.getText().toString();
                submitCode("86",phoneNum,message);
                if(flag){
                    Intent intent = new Intent(getApplicationContext(), SetPasswordActivity.class);
                    intent.putExtra("phoneNum",phoneNum);
                    startActivityForResult(intent, 0);
                }
                else{

                }

            }
        });
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //Toast.makeText(getApplicationContext(), "send message to your phone,", Toast.LENGTH_SHORT).show();
                } else{
                    //Toast.makeText(getApplicationContext(), "send message to your phone, failed , please back and retry", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 触发操作
        System.out.println("  AAAA ");
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //Toast.makeText(getApplicationContext(), "check message correct", Toast.LENGTH_SHORT).show();
                    flag = true;
                } else{
                    //Toast.makeText(getApplicationContext(), "check message incorrect", Toast.LENGTH_SHORT).show();
                    flag = false;
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };
}
