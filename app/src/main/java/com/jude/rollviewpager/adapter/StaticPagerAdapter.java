package com.jude.rollviewpager.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


/**
 * 静态存储的Adapter。概念参照{@link android.support.v4.app.FragmentStatePagerAdapter}
 * 每次都会创建新view，销毁旧View。节省内存消耗性能
 *
 * <p>Subclasses only need to implement {@link #getView(ViewGroup,int)}
 * and {@link #getCount()} to have a working adapter.
 *
 */
public abstract class StaticPagerAdapter extends PagerAdapter {


	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = container.getChildAt(position);
        if(itemView==null){
            itemView = getView(container,position);
            container.addView(itemView);
        }
        onBind(itemView,position);
		return itemView;
	}

    public void onBind(View view,int position){
    }

	public abstract View getView(ViewGroup container, int position);

}
