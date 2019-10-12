package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uart.hbapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class RegistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        getSupportActionBar().hide();

    }

    public void loginByPhone(View view){
        Intent intent = new Intent(this, AdditionalActivity.class);
        startActivity(intent);
        finish();
    }

}
