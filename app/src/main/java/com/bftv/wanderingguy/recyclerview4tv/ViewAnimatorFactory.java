package com.bftv.wanderingguy.recyclerview4tv;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * @author chenlian
 * @version 1.0
 * @title
 * @description
 * @company 北京奔流网络信息技术有线公司
 * @created 2018/5/2
 * @changeRecord [修改记录] <br/>
 */
public class ViewAnimatorFactory {


    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final float DEFAULT_FACTOR = 2F;

    public static void scaleTo(View view, float from, float to, long duration) {
        view.clearAnimation();

        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight() / 2);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, SCALE_X, from, to);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, SCALE_Y, from, to);
        scaleDownX.setDuration(duration);
        scaleDownY.setDuration(duration);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.setInterpolator(new DecelerateInterpolator(DEFAULT_FACTOR));
        scaleDown.setDuration(duration);
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.start();
    }

    public static void focusScale(View v, boolean hasFocus) {
        if (hasFocus) {
            scaleTo(v, 1.0f, 1.2f, 200);
        } else {
            scaleTo(v, 1.2f, 1.0f, 200);
        }
    }
}
