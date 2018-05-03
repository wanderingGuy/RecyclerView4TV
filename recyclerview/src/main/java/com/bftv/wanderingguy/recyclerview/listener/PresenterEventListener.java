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
public interface PresenterEventListener {
    /**
     * 焦点改变时
     *
     * @param view
     * @param hasFocus
     * @param data
     */
    void onFocusChanged(View view, boolean hasFocus, Object data);

    /**
     * item点击
     *
     * @param view
     * @param data
     */
    void onClickItem(View view, Object data);

}
