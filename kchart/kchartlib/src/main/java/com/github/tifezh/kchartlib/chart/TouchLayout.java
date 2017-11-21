package com.github.tifezh.kchartlib.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * 可以滑动和缩放的ViewGroup
 * 单击，双击，长按，滑动，自然滚动，缩放功能
 */
public abstract class TouchLayout extends FrameLayout {

    private float mDownX;
    private float mDownY;
    private float mLastX;
    private float mLastY;
    private float mSecondLastX;
    private float mSecondLastY;

    private int firstID;
    private int secondID;

    private ViewConfiguration configuration;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    public TouchLayout(Context context) {
        super(context);
    }

    public TouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context);
        configuration = ViewConfiguration.get(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                onActionDown(event);
                mDownX = event.getX();
                mDownY = event.getY();
                mLastX = event.getX();
                mLastY = event.getY();
                firstID = event.getPointerId(0);
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (event.getPointerCount() == 2) {
                    int index = event.getActionIndex();
                    secondID = event.getPointerId(index);
                    mSecondLastX = event.getX();
                    mSecondLastY = event.getY();
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if (event.getPointerId(event.getActionIndex()) == firstID
                        && event.getPointerCount() == 1) {
                    onActionMove(event);
                    long time = event.getEventTime() - event.getDownTime();
                    if (time <= ViewConfiguration.getLongPressTimeout()
                            && event.getX() - mDownX <= configuration.getScaledTouchSlop()
                            && event.getY() - mDownY <= configuration.getScaledTouchSlop()) {
                        onLongPressd(event);
                        return true;
                    }
                    if (Math.abs(event.getX() - mLastX) >= configuration.getScaledTouchSlop()) {
                        onScroll(event.getX() - mLastX);
                        mLastX = event.getX();
                        mLastY = event.getY();
                    }
                }
                if (event.getPointerCount() == 2) {
                    if (event.getPointerId(event.getActionIndex()) == firstID
                            && event.getX(event.getActionIndex()) - mLastX >= configuration.getScaledTouchSlop()
                            && event.getY(event.getActionIndex()) - mLastY >= configuration.getScaledTouchSlop()) {

                        mLastX = event.getX();
                        mLastY = event.getY();
                    }
                    if (event.getPointerId(event.getActionIndex()) == secondID
                            && event.getX(event.getActionIndex()) - mSecondLastX >= configuration.getScaledTouchSlop()
                            && event.getY(event.getActionIndex()) - mSecondLastY >= configuration.getScaledTouchSlop()) {

                        mSecondLastX = event.getX();
                        mSecondLastY = event.getY();
                    }
                }
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {
            }
            break;
            case MotionEvent.ACTION_UP: {
                onActionUp(event);
                mVelocityTracker.computeCurrentVelocity(1000, configuration.getScaledMaximumFlingVelocity());
                float velocityX = mVelocityTracker.getXVelocity();
                float velocityY = mVelocityTracker.getYVelocity();
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                if ((Math.abs(velocityX) > configuration.getScaledMinimumFlingVelocity())) {
                    onFling(velocityX);
                }
            }
            break;
        }
        return true;
    }

    public void onActionDown(MotionEvent event) {

    }

    public void onActionUp(MotionEvent event) {

    }

    public void onActionMove(MotionEvent event) {

    }

    public void onLongPressd(MotionEvent event) {

    }

    public void onScroll(float distanceX) {

    }

    public void onFling(float velocityX) {
        mScroller.fling(getScrollX(), getScrollY(), (int) velocityX, 0, 0, getWidth(), 0, 0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }


}
