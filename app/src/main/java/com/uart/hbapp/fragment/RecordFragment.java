package com.uart.hbapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.uart.hbapp.HbApplication;
import com.uart.hbapp.R;
import com.uart.hbapp.adapter.CommonAdapter;
import com.uart.hbapp.adapter.CommonViewHolder;
import com.uart.hbapp.bean.RecordBean;
import com.uart.hbapp.history.DayHistoryActivity;
import com.uart.hbapp.history.MonthHistoryActivity;
import com.uart.hbapp.history.WeekHistoryActivity;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordFragment extends Fragment {

    @BindView(R.id.list_record)
    ListView listRecord;


    private RecordViewModel mViewModel;
    private ActionBar actionBar;
    private CommonAdapter recordAdapter;
    private List<RecordBean> records=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, v);
        recordAdapter=new CommonAdapter<RecordBean>(getActivity(),records,R.layout.adapter_record) {
            @Override
            protected void convertView(CommonViewHolder holder, RecordBean bean) {
                if(!bean.isweek && !bean.ismonth){
                    holder.setVisibility(R.id.layout_week,View.GONE)
                    .setVisibility(R.id.layout_month,View.GONE);

                    View day = holder.getView(R.id.layout_content);
                    day.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DayHistoryActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    holder.setVisibility(R.id.layout_content,View.GONE);
                    if(bean.ismonth){
                        holder.setVisibility(R.id.layout_month,View.VISIBLE);
                        holder.setVisibility(R.id.layout_week,View.VISIBLE);
                    }
                    else{
                        holder.setVisibility(R.id.layout_month,View.GONE);
                        holder.setVisibility(R.id.layout_week,View.VISIBLE);
                    }

                    View week = holder.getView(R.id.btn_week);
                    week.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), WeekHistoryActivity.class);
                            startActivity(intent);
                        }
                    });
                    View month = holder.getView(R.id.btn_month);
                    month.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MonthHistoryActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        listRecord.setAdapter(recordAdapter);

        initDatas();
        return v;
    }

    private void initDatas(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", HbApplication.getInstance().loginUser.getToken());
        params.put("pageNo", 1);
        params.put("pageRows", 1000);
        params.put("type", 2);
        JSONObject jsonObject = new JSONObject(params);
        String base_url = URLUtil.url + URLUtil.historyQuery;
        OkGo.<String>post(base_url)
                .tag(this)
                .cacheKey("cachePostKey")
                .headers("Content-Type","application/json")
                .upJson(jsonObject.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
//                                records.add(new RecordBean());
//                                records.add(new RecordBean(true,false));

                            } else {
                                ToastUtils.showShort(jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShort("服务器异常");
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        setHasOptionsMenu(true);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.title_dashboard);
        // TODO: Use the ViewModel
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (actionBar != null && !hidden)
            actionBar.setTitle(R.string.title_dashboard);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.clear();
        inflater.inflate(R.menu.history_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


}
