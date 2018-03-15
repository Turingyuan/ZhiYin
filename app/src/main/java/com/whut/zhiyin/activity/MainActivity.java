package com.whut.zhiyin.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.whut.zhiyin.R;
import com.whut.zhiyin.readmusic.MusicScore;
import com.whut.zhiyin.readmusic.MusicScoreLayout;
import com.whut.zhiyin.readmusic.ReadMusicHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    ActionBarDrawerToggle mToggle;


    // TODO: 2018/2/1 这是一个测试数据的临时存储类，将来会被删除
    private List<Integer> syllableList=new ArrayList<Integer>();
    {
        for(int i=0;i<184;i++){
            syllableList.add(i);
        }
    }

    // 这个Activity是乐谱绘制的控制组件
    //而MusicScore类是信息传递类
    //这是由这个Activity所起的控制作用决定
    private MusicScoreLayout musicScoreLayout;//乐谱绘制类
    private MusicScore musicScore;//乐谱数据存储类

    private ReadMusicHandler readMusicHandler;

    //布局里的控件
    @BindView(R.id.readmusicButton)
    Button readMusicButton;
    @BindView(R.id.saveButtonInReadMusic) Button saveButtonInReadMusic;
    @BindView(R.id.importButtonInReadMusic) Button importButtonInReadMusic;
    @BindView(R.id.playMusicButtonInReadMusic) Button playMusicMuttonInReadMusic;

    @BindView(R.id.beginButtonInReadMusicControl) Button beginButtonInReadMusicControl;
    @BindView(R.id.endButtonInReadMusicControl) Button endButtonInReadMusicControl;
    @BindView(R.id.finishButtonInReadMusicControl) Button finishButtonInReadMusicControl;

    @BindView(R.id.ldoImageView)
    ImageView ldoImageView;
    @BindView(R.id.lrdoImageView)  ImageView lrdoImageView;
    @BindView(R.id.lreImageView) ImageView lreImageView;
    @BindView(R.id.lrreImageView) ImageView lrreImageView;
    @BindView(R.id.lmiImageView) ImageView lmiImageView;
    @BindView(R.id.lfaImageView) ImageView lfaImageView;
    @BindView(R.id.lrfaImageView) ImageView lrfaImageView;
    @BindView(R.id.lsolImageView) ImageView lsolImageView;
    @BindView(R.id.lrsolImageView) ImageView lrsolImageView;
    @BindView(R.id.llaImageView) ImageView llaImageView;
    @BindView(R.id.lrlaImageView) ImageView lrlaImageView;
    @BindView(R.id.lsiImageView) ImageView lsiImageView;
    @BindView(R.id.doImageView) ImageView doImageView;
    @BindView(R.id.rdoImageView) ImageView rdoImageView;
    @BindView(R.id.reImageView) ImageView reImageView;
    @BindView(R.id.rreImageView) ImageView rreImageView;
    @BindView(R.id.miImageView) ImageView miImageView;
    @BindView(R.id.faImageView) ImageView faImageView;
    @BindView(R.id.rfaImageView) ImageView rfaImageView;
    @BindView(R.id.solImageView) ImageView solImageView;
    @BindView(R.id.rsolImageView) ImageView rsolImageView;
    @BindView(R.id.laImageView) ImageView laImageView;
    @BindView(R.id.rlaImageView) ImageView rlaImageView;
    @BindView(R.id.siImageView) ImageView siImageView;
    @BindView(R.id.hdoImageView) ImageView hdoImageView;
    @BindView(R.id.hrdoImageView) ImageView hrdoImageView;
    @BindView(R.id.hreImageView) ImageView hreImageView;
    @BindView(R.id.hrreImageView) ImageView hrreImageView;
    @BindView(R.id.hmiImageView) ImageView hmiImageView;
    @BindView(R.id.hfaImageView) ImageView hfaImageView;
    @BindView(R.id.hrfaImageView) ImageView hrfaImageView;
    @BindView(R.id.hsolImageView) ImageView hsolImageView;
    @BindView(R.id.hrsolImageView) ImageView hrsolImageView;
    @BindView(R.id.hlaImageView) ImageView hlaImageView;
    @BindView(R.id.hrlaImageView) ImageView hrlaImageView;
    @BindView(R.id.hsiImageView) ImageView hsiImageView;
    @BindView(R.id.readMusicOperateAreaLayout)
    LinearLayout readMusicOperateAreaLayout;
    @BindView(R.id.keyBoardLayout)
    TableLayout keyBoardLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        musicScoreLayout = new MusicScoreLayout(this);
        musicScore=musicScoreLayout.getMusicScore();

        readMusicHandler = new ReadMusicHandler(this, musicScoreLayout);

        ButterKnife.bind(this);
    }
    private void init(){
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout=findViewById(R.id.drawer_layout);
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close);
        mToggle.syncState();
        drawerLayout.addDrawerListener(mToggle);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_play:
                intent=new Intent(this,PlayMusicActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_detect_play:
                intent=new Intent(this,DetectPlayActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    /**
     * 按键响应事件
     *
     * @param view
     */
    @OnClick({R.id.readmusicButton, R.id.saveButtonInReadMusic, R.id.importButtonInReadMusic, R.id.playMusicButtonInReadMusic,
            R.id.ldoImageView, R.id.lrdoImageView, R.id.lreImageView, R.id.lrreImageView, R.id.lmiImageView, R.id.lfaImageView, R.id.lrfaImageView,
            R.id.lsolImageView, R.id.lrsolImageView, R.id.llaImageView, R.id.lrlaImageView, R.id.lsiImageView, R.id.doImageView, R.id.rdoImageView,
            R.id.reImageView, R.id.rreImageView, R.id.miImageView, R.id.faImageView, R.id.rfaImageView, R.id.solImageView, R.id.rsolImageView, R.id.laImageView,
            R.id.rlaImageView, R.id.siImageView, R.id.hdoImageView, R.id.hrdoImageView, R.id.hreImageView, R.id.hrreImageView, R.id.hmiImageView, R.id.hfaImageView,
            R.id.hrfaImageView, R.id.hsolImageView, R.id.hrsolImageView, R.id.hlaImageView, R.id.hrlaImageView, R.id.hsiImageView,
            R.id.beginButtonInReadMusicControl, R.id.endButtonInReadMusicControl, R.id.finishButtonInReadMusicControl})
    public void onClick(View view) {
        // TODO: 2018/1/31 按钮相应事件，这种方法比较简洁
        switch (view.getId()) {
            case R.id.readmusicButton: {
                /*keyBoardLayout.setVisibility(View.INVISIBLE);
                readMusicOperateAreaLayout.setVisibility(View.VISIBLE);*/
                break;
            }
            case R.id.saveButtonInReadMusic: {
                Toast.makeText(this, "保存功能仍需实现", Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().toString(), "保存功能");
                break;
            }
            case R.id.importButtonInReadMusic: {
                Toast.makeText(this, "导入功能仍需实现", Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().toString(), "导入功能");
                break;
            }
            case R.id.playMusicButtonInReadMusic: {
                Toast.makeText(this, "播放功能仍需实现", Toast.LENGTH_SHORT).show();
                Log.d(this.getClass().toString(), "播放功能");
                break;
            }
            case R.id.ldoImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LDO);
                break;
            }
            case R.id.lrdoImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LRDO);
                break;
            }
            case R.id.lreImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LRE);
                break;
            }
            case R.id.lrreImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LRRE);
                break;
            }
            case R.id.lmiImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LMI);
                break;
            }
            case R.id.lfaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LFA);
                break;
            }
            case R.id.lrfaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LRFA);
                break;
            }
            case R.id.lsolImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LSOL);
                break;
            }
            case R.id.lrsolImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LRSOL);
                break;
            }
            case R.id.llaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LLA);
                break;
            }
            case R.id.lrlaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LRLA);
                break;
            }
            case R.id.lsiImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LSI);
                break;
            }
            case R.id.doImageView: {
                musicScoreLayout.addSyllableName(MusicScore.DO);
                break;
            }
            case R.id.rdoImageView: {
                musicScoreLayout.addSyllableName(MusicScore.RDO);
                break;
            }
            case R.id.reImageView: {
                musicScoreLayout.addSyllableName(MusicScore.RE);
                break;
            }
            case R.id.rreImageView: {
                musicScoreLayout.addSyllableName(MusicScore.RRE);
                break;
            }
            case R.id.miImageView: {
                musicScoreLayout.addSyllableName(MusicScore.MI);
                break;
            }
            case R.id.faImageView: {
                musicScoreLayout.addSyllableName(MusicScore.FA);
                break;
            }
            case R.id.rfaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.RFA);
                break;
            }
            case R.id.solImageView: {
                musicScoreLayout.addSyllableName(MusicScore.SOL);
                break;
            }
            case R.id.rsolImageView: {
                musicScoreLayout.addSyllableName(MusicScore.RSOL);
                break;
            }
            case R.id.laImageView: {
                musicScoreLayout.addSyllableName(MusicScore.LA);
                break;
            }
            case R.id.rlaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.RLA);
                break;
            }
            case R.id.siImageView: {
                musicScoreLayout.addSyllableName(MusicScore.SI);
                break;
            }
            case R.id.hdoImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HDO);
                break;
            }
            case R.id.hrdoImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HRDO);
                break;
            }
            case R.id.hreImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HRE);
                break;
            }
            case R.id.hrreImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HRRE);
                break;
            }
            case R.id.hmiImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HMI);
                break;
            }
            case R.id.hfaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HFA);
                break;
            }
            case R.id.hrfaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HRFA);
                break;
            }
            case R.id.hsolImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HSOL);
                break;
            }
            case R.id.hrsolImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HRSOL);
                break;
            }
            case R.id.hlaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HLA);
                break;
            }
            case R.id.hrlaImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HRLA);
                break;
            }
            case R.id.hsiImageView: {
                musicScoreLayout.addSyllableName(MusicScore.HSI);
                break;
            }
            case R.id.beginButtonInReadMusicControl: {
                // TODO: 2018/2/2 听音绘谱的 开始 按钮开始录音操作，并实时绘制音波，分析音乐特征，并绘制乐谱
                readMusicHandler.start();
                break;
            }
            case R.id.endButtonInReadMusicControl: {
                // TODO: 2018/2/2 听音绘谱的完成操作
                readMusicHandler.stop();
                break;
            }
            case R.id.finishButtonInReadMusicControl: {
                readMusicHandler.finish();
                //界面显示控制
                /*readMusicOperateAreaLayout.setVisibility(View.INVISIBLE);
                keyBoardLayout.setVisibility(View.VISIBLE);*/
                break;
            }
            default: {
                break;
            }
        }
    }

}
