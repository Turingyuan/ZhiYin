package com.whut.zhiyin.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.whut.zhiyin.readmusic.MusicScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11639 on 2018/4/9.
 */

public class DBUtil {


    SQLiteDatabase database=null;

    DBHelper dbHelper;


    public DBUtil(Context context) {
        dbHelper=new DBHelper(context);
    }



    /**
     * 提供插入musicScore数据
     * @param musicScore
     */
    public void insert(MusicScore musicScore){
        String sqlString="insert into "+DatabaseStatic.TABLE_NAME+" values(?,?,?,?,?,?,?)";

        database=dbHelper.getWritableDatabase();

        Log.d("db author", musicScore.getAuthor());

        database.execSQL(sqlString,new Object[]{musicScore.getMusicScoreName(),musicScore.getAuthor(),musicScore.getMajor(),musicScore.getNumOfBeatsPerBar(),
                musicScore.getWhichNoteInABeat(),musicScore.getBarsPerMin(),musicScore.getSyllableNamesList().toString()});

    }

    /**
     * 更新乐谱数据
     * @param musicScore
     */
    public void update(MusicScore musicScore) {
        String sqlString = "update " + DatabaseStatic.TABLE_NAME + "set " + DatabaseStatic.author + "=?," + DatabaseStatic.major + "=?,"+
                DatabaseStatic.numOfBeatsPerBar+"=?,"+DatabaseStatic.whichNoteInABeat+"=?,"+DatabaseStatic.barsPerMin+"=?,"+DatabaseStatic.syllableNamesList+
                "=? where "+DatabaseStatic.musicScoreName+"=?";

        database=dbHelper.getWritableDatabase();


        database.execSQL(sqlString,new Object[]{musicScore.getAuthor(),musicScore.getMajor(),musicScore.getNumOfBeatsPerBar(),
                musicScore.getWhichNoteInABeat(),musicScore.getBarsPerMin(),musicScore.getSyllableNamesList().toString(),musicScore.getMusicScoreName()});
    }



    /**
     * 删除乐谱数据
     */
    public void delete(String name) {
        String sqlString="delete from "+DatabaseStatic.TABLE_NAME+" where "+DatabaseStatic.musicScoreName+" = ?";
        database=dbHelper.getWritableDatabase();
        database.execSQL(sqlString, new Object[]{name});
    }


    /**
     * 搜索
     */
    public List<MusicScore> searchAll(){
        List<MusicScore> list=new ArrayList<MusicScore>();

        database=dbHelper.getReadableDatabase();

        String sqlString="select * from "+DatabaseStatic.TABLE_NAME;
        Cursor cursor = database.rawQuery(sqlString, null);


        if (cursor.moveToFirst()) {
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                MusicScore musicScore=new MusicScore();

                musicScore.setMusicScoreName(cursor.getString(cursor.getColumnIndex(DatabaseStatic.musicScoreName)));
                musicScore.setAuthor(cursor.getString(cursor.getColumnIndex(DatabaseStatic.author)));
                musicScore.setMajor(cursor.getInt(cursor.getColumnIndex(DatabaseStatic.major)));
                musicScore.setNumOfBeatsPerBar(cursor.getInt(cursor.getColumnIndex(DatabaseStatic.numOfBeatsPerBar)));
                musicScore.setWhichNoteInABeat(cursor.getInt(cursor.getColumnIndex(DatabaseStatic.whichNoteInABeat)));
                musicScore.setBarsPerMin(cursor.getInt(cursor.getColumnIndex(DatabaseStatic.barsPerMin)));
                String string = cursor.getString(cursor.getColumnIndex(DatabaseStatic.syllableNamesList));

                String[] strings = string.split(",|\\[|\\]");

                for(int i=1;i<strings.length;i++) {
                    String string2=strings[i].trim();
                    musicScore.getSyllableNamesList().add(Integer.valueOf(string2));
                }

                list.add(musicScore);

                Log.d("db author", musicScore.getAuthor());

            }
        }




        return list;
    }


}
