package io.appetizer.hci_fridge.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import io.appetizer.hci_fridge.R;


/**
 * Created by Administrator on 2018/5/7.
 */

public class PhoneRegisterActivity extends AppCompatActivity {

    private TextView next_step_text,phone_number_edittext;
    private CheckBox checkBox;
    private boolean flag;
    private ImageView returnbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
        a();
    }

    protected void a(){
        flag = true;
        phone_number_edittext = (TextView)findViewById(R.id.phone_number_edittext);
        next_step_text = (TextView)findViewById(R.id.next_step_btn);
        checkBox = (CheckBox) findViewById(R.id.register_agree_cb);
        returnbtn = (ImageView)findViewById(R.id.return_btn);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if(flag){
                    next_step_text.setEnabled(true);
                    flag = false;
                }
                else {
                    next_step_text.setEnabled(false);
                    flag = true;
                }

            }
        });
        next_step_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum = phone_number_edittext.getText().toString();
                Intent intent = new Intent(getApplicationContext(), InputMessageCodeActivity.class);
                intent.putExtra("phoneNum",phonenum);
                startActivityForResult(intent, 0);
            }
        });
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
