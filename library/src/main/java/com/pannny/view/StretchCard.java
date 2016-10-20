package com.pannny.view;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.widget.TextView;

/**
 * User: Morladim(gongw@maritech.cn)
 * Date: 2016-10-20
 * Time: 16:25
 */
interface StretchCard {

    void initTitle(CardView cardView, TextView titleView, GradientDrawable normalDrawable);

    void onAnimationStart(ValueAnimator valueAnimator, TextView titleView, GradientDrawable normalDrawable);

    void onAnimationEnd(ValueAnimator valueAnimator, TextView titleView, GradientDrawable tinyDrawable);
}
