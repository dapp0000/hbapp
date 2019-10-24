package com.uart.hbapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.uart.hbapp.adapter.CommonAdapter;
import com.uart.hbapp.adapter.CommonViewHolder;
import com.uart.hbapp.bean.RecordBean;
import com.uart.hbapp.history.MonthHistoryActivity;
import com.uart.hbapp.history.WeekHistoryActivity;

import java.util.ArrayList;
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
        View v = inflater.inflate(R.layout.record_fragment, container, false);
        ButterKnife.bind(this, v);

        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean(true,false));
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean(true,false));
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean(true,false));
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean(true,true));
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean());
        records.add(new RecordBean(true,false));

        recordAdapter=new CommonAdapter<RecordBean>(getActivity(),records,R.layout.record_list_item) {
            @Override
            protected void convertView(CommonViewHolder holder, RecordBean bean) {
                if(!bean.isweek && !bean.ismonth){
                    holder.setVisibility(R.id.layout_week,View.GONE)
                    .setVisibility(R.id.layout_month,View.GONE);
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

        return v;
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
