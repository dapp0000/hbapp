package com.uart.hbapp.fragment;

import android.content.Intent;
import android.icu.text.AlphabeticIndex;
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
import com.uart.hbapp.utils.TimeUtil;
import com.uart.hbapp.utils.URLUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordFragment extends Fragment {

    @BindView(R.id.list_record)
    ListView listRecord;


    private RecordViewModel mViewModel;
    private ActionBar actionBar;
    private CommonAdapter recordAdapter;
    private List<RecordBean> records=new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();

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

                    String sleepTimeStr = String.format("休息%d分钟",bean.sleepTime);
                    String vigorStr = bean.vigor+"%";

                    holder.setText(R.id.tv_content_date,bean.startDateString)
                            .setText(R.id.tv_content_time,bean.startTimeString)
                            .setText(R.id.tv_content_sleepTime,sleepTimeStr)
                            .setText(R.id.tv_vigor,vigorStr);

                    View day = holder.getView(R.id.layout_content);
                    day.setTag(bean);
                    day.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DayHistoryActivity.class);
                            intent.putExtra("record",(RecordBean)v.getTag());
                            startActivity(intent);
                        }
                    });
                }
                else{
                    holder.setVisibility(R.id.layout_content,View.GONE);
                    if(bean.ismonth){
                        holder.setVisibility(R.id.layout_month,View.VISIBLE);
                        holder.setVisibility(R.id.layout_week,View.GONE);
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
        //params.put("type", 2);
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
                            records.add(new RecordBean(false,true));
                            records.add(new RecordBean(true,false));

                            JSONObject jsonObject = new JSONObject(response.body());
                            int error = jsonObject.getInt("error");
                            if (error == 0) {
                                //data pager resultList
                                JSONObject dataJSON = jsonObject.getJSONObject("data");
                                if (dataJSON != null) {
                                    JSONObject pagerJSON = dataJSON.getJSONObject("pager");
                                    if (pagerJSON != null) {
                                        JSONArray resultListJSON = pagerJSON.getJSONArray("resultList");
                                        if(resultListJSON!=null){
                                           int size = resultListJSON.length();
                                           RecordBean cacheBean = null;
                                           for (int i=0;i<size;i++){
                                               JSONObject item = resultListJSON.getJSONObject(i);
                                               RecordBean bean = new RecordBean();
                                               bean.dataId = item.getInt("dataId");
                                               bean.logsId = item.getInt("logsId");
                                               bean.startTime = item.getLong("startTime");
                                               bean.endTime = item.getLong("endTime");
                                               bean.sleepTime = item.getInt("sleepTime");
                                               bean.sober_1 = item.getInt("sober_1");
                                               bean.sober_2 = item.getInt("sober_2");
                                               bean.sober_3 = item.getInt("sober_3");
                                               bean.sober_4 = item.getInt("sober_4");
                                               bean.sober_5 = item.getInt("sober_5");
                                               bean.relax_1 = item.getInt("relax_1");
                                               bean.relax_2 = item.getInt("relax_2");
                                               bean.relax_3 = item.getInt("relax_3");
                                               bean.relax_4 = item.getInt("relax_4");
                                               bean.relax_5 = item.getInt("relax_5");

                                               bean.startTimeString = TimeUtil.getTimeString(bean.startTime);
                                               bean.startDateString = TimeUtil.getDateString(bean.startTime);
                                               calendar.setTimeInMillis(bean.startTime);
                                               bean.weekNum = calendar.get(Calendar.WEEK_OF_YEAR);
                                               bean.monthNum = calendar.get(Calendar.MONTH);

                                               if(cacheBean!=null&&cacheBean.monthNum>bean.monthNum){
                                                   records.add(new RecordBean(false,true));
                                               }
                                               if(cacheBean!=null&&cacheBean.weekNum>bean.weekNum){
                                                   records.add(new RecordBean(true,false));
                                               }

                                               records.add(bean);
                                               cacheBean = bean;
                                           }
                                        }
                                    }
                                }
                            } else {
                                ToastUtils.showShort(jsonObject.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            recordAdapter.notifyDataSetChanged();
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
        if (actionBar != null && !hidden){
            actionBar.setTitle(R.string.title_dashboard);
            records.clear();
            recordAdapter.notifyDataSetChanged();
            initDatas();
        }

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
