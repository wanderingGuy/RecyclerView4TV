package com.bftv.wanderingguy.recyclerview.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bftv.wanderingguy.recyclerview.LoadMoreTrigger;
import com.bftv.wanderingguy.recyclerview.TriggerMode;
import com.bftv.wanderingguy.recyclerview.adapter.BaseArrayObjectAdapter;
import com.bftv.wanderingguy.recyclerview.listener.AdapterEventListener;
import com.bftv.wanderingguy.recyclerview.listener.OnPullListListener;

/**
 * @author chenlian
 * @version 1.0
 * @title LinearRecyclerView
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/4/26
 * @changeRecord [修改记录] <br/>
 */
public class LinearRecyclerView extends BaseRecyclerView {
    public static final String TAG = "LinearRecyclerView";

    private LoadMoreTrigger loadMoreTrigger;
    private AdapterEventListener mAdapterEventListener;

    public LinearRecyclerView(Context context) {
        super(context);
    }

    public LinearRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    public void setLoadMoreTrigger(final LoadMoreTrigger loadMoreTrigger) {
        this.loadMoreTrigger = loadMoreTrigger;
        if (loadMoreTrigger == null) {
            clearTrigger();
            return;
        }
        switch (loadMoreTrigger.getTriggerMode()) {
            case SELECTED_SPECIFIC_INDEX:
            case EXPLORE_END:
                initSpecificIndexLoad(loadMoreTrigger.getTriggerSpecificIndex());
                break;
            case PULL:
                if (getLayoutManager() instanceof BaseLinearLayoutManager) {
                    BaseLinearLayoutManager layoutManager = (BaseLinearLayoutManager) getLayoutManager();
                    layoutManager.setOnPullListListener(new OnPullListListener() {
                        @Override
                        public void onPullList() {
                            if (!loadMoreTrigger.isLoadingMore()) {
                                loadMoreTrigger.loadMore();
                            } else {
                                Log.i("testRV", "load more but doing");
                            }
                        }
                    });
                } else {
                    throw new RuntimeException("if you open trigger mode: PULL, use BaseLinearLayoutManager instead");
                }
                break;
            default:
                break;
        }
    }

    public void loadMoreComplete() {
        Log.i("testRV", "load more complete");
        if (loadMoreTrigger != null) {
            loadMoreTrigger.loadMoreComplete();
        }
    }

    private void clearTrigger() {
        if (getLayoutManager() instanceof BaseLinearLayoutManager) {
            BaseLinearLayoutManager layoutManager = (BaseLinearLayoutManager) getLayoutManager();
            layoutManager.setOnPullListListener(null);
        }
        if (getObjectAdapter() instanceof BaseArrayObjectAdapter) {
            BaseArrayObjectAdapter baseArrayObjectAdapter = (BaseArrayObjectAdapter) getObjectAdapter();
            baseArrayObjectAdapter.removeAdapterEventListener(mAdapterEventListener);
        }
    }

    /**
     * @param position
     */
    public void initSpecificIndexLoad(int position) {
        if (position < 0) {
            return;
        }
        clearTrigger();
        if (getObjectAdapter() == null) {
            return;
        }

        if (!(getObjectAdapter() instanceof BaseArrayObjectAdapter)) {
            throw new RuntimeException("if you open trigger mode: " + loadMoreTrigger.getTriggerMode() + ", use BaseArrayObjectAdapter instead");
        }
        final BaseArrayObjectAdapter baseArrayObjectAdapter = (BaseArrayObjectAdapter) getObjectAdapter();
        mAdapterEventListener = new AdapterEventListener() {
            @Override
            public void onFocusChanged(View view, boolean hasFocus, int position, Object data) {
                int itemCount = baseArrayObjectAdapter.size();
                if (loadMoreTrigger.getTriggerMode() == TriggerMode.EXPLORE_END
                        || loadMoreTrigger.getTriggerMode() == TriggerMode.SELECTED_SPECIFIC_INDEX) {
                    if (itemCount <= position + loadMoreTrigger.getTriggerSpecificIndex()) {
                        loadMoreTrigger.loadMore();
                    }
                }
            }

            @Override
            public void onItemClick(View view, int position, Object data) {

            }
        };
        baseArrayObjectAdapter.addAdapterEventListener(mAdapterEventListener);
    }

    @Override
    public void setObjectAdapter(ObjectAdapter adapter) {
        super.setObjectAdapter(adapter);
        if (loadMoreTrigger != null) {
            //重置adapter时应该重设loadMoreTrigger
            switch (loadMoreTrigger.getTriggerMode()) {
                case SELECTED_SPECIFIC_INDEX:
                case EXPLORE_END:
                    initSpecificIndexLoad(loadMoreTrigger.getTriggerSpecificIndex());
                    break;
            }
        }
    }

    /**
     * 下一页
     */
    public void nextPage(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int lastCompleteVisible = layoutManager.findLastCompletelyVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        Log.i("testRV", "next page lastCompleteVisible:"+lastCompleteVisible+" lastVisible:"+lastVisible);

        View itemView = layoutManager.findViewByPosition(lastVisible);
        if (itemView.getLayoutParams() instanceof LayoutParams) {
            Log.i("testRV", "next page itemView:"+itemView.getLayoutParams());
        }
        if(lastCompleteVisible == lastVisible) {
            //当前最后一个item就是最后一个完全显示的
            int right = itemView.getRight() + 15;
            Log.i("testRV", "next page right:"+right+" itemView:"+itemView.getId());
            smoothScrollBy(right, 0);
        } else {
            //最后一个item是不完全显示的
            int left = Math.max(itemView.getLeft() - 15, 0);
            Log.i("testRV", "next page left:"+left+" itemView:"+itemView.getId());
            smoothScrollBy(left, 0);

        }
    }

    /**
     * 上一页
     */
    public void previousPage(){

    }

}
