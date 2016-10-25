package com.seewo.note.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author silei
 * @file NoteSQLiteOpenHelper.java
 * @brief SQLite管理工具类
 * @date 2016/10/18
 */
public class NoteSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String notedb = "create table notes" +
            "(title varchar(50),time varchar(20),fileName varchar(50));";
    //数据库版本
    private static final int VERSION = 1;

    //未指定版本号则默认为1
    public NoteSQLiteOpenHelper(Context context, String name) {
        this(context, name, VERSION);
    }

    public NoteSQLiteOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public NoteSQLiteOpenHelper(Context context, String name, CursorFactory factory,
                                int version) {
        super(context, name, factory,
                version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(notedb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
