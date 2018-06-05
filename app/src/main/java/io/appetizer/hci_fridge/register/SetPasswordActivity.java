package io.appetizer.hci_fridge.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.appetizer.hci_fridge.LoginActivity;
import io.appetizer.hci_fridge.Model.Userinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;


/**
 * Created by Administrator on 2018/5/7.
 */

public class SetPasswordActivity extends AppCompatActivity {
    private TextView next_step_btn,password_edittext;
    private ImageView returnbtn;
    private okhttpManager manager = new okhttpManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_set_password);
        a();
    }

    protected void a(){
        final String phoneNum = (String)getIntent().getStringExtra("phoneNum");
        //final String phoneNum = "13511577582";
        next_step_btn = (TextView)findViewById(R.id.next_step_btn);
        password_edittext = (TextView)findViewById(R.id.password_edittext);
        returnbtn = (ImageView)findViewById(R.id.return_btn);
        next_step_btn.setEnabled(true);
        next_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String passWord = password_edittext.getText().toString();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        String result = sendHttpPost(Urlpath.loginUrl,phoneNum,passWord);
                    }}).start();
                /*if(result==0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, 0);
                }
                else{

                }*/
            }
        });
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public String sendHttpPost(String getUrl,String phoneNum,String password) {
        Gson gson = new Gson();
        Map<String,String> muser = new HashMap<>();
        muser.put("username","HHH");
        muser.put("tel","13511577582");
        muser.put("password","aaa");
        muser.put("role","1");
        String jsonstrs = gson.toJson(muser);
        getUrl = getUrl+"?username=aaa&password=bbb&tel=13511577582&role=1";
        jsonstrs = "";
        String result = manager.sendStringByPost(getUrl, jsonstrs);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+result);
        return result;
    }

    public void onLoginSuccess() {

    }

    public void onLoginFailed() {

    }
}
