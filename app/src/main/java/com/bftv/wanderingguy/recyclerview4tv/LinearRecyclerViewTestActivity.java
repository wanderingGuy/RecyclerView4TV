package com.bftv.wanderingguy.recyclerview4tv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bftv.wanderingguy.recyclerview.LoadMoreTrigger;
import com.bftv.wanderingguy.recyclerview.adapter.BaseArrayObjectAdapter;
import com.bftv.wanderingguy.recyclerview.listener.AdapterEventListener;
import com.bftv.wanderingguy.recyclerview.widgets.BaseLinearLayoutManager;
import com.bftv.wanderingguy.recyclerview.widgets.LinearRecyclerView;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description 线性列表
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/4/26
 * @changeRecord [修改记录] <br/>
 */
public class LinearRecyclerViewTestActivity extends FragmentActivity implements View.OnClickListener {
    LinearRecyclerView linearRecyclerView;
    BaseArrayObjectAdapter arrayObjectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_list);
        linearRecyclerView = findViewById(R.id.linear_list_view);

        linearRecyclerView.setLayoutFrozen(false);
        linearRecyclerView.setHasFixedSize(true);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        linearRecyclerView.setItemAnimator(defaultItemAnimator);

        initRecyclerView(OrientationHelper.HORIZONTAL);
        setListData(OrientationHelper.HORIZONTAL);

        findViewById(R.id.linear_horizontal_list).setOnClickListener(this);
        findViewById(R.id.linear_vertical_list).setOnClickListener(this);
    }

    private void initRecyclerView(int orientation) {
        if (orientation == OrientationHelper.HORIZONTAL) {
            BaseLinearLayoutManager linearLayoutManager = new BaseLinearLayoutManager(this, OrientationHelper.HORIZONTAL, false);
            linearLayoutManager.setScrollMode(BaseLinearLayoutManager.SCROLL_MODE_DEFAULT, dp2px(LinearRecyclerViewTestActivity.this, 0));
            linearLayoutManager.setFocusInterceptMode(BaseLinearLayoutManager.FOCUS_INTERCEPT_HORIZONTAL_EDGE);

            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                }

                @Override
                public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDrawOver(c, parent, state);
                }

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.right = dp2px(LinearRecyclerViewTestActivity.this, 10);
                    outRect.left = dp2px(LinearRecyclerViewTestActivity.this, 10);
                }
            };
            linearRecyclerView.removeItemDecoration(linearRecyclerView.getItemDecorationAt(0));
            linearRecyclerView.addItemDecoration(itemDecoration);
            linearRecyclerView.setLayoutManager(linearLayoutManager);
            linearRecyclerView.setClipBounds(new Rect(-18, -30, 900, 900));
        } else {
            BaseLinearLayoutManager linearLayoutManager = new BaseLinearLayoutManager(this, OrientationHelper.VERTICAL, false);
            linearLayoutManager.setScrollMode(BaseLinearLayoutManager.SCROLL_MODE_CENTER, dp2px(LinearRecyclerViewTestActivity.this, 0));
            linearLayoutManager.setFocusInterceptMode(BaseLinearLayoutManager.FOCUS_INTERCEPT_VERTICAL_EDGE);

            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                }

                @Override
                public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    super.onDrawOver(c, parent, state);
                }

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = dp2px(LinearRecyclerViewTestActivity.this, 10);
                }
            };

            linearRecyclerView.removeItemDecoration(linearRecyclerView.getItemDecorationAt(0));
            linearRecyclerView.addItemDecoration(itemDecoration);
            linearRecyclerView.setLayoutManager(linearLayoutManager);
            linearRecyclerView.setClipBounds(new Rect(-75, -15, 900, 900));
        }

        linearRecyclerView.setLoadMoreTrigger(new LoadMoreTrigger(1) {
            @Override
            public void loadMore() {
                super.loadMore();
                Log.i("testRV", "loadMoreDataListener ");
                linearRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                        linearRecyclerView.loadMoreComplete();
                    }
                }, 500);
            }
        });

    }

    private void setListData(int orientation) {
        arrayObjectAdapter = new BaseArrayObjectAdapter(orientation == OrientationHelper.HORIZONTAL ? new BaseVerItemPresenter() : new BaseHorItemPresenter());
        for (int i = 0; i < 20; i++) {
            arrayObjectAdapter.add(i);
        }
        arrayObjectAdapter.addAdapterEventListener(new AdapterEventListener() {
            @Override
            public void onFocusChanged(View view, boolean hasFocus, int position, Object data) {
                Log.i("testRV", "onFocusChanged view:" + view + " hasFocus:" + hasFocus + " position:" + position + " data:" + data);
            }

            @Override
            public void onItemClick(View view, int position, Object data) {
                Log.i("testRV", "onItemClick view:" + view + " position:" + position + " data:" + data);
            }
        });
        linearRecyclerView.setObjectAdapter(arrayObjectAdapter);
    }

    private void loadMoreData() {
        Log.i("testRV", "loadMoreData ");
        int size = arrayObjectAdapter.size();
        for (int i = size; i < 10 + size; i++) {
            arrayObjectAdapter.add(i);
        }
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linear_vertical_list) {
            initRecyclerView(OrientationHelper.VERTICAL);
            setListData(OrientationHelper.VERTICAL);
        } else if (v.getId() == R.id.linear_horizontal_list) {
            initRecyclerView(OrientationHelper.HORIZONTAL);
            setListData(OrientationHelper.HORIZONTAL);
        }

    }

    public void nextPage(View view) {
        linearRecyclerView.nextPage();
    }

    public void prePage(View view) {
        linearRecyclerView.previousPage();
    }
}
