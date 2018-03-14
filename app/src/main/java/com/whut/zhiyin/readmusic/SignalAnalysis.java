package com.whut.zhiyin.readmusic; /***********************************************************************
 * Module:  SignalAnalysis.java
 * Author:  11639
 * Purpose: Defines the Class SignalAnalysis
 ***********************************************************************/

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 这是一个工具类，信号处理，对声音信号的加工提取声音特征，如ADMF算法。需要进行端点检测，绘谱从开始端点进行绘制，计算声音频率时，也从开始端点进行计算，直到结束端点 */
public class SignalAnalysis {


    /** 通过ADMF算法计算声音信号基音频率，所需数据都是类里的参数，经端点检测后的声音数据，和采样频率
     *
     * @param signal 输入信号，已经经过端点检测过之后的信号 */
    public MusicScore getFrequencyByADMF(short[] signal) {

        //需要计算的数据长度
        int dataLength=signal.length;

        //准备返回结果
        MusicScore musicScore=new MusicScore();
        for(int i=0;i<dataLength;i++) {
            musicScore.getSignal().add(signal[i]);
        }

        //设置窗函数
        double[] win = {1000};

        //设置帧移
        int inc = dataLength;

        //获取数据
        double[] data = short2Double(signal);

        //消去直流分量
        data = eliminateTheDcComponent(data);

        //辐值归一化
        data = amplitudeNormalization(data);

        //数据分帧，每一行为一帧数据
        double[][] x = enframe(data, win, inc);

        //获取帧长
        int L=x[0].length;

        //获取帧数
        int Fn=x.length;

        // TODO: 2018/3/8 数字带通滤波
        
        /**
         * 计算本节拍时间的
         * 短时平均能量
         * 用于评判当前节拍状态
         */
        double avgEnergy = averageEnergy(x);

        //语音端点检测中
        //短时能量阈值
        double T = 0.05;

        if (avgEnergy >= T) {//设置阈值简化计算
            double[] frequency = null;//每帧频率

            //短时时域分析
            for (int i = 0; i < Fn; i++) {
                frequency = amdfMod(x, MusicScore.sampleRate);
            }
            //musicScore.getSignal().add(mean(frequency));// TODO: 2018/3/8 均值法计算一个节拍时间的频率，另外的方法是，先进行映射计算，用众数法差别当前节拍时间的状态

            Log.d("MODEC", mode(frequency) + "");
            Log.d("MUSICNOTE", fre2musicnote(mode(frequency)) + "");

            musicScore.getSyllableNamesList().add(fre2musicnote(mode(frequency)));//直接完成转换
        } else {
            musicScore.getSyllableNamesList().add(0);//休止符在频率表示中设为0
        }

        return musicScore;//返回结果
    }

    /**
     * 使用map求double的众数，作为当前包的评判结果
     * @param list
     * @return
     */
    public double mode(double[] list) {
        Map<Double, Integer> map = new HashMap<>();
        for(int i=0;i< list.length;i++) {
            if (map.containsKey(list[i])) {
                map.put((Double) list[i], map.get(list[i]) + 1);
            }else{
                map.put(list[i], 1);
            }
        }

        int max = -1;
        double result=0;
        Iterator iterator=map.keySet().iterator();
        while (iterator.hasNext()) {
            Double key=(Double)iterator.next();
            if (map.get(key) > max) {
                result=key;
                max = map.get(key);
            }
        }
        return result;
    }



    /**
     * 将频率表示转为乐谱表示
     * @param frequency
     * @return
     */
    public int fre2musicnote(double frequency) {
        return (int) (12 * (Math.log(frequency / 440) / Math.log(2))) + 49;//A0=1
    }


    /**
     * 将语音数据（short）转换为double类型，提高计算精确度
     * @param data
     * @return
     */
    public double[] short2Double(short[] data) {
        double[] doubles = new double[data.length];
        for(int i=0;i<data.length;i++) {
            doubles[i] = data[i];
        }
        return doubles;
    }

