package com.leibown.practiceprojects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 贝塞尔3阶曲线学习
 *
 * @author leibown
 *         created at 2016/11/22 17:29
 */

public class Bezier3 extends View {
    private Paint mPaint;
    private int centerX, centerY;

    private PointF start, end, control, control1;

    private boolean mode;

    public Bezier3(Context context) {
        this(context, null);
    }

    public Bezier3(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);

        start = new PointF(0, 0);
        end = new PointF(0, 0);
        control = new PointF(0, 0);
        control1 = new PointF(0, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;

        start.x = centerX - 200;
        start.y = centerY;

        end.x = centerX + 200;
        end.y = centerY;

        control.x = centerX;
        control.y = centerY - 100;

        control1.x = centerX;
        control1.y = centerY - 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(start.x, start.y, mPaint);
        canvas.drawPoint(end.x, end.y, mPaint);
        canvas.drawPoint(control.x, control.y, mPaint);

        //绘制辅助线
        mPaint.setStrokeWidth(4);
        canvas.drawLine(start.x, start.y, control.x, control.y, mPaint);
        canvas.drawLine(control.x, control.y, control1.x, control1.y, mPaint);
        canvas.drawLine(control1.x, control1.y, end.x, end.y, mPaint);


        //绘制文字
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        float dis = mPaint.measureText("控制点1");//文本长度
        canvas.drawText("控制点1", control.x - dis / 2, control.y - fontHeight / 2, mPaint);
        canvas.drawText("控制点2", control1.x - dis / 2, control1.y - fontHeight / 2, mPaint);
        canvas.drawText("数据点", start.x - dis, start.y, mPaint);
        canvas.drawText("数据点", end.x, end.y, mPaint);

        //绘制贝塞尔曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.cubicTo(control.x, control.y, control1.x, control1.y, end.x, end.y);
        canvas.drawPath(path, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode) {
            control.x = event.getX();
            control.y = event.getY();
        }else {
            control1.x = event.getX();
            control1.y = event.getY();
        }
        invalidate();
        return true;
    }

    public void setMode(boolean isControll1) {
        mode = isControll1;
    }
}
