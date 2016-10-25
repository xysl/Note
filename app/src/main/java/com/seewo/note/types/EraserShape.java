package com.seewo.note.types;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.seewo.note.util.Constants;

/**
 * @author silei
 * @file ${}
 * @brief
 * @date 2016/10/20
 */

public class EraserShape extends CurveShape {
    @Override
    public int getKind() {
        return Constants.ERASERSHAPE;
    }

    @Override
    public void setmPaint(Paint paint) {
        super.setmPaint(paint);
        mPaint.setAlpha(0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setDither(true);
    }
}
