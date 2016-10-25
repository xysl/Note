package com.seewo.note.been;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.seewo.note.types.CircleShape;
import com.seewo.note.types.CurveShape;
import com.seewo.note.types.EraserShape;
import com.seewo.note.types.LineShape;
import com.seewo.note.types.RectShape;
import com.seewo.note.types.Shape;
import com.seewo.note.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author silei
 * @file ${}
 * @brief
 * @date 2016/10/18
 */

public class MyCanvas extends View {
    private int mCurrentKind = Constants.CURVESHAPE;
    private int currentColor = Constants.COLORS[0];
    private float currentWidth = 5;

    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Bitmap bitmapTemp;

    private int mCanvansWidth;
    private int mCanvansHeight;
    float currentX;
    float currentY;

    private List<Shape> shapeList;
    private Stack<Shape> delShapeStack;

    private Shape mCurrentShape;

    private boolean isChanged = false;
    private boolean isFinished = true;

    private boolean isFirstLoad = true;

    private int mPaintState = Constants.PAINTINGSTATE;

    public MyCanvas(Context context) {
        this(context, null);
    }

    public MyCanvas(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        shapeList = new ArrayList<>();
        delShapeStack = new Stack<>();
        //初始化画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(currentWidth);
        mPaint.setColor(currentColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCanvansWidth = getMeasuredWidth();
        mCanvansHeight = getMeasuredHeight();
        if (isFirstLoad) {
            initCanvas();
            isFirstLoad = false;
        }
    }

    public void initCanvas() {
        mBitmap = Bitmap.createBitmap(mCanvansWidth, mCanvansHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFinished) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(bitmapTemp, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isChanged = true;
                actionDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - currentX) > 5 || Math.abs(y - currentY) > 5) {
                    actionMove(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                actionUp(x, y);
                break;
        }
        invalidate();
        return true;
    }


    private void actionDown(float x, float y) {
        if (mPaintState == Constants.PAINTINGSTATE) {
            switch (mCurrentKind) {
                case Constants.CURVESHAPE:
                    mCurrentShape = new CurveShape();
                    break;
                case Constants.LINESHAPE:
                    mCurrentShape = new LineShape();
                    break;
                case Constants.CIRCLESHAPE:
                    mCurrentShape = new CircleShape();
                    break;
                case Constants.RECTSHAPE:
                    mCurrentShape = new RectShape();
                    break;
                case Constants.ERASERSHAPE:
                    mCurrentShape = new EraserShape();
                    break;
            }
            mCurrentShape.downAction(x, y);
            Paint ownPaint = new Paint();
            if (mCurrentKind != Constants.ERASERSHAPE) {
                ownPaint.setColor(mPaint.getColor());
            }
            ownPaint.setStrokeWidth(mPaint.getStrokeWidth());
            mCurrentShape.setmPaint(ownPaint);
        }
        isFinished = false;
        bitmapTemp = Bitmap.createBitmap(mBitmap);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        mCurrentShape.draw(canvasTemp);
        currentX = x;
        currentY = y;
    }

    private void actionMove(float x, float y) {
        if (mPaintState == Constants.PAINTINGSTATE) {
            mCurrentShape.moveAction(currentX, currentY, x, y);
        }
        bitmapTemp = Bitmap.createBitmap(mBitmap);
        Canvas canvasTemp = new Canvas(bitmapTemp);
        mCurrentShape.draw(canvasTemp);
        currentX = x;
        currentY = y;
    }

    private void actionUp(float x, float y) {
        isFinished = true;
        bitmapTemp = null;
        mCurrentShape.upAction(x, y);
        //绘制到Bitmap上去
        mCurrentShape.draw(mCanvas);
        shapeList.add(mCurrentShape);
        mCurrentShape = null;

    }

    public void drawAllShape() {
        for (Shape shape : shapeList) {
            shape.draw(mCanvas);
        }
        invalidate();
    }

    public void revokeAction() {
        if (shapeList != null && shapeList.size() > 0) {
            delShapeStack.push(shapeList.remove(shapeList.size() - 1));
            initCanvas();
            drawAllShape();
        }
    }

    public void regainAction() {
        if (delShapeStack != null && delShapeStack.size() > 0) {
            shapeList.add(delShapeStack.remove(delShapeStack.size() - 1));
            initCanvas();
            drawAllShape();
        }
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public List<Shape> findShapeList() {
        return shapeList;
    }

    public void setShapeList(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }

    public void setPaintWidth(int paintWidth) {
        mPaint.setStrokeWidth(paintWidth);
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setmCurrentKind(int currentKind) {
        mCurrentKind = currentKind;
        Log.e("kind", currentKind + "");
    }
}
