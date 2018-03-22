package com.whut.zhiyin.readmusic; /***********************************************************************
 * Module:  MusicScore.java
 * Author:  11639
 * Purpose: Defines the Class MusicScore
 ***********************************************************************/

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** 这一个乐谱数据类，是音乐特征提取部分和绘谱部分的交互，包含经过信号分析后的乐谱数据，乐谱的基本特征，如乐谱的基本大调，每小节的节拍数。它向绘部分提供的便是一个唱名序列，绘谱部分只需要将唱名序列里的唱名绘制出来即可，不需变调等转换逻辑，仅仅是了个绘制工具。而其他的属性也会表现到不同的控件里。对音符处理逻辑部分完全由MusicScore类来完成。 */
public class MusicScore {
   /** 乐谱名 */
   private static String musicScoreName;
   /** 乐谱的作者 */
   private static String author;
   /** 乐谱数据，可以直接用来绘谱的 */
   private List<Short> signal;
   /** 乐谱的基本大调如C大调 */
   private static int major;
   /** 这个变量表示 以   几分音符为一拍  ，即2/4表示 中的4，以4分音符为一拍，不管能不能实现，先这样设计 */
   private static int whichNoteInABeat;
    /**
     * 每小节的节拍数
     */
    private static int numOfBeatsPerBar = 4;
   /** 这是一个唱名序列，用于绘制乐谱，其值范围为0、1、2、3、4、5、6、7、8、9、10、11、12、13、14、15、16、17、18、19、20、21、22
    * 其中0表示休止符，1——7为低音表示，8——14为正常音表示，15——21，为高音表示，之后再有特殊的图片，依次往后加
    * 乐谱绘制只需将这个序列里的音符绘制出来即可，而关于谱大调的轮换，乐谱数据的修改，都应该在这个类里面实现
    * TODO 如果仍有音符加入进来，需要设计一个完备的表示体系，原型暂时先这样，当然这只考虑了绘制7个音的情况，之后如果要扩展绘谱的规范性，则需要重新设计，代码实现时要尽量以减小与它的耦合性 */
   private List<Integer> syllableNamesList;
   /** 每分钟的节拍数,标识每节拍的时长 */
   private static int barsPerMin;

   /**
    * 采样频率
    */
   public static int sampleRate=44100;

   protected void finalize() {
      // TODO: implement
   }
   /** 一个八度的音差，即60 */
   public static final int OFFSET = 60;
   /** 这是一个类内常量，升高某个音符的音调 */
   public static final int RISETONE = 1;
   /** 这是一个类内常量，降低某个音的音调 */
   public static final int FALLTONE = 2;
   /** 这是一个类内常量，标志修改某个音符的音调 */
   public static final int CHANGETONE = 3;
   /** 这是一个类内常量，表标删除某个位置的音符 */
   public static final int DELETETONE = 4;
   public static final int ADDTONE = 5;




   public static final int LDO = 3;
   public static final int LRDO = 8;
   /** 低音RE */
   public static final int LRE = 13;
   public static final int LRRE = 18;
   /** 低音mi */
   public static final int LMI = 23;
   /** 低音FA */
   public static final int LFA = 28;
   public static final int LRFA = 33;
   /** 低音SOL */
   public static final int LSOL = 38;
   public static final int LRSOL = 43;
   /** 低音LA */
   public static final int LLA = 48;
   public static final int LRLA = 53;
   /** 低音SI */
   public static final int LSI = 58;
   /** 中音DUO */
   public static final int DO = 63;
   public static final int RDO = 68;
   /** 中音RE */
   public static final int RE = 73;
   public static final int RRE = 78;
   /** 中音MI */
   public static final int MI = 83;
   /** 中音FA */
   public static final int FA = 88;
   public static final int RFA = 93;
   /** 中音SOL */
   public static final int SOL = 98;
   public static final int RSOL = 103;
   /** 中音LA */
   public static final int LA = 108;
   public static final int RLA = 113;
   /** 中音SI */
   public static final int SI = 118;
   /** 高音DO */
   public static final int HDO = 123;
   public static final int HRDO = 128;
   /** 高音RE */
   public static final int HRE = 133;
   public static final int HRRE = 138;
   /** 高音MI */
   public static final int HMI = 143;
   /** 高音FA */
   public static final int HFA = 148;
   public static final int HRFA = 153;
   /** 高音SOL */
   public static final int HSOL = 158;
   public static final int HRSOL = 163;
   /** 高音LA */
   public static final int HLA = 168;
   public static final int HRLA = 173;
   /** 高音SI */
   public static final int HSI = 178;

