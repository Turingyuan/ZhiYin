package com.whut.zhiyin.util;


import com.whut.zhiyin.R;

/**
 * 这是一个资源类，
 * 一些预处理的资源都在这个类里作为工具使用
 * Created by 11639 on 2018/3/3.
 */

public class U {

    /**
     * 乐谱基本符号图片
     * 注意这里的图片最好放在mipmap文件夹下，最初时放在drawable文件夹下面，读取不出来图片
     */
    public static final int[] musicNote = new int[]{
            //低音区表示
            R.mipmap.ldo1, R.mipmap.ldo2, R.mipmap.ldo3, R.mipmap.ldo4, R.mipmap.ldo6,
            R.mipmap.lrdo1, R.mipmap.lrdo2, R.mipmap.lrdo3, R.mipmap.lrdo4, R.mipmap.lrdo6,
            R.mipmap.lre1, R.mipmap.lre2, R.mipmap.lre3, R.mipmap.lre4, R.mipmap.lre6,
            R.mipmap.lrre1, R.mipmap.lrre2, R.mipmap.lrre3, R.mipmap.lrre4, R.mipmap.lrre6,
            R.mipmap.lmi1, R.mipmap.lmi2, R.mipmap.lmi3, R.mipmap.lmi4, R.mipmap.lmi6,
            R.mipmap.lfa1, R.mipmap.lfa2, R.mipmap.lfa3, R.mipmap.lfa4, R.mipmap.lfa6,
            R.mipmap.lrfa1, R.mipmap.lrfa2, R.mipmap.lrfa3, R.mipmap.lrfa4, R.mipmap.lrfa6,
            R.mipmap.lsol1, R.mipmap.lsol2, R.mipmap.lsol3, R.mipmap.lsol4, R.mipmap.lsol6,
            R.mipmap.lrsol1, R.mipmap.lrsol2, R.mipmap.lrsol3, R.mipmap.lrsol4, R.mipmap.lrsol6,
            R.mipmap.lla1, R.mipmap.lla2, R.mipmap.lla3, R.mipmap.lla4, R.mipmap.lla6,
            R.mipmap.lrla1, R.mipmap.lrla2, R.mipmap.lrla3, R.mipmap.lrla4, R.mipmap.lrla6,
            R.mipmap.lsi1, R.mipmap.lsi2, R.mipmap.lsi3, R.mipmap.lsi4, R.mipmap.lsi6,

            //中音区表示
            R.mipmap.do1, R.mipmap.do2, R.mipmap.do3, R.mipmap.do4, R.mipmap.do6,
            R.mipmap.rdo1, R.mipmap.rdo2, R.mipmap.rdo3, R.mipmap.rdo4, R.mipmap.rdo6,
            R.mipmap.re1, R.mipmap.re2, R.mipmap.re3, R.mipmap.re4, R.mipmap.re6,
            R.mipmap.rre1, R.mipmap.rre2, R.mipmap.rre3, R.mipmap.rre4, R.mipmap.rre6,
            R.mipmap.mi1, R.mipmap.mi2, R.mipmap.mi3, R.mipmap.mi4, R.mipmap.mi6,
            R.mipmap.fa1, R.mipmap.fa2, R.mipmap.fa3, R.mipmap.fa4, R.mipmap.fa6,
            R.mipmap.rfa1, R.mipmap.rfa2, R.mipmap.rfa3, R.mipmap.rfa4, R.mipmap.rfa6,
            R.mipmap.sol1, R.mipmap.sol2, R.mipmap.sol3, R.mipmap.sol4, R.mipmap.sol6,
            R.mipmap.rsol1, R.mipmap.rsol2, R.mipmap.rsol3, R.mipmap.rsol4, R.mipmap.rsol6,
            R.mipmap.la1, R.mipmap.la2, R.mipmap.la3, R.mipmap.la4, R.mipmap.la6,
            R.mipmap.rla1, R.mipmap.rla2, R.mipmap.rla3, R.mipmap.rla4, R.mipmap.rla6,
            R.mipmap.si1, R.mipmap.si2, R.mipmap.si3, R.mipmap.si4, R.mipmap.si6,

            //高音区表示
            R.mipmap.hdo1, R.mipmap.hdo2, R.mipmap.hdo3, R.mipmap.hdo4, R.mipmap.hdo6,
            R.mipmap.hrdo1, R.mipmap.hrdo2, R.mipmap.hrdo3, R.mipmap.hrdo4, R.mipmap.hrdo6,
            R.mipmap.hre1, R.mipmap.hre2, R.mipmap.hre3, R.mipmap.hre4, R.mipmap.hre6,
            R.mipmap.hrre1, R.mipmap.hrre2, R.mipmap.hrre3, R.mipmap.hrre4, R.mipmap.hrre6,
            R.mipmap.hmi1, R.mipmap.hmi2, R.mipmap.hmi3, R.mipmap.hmi4, R.mipmap.hmi6,
            R.mipmap.hfa1, R.mipmap.hfa2, R.mipmap.hfa3, R.mipmap.hfa4, R.mipmap.hfa6,
            R.mipmap.hrfa1, R.mipmap.hrfa2, R.mipmap.hrfa3, R.mipmap.hrfa4, R.mipmap.hrfa6,
            R.mipmap.hsol1, R.mipmap.hsol2, R.mipmap.hsol3, R.mipmap.hsol4, R.mipmap.hsol6,
            R.mipmap.hrsol1, R.mipmap.hrsol2, R.mipmap.hrsol3, R.mipmap.hrsol4, R.mipmap.hrsol6,
            R.mipmap.hla1, R.mipmap.hla2, R.mipmap.hla3, R.mipmap.hla4, R.mipmap.hla6,
            R.mipmap.hrla1, R.mipmap.hrla2, R.mipmap.hrla3, R.mipmap.hrla4, R.mipmap.hrla6,
            R.mipmap.hsi1, R.mipmap.hsi2, R.mipmap.hsi3, R.mipmap.hsi4, R.mipmap.hsi6,

            //休止符
            R.mipmap.pause1,R.mipmap.pause2,R.mipmap.pause3,R.mipmap.pause4,R.mipmap.pause6,

            //问题符号
            R.mipmap.question,

            //小节线
            R.mipmap.barline
            };

}
