package andex.widget;

import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import andex.utils.SysUtils;


/**
 * @author
 */
public class AnimatedActivity extends BaseActivity {

    protected int animationSpeed = 500; // ms, Larger is faster

    // Gesture
    protected int gestureThrottleVelocityX = 500;
    protected int gestureThrottleVelocityY = 30;


    // TODO 要重新设计
//	protected final GestureDetector gestureDetector = new GestureDetector(new SimpleOnGestureListener() {
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//			Log.d("Fling...", e1 + "__" + e2 + "__" + velocityX + "__" + velocityY);
//			if (velocityX > gestureThrottleVelocityX && velocityY < gestureThrottleVelocityY) {
//				Log.d(this.getClass().getSimpleName(), "Fling to right");
//				onGesture(tabsController.currentTabIndex, tabsController.nextTabIndex());
//			}
//
//			else if (velocityX < -gestureThrottleVelocityX && velocityY > -gestureThrottleVelocityY) {
//				Log.d(this.getClass().getSimpleName(), "Fling to left");
//				onGesture(tabsController.currentTabIndex, tabsController.previousTabIndex());
//			}
//			return super.onFling(e1, e2, velocityX, velocityY);
//		}
//	});

    protected void onGesture(int oldTabIdx, int newTabIdx) {
        // DO NOTHING
    }

    /**
     * @param lastView
     * @param nextView
     * @param isLeftToRight
     */
    protected void playAnimation(View lastView, View nextView, boolean isLeftToRight) {
        if (lastView == null || nextView == null) {
            return;
        }
        AnimationSet aset = new AnimationSet(true);

        int sw = SysUtils.getScreenWidth(context);
        int distanceTo = isLeftToRight ? -sw : sw;
        int distanceFrom = isLeftToRight ? sw : -sw;

        TranslateAnimation ta1 = new TranslateAnimation(0, distanceTo, 0, 0);
        ta1.setDuration(animationSpeed);
        lastView.setAnimation(ta1);

        TranslateAnimation ta2 = new TranslateAnimation(distanceFrom, 0, 0, 0);
        ta2.setDuration(animationSpeed);
        nextView.setAnimation(ta2);

        aset.addAnimation(ta1);
        aset.addAnimation(ta2);
        aset.start();

        lastView.startAnimation(ta1);
        lastView.setVisibility(View.INVISIBLE);
        nextView.setVisibility(View.VISIBLE);
    }

}
