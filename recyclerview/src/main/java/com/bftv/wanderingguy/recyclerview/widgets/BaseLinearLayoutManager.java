package com.bftv.wanderingguy.recyclerview.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bftv.wanderingguy.recyclerview.R;
import com.bftv.wanderingguy.recyclerview.listener.OnPullListListener;

/**
 * @author chenlian
 * @version 1.0
 * @title BaseLinearLayoutManager
 * @description 处理滚动、焦点拦截、边界shake动画等等
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/4/27
 * @changeRecord [修改记录] <br/>
 */
public class BaseLinearLayoutManager extends LinearLayoutManager {
    public static final String TAG = "BaseLinearLayoutManager";

    public final static int SCROLL_MODE_DEFAULT = 0;
    public final static int SCROLL_MODE_CENTER = 4;

    public final static int FOCUS_INTERCEPT_DEFAULT = 0x00000000;
    public final static int FOCUS_INTERCEPT_HORIZONTAL_EDGE = 0x00000001;
    public final static int FOCUS_INTERCEPT_VERTICAL_EDGE = 0x00000010;
    public final static int FOCUS_INTERCEPT_BORDER = 0x00000011;

    private int scrollOffset;
    private int mCurrentScrollMode = SCROLL_MODE_DEFAULT;
    private int mFocusInterceptMode = FOCUS_INTERCEPT_DEFAULT;
    private OnPullListListener onPullListListener;

