package com.whut.zhiyin.readmusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.whut.zhiyin.R;
import com.whut.zhiyin.util.U;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 11639 on 2018/1/31.
 */


/**
 * 与乐谱绘制有关的布局控制与显示类，控制工作由声音的类来完成
 * 整个MusicScoreLayout的控件内容都有这个类来实现
 * 目的是为了提出乐谱显示模块
 * 以便将来导入时调用，分享乐谱等等功能都需要调用该模块
 */
public class MusicScoreLayout implements AdapterView.OnItemClickListener, View.OnFocusChangeListener, AdapterView.OnItemSelectedListener {


    public static final int[] musicNote = U.musicNote;//读取资源

    private Activity activity;  //当前乐谱显示的Activity

    GridView gridView;  //乐谱显示容器，以gridView显示
    Spinner majorSpinner;
    Spinner timeSignatureSpinner;
    Spinner barSpinner;
    EditText authorEditText;
    EditText musicScoreNameEditText;


    /**
     * 这里的MusicScore是最原生的MusicScore,也是全局的MusicScore
     */
    MusicScore musicScore = new MusicScore(); //乐谱数据类,其数据全由MusicScoreLayout进行数据设置


    public MusicScore getMusicScore() {
        return musicScore;
    }

    public void setMusicScore(MusicScore musicScore) {
        this.musicScore = musicScore;
    }


    /**
     * 类的初始化工作
     *
     * @param activity 当前activity,即显示MusicScoreLayout的activity
     */
    public MusicScoreLayout(Activity activity) {
        this.activity = activity;
        bind();
        listen();
    }

    void bind(){
        gridView = activity.findViewById(R.id.musicNoteLayoutGridView);
        majorSpinner = activity.findViewById(R.id.majorSpinner);
        timeSignatureSpinner = activity.findViewById(R.id.timeSignatureSpinner);
        barSpinner = activity.findViewById(R.id.barsSpinner);
        authorEditText = activity.findViewById(R.id.authorEditText);
        musicScoreNameEditText = activity.findViewById(R.id.musicScoreNameEditText);
    }

    void listen(){
        gridView.setOnItemClickListener(this);
        majorSpinner.setOnItemSelectedListener(this);
        timeSignatureSpinner.setOnItemSelectedListener(this);
        barSpinner.setOnItemSelectedListener(this);
        authorEditText.setOnFocusChangeListener(this);
        musicScoreNameEditText.setOnFocusChangeListener(this);
    }



    public Map<String, Integer> map = new HashMap<>();
    {
        map.put("C", 40);
        map.put("D",42);
        map.put("E", 44);
        map.put("F", 45);
        map.put("G", 47);
        map.put("A", 49);
        map.put("B", 51);
    }

    /**
     * 获取根据屏幕上乐谱信息，
     * 获取乐谱基础数据，
     * 这一般是在听音绘谱部分使用
     */
    public void getBaseMusicFinaleData() {
        musicScore.setAuthor(authorEditText.getText().toString());
        musicScore.setMajor(map.get(majorSpinner.getSelectedItem().toString()));//有关音符的转换工作都交由MusicScore类
        String timeSignature = timeSignatureSpinner.getSelectedItem().toString();
        String[] bars = (timeSignature.split("/"));
        musicScore.setNumOfBeatsPerBar(Integer.valueOf(bars[0]));//仅考虑，以4分音符为一拍
        Log.d(this.getClass().toString(),
                "musicScore.numOfBeatsPerBar:   " + musicScore.getNumOfBeatsPerBar());
        musicScore.setBarsPerMin(Integer.valueOf(barSpinner.getSelectedItem().toString()));
    }


    /**
     * TODO 当加入音乐特征提取部分之后 ，这里的代码需要调整
     */
    public void updateMusicNoteLayout() {
        getBaseMusicFinaleData();

        //划分小节线
        musicScore.barMap();

        //设置数据
        SimpleAdapter adapter = new SimpleAdapter(activity, getData(musicScore.getSyllableNamesList()),
                R.layout.musicnote,
                new String[]{"musicnote"},
                new int[]{R.id.musicnote});
        gridView.setAdapter(adapter);
    }

