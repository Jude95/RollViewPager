package com.jude.rollviewpagerdome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;

public class MainActivity extends AppCompatActivity {
    private RollPagerView mRollViewPager;
    private TestLoopAdapter mLoopAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRollViewPager= (RollPagerView) findViewById(R.id.roll_view_pager);
        mRollViewPager.setPlayDelay(1000);
        mRollViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mRollViewPager));
//        mRollViewPager.setAdapter(new TestNomalAdapter());
        mRollViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
        //mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoopAdapter.minus();
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoopAdapter.add();
            }
        });
    }

    private class TestLoopAdapter extends LoopPagerAdapter{
        private int[] imgs = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
        };
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
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return count;
        }

    }

    private class TestNomalAdapter extends StaticPagerAdapter{
        private int[] imgs = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
        };
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
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }


        @Override
        public int getCount() {
            return count;
        }
    }
}
