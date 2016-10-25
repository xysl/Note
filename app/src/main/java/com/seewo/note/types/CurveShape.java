package com.seewo.note.types;

import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Log;

import com.seewo.note.been.Point;
import com.seewo.note.util.Constants;

/**
 * @author silei
 * @brief
 * @date 2016/10/18
 */

public class CurveShape extends Shape {

    private Path pointPath;

    public CurveShape() {
        pointPath = new Path();
    }

    @Override
    public void draw(Canvas mCanvas) {

        if (this.pointPath != null) {
            if (mPointList.size() == 1) {
                mCanvas.drawPoint(mPointList.get(0).getX(), mPointList.get(0).getY(), mPaint);
            } else {
                mCanvas.drawPath(pointPath, mPaint);
            }
        }
    }

    @Override
    public void addPoint(float x, float y) {
        mPointList.add(new Point(x, y));
    }

    @Override
    public void downAction(float x, float y) {
        getPointPath().moveTo(x, y);
        addPoint(x, y);
    }

    @Override
    public void moveAction(float previousX, float previousY, float x, float y) {
        pointPath.quadTo(previousX, previousY, (x + previousX) / 2, (y + previousY) / 2);
        addPoint(x, y);
    }

    @Override
    public void upAction(float x, float y) {

    }

    @Override
    public int getKind() {
        return Constants.CURVESHAPE;
    }

    private Path getPointPath() {
        return pointPath;
    }

    private void setPointPath(Path pointPath) {
        this.pointPath = pointPath;
    }

    @Override
    public void pointsToPointPath() {
        Path path = new Path();
        path.moveTo(mPointList.get(0).getX(), mPointList.get(0).getY());
        float previousX = mPointList.get(0).getX();
        float previousY = mPointList.get(0).getY();
        float x;
        float y;
        path.moveTo(previousX, previousY);
        for (int i = 1; i < mPointList.size() - 1; i++) {
            x = mPointList.get(i).getX();
            y = mPointList.get(i).getY();
            path.quadTo(previousX, previousY, (x + previousX) / 2, (y + previousY) / 2);
            previousX = x;
            previousY = y;
        }
        path.lineTo(mPointList.get(mPointList.size() - 1).getX(), mPointList.get(mPointList.size() - 1).getY());
        setPointPath(path);
    }
}
