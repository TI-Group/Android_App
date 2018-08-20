package io.appetizer.hci_fridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MyFridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);
        Toolbar toolbar = findViewById(R.id.myFridgeToolBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("我的冰箱");
        setSupportActionBar(toolbar);
    }
}
