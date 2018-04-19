package com.whut.zhiyin.activity;

import android.Manifest;
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
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.zhiyin.R;
import com.whut.zhiyin.readmusic.MusicScore;
import com.whut.zhiyin.readmusic.MusicScoreLayout;
import com.whut.zhiyin.readmusic.ReadMusicHandler;
import com.whut.zhiyin.util.DBUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    ActionBarDrawerToggle mToggle;


    // 这个Activity是乐谱绘制的控制组件
    //而MusicScore类是信息传递类
    //这是由这个Activity所起的控制作用决定
    private MusicScoreLayout musicScoreLayout;//乐谱绘制类
    private MusicScore musicScore;//乐谱数据存储类

    private ReadMusicHandler readMusicHandler;


    @BindView(R.id.beginButtonInReadMusicControl) Button beginButtonInReadMusicControl;
    @BindView(R.id.endButtonInReadMusicControl) Button endButtonInReadMusicControl;
    @BindView(R.id.finishButtonInReadMusicControl) Button finishButtonInReadMusicControl;


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



    @BindView(R.id.drawMusicNoteByHandTextView) TextView drawMusicNoteByHandTextView;
    @BindView(R.id.readmusicTextView) TextView readMusicTextView;
    @BindView(R.id.saveTextView) TextView saveTextView;


    @BindView(R.id.readMusicOperateAreaLayout)
    LinearLayout readMusicOperateAreaLayout;
    @BindView(R.id.keyBoardLayout)
    TableLayout keyBoardLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 权限分配
         */
        MainActivityPermissionsDispatcher.applyForPermissionWithPermissionCheck(this);
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


    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void applyForPermission() {
        return;
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
            case R.id.localMusicScore:{
                // TODO: 2018/4/18 本地乐谱 
                intent=new Intent(this,LocalMusicScoreActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.cloudMusicScore:{

                // TODO: 2018/4/18 云端乐谱
                return true;
            }
        }
        return false;
    }

    /**
     * 按键响应事件
     *
     * @param view
     */
    @OnClick({R.id.doImageView, R.id.rdoImageView, R.id.reImageView, R.id.rreImageView, R.id.miImageView, R.id.faImageView, R.id.rfaImageView, R.id.solImageView, R.id.rsolImageView, R.id.laImageView,
            R.id.rlaImageView, R.id.siImageView, R.id.beginButtonInReadMusicControl, R.id.endButtonInReadMusicControl, R.id.finishButtonInReadMusicControl,R.id.readmusicTextView,R.id.drawMusicNoteByHandTextView,R.id.saveTextView})
    public void onClick(View view) {
        // TODO: 2018/1/31 按钮相应事件，这种方法比较简洁
        switch (view.getId()) {
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
                readMusicOperateAreaLayout.setVisibility(View.INVISIBLE);
                keyBoardLayout.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.drawMusicNoteByHandTextView:{
                readMusicOperateAreaLayout.setVisibility(View.INVISIBLE);
                keyBoardLayout.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.readmusicTextView:{
                readMusicOperateAreaLayout.setVisibility(View.VISIBLE);
                keyBoardLayout.setVisibility(View.INVISIBLE);
                break;
            }
            case R.id.saveTextView:{
                // TODO: 2018/4/9 数据保存功能，保存到本地数据库
                DBUtil dbUtil = new DBUtil(this);
                dbUtil.insert(musicScore);
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                break;
            }
        }
    }




}
