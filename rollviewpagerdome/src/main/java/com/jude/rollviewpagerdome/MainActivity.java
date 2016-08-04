package com.jude.rollviewpagerdome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;

public class MainActivity extends AppCompatActivity {
    private RollPagerView mLoopViewPager;
    private RollPagerView mNormalViewPager;
    private TestLoopAdapter mLoopAdapter;
    private TestNomalAdapter mNormalAdapter;

    public static final String[] imgs = {
            "http://pic.500px.me/picurl/vcg5da48ce9497b91f9c81c17958d4f882e?code=e165fb4d228d4402",
            "http://pic.500px.me/picurl/49431365352e4e94936d4562a7fbc74a---jpg?code=647e8e97cd219143",
            "http://pic.500px.me/picurl/vcgd5d3cfc7257da293f5d2686eec1068d1?code=2597028fc68bd766",
            "http://pic.500px.me/picurl/vcg1aa807a1b8bd1369e4f983e555d5b23b?code=c0c4bb78458e5503",
            "http://pic.500px.me/picurl/vcg9327737b7b040770e4f983e555d5b23b?code=7567a9d1acc29f24"
    };

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



        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoopAdapter.minus();
                mNormalAdapter.minus();
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoopAdapter.add();
                mNormalAdapter.add();
            }
        });


    }


    private class TestLoopAdapter extends LoopPagerAdapter{

        private int count = imgs.length;

        public void add(){
            Log.i("RollViewPager","Add");
            count++;
            if (count>imgs.length)count = imgs.length;
            notifyDataSetChanged();
        }
        public void minus(){
            Log.i("RollViewPager","Minus");
            count--;
            if (count<1)count=1;
            notifyDataSetChanged();
        }

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            Log.i("RollViewPager","getView:"+imgs[position%count]);

            ImageView view = new ImageView(container.getContext());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("RollViewPager","onClick");
                }
            });
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(MainActivity.this).load(imgs[position%count])
                    .into(view);
            return view;
        }

        @Override
        public int getRealCount() {
            return count;
        }

    }

    private class TestNomalAdapter extends StaticPagerAdapter{

        private int count = imgs.length;

        public void add(){
            Log.i("RollViewPager","Add");
            count++;
            if (count>imgs.length)count = imgs.length;
            notifyDataSetChanged();
        }
        public void minus(){
            Log.i("RollViewPager","Minus");
            count--;
            if (count<1)count=1;
            notifyDataSetChanged();
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(MainActivity.this).load(imgs[position%count]).into(view);
            return view;
        }


        @Override
        public int getCount() {
            return count;
        }
    }
}
