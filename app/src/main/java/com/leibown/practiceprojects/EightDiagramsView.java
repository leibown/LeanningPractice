package com.leibown.practiceprojects;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Path中的布尔操作（交集，并集，差集等操作）
 *
 * @author leibown
 *         create at 2016/11/22 下午9:38
 */

public class EightDiagramsView extends View {

    private int mWidth, mHeight;
    private float mRadius;
    private Paint mPaint;

    public EightDiagramsView(Context context) {
        this(context, null);
    }

    public EightDiagramsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth / 2, mHeight / 2) * 0.8f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);

        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        Path path4 = new Path();
        Path path5 = new Path();
        Path path6 = new Path();

        path1.addCircle(0, 0, mRadius, Path.Direction.CW);
        path2.addRect(0, mRadius, mRadius, -mRadius, Path.Direction.CW);
        path3.addCircle(0, mRadius / 2, mRadius / 2, Path.Direction.CW);
        path4.addCircle(0, -mRadius / 2, mRadius / 2, Path.Direction.CW);
        path5.addCircle(0, -mRadius / 2, mRadius / 6, Path.Direction.CW);
        path6.addCircle(0, mRadius / 2, mRadius / 6, Path.Direction.CW);

        path1.op(path2, Path.Op.DIFFERENCE);
        path1.op(path3, Path.Op.DIFFERENCE);
        path1.op(path4, Path.Op.UNION);
        path1.op(path5, Path.Op.DIFFERENCE);

        canvas.drawPath(path1,mPaint);
        canvas.drawPath(path6,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(0,0,mRadius,mPaint);
    }
}
