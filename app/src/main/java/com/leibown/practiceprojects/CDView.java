package com.leibown.practiceprojects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 光碟View
 *
 * @author leibown
 *         created at 2016/11/25 14:03
 */
public class CDView extends ImageView {

    private Paint mPaint;

    private int mWidth, mHeight;

    private float mRadius;

    private Path mPath;
    private Bitmap mBitmap;
    private Matrix mMatrix;

    public CDView(Context context) {
        this(context, null);
    }

    public CDView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(30);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.touxiang);
        mMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth / 2, mHeight / 2) * 0.8f;

        mPath.addCircle(0, 0, mRadius, Path.Direction.CCW);
        mPath.addCircle(0, 0, 50, Path.Direction.CW);
        RectF src = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF dst = new RectF(0, 0, mWidth, mHeight);
        mMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);

        canvas.save();
        canvas.drawColor(Color.GRAY);
        canvas.clipPath(mPath, Region.Op.INTERSECT);
        mMatrix.setTranslate(-mWidth / 2, -mHeight / 2);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }
}
