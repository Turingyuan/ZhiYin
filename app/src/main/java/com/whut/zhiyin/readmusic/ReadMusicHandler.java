package com.whut.zhiyin.readmusic;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**这是一个听音识谱的控制类
 *ReadMusicHandler readmusicHandler=new ReadMusicHandler(editText);
 * readmusicHandler.start();//开始听音绘谱，包括开始录音线程，开始绘制乐谱，开始绘制实时绘制音波
 * readmusicHandler.stop();//结束听音绘谱
 * readmusicHandler.finish();//完成听音绘制谱，对乐谱进行自动小节划分等待
 * Created by 11639 on 2018/3/8.
 */

public class ReadMusicHandler {

    /**
     * 当前部件工件的Activity
     */
    Activity activity;

    /**
     * 显示控件
     */
    MusicScoreLayout musicScoreLayout;


    /**
     * 全局乐谱类
     */
    MusicScore musicScore;

    /**
     *录音控制类
     */
    AudioRecoderHandler audioRecoderHandler;

    /**
     * 数据包接收容器
     */
    LinkedBlockingQueue<Object> dataQueue;

    public ReadMusicHandler(Activity activity1,MusicScoreLayout musicScoreLayout1) {
        activity=activity1;
        musicScoreLayout=musicScoreLayout1;
        musicScore = musicScoreLayout.getMusicScore();
        dataQueue = new LinkedBlockingQueue<>();
    }

    /**
     * 开始听音绘谱，包括开始录音线程，开始绘制乐谱，开始绘制实时绘制音波
     */
    public void start(){

        /**
         * 每次点击开始都会新建一个对象
         */
        audioRecoderHandler = new AudioRecoderHandler(activity);

        /**
         * 开始录音并绘制音波
         */
        audioRecoderHandler.startRecord(new AudioRecoderHandler.AudioRecordingCallback() {
            @Override
            public void onRecording(short[] data) {
                //传回来的数据是没有问题的
                synchronized (dataQueue) {
                    dataQueue.add(data);//取出收到的数据包
                }
            }

            @Override
            public void onStopRecord(String savePath) throws IOException {

            }
        });

        /**
         * 开启监听数据线程
         * 进行音乐特征计算分析
         * 并绘制乐谱
         */
        CalculateTask calculateTask = new CalculateTask();
        calculateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    /**
     * 结束听音绘谱
     */
    public void stop(){

        /**
         * 只有对象已经被建立的时候才能使用
         */
        if (audioRecoderHandler != null) {
            audioRecoderHandler.stopRecord();
        }
    }

    /**
     * 完成听音绘制谱，对乐谱进行自动小节划分等待
     */
    public void finish(){
        musicScore.barAdjust();
        musicScoreLayout.setSyllableNameList(musicScore);
    }


    /**
     * 计算分析任务类，后台
     * CalculateTask calculateTask=new CalculateTask();
     * calculateTask.execute();
     * 可以自动检测数据，执行amdf计算，并根据结果，更新UI
     */
    class CalculateTask extends AsyncTask<String, Integer, Void> {


        //计算
        /**
         * 计算amdf
         * @param data
         * @return
         */
        private MusicScore amdf(short[] data) {
            SignalAnalysis signalAnalysis=new SignalAnalysis();
            return signalAnalysis.getFrequencyByADMF(data);
        }


        /**
         * 在另一个线程里执行amdfMode分析计算
         *并将这一个数据包写入文件
         * doingbacking不是一直运行的线程,wtf
         * @return
         */
        @Override
        protected Void doInBackground(String... strings) {
            while (audioRecoderHandler.isRecording() || dataQueue.size() > 0) {
                if (dataQueue.size() > 0) {
                    short[] data = null;

                    /**
                     * 接收一个数据包
                     */
                    synchronized (dataQueue) {
                        data = (short[]) dataQueue.poll();
                    }

                    //执行计算
                    MusicScore musicScore1 = amdf(data);
                    musicScore1.syllableNameMap();

                    //计算amdf
                    synchronized (musicScore) {
                        musicScore.mergeMusicScore(musicScore1);

                    }

                    //更新UI，必须调用这个函数来更新UI
                    publishProgress();

                }
            }
            return null;
        }



        /**
         * 更新UI
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            synchronized (musicScore) {
                musicScoreLayout.setSyllableNameList(musicScore);
            }
            super.onProgressUpdate(values);
        }
    }

}
