package com.seewo.note.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.seewo.note.been.NoteItem;
import com.seewo.note.types.Shape;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author silei
 * @file NoteManager.java
 * @brief 存储读取note的工具类
 * @date 2016/10/18
 */

public final class NoteManager {
    public static List<NoteItem> findNoteItem(Context mContext) {
        List<NoteItem> noteItems = new ArrayList<>();
        SQLiteDatabase db = new NoteSQLiteOpenHelper(mContext, "note.db").getReadableDatabase();
        Cursor cursor = db.query("notes", null, null, null, null, null, null);
        NoteItem bean;
        while (cursor.moveToNext()) {
            bean = new NoteItem();
            bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            bean.setModifiedTime(cursor.getString(cursor.getColumnIndex("time")));
            bean.setFileName(cursor.getLong(cursor.getColumnIndex("fileName"))+"");
            noteItems.add(bean);
        }
        cursor.close();
        db.close();
        return noteItems;
    }

    public static void saveNoteItem(Context mContext, String title, List<Shape> shapeList, int workState,String  fileName) {
        SQLiteDatabase db = new NoteSQLiteOpenHelper(mContext, "note.db").getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put("time", format.format(time));
        values.put("title", title);
        switch (workState) {
            case Constants.CREATENOTE:
                values.put("fileName", time);
                db.insert("notes", null, values);
                XmlOperation.saveToXml(shapeList, time + "");
                break;
            case Constants.OPENNOTE:
                String whereClause = "fileName=?";
                String[] whereArgs = new String[]{fileName};
                db.update("notes", values, whereClause, whereArgs);
                XmlOperation.saveToXml(shapeList, fileName + "");
                break;
        }

    }

    public static boolean deleteNote(Context mContext, String fileName) {
        SQLiteDatabase db = new NoteSQLiteOpenHelper(mContext, "note.db").getWritableDatabase();
        String whereClause = "fileName=?";
        String[] whereArgs = new String[]{String.valueOf(fileName)};
        db.delete("notes", whereClause, whereArgs);
        File file = new File(Constants.LOCALDIR + File.separator + fileName+".xml");
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            }
        }
        return true;
    }

    public static List<Shape> findShapesForXml(String fileName){
        try {
            return XmlOperation.parseXmlToShape(Constants.XMLDIR + File.separator + fileName+".xml");
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
