package com.bftv.wanderingguy.recyclerview.adapter;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.view.View;

import com.bftv.wanderingguy.recyclerview.item.BaseItemPresenter;
import com.bftv.wanderingguy.recyclerview.listener.AdapterEventListener;
import com.bftv.wanderingguy.recyclerview.listener.PresenterEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/5/2
 * @changeRecord [修改记录] <br/>
 */
public class BaseArrayObjectAdapter extends ArrayObjectAdapter implements PresenterEventListener {

    private List<AdapterEventListener> mAdapterEventListeners;

    public BaseArrayObjectAdapter(PresenterSelector presenterSelector) {
        super(presenterSelector);
        for (Presenter presenter : presenterSelector.getPresenters()) {
            if (presenter instanceof BaseItemPresenter) {
                ((BaseItemPresenter) presenter).setItemEventListener(this);
            } else {
                throw new IllegalArgumentException("presenter must be a BaseItemPresenter");
            }
        }
    }

    public BaseArrayObjectAdapter(BaseItemPresenter presenter) {
        super(presenter);
        presenter.setItemEventListener(this);
    }

    public void addAdapterEventListener(AdapterEventListener adapterEventListener) {
        if (mAdapterEventListeners == null) {
            mAdapterEventListeners = Collections.synchronizedList(new ArrayList<AdapterEventListener>());
        }
        mAdapterEventListeners.add(adapterEventListener);
    }

    public void removeAdapterEventListener(AdapterEventListener adapterEventListener) {
        if (mAdapterEventListeners != null && !mAdapterEventListeners.isEmpty()) {
            mAdapterEventListeners.remove(adapterEventListener);
        }
    }

    @Override
    public void onFocusChanged(View view, boolean hasFocus, Object data) {
        if (mAdapterEventListeners != null) {
            Iterator<AdapterEventListener> iterator = mAdapterEventListeners.iterator();
            while (iterator.hasNext()) {
                AdapterEventListener adapterEventListener = iterator.next();
                adapterEventListener.onFocusChanged(view, hasFocus, indexOf(data), data);
            }
        }
    }

    @Override
    public void onClickItem(View view, Object data) {
        if (mAdapterEventListeners != null) {
            Iterator<AdapterEventListener> iterator = mAdapterEventListeners.iterator();
            while (iterator.hasNext()) {
                AdapterEventListener adapterEventListener = iterator.next();
                adapterEventListener.onItemClick(view, indexOf(data), data);
            }
        }
    }
}
