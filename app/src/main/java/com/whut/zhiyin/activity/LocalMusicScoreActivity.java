package com.whut.zhiyin.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.whut.zhiyin.R;
import com.whut.zhiyin.readmusic.MusicScore;
import com.whut.zhiyin.util.DBUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalMusicScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music_score);
        ListView localMusicScoreListView=(ListView)findViewById(R.id.localMusicScoreListView);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        DBUtil dbUtil = new DBUtil(this);
        final List<MusicScore> list=dbUtil.searchAll();

        for(int i=0;i<list.size();i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("musicScoreName",list.get(i).getMusicScoreName());
            map.put("musicScoreAuthor", list.get(i).getAuthor());
            listItem.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItem, R.layout.listviewitem,
                new String[]{"musicScoreName", "musicScoreAuthor"},
                new int[]{R.id.musicScoreName, R.id.musicScoreAuthor});
        localMusicScoreListView.setAdapter(simpleAdapter);
        localMusicScoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LocalMusicScoreActivity.this,"你点击了"+list.get(position).getMusicScoreName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
