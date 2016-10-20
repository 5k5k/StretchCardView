package com.pannny.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.widget.TextView;

/**
 * User: Morladim(gongw@maritech.cn)
 * Date: 2016-10-20
 * Time: 16:29
 */
class StretchCardApi21Impl implements StretchCard {

    @TargetApi(21)
    @Override
    public void initTitle(CardView cardView, TextView titleView, GradientDrawable normalDrawable) {
        cardView.setPreventCornerOverlap(true);
        titleView.setElevation(cardView.getResources().getDimension(R.dimen.stretch_card_view_default_title_elevation));
    }

    @Override
    public void onAnimationStart(ValueAnimator valueAnimator, TextView titleView, GradientDrawable normalDrawable) {
    }

    @Override
    public void onAnimationEnd(ValueAnimator valueAnimator, TextView titleView, GradientDrawable tinyDrawable) {
    }
}
