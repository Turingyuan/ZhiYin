package com.whut.zhiyin.readmusic;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11639 on 2018/3/4.
 */


public class AudioRecoderHandler {

    /**
     * 录音数据单次回调数组最大为多少
     */
    private static int MAX_DATA_LENGTH = 160;

    private AudioRecord audioRecord;//录音对象

    public boolean isRecording() {
        return isRecording;
    }

    private boolean isRecording=false;//标记是否正在录音中
    private int frequence=44100;//采样率 44100
    private int channelInConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;//定义采样通道（过时，但是使用其他的又不行）
    private int audioEncoding=AudioFormat.ENCODING_PCM_16BIT;//定义音频编码（16位）
    private short[] buffer=null;//录制的缓冲数组，这里用的是byte，一般我自己用的时候short
    private File lastCacheFile = null;//记录上次录制的文件名

    private int LEN = 44100 / 4;//包长度
    private short[] distribute=null;//分发缓存数据，当数据装满以后，分发出去


    public static List<Short> list = new ArrayList<>();

    public AudioRecoderHandler(Context context) {
        if (context == null) {
            throw new RuntimeException("Context could not be null!");
        }
    }


    /**
     * 开始录制音频
     * @param audioRecordingCallback 录制过程中的回调函数
     */
    public void startRecord(AudioRecordingCallback audioRecordingCallback) {
        RecordTask task = new RecordTask(audioRecordingCallback);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }


    /**
     * 停止录制
     */
    public void stopRecord(){
        isRecording=false;
    }

    /**
     * 删除上次录制的文件（一般是用户取消发送导致删除上次录制的内容）
     * @return true表示删除成功，false表示删除失败,一般没有上次录制的文件，或者文件已经被删除了
     */
    public boolean deleteLastRecordFile() {
        boolean success=false;
        if (lastCacheFile != null && lastCacheFile.exists()) {
            success=lastCacheFile.delete();
        }
        return success;
    }


    /**
     * 录制音频的任务类
     */
    public class RecordTask extends AsyncTask<String, Integer, String> {


        private AudioRecordingCallback audioRecordingCallback=null;

        public RecordTask(AudioRecordingCallback audioRecordingCallback) {
            this.audioRecordingCallback = audioRecordingCallback;
        }


        @Override
        protected void onPreExecute() {
            //根据定义好的几个配置，来获取合适的缓冲大小
            //int bufferSize=800
            int bufferSize = AudioRecord.getMinBufferSize(frequence, channelInConfig, audioEncoding);
            Log.d("bufferSize", bufferSize + "");


            //实例化AudioRecord
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelInConfig, audioEncoding, bufferSize);

            //定义缓冲数组
            buffer = new short[bufferSize];

            // TODO: 2018/3/4  格式化数组
            distribute = new short[LEN];

            audioRecord.startRecording();//开始录制
            isRecording=true;//设置录制标记为true
        }


        @Override
        protected void onPostExecute(String s) {

            audioRecord=null;
            if (s == null) {
                lastCacheFile=null;
            }else{
                lastCacheFile = new File(s);
            }
            if (audioRecordingCallback != null) {
                try {
                    audioRecordingCallback.onStopRecord(s);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        protected String doInBackground(String... strings) {
            String tempFileName = null;
            int pos=0;//手动维护记录缓存
            int cnt=0;
            while (isRecording) {
                //录制的内容放置到buffer中，result代表存储长度
                int result = audioRecord.read(buffer, 0, buffer.length);

                for(int i=0;i<result;i++) {
                    list.add(buffer[i]);
                    distribute[pos++] = buffer[i];///一级缓存
                    //将数据回调回去
                    //一旦数据满足长度就把它给回调回去
                    //result是数据长度
                    //由于java的数据传送机制，所以必须要新开一个传送数组，否则，后来的录音数据会覆盖之前的数据，这个bug很难修改
                    if (audioRecordingCallback != null && pos == LEN) {
                        short[] disBuffer = new short[distribute.length];//第二级缓存，传送缓存
                        System.arraycopy(distribute, 0, disBuffer, 0, distribute.length);
                        audioRecordingCallback.onRecording(disBuffer);
                        pos=0;//重新开始缓存
                    }
                }

            }

            if (audioRecord != null) {
                audioRecord.stop();
            }
            return tempFileName;
        }


        // TODO: 2018/3/4 如果需要绘制波形，直接在这里进行绘制
        /**
         * 更新UI
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    /**
     * 监听录制过程，用于实时获取录音数据
     */
    public static interface AudioRecordingCallback {
        /**
         * 录音数据获取回调
         * @param data 数据数组对象
         */
        public void onRecording(short[] data);

        /**
         * 录音结束后的回调
         * @param savePath 录音文件的存储路径
         */
        public void onStopRecord(String savePath) throws IOException;

    }

    /**
     * 释放资源
     *
     */
    public void release(){
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
    }
}
