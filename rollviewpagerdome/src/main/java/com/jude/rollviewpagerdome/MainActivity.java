package com.jude.rollviewpagerdome;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RollPagerView mLoopViewPager;
    private RollPagerView mNormalViewPager;
    private TestLoopAdapter mLoopAdapter;
    private TestNomalAdapter mNormalAdapter;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoopViewPager= (RollPagerView) findViewById(R.id.loop_view_pager);
        mLoopViewPager.setPlayDelay(1000);
        mLoopViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mLoopViewPager));
        mLoopViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
        //mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);
        mLoopViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
            }
        });

        mNormalViewPager= (RollPagerView) findViewById(R.id.normal_view_pager);
        mNormalViewPager.setPlayDelay(1000);
        mNormalViewPager.setAdapter(mNormalAdapter = new TestNomalAdapter());
        mNormalViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
        mNormalViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = NetUtils.get("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1");
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray strArr = jsonObject.getJSONArray("results");
                    final String[] imgs  = new String[strArr.length()];
                    for (int i = 0; i < strArr.length(); i++) {
                        JSONObject obj = strArr.getJSONObject(i);
                        imgs[i] = obj.getString("url");
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoopAdapter.setImgs(imgs);
                            mNormalAdapter.setImgs(imgs);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private class TestLoopAdapter extends LoopPagerAdapter{
        String[] imgs = new String[0];

        public void setImgs(String[] imgs){
            this.imgs = imgs;
            notifyDataSetChanged();
        }


        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            Log.i("RollViewPager","getView:"+imgs[position]);

            ImageView view = new ImageView(container.getContext());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("RollViewPager","onClick");
                }
            });
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(MainActivity.this).load(imgs[position])
                    .into(view);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }

    }

    private class TestNomalAdapter extends StaticPagerAdapter{
        String[] imgs = new String[0];

        public void setImgs(String[] imgs){
            this.imgs = imgs;
            notifyDataSetChanged();
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(MainActivity.this).load(imgs[position]).into(view);
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }
}
