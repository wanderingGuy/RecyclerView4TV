package com.bftv.wanderingguy.recyclerview.item;

import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.view.ViewGroup;

import com.bftv.wanderingguy.recyclerview.listener.PresenterEventListener;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/5/2
 * @changeRecord [修改记录] <br/>
 */
public class BaseItemPresenter extends Presenter {
    private PresenterEventListener mItemEventListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Object item) {
        View itemView = viewHolder.view;
        if (itemView != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem(v, item);
                }
            });
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    onFocusChanged(v, hasFocus, item);
                }
            });
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        viewHolder.view.setOnFocusChangeListener(null);
        viewHolder.view.setOnClickListener(null);
    }

    protected void onFocusChanged(View view, boolean hasFocus, Object data) {
        if (mItemEventListener != null) {
            mItemEventListener.onFocusChanged(view, hasFocus, data);
        }

    }

    protected void onClickItem(View view, Object data) {
        if (mItemEventListener != null) {
            mItemEventListener.onClickItem(view, data);
        }
    }

    public void setItemEventListener(PresenterEventListener itemEventListener) {
        this.mItemEventListener = itemEventListener;
    }
}
