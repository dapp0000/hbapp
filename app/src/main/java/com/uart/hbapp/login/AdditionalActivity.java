package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uart.hbapp.R;
import com.uart.hbapp.search.ScanActivity;

import androidx.appcompat.app.AppCompatActivity;

public class AdditionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);
        getSupportActionBar().hide();
    }

    public void confirmInfo(View view){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
        finish();
    }
}
