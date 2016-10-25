package com.seewo.note.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Xml;

import com.seewo.note.been.Point;
import com.seewo.note.types.CircleShape;
import com.seewo.note.types.CurveShape;
import com.seewo.note.types.EraserShape;
import com.seewo.note.types.LineShape;
import com.seewo.note.types.RectShape;
import com.seewo.note.types.Shape;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author silei
 * @brief xml相关
 * @date 2016/10/18
 */

public final class XmlOperation {
    /**
     * 将画图保存成XML格式文件
     *
     * @param shapeList
     */
    public static boolean saveToXml(List<Shape> shapeList, String fileName) {

        boolean bFlag = false;
        String strTmpName = fileName + ".xml";
        FileOutputStream fileOutputStream = null;

        File newXmlFile = new File(Constants.XMLDIR + File.separator + strTmpName);
        try {
            bFlag = !newXmlFile.exists() || newXmlFile.delete();
            if (bFlag) {
                if (newXmlFile.createNewFile()) {
                    fileOutputStream = new FileOutputStream(newXmlFile);

                    XmlSerializer serializer = Xml.newSerializer();

                    // we set the FileOutputStream as output for the serializer,
                    // using UTF-8 encoding
                    serializer.setOutput(fileOutputStream, "UTF-8");
                    serializer.startDocument("UTF-8", true);

                    // start a tag called "root"
                    serializer.startTag(null, "root");
                    for (Shape sp : shapeList) {
                        serializer.startTag(null, "Shape");

                        serializer.startTag(null, "Kind");
                        serializer.text(sp.getKind() + "");
                        serializer.endTag(null, "Kind");

                        serializer.startTag(null, "Color");
                        serializer.text(sp.getPaintColor() + "");
                        serializer.endTag(null, "Color");

                        serializer.startTag(null, "Width");
                        serializer.text(sp.getPaintWidth() + "");
                        serializer.endTag(null, "Width");

                        serializer.startTag(null, "PointList");
                        serializer.text(pointToStr(sp.getmPointList()));
                        serializer.endTag(null, "PointList");

                        serializer.endTag(null, "Shape");
                    }
                    serializer.endTag(null, "root");
                    serializer.endDocument();

                    // write xml data into the FileOutputStream
                    serializer.flush();
                    // finally we close the file stream
                    fileOutputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bFlag;
    }

    static List<Shape> parseXmlToShape(String filePath) throws Exception {
        List<Shape> mList = null;
        Shape shape = null;
        int paintColor= Color.BLACK;
        float paintWidth=5;
        InputStream is = new FileInputStream(new File(filePath));
        XmlPullParser xpp = Xml.newPullParser();

        xpp.setInput(is, "UTF-8");
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    mList = new ArrayList<>();
                    break;

                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("Kind")) {

                        int type = Integer.valueOf(xpp.nextText());
                        switch (type) {
                            case Constants.CURVESHAPE:
                                shape = new CurveShape();
                                break;
                            case Constants.LINESHAPE:
                                shape= new LineShape();
                                break;
                            case Constants.CIRCLESHAPE:
                                shape=new CircleShape();
                                break;
                            case Constants.RECTSHAPE:
                                shape=new RectShape();
                                break;
                            case Constants.ERASERSHAPE:
                                shape=new EraserShape();
                                break;
                        }
                    } else if (xpp.getName().equals("Color")) {

                        paintColor=Integer.valueOf(xpp.nextText());

                    } else if (xpp.getName().equals("Width")) {
                        paintWidth=Float.valueOf(xpp.nextText());
                    } else if (xpp.getName().equals("PointList")) {
                        List<Point> pointList = strToPoint(xpp.nextText());
                        Paint paint=new Paint();
                        if (shape.getKind()!=Constants.ERASERSHAPE) {
                            paint.setColor(paintColor);
                        }
                        paint.setStrokeWidth(paintWidth);
                        shape.setmPaint(paint);
                        shape.setmPointList(pointList);
                    }
                    break;

                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    if (xpp.getName().equals("Shape")) { // 判断结束标签元素是否是book
                        mList.add(shape); // 将book添加到books集合
                        shape = null;
                    }
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType=xpp.next();
        }
        return mList;

    }

    private static List<Point> strToPoint(String text) {
        String[] strArr = text.split(",");
        List<Point> pointList = new ArrayList<>();
        for (int i = 0; i < strArr.length; ) {
            Point point = new Point(Float.valueOf(strArr[i]), Float.valueOf(strArr[i + 1]));
            i += 2;
            pointList.add(point);
        }
        return pointList;
    }

    private static String pointToStr(List<Point> points) {
        StringBuffer result = new StringBuffer();
        for (Point p : points) {
            result.append(p.getX() + "," + p.getY() + ",");
        }
        return result.toString();
    }
}