   /** 休止符 */
   public static final int PAUSE = 180;

    /** 小节线标识 */
    public static final int BARLINE = 186;
    /**
     * 不能识别的符号
     */
   public static final int QUESTION=185;

   /** 清除乐谱数据中的BarLine标识，以便重新进行barMap() */
   public void cleanBarLine() {
      //使用迭代器删除BARLINESIGN
         Iterator<Integer> integerIterator=syllableNamesList.iterator();
         while (integerIterator.hasNext()) {
            Integer integer=integerIterator.next();
            if (integer==BARLINE) {
               integerIterator.remove();
            }
         }
   }


    /** syllableNamesList里的数据是经过大调调整过的的 */
    public void barAdjust() {
        barMap();//确定时间分隔线
        List<Integer> list = new ArrayList<>();
        if (syllableNamesList.size() > 0) {
            int cnt = 1;
            int N = syllableNamesList.get(0);
            for (int i = 1; i < syllableNamesList.size(); i++) {
                int temp = syllableNamesList.get(i);
                if (temp == BARLINE) {//如果当前值是BARLINE
                    list.add(N + cnt - 1);
                    list.add(temp);//再将BARLINE加入进去
                    cnt = 1;
                    if (i < syllableNamesList.size() - 1) {
                        N = syllableNamesList.get(++i);//置为BARLINE的下一个值
                    }else{
                        N = 0;
                    }
                } else {//如果当前值不是BARLINE
                    if (N != temp || cnt == 5) {//已经不连续了，或者达到了最大个数5个
                        if (cnt == 5) {//达到5个
                            list.add(N + 3);
                            list.add(N + 0);
                        } else {
                            list.add(N + cnt - 1);
                        }
                        cnt = 1;
                        N = temp;
                    } else {
                        cnt++;
                    }
                }
            }
            syllableNamesList=list;
        }
        //barMap();
    }

   
   /** 实现了按时间进行小节划分
    * 先把里面的BarLine全部都清除掉了，然后根据时值进行小节的重新划分
    * bar 小节，根据mOfBeatsMeasure，
    * 即每小节的节拍数，实现对音符数组的小节划分，
    * 更新sylableNamesList，内部封装方法，无参数 */
   public void barMap() {
       cleanBarLine();//每次barMap之前必须进行一次清除操作，防止乱码
       /**
        *从前往后依次扫描，如果累计值达到numOfBeatsPerBar*4，则添加小节线
        */
       int cnt = 1;
       for (int i = 0; i < syllableNamesList.size(); i++) {
           if (cnt == numOfBeatsPerBar * 4) {
               syllableNamesList.add(++i, new Integer(BARLINE));//之所以 是++i，是因为添加一个之后，i的位置也加1，指向后面那个位置
               cnt = 1;//计数器复位
           } else if (cnt < numOfBeatsPerBar * 4) {
               int temp = syllableNamesList.get(i) % 5;
               if (temp == 4) {
                   cnt += 6;//要对这里进行修改，一+6之后，变得太多，导致没有能达到16个值了，所以后面就没有了：使用拆分法解决这个问题
               } else {
                   cnt += (temp + 1);
               }
           } else {
               i--;//i得回退一个
               int offset = cnt - numOfBeatsPerBar * 4;
               int temp = syllableNamesList.get(i) % 5;
               syllableNamesList.set(i, syllableNamesList.get(i) - offset);
               syllableNamesList.add(i+1, syllableNamesList.get(i) - temp + offset);
               cnt = cnt + temp - offset;
           }
       }
   }
   
