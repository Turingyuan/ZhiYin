package com.whut.zhiyin.activity;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
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

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DetectPlayActivity extends AppCompatActivity  implements CameraBridgeViewBase.CvCameraViewListener2,Button.OnClickListener{
    private static final String TAG = "OCVSample::Activity";
    private Button button[];// 按钮数组
    private TextView setting;// 设置按钮
    private TextView textView;// 简谱
    private MyMusicUtils utils;// 工具类
    private View parent;// 父视图
    private int buttonId[];// 按钮id
    private boolean havePlayed[];// 是否已经播放了声音，当手指在同一个按钮内滑动，且已经发声，就为true

    private Dialog dialog;
    private View dialogView;
    private Button cancel;
    private Button quit;
    private Spinner spinner;
    private MediaPlayer mediaPlayer01;
    int Music[] = {R.raw.c52,R.raw.d54,R.raw.e56,R.raw.f57,R.raw.g59,R.raw.a61,R.raw.b63,
            R.raw.c40,R.raw.d42,R.raw.e44,R.raw.f45,R.raw.g47,R.raw.a49,R.raw.b51,
            R.raw.c28,R.raw.d30,R.raw.e32,R.raw.f33,R.raw.g35,R.raw.a37,R.raw.b39, };
    Toolbar toolbar;
    //手势识别变量
    private File mCascadeFile;
    private CascadeClassifier haarCascade;
    Button button_puase;
    Button button_continue;
    Mat mRgba;
    private CameraBridgeViewBase mOpenCvCameraView;
    boolean mIsFrontCamera=false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    try{InputStream is=
                            getResources().openRawResource(R.raw.cascade_5);
                        File cascadeDir=getDir("haarcascade", Context.MODE_PRIVATE);
                        mCascadeFile=new File(cascadeDir,"cascade.xml");
                        FileOutputStream os=new FileOutputStream(mCascadeFile);
                        byte[] buffer=new byte[4096];
                        int bytesRead;
                        while((bytesRead=is.read(buffer))!=-1){
                            os.write(buffer,0,bytesRead);
                        }
                        is.close();
                        os.close();
                        haarCascade=new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if(haarCascade.empty()){
                            Log.i("Cascade Error:","级联分类器加载失败");
                            haarCascade=null;
                        }

                    }
                    catch(Exception e)
                    {
                        Log.i("Cascade Error: ","Cascase not found");
                    }
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_play);
        init();
        mediaPlayer01 = new MediaPlayer();

        parent = (View) findViewById(R.id.detectplay_layout);
        parent.setClickable(true);

        button[0].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    play(R.raw.c52);
                    button[0].setBackgroundResource(R.drawable.button_pressed);

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[0].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[1].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[1].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.d54);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[1].setBackgroundResource(R.drawable.button);
                }

                return true;
            }
        });
        button[2].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[2].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.e56);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[2].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[3].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[3].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.f57);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[3].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[4].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[4].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.g59);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[4].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[5].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[5].setBackgroundResource(R.drawable.button_pressed);
                    play(R.raw.a61);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[5].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[6].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.b63);
                    button[6].setBackgroundResource(R.drawable.button_pressed);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[6].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[7].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.c40);
                    button[7].setBackgroundResource(R.drawable.button_pressed);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[7].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[8].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    play(Music[8]);
                    button[8].setBackgroundResource(R.drawable.button_pressed);

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[8].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[9].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[9].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[9]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[9].setBackgroundResource(R.drawable.button);
                }

                return true;
            }
        });
        button[10].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[10].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[10]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[10].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[11].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[11].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[11]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[11].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[12].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[12].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[12]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[12].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[13].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[13].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[13]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[13].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[14].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[14].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[14]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[14].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[15].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[15].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[15]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[15].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[16].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[16].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[16]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[16].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[17].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[17].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[17]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[17].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[18].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[18].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[18]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[18].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[19].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[19].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[19]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[19].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
        button[20].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    button[20].setBackgroundResource(R.drawable.button_pressed);
                    play(Music[20]);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    button[20].setBackgroundResource(R.drawable.button);
                }
                return true;
            }
        });
    }
    private void play(int resource) {
        try {

            mediaPlayer01.release();
            mediaPlayer01 = MediaPlayer.create(DetectPlayActivity.this, resource);
            mediaPlayer01.start();
        } catch (Exception e) {
            Toast.makeText(DetectPlayActivity.this, "发生错误了:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer01 != null) {
            mediaPlayer01.release();
            mediaPlayer01 = null;
        }
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    private void init(){
        toolbar=(Toolbar)findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.java_surface_view);
        //mOpenCvCameraView.setMaxFrameSize(100,100);
        mOpenCvCameraView.setCvCameraViewListener(this);
        button_continue=(Button)findViewById(R.id.btn_detect_continue);
        button_continue.setOnClickListener(this);
        button_puase=(Button)findViewById(R.id.btn_detect_puase);
        button_puase.setOnClickListener(this);

        mediaPlayer01=new MediaPlayer();

        // 新建工具类
        utils = new MyMusicUtils(getApplicationContext());
        textView = (TextView) findViewById(R.id.tv_detect_text);
        textView.setClickable(true);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        // 注意如果想要滚动条时刻显示, 必须加上以下语句:

        // 按钮资源Id
        buttonId = new int[21];
        buttonId[0] = R.id.btn_detect_1;
        buttonId[1] = R.id.btn_detect_2;
        buttonId[2] = R.id.btn_detect_3;
        buttonId[3] = R.id.btn_detect_4;
        buttonId[4] = R.id.btn_detect_5;
        buttonId[5] = R.id.btn_detect_6;
        buttonId[6] = R.id.btn_detect_7;
        buttonId[7] = R.id.btn_detect_11;
        buttonId[8] = R.id.btn_detect_22;
        buttonId[9] = R.id.btn_detect_33;
        buttonId[10] = R.id.btn_detect_44;
        buttonId[11] = R.id.btn_detect_55;
        buttonId[12] = R.id.btn_detect_66;
        buttonId[13] = R.id.btn_detect_77;
        buttonId[14] = R.id.btn_detect_111;
        buttonId[15] = R.id.btn_detect_222;
        buttonId[16] = R.id.btn_detect_333;
        buttonId[17] = R.id.btn_detect_444;
        buttonId[18] = R.id.btn_detect_555;
        buttonId[19] = R.id.btn_detect_666;
        buttonId[20] = R.id.btn_detect_777;

        button = new Button[21];
        havePlayed = new boolean[21];

        // 获取按钮对象
        for (int i = 0; i < button.length; i++) {
            button[i] = (Button) findViewById(buttonId[i]);
            button[i].setClickable(false);
            havePlayed[i] = false;
        }
        //乐谱设置对话框
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_setting,
                null);
        dialog = new Dialog(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom));
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.setTitle("设置");
        cancel = (Button) dialogView.findViewById(R.id.buttoncancel);
        quit = (Button) dialogView.findViewById(R.id.buttonquit);
        spinner = (Spinner) dialogView.findViewById(R.id.spinner1);

        List<String> list = new ArrayList<String>();
        list.add("祝你生日快乐");
        list.add("两只老虎");
        list.add("小星星");
        list.add("铃儿响叮当");
        list.add("世上只有妈妈好");
        list.add("我是一个粉刷匠");
        list.add("上学歌");
        list.add("无");

        spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                R.layout.geci_item, list));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        textView.setText(DetectPlayActivity.this
                                .getString(R.string.happybirthday));
                        break;
                    case 1:
                        textView.setText(DetectPlayActivity.this
                                .getString(R.string.twotigers));
                        break;
                    case 2:
                        textView.setText(DetectPlayActivity.this
                                .getString(R.string.stars));
                        break;
                    case 3:
                        textView.setText(DetectPlayActivity.this.getString(R.string.ding));
                        break;
                    case 4:
                        textView.setText(DetectPlayActivity.this.getString(R.string.mom));
                        break;
                    case 5:
                        textView.setText(DetectPlayActivity.this
                                .getString(R.string.brush));
                        break;
                    case 6:
                        textView.setText(DetectPlayActivity.this
                                .getString(R.string.schooling));
                        break;
                    case 7:
                        textView.setText(DetectPlayActivity.this
                                .getString(R.string.title));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                textView.setText("简谱");
            }
        });
        spinner.setSelection(7);

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DetectPlayActivity.this.finish();
            }
        });

        // 设置界面
        setting = (TextView) findViewById(R.id.tv_appbar_setting);

        setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.show();
            }
        });

    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat mGray = inputFrame.gray();

        mRgba = inputFrame.rgba();
        if (mIsFrontCamera)
        {
            //Core.transpose(mRgba, mRgbaT);
            //Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size());
            Core.flip(mRgba, mRgba, 1);
        }


        //Detecting face in the frame
        MatOfRect faces = new MatOfRect();
        if(haarCascade != null)
        {
            haarCascade.detectMultiScale(mGray, faces, 1.1, 2, 2, new Size(200,200), new Size());
        }

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(100), 3);
        if(!faces.empty()){
            play(R.raw.c52);
        }
        return mRgba;

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_detect_continue:
                mOpenCvCameraView.enableView();
                break;
            case R.id.btn_detect_puase:
                mOpenCvCameraView.disableView();
                break;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this,mLoaderCallback);
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    static{
        if(!OpenCVLoader.initDebug()){
            //handle initialization error
        }
    }

}
