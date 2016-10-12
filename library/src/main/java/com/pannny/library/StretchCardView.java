package com.pannny.library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * User: Morladim(gongw@maritech.cn)
 * Date: 2016-10-08
 * Time: 10:25
 */
@SuppressWarnings("unused,deprecation")
public class StretchCardView extends CardView {

    /**
     * Title view.
     */
    private TextView titleView;

    /**
     * If after API21 or not.
     */
    private boolean api21 = false;

    /**
     * parent is LinearLayout and weight > 0.
     */
    private boolean parentHasWeight = false;

    /**
     * Title text "title" by default.
     */
    private static final String DEFAULT_TITLE_TEXT = "title";

    /**
     * stretch state.
     */
    private boolean stretch = true;

    /**
     * title view height.
     */
    private int titleHeight;

    /**
     * animation cost time.
     */
    private static final int ANIMATION_DURATION = 300;

    private GradientDrawable normalDrawable;

    private GradientDrawable tinyDrawable;

    private int titleBackgroundColor;

    public StretchCardView(Context context) {
        super(context);
        initialize(context, null);
    }

    public StretchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public StretchCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        setApi21();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StretchCardView);
        TypedArray card = context.obtainStyledAttributes(attrs, android.support.v7.cardview.R.styleable.CardView);
        initTitleView(context, a, card);
        a.recycle();
        card.recycle();
        addView(titleView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void setApi21() {
        api21 = Build.VERSION.SDK_INT >= 21;
    }

    @TargetApi(21)
    private void initTitleView(Context context, TypedArray typedArray, TypedArray cardTypeArray) {
        titleView = new TextView(context);
        //set title text
        String titleText = typedArray.getString(R.styleable.StretchCardView_stretchCardTitleText);
        if (titleText == null) {
            titleText = DEFAULT_TITLE_TEXT;
        }
        titleView.setText(titleText);
        //set title text color. default by white.
        if (typedArray.hasValue(R.styleable.StretchCardView_stretchCardTitleTextColor)) {
            ColorStateList titleTextColor = typedArray.getColorStateList(R.styleable.StretchCardView_stretchCardTitleTextColor);
            titleView.setTextColor(titleTextColor);
        } else {
            titleView.setTextColor(Color.WHITE);
        }
        //set title background. color primary by default.
        if (typedArray.hasValue(R.styleable.StretchCardView_stretchCardTitleBackgroundColor)) {
            ColorStateList titleTextColor = typedArray.getColorStateList(R.styleable.StretchCardView_stretchCardTitleBackgroundColor);
            titleBackgroundColor = titleTextColor == null ? ContextCompat.getColor(context, R.color.colorPrimary) : titleTextColor.getDefaultColor();
        } else {
            titleBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
        }
        titleView.setBackgroundColor(titleBackgroundColor);
        //set corner radius
        float cornerRadius = cardTypeArray.getDimensionPixelSize(android.support.v7.cardview.R.styleable.CardView_cardCornerRadius, getResources().getDimensionPixelSize(R.dimen.stretch_card_view_corner_radius));
        setRadius(cornerRadius);
        //set title text size. 6sp by default.
        int titleTextSize = typedArray.getDimensionPixelSize(R.styleable.StretchCardView_stretchCardTitleTextSize, getResources().getDimensionPixelSize(R.dimen.stretch_card_view_title_text_size));
        titleView.setTextSize(titleTextSize);
        titleView.setPadding((int) getResources().getDimension(R.dimen.custom_title_text_padding_left), (int) getResources().getDimension(R.dimen.custom_title_text_padding), 0, (int) getResources().getDimension(R.dimen.custom_title_text_padding));
        //set title view elevation.
        if (api21) {
            setPreventCornerOverlap(true);
            titleView.setElevation(getResources().getDimension(R.dimen.stretch_card_view_default_title_elevation));
        } else {
            setPreventCornerOverlap(false);
            titleView.setBackgroundDrawable(normalDrawable);
        }
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTitle();
            }
        });
    }

    @Override
    public void setRadius(float radius) {
        setGradientDrawables(radius);
        super.setRadius(radius);
    }

    private void setGradientDrawables(float radius) {
        if (normalDrawable == null) {
            normalDrawable = new GradientDrawable();
        }
        normalDrawable.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        normalDrawable.setColor(titleBackgroundColor);
        if (tinyDrawable == null) {
            tinyDrawable = new GradientDrawable();
        }
        tinyDrawable.setCornerRadius(radius);
        tinyDrawable.setColor(titleBackgroundColor);
    }

    private void titleCallback() {
        if (stretchListener == null) {
            return;
        }
        //title can not click will go here
        if (!titleTouchAble) {
            stretchListener.onStretchBlocked(stretch);
            return;
        }
        // start stretch
        stretchListener.onStretchChangedListener(stretch);
    }

    public void setTitleBackgroundColor(int color) {
        titleBackgroundColor = color;
        normalDrawable.setColor(color);
        tinyDrawable.setColor(color);
    }

    private void trigger() {
        if (!titleTouchAble)
            return;
        stretch = !stretch;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (stretch) {//stretch out
            ValueAnimator valueAnimator;
            if (parentHasWeight) {
                valueAnimator = ValueAnimator.ofFloat(minWeight, normalWeight).setDuration(ANIMATION_DURATION);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ((LinearLayout.LayoutParams) layoutParams).weight = (float) animation.getAnimatedValue();
                        setLayoutParams(layoutParams);
                    }
                });
            } else {
                valueAnimator = ValueAnimator.ofInt(titleHeight, normalHeight).setDuration(ANIMATION_DURATION);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        layoutParams.height = (int) animation.getAnimatedValue();
                        requestLayout();
                    }
                });
            }
            //before 5.0 ,set title corner.
            if (!api21) {
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
            valueAnimator.start();
        } else {//close
            ValueAnimator valueAnimator;
            if (parentHasWeight) {
                valueAnimator = ValueAnimator.ofFloat(normalWeight, minWeight).setDuration(ANIMATION_DURATION);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @TargetApi(21)
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ((LinearLayout.LayoutParams) layoutParams).weight = (float) animation.getAnimatedValue();
                        setLayoutParams(layoutParams);
                    }
                });
            } else {
                valueAnimator = ValueAnimator.ofInt(normalHeight, titleHeight).setDuration(ANIMATION_DURATION);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @TargetApi(21)
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        layoutParams.height = (int) animation.getAnimatedValue();
                        requestLayout();
                    }
                });
            }
            //before 5.0 ,set title corner.
            if (!api21) {
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
            valueAnimator.start();
        }
    }

    synchronized final public void clickTitle() {
        trigger();
        titleCallback();
    }

    /**
     * children margin to top
     */
    private int childTopMargin = 0;

    private void setChildTopMargin() {
        childTopMargin = titleView.getHeight();
    }

    /**
     * when parent layout is LinearLayout,set min weight for animation.
     */
    private float minWeight = 1;

    /**
     * when parent layout is LinearLayout,set normal weight for animation.
     */
    private float normalWeight = 1;

    @TargetApi(21)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        titleHeight = getTitleHeight();
        setChildTopMargin();
        int count = getChildCount();
        //except title view.
        for (int i = 1; i < count; i++) {
            final View child = getChildAt(i);
            final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            lp.topMargin = childTopMargin;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * when attach to window, the StretchCardView's height.
     */
    private int normalHeight;

    private void initNormalHeight() {
        if (normalHeight > titleHeight) {
            return;
        }
        normalHeight = this.getMeasuredHeight();
        setParentHasWeight();
    }

    /**
     * set StretchCardView's normal height.
     *
     * @param height normal height.
     * @throws IllegalArgumentException
     */
    public void setNormalHeight(int height) {
        if (height > titleHeight) {
            normalHeight = height;
        } else {
            throw new IllegalArgumentException("Normal height must grater than title height.");
        }
    }

    private int getTitleHeight() {
        return titleView.getHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int height = titleView != null && titleView.getVisibility() != View.GONE ? titleView.getHeight() : 0;
        super.onLayout(changed, left, top + height, right, bottom);
        initNormalHeight();
    }

    /**
     * set title text.
     *
     * @param titleText title
     */
    public void setTitleText(String titleText) {
        if (titleView != null)
            titleView.setText(titleText);
    }

    /**
     * get title text.
     *
     * @return title string
     */
    public String getTitleText() {
        return titleView == null ? "" : titleView.getText().toString();
    }

    /**
     * title can be touched or not..
     */
    private boolean titleTouchAble = true;

    public boolean getTitleTouchAble() {
        return titleTouchAble;
    }

    public void setTitleTouchAble(boolean able) {
        titleTouchAble = able;
    }

    /**
     * if parent is instance of LinearLayout ,set parentHasWeight, normalWeight and minWeight.
     */
    private void setParentHasWeight() {
        if (getParent() instanceof LinearLayout && ((LinearLayout.LayoutParams) (getLayoutParams())).weight > 0) {
            parentHasWeight = true;
            normalWeight = ((LinearLayout.LayoutParams) (getLayoutParams())).weight;
            float otherWeight = 0;
            int count = ((LinearLayout) getParent()).getChildCount();
            for (int i = 0; i < count; i++) {
                View v = ((LinearLayout) getParent()).getChildAt(i);
                if (v.equals(this)) {
                    continue;
                }
                otherWeight += ((LinearLayout.LayoutParams) (v.getLayoutParams())).weight;
            }
            float weightOfSelf = ((LinearLayout.LayoutParams) (getLayoutParams())).weight * getTitleHeight() / (normalHeight);
            minWeight = weightOfSelf * (otherWeight / ((normalWeight - weightOfSelf) + otherWeight));
        }
    }

    /**
     * stretch callback.
     */
    private StretchListener stretchListener;

    /**
     * get event listener.
     *
     * @return instance
     */
    public StretchListener getStretchListener() {
        return stretchListener;
    }

    /**
     * set event listener.
     *
     * @param stretchListener instance
     */
    public void setStretchListener(StretchListener stretchListener) {
        this.stretchListener = stretchListener;
    }

    public interface StretchListener {

        /**
         * stretch state changed will call this.
         *
         * @param stretch true:hide to show ; false: show to hide
         */
        void onStretchChangedListener(boolean stretch);

        /**
         * when cant stretch and user click title,will call this.
         *
         * @param stretch current state, true:show; false:hide
         */
        void onStretchBlocked(boolean stretch);
    }
}