
package com.atomic.android.utils;
/**
 * Created by Hung Hoang on 09/07/2017.
 */

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;

@SuppressWarnings("UnnecessaryLocalVariable")
public class AnimationUtils {

    public final static int DEFAULT_DELAY = 0;
    public final static int SHORT_DURATION = 200;
    public final static int ALPHA_SHORT_DURATION = 400;

    /**
     * Reduces the X & Y
     *
     * @param v the view to be scaled
     *
     * @return the ViewPropertyAnimation to manage the animation
     */
    public static ViewPropertyAnimator hideViewByScale (View v) {

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY).setDuration(SHORT_DURATION)
          .scaleX(0).scaleY(0);

        return propertyAnimator;
    }

    /**
     * Shows a view by scaling
     *
     * @param v the view to be scaled
     *
     * @return the ViewPropertyAnimation to manage the animation
     */
    public static ViewPropertyAnimator showViewByScale (View v) {

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY)
            .scaleX(1).scaleY(1);

        return propertyAnimator;
    }

    public static AlphaAnimation hideViewByAlpha(final View v) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(ALPHA_SHORT_DURATION);
        v.setAnimation(alphaAnimation);
        return alphaAnimation;
    }

    public static ViewPropertyAnimator showViewByScaleAndVisibility (View v) {
        v.setVisibility(View.VISIBLE);

        ViewPropertyAnimator propertyAnimator = v.animate().setStartDelay(DEFAULT_DELAY)
                .scaleX(1).scaleY(1);

        return propertyAnimator;
    }
}