   /** 根据输入参数修改SyllableNameList里的值，实现乐谱的修改功能
    * 
    * @param position 这个参数是要修改的乐谱音符的位置
    * @param syllableName 目标乐谱音符的特征值
    * @param type */
   public int changeSyllableNameList(int position, int syllableName, int type) {
       switch (type) {
            case ADDTONE:{
                syllableNamesList.add(position,new Integer(syllableName));
               break;
            }
            case DELETETONE:{
               syllableNamesList.remove(position);
               break;
            }
            case CHANGETONE:{
                syllableNamesList.set(position,new Integer(syllableName));
               break;
            }
            case FALLTONE:{
               syllableNamesList.set(position,new Integer(syllableNamesList.get(position)-OFFSET));
               break;
            }
            case RISETONE:{
                syllableNamesList.set(position, new Integer(syllableNamesList.get(position) + OFFSET));
               break;
            }
            default:{
               break;
            }
         }
         return 0;
   }
   
   /** 这是一个计算唱名的方法，根据singal里的频率和大调major，
    * key0 = 12*log2(y/440)+57;
    * 然后由频率和唱名映射关系来计算乐谱的唱名 */
   public void syllableNameMap() {
      for(int i=0;i<syllableNamesList.size();i++) {
          if (syllableNamesList.get(i) > 0) {
              Log.d("MAJOR", major + "");
              int syllableName = syllableNamesList.get(i) - major + 12;//进行映射

              //如果计算的音符在这个范围内，即是可以表示的，否则定为不可以表示即从低音1到高音7，这是乐谱的表示范围
              if (syllableName >= 1 && syllableName <= 36) {
                  syllableNamesList.set(i, (syllableName - 1) * 5 );//添加四分之一节拍的内容
              } else {
                  syllableNamesList.set(i, QUESTION);
              }
          } else {
              syllableNamesList.set(i, PAUSE);
          }
      }
   }
   
   /** 将两个乐谱数据进行合并，以得到完整的乐谱数据。能够合并的条件是Major和numOfBeatsPerMeasure和beatTime这三项必须相同，返回值表示合并的状态：1--合并成功；2--基本大调不同；3--每小节节拍数不同；4--节拍时间不同
    * 
    * @param sourceMusicScore */
   public int mergeMusicScore( MusicScore sourceMusicScore) {

      //合并乐谱表示
      int len=sourceMusicScore.getSyllableNamesList().size();
      for(int i=0;i<len;i++) {
         syllableNamesList.add(sourceMusicScore.getSyllableNamesList().get(i));
      }

      //合并数据
      len=sourceMusicScore.getSignal().size();
      for(int i=0;i<len;i++) {
         signal.add(sourceMusicScore.getSignal().get(i));
      }
      return 0;
   }
   
   public List<Short> getSignal() {
      return signal;
   }
   
   /** @param newSignal */
   public void setSignal(List<Short> newSignal) {
      signal = newSignal;
   }
   
   public int getMajor() {
      return major;
   }
   
   /** @param newMajor */
   public void setMajor(int newMajor) {
      major = newMajor;
   }
   
   public int getNumOfBeatsPerBar() {
      return numOfBeatsPerBar;
   }
   
   /** @param newNumOfBeatsPerBar */
   public void setNumOfBeatsPerBar(int newNumOfBeatsPerBar) {
      numOfBeatsPerBar = newNumOfBeatsPerBar;
   }
   
   public List<Integer> getSyllableNamesList() {
      return syllableNamesList;
   }
   
   /** @param newSyllableNamesList */
   public void setSyllableNamesList(List<Integer> newSyllableNamesList) {
      syllableNamesList = newSyllableNamesList;
   }
   
   public int getBarsPerMin() {
      return barsPerMin;
   }
   
   /** @param newBarsPerMin */
   public void setBarsPerMin(int newBarsPerMin) {
      barsPerMin = newBarsPerMin;
   }
   
   public String getAuthor() {
      return author;
   }
   
   /** @param newAuthor */
   public void setAuthor(String newAuthor) {
      author = newAuthor;
   }
   
   public String getMusicScoreName() {
      return musicScoreName;
   }
   
   /** @param newMusicScoreName */
   public void setMusicScoreName(String newMusicScoreName) {
      musicScoreName = newMusicScoreName;
   }
   
   public int getWhichNoteInABeat() {
      return whichNoteInABeat;
   }
   
   /** @param newWhichNoteInABeat */
   public void setWhichNoteInABeat(int newWhichNoteInABeat) {
      whichNoteInABeat = newWhichNoteInABeat;
   }
   
   public MusicScore() {
          author=new String();
          signal = new ArrayList<>();
          syllableNamesList = new ArrayList<>();
   }

}