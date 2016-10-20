package com.pannny.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.widget.TextView;

/**
 * User: Morladim(gongw@maritech.cn)
 * Date: 2016-10-20
 * Time: 16:29
 */
@SuppressWarnings("deprecation")
class StretchCardImpl implements StretchCard {

    @Override
    public void initTitle(CardView cardView, TextView titleView, GradientDrawable normalDrawable) {
        cardView.setPreventCornerOverlap(false);
        titleView.setBackgroundDrawable(normalDrawable);
    }

    @Override
    public void onAnimationStart(ValueAnimator valueAnimator, final TextView titleView, final GradientDrawable normalDrawable) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                titleView.setBackgroundDrawable(normalDrawable);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    @Override
    public void onAnimationEnd(ValueAnimator valueAnimator, final TextView titleView, final GradientDrawable tinyDrawable) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                titleView.setBackgroundDrawable(tinyDrawable);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}