    /**
     * 计算平均能量，根据平均能量来进行分析
     * @param data
     * @return
     */
    public double averageEnergy(double[][] data){
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                sum += (data[i][j] * data[i][j]);
            }
        }
        return sum / (data.length * data[0].length);
    }


   /**
    * 改进的短时平均幅度差函数
    * admfMod 法计算基音频率
    * @param data
    * @param sampleRate
    * @return
    */
   public double[] amdfMod(double[][] data, int sampleRate) {
      int fn=data.length;//帧数
      double[] period = new double[fn];
      int wlen=data[0].length;//窗长，一帧数据个数
      for(int i=0;i<fn;i++) {
         double[] R0 = averageMagnitudeDifferenceFunction(data[i]);//计算一帧的amdf

         //求AMDF中最大值和对应位置
         double Rmax=Double.MIN_VALUE;//记录最大值
         int Nmax=0;//记录最大值的位置
         for(int j=0;j<R0.length;j++) {
            if (R0[j] > Rmax) {
               Nmax = j;
               Rmax = R0[j];
            }
         }

         //进行线性变换
         double[] R = new double[wlen];
         for(int j=0;j<wlen;j++) {
            R[j] = Rmax * (wlen - j) / ((wlen - Nmax)*1.0) - R0[j];
         }

         //求出最大值
         int lmin=sampleRate/500;
         int lmax=sampleRate/60;

         Rmax=Double.MIN_VALUE;//记录最大值
         int T=0;//记录最大值的位置
         for(int j=lmin;j<lmax;j++) {
            if (R[j] > Rmax) {
               Rmax = R[j];
               T=j;
            }
         }
          int T0 = T;// TODO: 2018/2/28 这里的原理需要探索
         period[i] = T0;//给出了该帧的基间周期

      }

       for(int i=0;i<period.length;i++) {
          Log.d("period",period[i]+" ");
       }
       System.out.println();

      //计算频率 f=1/t
       double timePerDot = 1 / (sampleRate * 1.0);
      double[] frequency = new double[fn];
      for(int i=0;i<fn;i++) {
          frequency[i] = 1 / (period[i] * timePerDot);
      }
      return frequency;
   }


   /**
    * 计算一帧数据的admf
    * @param u 数据源
    * @return
    */
   public double[] averageMagnitudeDifferenceFunction(double[] u) {
      int wlen=u.length;
      double[] R = new double[wlen];
      for(int i=0;i<wlen;i++) {
         R[i] = amplitudeDifference(u, i);
      }
      return R;
   }



   /**
    * 计算每个校点的幅度差再累加
    * @param u 数据源
    * @param k 偏移量
    * @return
    */
   public double amplitudeDifference(double[] u,int k) {
      double sum=0;
      for(int i=0;i<u.length-k;i++) {
         sum = sum + Math.abs(u[k+i]-u[i]);
      }
      return sum;
   }



   /**
    * 计算数据的均值
    * @param data 语音数据
    * @return
    */
   public double mean(double[] data) {
      double m=0;
      for(int i=0;i<data.length;i++) {
         m += data[i];
      }
      m/=data.length;
      return m;
   }


   /**
    * 消去数据的直流分量
    * @param data 语音数据
    * @return
    */
   public double[] eliminateTheDcComponent(double[] data){
      double m = mean(data);
      for(int i=0;i<data.length;i++) {
         data[i] = data[i] - m;
      }
      return data;
   }


   /**
    * 取数据绝对值最大值
    * @param data
    * @return
    */
   public double maxAbs(double[] data) {
      double ma=Double.MIN_VALUE;
      for (int i = 0; i < data.length; i++) {
         ma = data[i] > ma ? data[i] : ma;//取最大值
      }
      return ma;
   }


   /**
    * 幅值归一化计算
    * @param data
    * @return
    */
   public double[] amplitudeNormalization(double[] data) {
      double[] mData = new double[data.length];
      double mAbs = (double) maxAbs(data);
      for(int i=0;i< data.length;i++) {
         mData[i]=data[i]/mAbs;
      }
      return mData;
   }



   /**
    * 未加窗
    * 短时时域处理中的分帧函数
    * 测试通过
    * @param signal 语音信号
    * @param win 窗函数
    * @param inc 帧移
    * @return
    */
   public double[][] enframe(double[] signal, double[] win, int inc) {
      int nx=signal.length;//取数据长度
      int nwin=win.length;//取窗长
      int len=0;//帧长
      if (nwin == 1) {//判断窗长是否为1，若为1，即表示没有设窗函数
         len=(int)win[0];//没有设窗函数，帧长=win[0]
      }else{
         len=nwin;//设了窗函数，帧长=窗长
      }

      int nf = (nx - len + inc) / inc;//计算帧数
      double[][] f=new double[nf][len];//开辟空间
      for(int i=0;i<nf;i++) {//遍历分帧
         int startIndex=i*inc;
         for(int j=0;j<len;j++){
            f[i][j] = signal[startIndex + j];
         }
      }
      //TODO 加窗
      return f;
   }


   /**
    * 计算短时平均过零率
    * @param x 经过分帧后的数据
    * @return
    */
   public int[] zeroCrossingRate (double[][] x){
      int fn=x.length;
      int zcr[] = new int[fn];
      for(int i=0;i<zcr.length;i++) {
         zcr[i]=0;
      }
      for(int i=0;i<fn;i++) {
         for(int j=0;j<x[0].length-1;j++) {
            if (x[i][j] * x[i][j + 1] < 0) {//判断是否为过零点
               zcr[i]++;
            }
         }
      }
      return zcr;//每一帧的平均过零率
   }

}