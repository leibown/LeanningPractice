package com.leibown.practiceprojects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/11/23.
 */

public class SearchView extends View {

    private int mWidth, mHeight;

    private float mRadius;
    private float[] pos;

    private Paint mPaint;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pos = new float[2];

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth / 2, mHeight / 2) * 0.8f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        Path pathCircle = new Path();
        Path pathSearch = new Path();

        RectF rectFBigCircle = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        pathCircle.addArc(rectFBigCircle, 45, -359.9f);
//        pathCircle.addCircle(0, 0, mRadius, Path.Direction.CCW);

        RectF rectFSmallCircle = new RectF(-mRadius / 2, -mRadius / 2, mRadius / 2, mRadius / 2);
        pathSearch.addArc(rectFSmallCircle, 65, 340);
//        pathSearch.addCircle(0, 0, mRadius / 2, Path.Direction.CW);

        PathMeasure measure = new PathMeasure();

        measure.setPath(pathCircle, false);
        measure.getPosTan(0, pos, null);

        pathSearch.lineTo(pos[0], pos[1]);

        canvas.drawPath(pathCircle, mPaint);

        mPaint.setColor(Color.BLUE);
        canvas.drawPath(pathSearch, mPaint);
    }
}