    /**
     * SimplaAdapter的数据来源
     *
     * @param list list为数据集
     * @return
     */
    private List<Map<String, Object>> getData(List<Integer> list) {
        List<Map<String, Object>> datalist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            int index = list.get(i).intValue();
            map.put("musicnote", musicNote[index]);
            datalist.add(map);
        }
        return datalist;
    }


    /**
     * 当音符键盘按下时响应事件
     * 直接在音符后面进行添加
     *
     * @param syllableName 输入的音符
     */
    public void addSyllableName(int syllableName) {
        int position = musicScore.getSyllableNamesList().size();
        musicScore.changeSyllableNameList(position, syllableName, MusicScore.ADDTONE);
        updateMusicNoteLayout();
    }


    /**
     * 由调用它的类来设置musicScore类的音符数据
     *
     * @param musicScore1
     */
    public void setSyllableNameList(MusicScore musicScore1) {
        musicScore=musicScore1;
        updateMusicNoteLayout();
    }

    /**
     * 实现的gridView点击响应函数
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //点击音符之后的修改事件逻辑
        //小节线不能响应菜单
        if (MusicScore.BARLINE != musicScore.getSyllableNamesList().get(i)) {
            showPopupMenu(view, i);
        }
    }

    /**
     * popupMenu显示操作
     *
     * @param view View当前PopupMenu显示的相对View的位置
     */
    private void showPopupMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(activity, view);

        popupMenu.getMenuInflater().inflate(R.menu.musicnoteclickedmenu, popupMenu.getMenu()); // menu布局

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // menu的item点击事件
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // TODO: 2018/1/31 菜单点击操作效果
                switch (menuItem.getItemId()) {
                    case R.id.riseToneMenuItem: {
                        //升调的上限是高音，即高音不能再往上升高了
                        if ((musicScore.getSyllableNamesList().get(position) + MusicScore.OFFSET) < MusicScore.PAUSE) {
                            musicScore.changeSyllableNameList(position, 0, MusicScore.RISETONE);
                            updateMusicNoteLayout();
                        } else {
                            Toast.makeText(activity, "高音不能再往上升了", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case R.id.fallToneMenuItem: {
                        //降调的上限是对应的低音，即低音不能再往下降了
                        if ((musicScore.getSyllableNamesList().get(position) - MusicScore.OFFSET) > MusicScore.BARLINE) {
                            musicScore.changeSyllableNameList(position, 0, MusicScore.FALLTONE);
                            updateMusicNoteLayout();
                        } else {
                            Toast.makeText(activity, "低音不能再往下降了", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    }
                    case R.id.pauseToneMenuItem: {
                        musicScore.changeSyllableNameList(position, MusicScore.PAUSE, MusicScore.CHANGETONE);
                        updateMusicNoteLayout();
                        break;
                    }
                    case R.id.deleteToneMenuItem: {
                        musicScore.changeSyllableNameList(position, 0, MusicScore.DELETETONE);
                        updateMusicNoteLayout();
                        break;
                    }
                    case R.id.addToneMenuItem: {
                        listDialog(position, MusicScore.ADDTONE);
                        break;
                    }
                    case R.id.changeToneMenuItem: {
                        listDialog(position, MusicScore.CHANGETONE);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() { // PopupMenu关闭事件
            @Override
            public void onDismiss(PopupMenu popupMenu) {
                // TODO: 2018/1/31  菜单关闭效果
                Log.d(this.getClass().toString(), "setOnDismissListener:   ");
            }
        });
        popupMenu.show();
    }

    public void listDialog(final int position, final int TYPE) {

        String data[] = {"1", "#1", "2", "#2", "3", "4", "#4", "5", "#5", "6", "#6", "7"};
        //使用用列表对话框实现音符的选取
        AlertDialog.Builder listDialog = new AlertDialog.Builder(activity);//这里如果用getApplicationContext则会有异常
        listDialog.setTitle("提示：请选择中音音符，若有其它需要请再进行更改");
        listDialog.setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //添加和修改的逻辑是，只添加中音1-7这几个音，如果有其他需要，则再升降调，或者改为休止符
                musicScore.changeSyllableNameList(position, (i + 1) * 5 + MusicScore.OFFSET, TYPE);
                updateMusicNoteLayout();
            }
        });
        listDialog.show();
    }


    /**
     * 当Spinner被选中时的操作，奇葩的是下面几个参数
     *
     * @param adapterView 操作的那个Spinner，可以能过apapterView.getId()来获取它的id值
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.barsSpinner: {
                musicScore.setBarsPerMin(Integer.valueOf(barSpinner.getSelectedItem().toString()));
                updateMusicNoteLayout();
                break;
            }
            case R.id.timeSignatureSpinner: {
                String timeSignature = timeSignatureSpinner.getSelectedItem().toString();
                String[] bars = (timeSignature.split("/"));
                musicScore.setNumOfBeatsPerBar(Integer.valueOf(bars[0]));//每小节的节拍数
                musicScore.setWhichNoteInABeat(Integer.valueOf(bars[1]));//以几分音符为一拍
                updateMusicNoteLayout();
                break;
            }
            case R.id.majorSpinner: {
                musicScore.setMajor(map.get(majorSpinner.getSelectedItem().toString()));//有关音符的转换工作都交由MusicScore类
                updateMusicNoteLayout();
                Log.d(this.getClass().toString(), "majorSpinner Major:    " + musicScore.getMajor());
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()) {
            case R.id.musicScoreNameEditText: {
                if (b) {
                    // TODO: 2018/2/2 当焦点不在此EditText时，仍有光标，这个问题需要解决
                    //musicScoreNameEditText.setCursorVisible(true);
                    Log.d(this.getClass().toString(), "TextView 获得焦点");
                } else {
                    musicScore.setMusicScoreName(musicScoreNameEditText.getText().toString());
                    //musicScoreNameEditText.setCursorVisible(false);
                }
            }
            case R.id.authorEditText: {
                if (b) {
                    //authorEditText.setCursorVisible(true);
                    Log.d(this.getClass().toString(), "TextView 获得焦点");
                } else {
                    musicScore.setAuthor(authorEditText.getText().toString());
                    //authorEditText.setCursorVisible(false);
                }
            }
            default: {
                break;
            }

        }
    }

}
