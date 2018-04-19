package com.whut.zhiyin.util;

import java.util.List;

/**
 * Created by 11639 on 2018/4/9.
 */

public class DatabaseStatic {
    public final static String DATABASE_NAME = "zhiYin.db";

    public final static String TABLE_NAME="musicScore";

    public final static int DATABASE_VERSION = 1;

    /** 乐谱名 */
    public final static String musicScoreName="musicScoreName";
    /** 乐谱的作者 */
    public final static String author="author";

    /** 乐谱的基本大调如C大调 */
    public final static String major="major";

    /** 这个变量表示 以   几分音符为一拍  ，即2/4表示 中的4，以4分音符为一拍，不管能不能实现，先这样设计 */
    public final static String whichNoteInABeat="whichNoteInABeat";

    /**
     * 每小节的节拍数
     */
    public final static String numOfBeatsPerBar = "numOfBeatsPerBar";

    /**
     * 乐谱数据
     */
    public final static String syllableNamesList="syllableNamesList";

    /** 每分钟的节拍数,标识每节拍的时长 */
    public final static String barsPerMin="barsPerMin";

}
