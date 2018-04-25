package com.whut.zhiyin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whut.zhiyin.JavaBean.MusicTeachVideo;
import com.whut.zhiyin.R;
import com.whut.zhiyin.adapter.MusicVideoAdapter;
import com.whut.zhiyin.model.FoodModelImpl;
import com.whut.zhiyin.model.MusicVideoModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowMusicTeachVideoActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.listview)
    ListView listview;
    private MusicVideoModel mFoodVideoModel = new MusicVideoModel();
    private MusicVideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_music_teach_video);
        ButterKnife.bind(this);
        title.setText("视频教学");
        mFoodVideoModel.getGeneralFoodsVideoItem(new FoodModelImpl.BaseListener() {
            @Override
            public void getSuccess(Object o) {
                List<MusicTeachVideo> list = (List<MusicTeachVideo>) o;
                Log.i("TAG", "getSuccess: "+list.get(0).getFoodname());
                mAdapter = new MusicVideoAdapter(list);
                listview.setAdapter(mAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ShowMusicTeachVideoActivity.this, MusicTeachVideoActivity.class);
                        MusicTeachVideo item = (MusicTeachVideo) mAdapter.getItem(position);
                        intent.putExtra("URL", item.getFoodvideo().getUrl());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void getFailure() {

            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
