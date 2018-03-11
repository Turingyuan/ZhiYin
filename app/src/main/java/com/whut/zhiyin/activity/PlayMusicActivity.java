package com.whut.zhiyin.activity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.zhiyin.R;
import com.whut.zhiyin.util.MyMusicUtils;

public class PlayMusicActivity extends AppCompatActivity {
    private Button button[];// 按钮数组
    private Button setting;// 设置按钮
    private TextView textView;// 简谱
    private MyMusicUtils utils;// 工具类
    private View parent;// 父视图
    private int buttonId[];// 按钮id
    private boolean havePlayed[];// 是否已经播放了声音，当手指在同一个按钮内滑动，且已经发声，就为true
    private View keys;// 按钮们所在的视图
    private int pressedkey[];

    private Dialog dialog;
    private View dialogView;
    private Button cancel;
    private Button quit;
    private Spinner spinner;
    private MediaPlayer mediaPlayer01;
    int Music[] = {R.raw.c52,R.raw.d54,R.raw.e56,R.raw.f57,R.raw.g59,R.raw.a61,R.raw.b63,
            R.raw.c40,R.raw.d42,R.raw.e44,R.raw.f45,R.raw.g47,R.raw.a49,R.raw.b51,
            R.raw.c28,R.raw.d30,R.raw.e32,R.raw.f33,R.raw.g35,R.raw.a37,R.raw.b39, };
    int cnt;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_play_music);
        init();

        parent = (View) findViewById(R.id.parent);
        parent.setClickable(true);
        button[0].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    play(R.raw.c52);
                    button[0].setBackgroundResource(R.drawable.button_pressed);

                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[0].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[1].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[1].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.d54);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[1].setBackgroundResource(R.drawable.button);
                }

                return true;
            }
        });
        button[2].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[2].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.e56);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[2].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[3].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[3].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.f57);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[3].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[4].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[4].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.g59);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[4].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[5].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[5].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.a61);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[5].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[6].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.b63);
                    button[6].setBackgroundResource(R.drawable.button_pressed);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[6].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[7].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.c40);
                    button[7].setBackgroundResource(R.drawable.button_pressed);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[7].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[8].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    play(Music[8]);
                    button[8].setBackgroundResource(R.drawable.button_pressed);

                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[8].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[9].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[9].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[9]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[9].setBackgroundResource(R.drawable.button);
                }

                return true;
            }
        });
        button[10].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[10].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[10]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[10].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[11].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[11].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[11]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[11].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[12].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[12].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[12]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[12].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[13].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[13].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[13]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[13].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[14].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[14].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[14]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[14].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[15].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[15].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[15]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[15].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[16].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[16].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[16]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[16].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[17].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[17].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[17]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[17].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[18].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[18].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[18]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[18].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[19].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[19].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[19]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[19].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[20].setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[20].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[20]);
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    button[20].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });


    }
    private  void init(){
        toolbar=(Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaPlayer01=new MediaPlayer();

        // 新建工具类
        utils = new MyMusicUtils(getApplicationContext());
        textView = (TextView) findViewById(R.id.text);
        textView.setClickable(true);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        // 注意如果想要滚动条时刻显示, 必须加上以下语句:
        textView.setScrollbarFadingEnabled(false);

        // 按钮资源Id
        buttonId = new int[21];
        buttonId[0] = R.id.button1;
        buttonId[1] = R.id.button2;
        buttonId[2] = R.id.button3;
        buttonId[3] = R.id.button4;
        buttonId[4] = R.id.button5;
        buttonId[5] = R.id.button6;
        buttonId[6] = R.id.button7;
        buttonId[7] = R.id.button11;
        buttonId[8] = R.id.button22;
        buttonId[9] = R.id.button33;
        buttonId[10] = R.id.button44;
        buttonId[11] = R.id.button55;
        buttonId[12] = R.id.button66;
        buttonId[13] = R.id.button77;
        buttonId[14] = R.id.button111;
        buttonId[15] = R.id.button222;
        buttonId[16] = R.id.button333;
        buttonId[17] = R.id.button444;
        buttonId[18] = R.id.button555;
        buttonId[19] = R.id.button666;
        buttonId[20] = R.id.button777;

        button = new Button[21];
        havePlayed = new boolean[21];

        // 获取按钮对象
        for (int i = 0; i < button.length; i++) {
            button[i] = (Button) findViewById(buttonId[i]);
            button[i].setClickable(false);
            havePlayed[i] = false;
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void play(int resource) {
        try {

            mediaPlayer01.release();
            mediaPlayer01 = MediaPlayer.create(PlayMusicActivity.this, resource);
            mediaPlayer01.start();
        } catch (Exception e) {
            Toast.makeText(PlayMusicActivity.this, "发生错误了:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer01 != null) {
            mediaPlayer01.release();
            mediaPlayer01 = null;
        }
    }
}
