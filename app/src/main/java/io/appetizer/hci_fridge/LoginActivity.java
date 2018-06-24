package io.appetizer.hci_fridge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import io.appetizer.hci_fridge.register.PhoneRegisterActivity;
import io.appetizer.hci_fridge.util.Urlpath;
import io.appetizer.hci_fridge.util.okhttpManager;


/**
 * Created by Administrator on 2018/5/7.
 */

public class LoginActivity extends AppCompatActivity {

    private TextView login,register;
    private TextView userName, passWord;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private okhttpManager manager = new okhttpManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("hci_fridge",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        a();
    }

    protected void a(){
        login = (TextView)findViewById(R.id.sign_in_button);
        register = (TextView)findViewById(R.id.sign_up_button);
        userName = (TextView)findViewById(R.id.username_edittext) ;
        passWord = (TextView)findViewById(R.id.password_edittext);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = userName.getText().toString();
                final String password = passWord.getText().toString();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        int result = validate(username , password);
                        if(result == 0){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            //Toast.makeText(getApplicationContext(), "wrong username or wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }}).start();


            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneRegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private int validate(String username, String password){
        try{
            String url = Urlpath.loginUrl+"?username="+username+"&password="+password;
            String jsonstrs = "";
            String result = manager.sendStringByPost(url, jsonstrs);
            JSONObject json = new JSONObject(result);
            String userid = json.getString("user_id");
            String token = json.getString("token");
            if(Integer.parseInt(userid)!=0) {
                editor.putString("userid",userid);
                editor.putString("token",token);
                editor.commit();
                return 0;
            }
            else return -1;
        }catch (org.json.JSONException e){
            e.printStackTrace();
            return -1;
        }
    }

}
