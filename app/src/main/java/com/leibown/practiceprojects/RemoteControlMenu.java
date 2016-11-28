package com.leibown.practiceprojects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 防遥控器按钮
 *
 * @author leibown
 *         created at 2016/11/28 14:30
 */

public class RemoteControlMenu extends View {

    private Path upPath, downPath, leftPath, rightPath, centerPath;

    private Region up, down, left, right, center;

    Matrix mMapMatrix = null;

    int mDefauColor = 0xFF4E5268;
    int mTouchedColor = 0xFFDF9C81;
    private int mViewWidth;
    private int mViewHeight;

    private Paint mDefaultPaint;

    private int touchFlag = -1;
    private int currentFlag = -1;

    private MenuListener menuListener = null;


    public RemoteControlMenu(Context context) {
        this(context, null);
    }

    public RemoteControlMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        upPath = new Path();
        downPath = new Path();
        leftPath = new Path();
        rightPath = new Path();
        centerPath = new Path();

        up = new Region();
        down = new Region();
        left = new Region();
        right = new Region();
        center = new Region();

        mMapMatrix = new Matrix();

        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(mDefauColor);
        mDefaultPaint.setStyle(Paint.Style.FILL);
        mDefaultPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mMapMatrix.reset();

        // 注意这个区域的大小
        Region globalRegion = new Region(-w, -h, w, h);
        int minWidth = (int) (Math.min(w, h) * 0.8);

        int br = minWidth / 2;
        RectF bigCircleRectF = new RectF(-br, -br, br, br);

        int sr = minWidth / 4;
        RectF smallCircleRectF = new RectF(-sr, -sr, sr, sr);

        float bigSweepAngle = 84;
        float smallSweepAngle = -80;

        centerPath.addCircle(0, 0, minWidth * 0.2f, Path.Direction.CW);
        center.setPath(centerPath, globalRegion);

        upPath.addArc(bigCircleRectF, -40, bigSweepAngle);
        upPath.arcTo(smallCircleRectF, 40, smallSweepAngle);
        upPath.close();
        up.setPath(upPath, globalRegion);

        rightPath.addArc(bigCircleRectF, 50, bigSweepAngle);
        rightPath.arcTo(smallCircleRectF, 130, smallSweepAngle);
        rightPath.close();
        right.setPath(rightPath, globalRegion);

        downPath.addArc(bigCircleRectF, 140, bigSweepAngle);
        downPath.arcTo(smallCircleRectF, 220, smallSweepAngle);
        downPath.close();
        down.setPath(downPath, globalRegion);

        leftPath.addArc(bigCircleRectF, 230, bigSweepAngle);
        leftPath.arcTo(smallCircleRectF, 310, smallSweepAngle);
        leftPath.close();
        left.setPath(leftPath, globalRegion);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pts = new float[2];
        pts[0] = event.getX();
        pts[1] = event.getY();
        mMapMatrix.mapPoints(pts);

        int x = (int) pts[0];
        int y = (int) pts[1];

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchFlag = getTouchedPath(x, y);
                currentFlag = touchFlag;
                break;
            case MotionEvent.ACTION_MOVE:
                currentFlag = getTouchedPath(x, y);
                break;
            case MotionEvent.ACTION_UP:
                currentFlag = getTouchedPath(x, y);
                if (currentFlag != -1 && currentFlag == touchFlag && menuListener != null) {
                    switch (currentFlag) {
                        case 0:
                            menuListener.onCenterClick();
                            break;
                        case 1:
                            menuListener.onUpClick();
                            break;
                        case 2:
                            menuListener.onRightClick();
                            break;
                        case 3:
                            menuListener.onLeftClick();
                            break;
                    }
                }
                currentFlag = touchFlag = -1;
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 获取点所在的区域
     *
     * @param x
     * @param y
     */
    private int getTouchedPath(int x, int y) {
        if (center.contains(x, y)) {
            return 0;
        } else if (up.contains(x, y)) {
            return 1;
        } else if (right.contains(x, y)) {
            return 2;
        } else if (down.contains(x, y)) {
            return 3;
        } else if (left.contains(x, y)) {
            return 4;
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        // 获取测量矩阵(逆矩阵)
        if (mMapMatrix.isIdentity()) {
            canvas.getMatrix().invert(mMapMatrix);
        }

        canvas.drawPath(centerPath, mDefaultPaint);
        canvas.drawPath(upPath, mDefaultPaint);
        canvas.drawPath(downPath, mDefaultPaint);
        canvas.drawPath(leftPath, mDefaultPaint);
        canvas.drawPath(rightPath, mDefaultPaint);

        mDefaultPaint.setColor(mTouchedColor);
        switch (currentFlag) {
            case 0:
                canvas.drawPath(centerPath, mDefaultPaint);
                break;
            case 1:
                canvas.drawPath(upPath, mDefaultPaint);
                break;
            case 2:
                canvas.drawPath(rightPath, mDefaultPaint);
                break;
            case 3:
                canvas.drawPath(downPath, mDefaultPaint);
                break;
            case 4:
                canvas.drawPath(leftPath, mDefaultPaint);
                break;
        }
        mDefaultPaint.setColor(mDefauColor);
    }


    public void setOnMenuClickListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }
}
