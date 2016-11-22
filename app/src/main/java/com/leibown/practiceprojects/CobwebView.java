package com.leibown.practiceprojects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * 蜘蛛网状图
 *
 * @author leibown
 *         created at 2016/11/22 14:03
 */
public class CobwebView extends View {

    private List<CobWebData> mDatas;
    private int mCount = 6;//定点个数

    private int mPolygonCount = 4;//需要绘制的多边形个数

    private float mAngle;

    private float mRadius; //网格最大半径
    private int mCenterX;   //中心X坐标
    private int mCenterY; //中心Y坐标

    private float mMaxValue = 100;             //数据最大值
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔

    public CobwebView(Context context) {
        this(context, null);
    }

    public CobwebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(Color.GRAY);
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setColor(Color.GRAY);
        valuePaint.setAntiAlias(true);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        valuePaint.setStrokeWidth(2);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float dis = textPaint.measureText(mDatas.get(0).getTitle());//文本长度
        mRadius = Math.min(w, h) / 2 - 2 * dis;
        mCenterX = w / 2;
        mCenterY = h / 2;
        postInvalidate();
        Log.i("leibown:", "mCenterX_" + mCenterX + "     mCenterY" + mCenterY);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制多边形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = mRadius / mPolygonCount;//r是蜘蛛丝之间的间距
        for (int i = 1; i <= mPolygonCount; i++) {//中心点不用绘制
            float curR = r * i;//当前半径
            path.reset();
            for (int j = 0; j < mCount; j++) {
                if (j == 0) {
                    path.moveTo(mCenterX + curR, mCenterY);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x = (float) (mCenterX + curR * Math.cos(mAngle * j));
                    float y = (float) (mCenterY + curR * Math.sin(mAngle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();//闭合路径
            canvas.drawPath(path, mainPaint);
            Log.i("leibown", "开始画多边形:" + mCount);
        }
    }

    /**
     * 绘制中心点到定点的连线
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mCount; i++) {
            path.moveTo(mCenterX, mCenterY);
            //根据半径，计算出蜘蛛丝上每个点的坐标
            float x = (float) (mCenterX + mRadius * Math.cos(mAngle * i));
            float y = (float) (mCenterY + mRadius * Math.sin(mAngle * i));
            path.lineTo(x, y);
        }
        canvas.drawPath(path, mainPaint);
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < mCount; i++) {
            float dis = textPaint.measureText(mDatas.get(i).getTitle());//文本长度
            float x = (float) (mCenterX + (mRadius + fontHeight / 2) * Math.cos(mAngle * i));
            float y = (float) (mCenterY + (mRadius + fontHeight / 2) * Math.sin(mAngle * i));
            if (mAngle * i >= 0 && mAngle * i <= Math.PI / 2) {//第4象限
                canvas.drawText(mDatas.get(i).getTitle(), x, y, textPaint);
            } else if (mAngle * i >= 3 * Math.PI / 2 && mAngle * i <= Math.PI * 2) {//第3象限
                canvas.drawText(mDatas.get(i).getTitle(), x, y, textPaint);
            } else if (mAngle * i > Math.PI / 2 && mAngle * i <= Math.PI) {//第2象限
                canvas.drawText(mDatas.get(i).getTitle(), x - dis, y, textPaint);
            } else if (mAngle * i >= Math.PI && mAngle * i < 3 * Math.PI / 2) {//第1象限
                canvas.drawText(mDatas.get(i).getTitle(), x - dis, y, textPaint);
            }
        }
    }

    /**
     * 绘制覆盖区域
     *
     * @param canvas
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        valuePaint.setAlpha(255);
        for (int i = 0; i < mCount; i++) {
            double percent = mDatas.get(i).getData() / mMaxValue;
            float x = (float) (mCenterX + mRadius * Math.cos(mAngle * i) * percent);
            float y = (float) (mCenterY + mRadius * Math.sin(mAngle * i) * percent);
            if (i == 0) {
                path.moveTo(x, mCenterY);
            } else {
                path.lineTo(x, y);
            }
            //绘制小圆点
            canvas.drawCircle(x, y, 10, valuePaint);
        }
        path.close();
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);
        valuePaint.setAlpha(127);
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }


    /**
     * 设置需要绘制的多边形个数
     *
     * @param polygonCount
     */
    public void setPolygonCount(int polygonCount) {
        this.mPolygonCount = polygonCount;
        postInvalidate();
    }


    public void setData(List<CobWebData> datas) {
        mDatas = datas;
        mCount = mDatas.size();
        mAngle = (float) (Math.PI * 2 / mCount);
        postInvalidate();
    }

    //设置最大值
    public void setMaxValue(float maxValue) {
        this.mMaxValue = maxValue;
    }
}
