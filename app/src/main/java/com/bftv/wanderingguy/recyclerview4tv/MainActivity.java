package com.bftv.wanderingguy.recyclerview4tv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/4/26
 * @changeRecord [修改记录] <br/>
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        findViewById(R.id.linear_list_btn).setOnClickListener(this);
        findViewById(R.id.grid_list_btn).setOnClickListener(this);

        findViewById(R.id.linear_list_btn).setOnFocusChangeListener(new ItemOnFocusChangeListener());
        findViewById(R.id.grid_list_btn).setOnFocusChangeListener(new ItemOnFocusChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_list_btn:
                startActivity(new Intent(this, LinearRecyclerViewTestActivity.class));
                break;
            case R.id.grid_list_btn:
                startActivity(new Intent(this, GridRecyclerViewTestActivity.class));
                break;
            default:
                break;
        }
    }
}
