package com.uart.hbapp.history;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uart.hbapp.R;
import com.uart.hbapp.bean.RecordBean;
import com.uart.hbapp.utils.view.RestColorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayHistoryActivity extends AppCompatActivity {

    @BindView(R.id.txt_start_date)
    TextView txtStartDate;
    @BindView(R.id.txt_start_time)
    TextView txtStartTime;
    @BindView(R.id.txt_sleepTime)
    TextView txtSleepTime;
    @BindView(R.id.btn_share_friend)
    ImageView btnShareFriend;
    @BindView(R.id.btn_share_send)
    ImageView btnShareSend;
    @BindView(R.id.layout_week)
    LinearLayout layoutWeek;
    @BindView(R.id.txt_vigor)
    TextView txtVigor;
    @BindView(R.id.rest_0)
    RestColorView rest0;
    @BindView(R.id.rest_1)
    RestColorView rest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_history);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        RecordBean bean = (RecordBean) getIntent().getSerializableExtra("record");
        if (bean != null) {
            txtStartDate.setText(bean.startDateString);
            txtStartTime.setText(bean.startTimeString);
            txtSleepTime.setText(String.format("休息%d分钟", bean.sleepTime));
            txtVigor.setText(bean.vigor + "%");
            rest0.setNumber(bean.sober_1,bean.sober_2,bean.sober_3,bean.sober_4,bean.sober_5);
            rest1.setNumber(bean.relax_1,bean.relax_2,bean.relax_3,bean.relax_4,bean.relax_5);
        }

    }
}
