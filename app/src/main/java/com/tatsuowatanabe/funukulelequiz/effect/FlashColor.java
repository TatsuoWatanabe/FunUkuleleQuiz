package com.tatsuowatanabe.funukulelequiz.effect;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

/**
 * Created by tatsuo on 5/26/16.
 */
public class FlashColor {

    public static void show(final View view, final Integer originColor, final Integer effectColor, final Integer duration) {
        ValueAnimator va = animateBetweenColors(view, originColor, effectColor, duration);
        va.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
               animateBetweenColors(view, effectColor, originColor, duration).start();
            }
            @Override public void onAnimationStart(Animator animation)  {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        va.start();
    }

    /**
     * @see   ://stackoverflow.com/questions/2614545/animate-change-of-view-background-color-in-android
     * @param viewToAnimateItBackground
     * @param colorFrom
     * @param colorTo
     * @param durationInMs
     * @return
     */
    private static ValueAnimator animateBetweenColors(
            final View viewToAnimateItBackground,
            final int colorFrom,
            final int colorTo,
            final int durationInMs
    ) {
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            ColorDrawable colorDrawable = new ColorDrawable(colorFrom);

            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                colorDrawable.setColor((Integer)animator.getAnimatedValue());
                viewToAnimateItBackground.setBackground(colorDrawable);
            }
        });

        if (durationInMs >= 0) {
            colorAnimation.setDuration(durationInMs);
        }

        return colorAnimation;
    }

}
