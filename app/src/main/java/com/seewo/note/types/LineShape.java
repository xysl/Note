package com.seewo.note.types;

import android.graphics.Canvas;

import com.seewo.note.been.Point;
import com.seewo.note.util.Constants;

/**
 * @author silei
 * @file ${}
 * @brief
 * @date 2016/10/20
 */

public class LineShape extends Shape {
    @Override
    public void draw(Canvas mCanvas) {
        if(mPointList.size()>=2){
            mCanvas.drawLine(mPointList.get(0).getX(),mPointList.get(0).getY(),mPointList.get(1).getX(),mPointList.get(1).getY(),mPaint);
        }

    }
    public void addPoint(float x, float y) {
        if (mPointList.size()<=1) {
            mPointList.add(new Point(x, y));
        }else{
            mPointList.remove(1);
            mPointList.add(new Point(x,y));
        }
    }
    @Override
    public void downAction(float x, float y) {
        addPoint(x, y);
    }

    @Override
    public void moveAction(float mx, float my, float x, float y) {
        addPoint(x, y);
    }

    @Override
    public void upAction(float x, float y) {
        addPoint(x, y);
    }

    @Override
    public int getKind() {
        return Constants.LINESHAPE;
    }

    @Override
    public void pointsToPointPath() {

    }

}
