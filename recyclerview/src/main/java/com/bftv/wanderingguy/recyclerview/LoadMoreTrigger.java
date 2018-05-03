package com.bftv.wanderingguy.recyclerview;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/5/2
 * @changeRecord [修改记录] <br/>
 */
public abstract class LoadMoreTrigger {
    /**
     * 是否正在加载更多
     */
    private boolean isLoadingMore;
    /**
     * 加载模式
     */
    private TriggerMode triggerMode = TriggerMode.EXPLORE_END;
    /**
     * 出发加载更多的指定位置 基数倒数 从1开始
     */
    public static final int SPECIFIC_END = 1;
    public static final int UNSPECIFIC_INDEX = -1;

    private int triggerSpecificIndex = UNSPECIFIC_INDEX;

    public LoadMoreTrigger(TriggerMode triggerMode) {
        this.triggerMode = triggerMode;
        if (triggerMode == TriggerMode.EXPLORE_END) {
            triggerSpecificIndex = SPECIFIC_END;
        }
    }

    public LoadMoreTrigger(int triggerSpecificIndex) {
        this.triggerMode = TriggerMode.SELECTED_SPECIFIC_INDEX;
        this.triggerSpecificIndex = triggerSpecificIndex;
    }

    public TriggerMode getTriggerMode() {
        return triggerMode;
    }

    public int getTriggerSpecificIndex() {
        return triggerSpecificIndex;
    }

    /**
     * 调用触发器
     *
     * @return
     */
    public void loadMore() {
        isLoadingMore = true;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void loadMoreComplete() {
        isLoadingMore = false;
    }


}
