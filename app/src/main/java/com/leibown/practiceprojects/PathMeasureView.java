package com.leibown.practiceprojects;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * PathMeasure学习
 *
 * @author leibown
 *         created at 2016/11/23 15:14
 */

public class PathMeasureView extends View {

    private float currentValue = 0; // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度

    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作

    private float mRadius;

    private int mWidth, mHeight;

    private Paint mPaint;

    public PathMeasureView(Context context) {
        this(context, null);
    }

    public PathMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10; //缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.jiantou, options);
        mMatrix = new Matrix();

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth, mHeight) / 2 * 0.8f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);

        Path path = new Path();
        path.addCircle(0, 0, mRadius, Path.Direction.CCW);

        PathMeasure pathMeasure = new PathMeasure(path, false);

        pathMeasure.getPosTan(pathMeasure.getLength() * currentValue, pos, tan);
        mMatrix.reset();

        float degress = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
        mMatrix.postRotate(degress, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);

        canvas.drawPath(path, mPaint);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);

    }

    ValueAnimator animator;

    public void startRotation(int duration) {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(duration);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentValue = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        } else
            animator.end();
        animator.start();
    }

}
