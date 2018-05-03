package com.bftv.wanderingguy.recyclerview.listener;

import android.view.View;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/5/2
 * @changeRecord [修改记录] <br/>
 */
public interface AdapterEventListener {

    /**
     * 扩展监听器
     *
     * @param view
     * @param hasFocus
     * @param position
     * @param data
     */
    void onFocusChanged(View view, boolean hasFocus, int position, Object data);

    /**
     * 点击事件
     *
     * @param view
     * @param position
     * @param data
     */
    void onItemClick(View view, int position, Object data);
}
