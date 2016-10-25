package com.seewo.note.types;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.seewo.note.been.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author silei
 * @brief 形状的抽象类
 * @date 2016/10/18
 */

public abstract class Shape {
    List<Point> mPointList;
    Paint mPaint;
    private float paintWidth;
    private int paintColor;


    Shape() {
        mPointList = new ArrayList<>();
        mPaint = new Paint();
    }

    public List<Point> getmPointList() {
        return mPointList;
    }

    public void setmPointList(List<Point> mPointList) {
        this.mPointList = mPointList;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint paint) {
        this.mPaint = paint;
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        paintWidth=mPaint.getStrokeWidth();
        paintColor=mPaint.getColor();
    }

    public abstract void addPoint(float x, float y);

    public abstract void draw(Canvas mCanvas);
    /**
     * action_down对应的相关操作
     *
     * @param x
     * @param y
     */
    public abstract void downAction(float x, float y);

    /**
     * action_move相关操作
     *
     * @param x
     * @param y
     */
    public abstract void moveAction(float mx, float my, float x, float y);

    /**
     * action_up的相关操作
     *
     * @param x
     * @param y
     */
    public abstract void upAction(float x, float y);

    /**
     * 返回种类
     *
     * @return
     */
    public abstract int getKind();

    public abstract void pointsToPointPath();

    public float getPaintWidth() {
        return paintWidth;
    }

    public int getPaintColor() {
        return paintColor;
    }

}