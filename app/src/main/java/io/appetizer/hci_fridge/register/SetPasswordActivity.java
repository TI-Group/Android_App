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

import org.json.JSONObject;

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
                        int result = sendHttpPost(Urlpath.registerUrl,phoneNum,passWord);
                        if(result==0){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivityForResult(intent, 0);
                        }
                        else{

                        }
                    }}).start();

            }
        });
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public int sendHttpPost(String getUrl,String phoneNum,String password)  {
        try{
            getUrl = getUrl+"?username="+phoneNum+"&password="+password+"&tel="+phoneNum+"&role=1";
            String jsonstrs = "";
            String result = manager.sendStringByPost(getUrl, jsonstrs);
            JSONObject json = new JSONObject(result);
            String bool = json.getString("result");
            if(bool=="true") return 0;
            else return -1;
        }catch (Throwable e){
            e.printStackTrace();
            return -1;
        }

    }

    public void onLoginSuccess() {

    }

    public void onLoginFailed() {

    }
}