    public BaseLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        registerShakeAnimation(AnimationUtils.loadAnimation(context, getOrientation() == OrientationHelper.VERTICAL ? R.anim.ver_shake : R.anim.hor_shake));
    }

    /**
     * 设置滚动方式
     *
     * @param scrollMode
     */
    public void setScrollMode(int scrollMode) {
        mCurrentScrollMode = scrollMode;
    }

    /**
     * 设置滚动方式并可提供一个偏移量
     *
     * @param scrollMode
     * @param scrollOffset
     */
    public void setScrollMode(int scrollMode, int scrollOffset) {
        setScrollMode(scrollMode);
        this.scrollOffset = scrollOffset;
    }

    public void setOnPullListListener(OnPullListListener onPullListListener) {
        this.onPullListListener = onPullListListener;
    }

    /**
     * 设置焦点拦截模式
     *
     * @param focusInterceptMode
     */
    public void setFocusInterceptMode(int focusInterceptMode) {
        mFocusInterceptMode = focusInterceptMode;
    }

    /**
     * 设置焦点拦截模式为边界拦截
     */
    public void openBorderFocusIntercept() {
        setFocusInterceptMode(FOCUS_INTERCEPT_BORDER);
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        Log.v(TAG, "onInterceptFocusSearch direction:" + direction);
        if (!(focused.getLayoutParams() instanceof RecyclerView.LayoutParams)) {
            return super.onInterceptFocusSearch(focused, direction);
        }
        Log.v(TAG, "onInterceptFocusSearch view:" + focused);
        if (mFocusInterceptMode == FOCUS_INTERCEPT_DEFAULT) {
            return super.onInterceptFocusSearch(focused, direction);
        } else if (mFocusInterceptMode == FOCUS_INTERCEPT_HORIZONTAL_EDGE) {
            if (isReachEdge(direction, focused)) {
                exeShakeAnimator(focused);
                notifyPullListCallback(direction);
                return focused;
            }
        } else if (mFocusInterceptMode == FOCUS_INTERCEPT_VERTICAL_EDGE) {
            if (isReachEdge(direction, focused)) {
                exeShakeAnimator(focused);
                notifyPullListCallback(direction);
                return focused;
            }
        } else if (mFocusInterceptMode == FOCUS_INTERCEPT_BORDER) {
            if (getOrientation() == OrientationHelper.HORIZONTAL && (direction == View.FOCUS_UP || direction == View.FOCUS_DOWN)) {
                return focused;
            }
            if (getOrientation() == OrientationHelper.VERTICAL && (direction == View.FOCUS_LEFT || direction == View.FOCUS_RIGHT)) {
                return focused;
            }
            if (isReachEdge(direction, focused)) {
                exeShakeAnimator(focused);
                notifyPullListCallback(direction);
                return focused;
            }
        }
        return super.onInterceptFocusSearch(focused, direction);
    }

    /**
     * 是否已经达到边界
     *
     * @return
     */
    private boolean isReachEdge(int direction, View currentFocusView) {
        int currentPos = getPosition(currentFocusView);
        int orientation = getOrientation();

        if (currentPos == 0) {
            if (orientation == OrientationHelper.VERTICAL && direction == View.FOCUS_UP) {
                return true;
            }
            if (orientation == OrientationHelper.HORIZONTAL && direction == View.FOCUS_LEFT) {
                return true;
            }
        }

        int itemSum = getItemCount();
        if (currentPos + 1 == itemSum) {
            if (orientation == OrientationHelper.VERTICAL && direction == View.FOCUS_DOWN) {
                return true;
            }
            if (orientation == OrientationHelper.HORIZONTAL && direction == View.FOCUS_RIGHT) {
                return true;
            }
        }
        return false;
    }

    private void notifyPullListCallback(int direction){
        int orientation = getOrientation();
        boolean isReachEnd = false;
        if (orientation == OrientationHelper.VERTICAL && direction == View.FOCUS_DOWN) {
            isReachEnd = true;
        }
        if (orientation == OrientationHelper.HORIZONTAL && direction == View.FOCUS_RIGHT) {
            isReachEnd = true;
        }
        if(isReachEnd && onPullListListener != null) {
            onPullListListener.onPullList();
        }

    }

    private Animation animation;
    private boolean isShakeAnimationEnd = true;

    private void exeShakeAnimator(View view) {
        if (!isShakeAnimationEnd || animation == null) {
            return;
        }
        view.clearAnimation();
        view.startAnimation(animation);
    }

    public void registerShakeAnimation(Animation animation) {
        this.animation = animation;
        isShakeAnimationEnd = true;
        if (animation == null) {
            return;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isShakeAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isShakeAnimationEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        int[] scrollAmount = getChildRectangleOnScreenScrollAmount(parent, child, rect,
                immediate);
        int dx = scrollAmount[0];
        int dy = scrollAmount[1];
        if (!focusedChildVisible) {
            if (dx != 0 || dy != 0) {
                if (immediate) {
                    parent.scrollBy(dx, dy);
                } else {
                    parent.smoothScrollBy(dx, dy);
                }
                return true;
            }
        }
        return false;
    }

    protected int[] getChildRectangleOnScreenScrollAmount(RecyclerView parent, View child,
                                                          Rect rect, boolean immediate) {
        int[] out = new int[2];
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();
        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
        final int childTop = child.getTop() + rect.top - child.getScrollY();
        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        int horizontalOffset = 0;
        int verticalOffset = 0;
        if (getOrientation() == HORIZONTAL) {
            horizontalOffset = scrollOffset;
        } else {
            verticalOffset = scrollOffset;
        }

        int offScreenRight, offScreenLeft, offScreenTop, offScreenBottom;
        if (mCurrentScrollMode == SCROLL_MODE_DEFAULT) {
            offScreenLeft = Math.min(0, childLeft - parentLeft - horizontalOffset);
            offScreenRight = Math.max(0, childRight - parentRight + horizontalOffset);
            offScreenTop = Math.min(0, childTop - parentTop - verticalOffset);
            offScreenBottom = Math.max(0, childBottom - parentBottom + verticalOffset);

        } else if (mCurrentScrollMode == SCROLL_MODE_CENTER) {
            int childWidth = child.getWidth();
            int childHeight = child.getHeight();
            int itemLeftDecor = Math.abs(rect.left);
            int itemRightDecor = rect.right - childWidth;

            int itemTopDecor = Math.abs(rect.top);
            int itemBottomDecor = rect.bottom - childHeight;
            Log.v(TAG, "child itemLeftDecor:" + itemLeftDecor + " itemRightDecor:" + itemRightDecor + " childWidth:" + childWidth);

            offScreenLeft = Math.min(0, childLeft - (parentRight - rect.width()) / 2 - (itemRightDecor - itemLeftDecor) / 2 - horizontalOffset);
            offScreenRight = Math.max(0, childLeft - (parentRight - rect.width()) / 2 - (itemRightDecor - itemLeftDecor) / 2 + horizontalOffset);
            offScreenTop = Math.min(0, childTop - (parentBottom - rect.height()) / 2 - (itemBottomDecor - itemTopDecor) / 2 - verticalOffset);
            offScreenBottom = Math.max(0, childTop - (parentBottom - rect.height()) / 2 - (itemBottomDecor - itemTopDecor) / 2 + verticalOffset);
        } else {
            throw new IllegalArgumentException("scroll mode must be one of SCROLL_MODE_DEFAULT, SCROLL_MODE_CENTER or SCROLL_MODE_SPECIFIED");
        }

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        final int dx;
        if (getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL) {
            dx = offScreenRight != 0 ? offScreenRight
                    : Math.max(offScreenLeft, childRight - parentRight);
        } else {
            dx = offScreenLeft != 0 ? offScreenLeft
                    : Math.min(childLeft - parentLeft, offScreenRight);
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        final int dy = offScreenTop != 0 ? offScreenTop
                : Math.min(childTop - parentTop, offScreenBottom);
        out[0] = dx;
        out[1] = dy;

        Log.v(TAG, "getChildRectangleOnScreenScrollAmount");
        Log.v(TAG, "child id:" + child.getId() + " rect:" + rect.toString());
        Log.v(TAG, "child left:" + child.getLeft() + " top:" + child.getTop());
        Log.v(TAG, "child right:" + child.getRight() + " bottom:" + child.getBottom());
        Log.v(TAG, "parentLeft:" + parentLeft + " childLeft:" + childLeft + " offScreenLeft:" + offScreenLeft + " offScreenRight:" + offScreenRight);
        Log.v(TAG, "parentRight:" + parentRight + " childRight:" + childRight);
        return out;
    }

}
