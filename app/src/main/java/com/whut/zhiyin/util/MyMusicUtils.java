package com.whut.zhiyin.util;

/**
 * Created by 袁帅 on 2018/1/24.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


import com.whut.zhiyin.R;

import java.util.HashMap;

public class MyMusicUtils {
    // 资源文件
    int Music[] = {R.raw.c52,R.raw.d54,R.raw.e56,R.raw.f57,R.raw.g59,R.raw.a61,R.raw.b63,
            R.raw.c40,R.raw.d42,R.raw.e44,R.raw.f45,R.raw.g47,R.raw.a49,R.raw.b51,
            R.raw.c28,R.raw.d30,R.raw.e32,R.raw.f33,R.raw.g35,R.raw.a37,R.raw.b39, };
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;

    /**
     *
     * @param context
     *            用于soundpool.load
     *
     *            播放声音的编号
     */
    public MyMusicUtils(Context context) {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < Music.length; i++) {
            soundPoolMap.put(i, soundPool.load(context, Music[i], 1));
        }
    }

    public int soundPlay(int no) {
        return soundPool.play(soundPoolMap.get(no), 100, 100, 1, 0, 1.0f);
    }

    public int soundOver() {
        return soundPool.play(soundPoolMap.get(1), 100, 100, 1, 0, 1.0f);
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        soundPool.release();
        super.finalize();
    }
}
