package com.jude.rollviewpager.adapter;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.HintView;
import com.jude.rollviewpager.RollPagerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Mr.Jude on 2016/1/9.
 */
public abstract class LoopPagerAdapter extends PagerAdapter{
    private RollPagerView mViewPager;

    private SparseArray<View> mViews;
    
    private class LoopHintViewDelegate implements RollPagerView.HintViewDelegate{
        @Override
        public void setCurrentPosition(int position, HintView hintView) {
            if (hintView!=null&&getRealCount()>0)
                hintView.setCurrent(position%getRealCount());
        }

        @Override
        public void initView(int length, int gravity, HintView hintView) {
            if (hintView!=null)
                hintView.initView(getRealCount(),gravity);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        mViews.clear();
        initPosition();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        initPosition();
    }

    private void initPosition(){
        if (mViewPager.getViewPager().getCurrentItem() == 0&&getRealCount()>0){
            int half = Integer.MAX_VALUE/2;
            int start = half - half%getRealCount();
            setCurrent(start);
        }
    }

    private void setCurrent(int index){
        try {
            Field field = ViewPager.class.getDeclaredField("mCurItem");
            field.setAccessible(true);
            field.set(mViewPager.getViewPager(),index);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public LoopPagerAdapter(RollPagerView viewPager){
        this.mViewPager = viewPager;
        viewPager.setHintViewDelegate(new LoopHintViewDelegate());
        mViews = new SparseArray<>();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = position % getRealCount();
        container.removeView(mViews.get(realPosition));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position % getRealCount();
        View itemView = mViews.get(realPosition) == null ?
             getView(container, realPosition) : mViews.get(realPosition);
        mViews.put(position, itemView);
        return itemView;
    }

    public abstract View getView(ViewGroup container, int position);

    @Deprecated
    @Override
    public final int getCount() {
        return getRealCount()<=0?getRealCount():Integer.MAX_VALUE;
    }

    public abstract int getRealCount();
}
