package com.rtdev.switchdaynight;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class DayNightSwitch extends View implements Animator.AnimatorListener {


    private DayNightListener listener;
    private DayNightAnimListener animListener;

        private boolean isAnimate = false;

        private final GradientDrawable lightBackDraw;
        private final BitmapDrawable darkBackBit;
        private final BitmapDrawable sunBit;
        private final BitmapDrawable moonBit;
        private final BitmapDrawable cloudsBit;

        private float value;
        private boolean isNight;
        private int duration;



        public DayNightSwitch(Context context) {
            this(context, null, 0);
        }

        public DayNightSwitch(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DayNightSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setWillNotDraw(false);

            value = 0;
            isNight = false;
            duration = 500;

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                }
            });
            lightBackDraw = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT
                    , new int[]{Color.parseColor("#21b5e7"), Color.parseColor("#59ccda")});
            lightBackDraw.setGradientType(GradientDrawable.LINEAR_GRADIENT);


            darkBackBit = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.dark_backgrounds);
            sunBit = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.img_suns);
            moonBit = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.img_moons);
            cloudsBit = (BitmapDrawable) getContext().getResources().getDrawable(R.drawable.img_clouds);
        }

        public void toggle() {
            if (!isAnimate) {
                isAnimate = true;
                isNight = !isNight;
                if (listener != null)
                    listener.onSwitch(isNight);
                startAnimation();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int space = getWidth() - getHeight();

            darkBackBit.setBounds(0, 0, getWidth(), getHeight());
            darkBackBit.setAlpha((int) (value * 255));
            darkBackBit.draw(canvas);

            lightBackDraw.setCornerRadius((float) getHeight() / 2);
            lightBackDraw.setBounds(0, 0, getWidth(), getHeight());
            lightBackDraw.setAlpha(255 - ((int) (value * 255)));
            lightBackDraw.draw(canvas);


            moonBit.setBounds((space) - (int) (value * space)
                    , 0
                    , getWidth() - (int) (value * space)
                    , getHeight());
            moonBit.setAlpha((int) (value * 255));
            moonBit.getBitmap();

            sunBit.setBounds((space) - (int) (value * space)
                    , 0
                    , getWidth() - (int) (value * space)
                    , getHeight());
            sunBit.setAlpha(255 - ((int) (value * 255)));

            moonBit.draw(canvas);
            sunBit.draw(canvas);

            int clouds_bitmap_left = (int) ((getHeight() / 2) - (value * (getHeight() / 2)));
            cloudsBit.setBounds(clouds_bitmap_left
                    , 0
                    , clouds_bitmap_left + getHeight()
                    , getHeight());

            cloudsBit.setAlpha(cloudBitmapAlpha());

            cloudsBit.draw(canvas);
        }

        private int cloudBitmapAlpha() {
            if (value <= 0.5) {
                double a = (value - 0.5) * 2 * 255;
                return 255 - Math.min(Math.max((int) a, 0), 255);
            }
            return 0;
        }

        private void startAnimation() {
            final ValueAnimator va = ValueAnimator.ofFloat(0, 1);
            if (value == 1)
                va.setFloatValues(1, 0);

            va.setDuration(duration);
            va.addListener(this);
            va.setInterpolator(new DecelerateInterpolator());
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    value = (float) animation.getAnimatedValue();
                    if (animListener != null)
                        animListener.onAnimValueChanged(value);
                    invalidate();
                }
            });
            va.start();
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (animListener != null)
                animListener.onAnimStart();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimate = false;
            if (animListener != null)
                animListener.onAnimEnd();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        public void setIsNight(boolean is_night, boolean trigger_listener) {
            this.isNight = is_night;
            value = is_night ? 1 : 0;
            invalidate();
            if (listener != null && trigger_listener)
                listener.onSwitch(is_night);

        }

        public boolean isNight() {
            return isNight;
        }

        public void setListener(DayNightListener listener) {
            this.listener = listener;
        }


        public void setAnimListener(DayNightAnimListener animListener) {
            this.animListener = animListener;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
