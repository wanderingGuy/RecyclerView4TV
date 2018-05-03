package com.bftv.wanderingguy.recyclerview4tv;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bftv.wanderingguy.recyclerview.item.BaseItemPresenter;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/4/26
 * @changeRecord [修改记录] <br/>
 */
public class BaseVerItemPresenter extends BaseItemPresenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ver_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        super.onBindViewHolder(viewHolder, item);
        if (viewHolder instanceof ViewHolder) {
            viewHolder.view.setId((int) item);
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.setText(item.toString());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    @Override
    protected void onFocusChanged(View view, boolean hasFocus, Object data) {
        super.onFocusChanged(view, hasFocus, data);
        ViewAnimatorFactory.focusScale(view, hasFocus);
    }

    class ViewHolder extends Presenter.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.item_text);
        }

        public void setText(String text) {
            if (textView != null) {
                textView.setText(text);
            }
        }
    }
}
