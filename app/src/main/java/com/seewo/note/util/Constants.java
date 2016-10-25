package com.seewo.note.util;

import android.graphics.Color;
import android.os.Environment;

import com.seewo.note.types.CurveShape;

import java.io.File;

/**
 * @author silei
 * @brief 项目中涉及的常量
 * @date 2016/10/18
 */

public final class Constants {
    public static final int CURVESHAPE = 4;
    public static final int LINESHAPE = 1;
    public static final int RECTSHAPE = 2;
    public static final int CIRCLESHAPE = 3;
    public static final int ERASERSHAPE = 0;

    public static final int CREATENOTE = 21;
    public static final int OPENNOTE = 22;
    public static final int DELETNOTE = 23;

    public static final int ERASERSTATE = 11;
    public static final int PAINTINGSTATE = 12;
    public static final String LOCALDIR = Environment.getExternalStorageDirectory() + File.separator + "Qnote";
    public static final String PNGDIR = LOCALDIR + File.separator + "PNG";
    public static final String XMLDIR = LOCALDIR + File.separator + "XML";

    public static final int[] COLORS = new int[]
            {
                    Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
            };


    public static final int LOADSUCCESS = 31;
    public static final int LOADFAIL = 32;

    public static final int SAVESUCCESS = 41;
    public static final int SAVEFAIL = 42;
    public static final int DELETESUCCESS=43;
    public static final int DELETEFAIL=44;
}
