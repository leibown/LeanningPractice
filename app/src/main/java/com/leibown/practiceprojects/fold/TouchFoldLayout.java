package com.leibown.practiceprojects.fold;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 带触摸的窗帘控件
 *
 * @author leibown
 *         created at 2016/11/29 16:29
 */

public class TouchFoldLayout extends FoldLayout {

    private GestureDetector mScrollGestureDetector;


    public TouchFoldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mScrollGestureDetector = new GestureDetector(context,
                new ScrollGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScrollGestureDetector.onTouchEvent(event);
    }


    private int mTranslation = -1;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mTranslation == -1)
            mTranslation = getWidth();
        super.dispatchDraw(canvas);
    }


    private class ScrollGestureDetector implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mTranslation -= distanceX;
            if (mTranslation < 0) {
                mTranslation = 0;
            }
            if (mTranslation > getWidth()) {
                mTranslation = getWidth();
            }
            float factor = Math.abs(((float) mTranslation) / ((float) getWidth()));

            setFactor(factor);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
