package com.uart.hbapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uart.hbapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistActivity extends AppCompatActivity {


    @BindView(R.id.welcome)
    TextView welcome;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.regist)
    Button regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

    }

    public void loginByPhone(View view) {
        Intent intent = new Intent(this, AdditionalActivity.class);
        startActivity(intent);
        finish();
    }

}
