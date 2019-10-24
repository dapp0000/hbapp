package com.uart.hbapp.history;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.uart.hbapp.R;

import butterknife.ButterKnife;

public class WeekHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_history);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
    }
}
