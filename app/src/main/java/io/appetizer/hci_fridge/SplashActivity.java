package io.appetizer.hci_fridge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Administrator on 2018/5/15.
 */

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try{
            sharedPreferences = getSharedPreferences("hci_fridge",MODE_PRIVATE);
            editor = sharedPreferences.edit();
            String userid = sharedPreferences.getString("userid", null);
            String token = sharedPreferences.getString("token", null);
            if(userid!=null&&token!=null){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
            else{
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        }catch (Exception e){
            System.out.println("SplashACtivity" + e.getMessage());
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

}