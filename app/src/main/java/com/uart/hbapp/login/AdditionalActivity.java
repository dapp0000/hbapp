package com.uart.hbapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uart.hbapp.R;

public class AdditionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);
        getSupportActionBar().hide();
    }

    public void confirmInfo(View view){
        Intent intent = new Intent(this,TipsActivity.class);
        startActivity(intent);
        finish();
    }
}
