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

public class CircleShape extends Shape {
    @Override
    public void addPoint(float x, float y) {
        if (mPointList.size()<=1) {
            mPointList.add(new Point(x, y));
        }else{
            mPointList.remove(1);
            mPointList.add(new Point(x,y));
        }
    }

    @Override
    public void draw(Canvas mCanvas) {
        if(mPointList.size()>=2){
            float startX=mPointList.get(0).getX();
            float startY=mPointList.get(0).getY();
            float endX=mPointList.get(1).getX();
            float endY=mPointList.get(1).getY();
            float radius=Math.abs(startX-endX)>=Math.abs(startY-endY)?Math.abs(startX-endX):Math.abs(startY-endY);
            mCanvas.drawCircle((startX+endX)/2,(endX+endY)/2,radius,mPaint);
        }
    }

    @Override
    public void downAction(float x, float y) {
        addPoint(x,y);
    }

    @Override
    public void moveAction(float mx, float my, float x, float y) {
        addPoint(x,y);
    }

    @Override
    public void upAction(float x, float y) {
        addPoint(x, y);
    }

    @Override
    public int getKind() {
        return Constants.CIRCLESHAPE;
    }


    @Override
    public void pointsToPointPath() {

    }
}
