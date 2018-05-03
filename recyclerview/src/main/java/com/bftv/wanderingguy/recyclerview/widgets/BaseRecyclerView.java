package com.bftv.wanderingguy.recyclerview.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;

/**
 * @author chenlian
 * @version 1.0
 * @title BaseRecyclerView
 * @description recyclerView支持ArrayObjectAdapter
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/4/26
 * @changeRecord [修改记录] <br/>
 */
public class BaseRecyclerView extends RecyclerView {

    public static final String TAG = "BaseRecyclerView";
    private ObjectAdapter mObjectAdapter;
    protected PresenterSelector mPresenterSelector;
    private ItemBridgeAdapter mBridgeAdapter;

    public BaseRecyclerView(Context context) {
        super(context);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setObjectAdapter(ObjectAdapter adapter) {
        this.mObjectAdapter = adapter;
        this.updateAdapter();
    }

    public void setPresenterSelector(PresenterSelector presenterSelector) {
        this.mPresenterSelector = presenterSelector;
    }

    void updateAdapter() {
        if (this.mBridgeAdapter != null) {
            this.mBridgeAdapter.clear();
            this.mBridgeAdapter = null;
        }

        if (this.mObjectAdapter != null) {
            this.mBridgeAdapter = new ItemBridgeAdapter(this.mObjectAdapter, this.mPresenterSelector == null ? this.mObjectAdapter.getPresenterSelector() : this.mPresenterSelector) {
                protected void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
                    super.onCreate(viewHolder);
                    Log.v(TAG, "ItemBridgeAdapter onCreate viewHolder is " + viewHolder);

                }

                protected void onBind(ViewHolder viewHolder) {
                    super.onBind(viewHolder);
                }

                public int getItemViewType(int position) {
                    int type = super.getItemViewType(position);
                    Log.v(TAG, "ItemBridgeAdapter getItemViewType is " + type + " position is " + position);

                    return type;
                }
            };
            PresenterSelector selector = this.mPresenterSelector == null ? this.mObjectAdapter.getPresenterSelector() : this.mPresenterSelector;
            Log.v(TAG, "ItemBridgeAdapter PresenterSelector is " + selector + " getPresenters is " + selector.getPresenters());
            if (selector != null) {
                Presenter[] presenters = selector.getPresenters();
                if (presenters != null) {
                    ArrayList arrayList = new ArrayList();
                    Presenter[] var4 = presenters;
                    int var5 = presenters.length;

                    for (int var6 = 0; var6 < var5; ++var6) {
                        Presenter p = var4[var6];
                        arrayList.add(p);
                    }

                    this.mBridgeAdapter.setPresenterMapper(arrayList);
                }
            }
        }

        this.setAdapter(this.mBridgeAdapter);
    }

    public ObjectAdapter getObjectAdapter() {
        return mObjectAdapter;
    }
}
