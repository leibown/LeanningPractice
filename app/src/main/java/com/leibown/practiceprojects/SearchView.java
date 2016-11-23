package com.leibown.practiceprojects;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

import static com.leibown.practiceprojects.SearchView.State.ENDING;
import static com.leibown.practiceprojects.SearchView.State.NONE;
import static com.leibown.practiceprojects.SearchView.State.SEARCHING;
import static com.leibown.practiceprojects.SearchView.State.STARTING;

/**
 * Created by Administrator on 2016/11/23.
 */

public class SearchView extends View {

    private int mWidth, mHeight;

    private float mRadius;
    private float[] pos;

    private Paint mPaint;

    private Path pathCircle;
    private Path pathSearch;

    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    // 控制各个过程的动画
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;

    private int mDefaultDuration = 2000;

    private Handler mHandler;

    // 当前的状态(非常重要)
    private State mCurrentState = NONE;

    private PathMeasure mMeasure;

    private float mAnimatorValue;

    // 判断是否已经搜索结束
    private boolean isOver = false;

    private int mDefaultColor = Color.WHITE;

    private int mDistance;//搜索过程中，起点与终点的动态距离
    private int circleLength;//圆周长

    private Random mRandom;


    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pos = new float[2];
        mRandom = new Random();
        initAll();
    }

    private void initAll() {
        initPaint();
        initListener();
        initAnimator();
        initHandler();

    }


    // 这个视图拥有的状态
    public static enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    private void initPath() {
        pathCircle = new Path();
        pathSearch = new Path();

        RectF rectFBigCircle = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        pathCircle.addArc(rectFBigCircle, 45, -359.999f);

        RectF rectFSmallCircle = new RectF(-mRadius / 2, -mRadius / 2, mRadius / 2, mRadius / 2);
        pathSearch.addArc(rectFSmallCircle, 65, 340);

        mMeasure = new PathMeasure();

        mMeasure.setPath(pathCircle, false);
        mMeasure.getPosTan(0, pos, null);

        pathSearch.lineTo(pos[0], pos[1]);
    }

    private void initListener() {

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurrentState != SEARCHING)
                    mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mHandler.sendEmptyMessage(0);
                mDistance = mRandom.nextInt(circleLength / 2) + circleLength / 4;
            }
        };


        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };
    }

    private void initAnimator() {
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mDefaultDuration / 2);
        mSearchingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mDefaultDuration);
        mEndingAnimator = ValueAnimator.ofFloat(1, 0).setDuration(mDefaultDuration / 2);

        mStartingAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);

        mStartingAnimator.addListener(mAnimatorListener);
        mSearchingAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);

        mSearchingAnimator.setInterpolator(new LinearInterpolator());
        mSearchingAnimator.setRepeatCount(ValueAnimator.INFINITE);

    }


    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.i("leibown", "收到了");
                switch (mCurrentState) {
                    case STARTING:
                        // 从开始动画转换到搜索动画
                        isOver = false;
                        mCurrentState = SEARCHING;
//                        mStartingAnimator.removeAllListeners();
                        mSearchingAnimator.start();
                        break;
                    case SEARCHING:
                        if (!isOver) {

                        } else {
                            mSearchingAnimator.end();
                            mCurrentState = ENDING;
                            mEndingAnimator.start();
                        }
                        break;
                    case ENDING:
                        // 从结束动画转变为无状态
                        mCurrentState = State.NONE;
                        break;
                }
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth / 2, mHeight / 2) * 0.8f;
        circleLength = (int) (Math.PI * 2 * mRadius);
        mDistance = circleLength / 2;
        initPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearch(canvas);
    }

    private void drawSearch(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint.setColor(mDefaultColor);
//        canvas.drawColor(Color.parseColor("#0082D7"));
        switch (mCurrentState) {
            case STARTING:
                mMeasure.setPath(pathSearch, false);
                float length = mMeasure.getLength();
                Path dst = new Path();
                mMeasure.getSegment(length * mAnimatorValue, length, dst, true);
                canvas.drawPath(dst, mPaint);
                break;
            case SEARCHING:
                mMeasure.setPath(pathCircle, false);
                Path dst1 = new Path();
                float stop = mAnimatorValue * mMeasure.getLength();
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mDistance));
                mMeasure.getSegment(start, stop, dst1, true);
                canvas.drawPath(dst1, mPaint);
                break;
            case ENDING:
                Log.i("leibown", "进入ENDING");
                mMeasure.setPath(pathSearch, false);
                Path dst2 = new Path();
                mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue, mMeasure.getLength(), dst2, true);
                canvas.drawPath(dst2, mPaint);
                break;
            case NONE:
//                canvas.drawPath(pathCircle, mPaint);
//                mPaint.setColor(Color.BLUE);
                canvas.drawPath(pathSearch, mPaint);
                break;
        }
    }

    /**
     * 开始搜索
     */
    public void startSearch() {
        mCurrentState = STARTING;
        mStartingAnimator.start();
        isOver = false;
    }

    /**
     * 结束搜索
     */
    public void endSearch() {
        isOver = true;
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    public void setDuration(int duration) {
        mDefaultDuration = duration;
        mStartingAnimator.setDuration(mDefaultDuration / 2);
        mSearchingAnimator.setDuration(mDefaultDuration);
        mEndingAnimator.setDuration(mDefaultDuration / 2);
    }


    public void setColor(int color) {
        mDefaultColor = color;
    }

}
