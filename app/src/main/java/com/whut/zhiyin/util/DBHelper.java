package com.whut.zhiyin.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 11639 on 2018/4/9.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static String CREATE_TABLE = "create table " + DatabaseStatic.TABLE_NAME + "(" +
            DatabaseStatic.musicScoreName + " TEXT PRIMARY KEY," +
            DatabaseStatic.author + " TEXT," +
            DatabaseStatic.major + " TEXT," +
            DatabaseStatic.numOfBeatsPerBar + " INTEGER," +
            DatabaseStatic.whichNoteInABeat + " INTEGER," +
            DatabaseStatic.barsPerMin + " INTEGER," +
            DatabaseStatic.syllableNamesList+" TEXT)";

    private Context context=null;


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        super(context, DatabaseStatic.DATABASE_NAME, null, DatabaseStatic.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("db","创建数据库");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
