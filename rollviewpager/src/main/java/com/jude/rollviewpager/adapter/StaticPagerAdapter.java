package com.jude.rollviewpager.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * 静态存储的Adapter。概念参照{@link android.support.v4.app.FragmentStatePagerAdapter}
 * view添加进去就不管了，View长在，内存不再。
 * <p>Subclasses only need to implement {@link #getView(ViewGroup,int)}
 * and {@link #getCount()} to have a working adapter.
 *
 */
public abstract class StaticPagerAdapter extends PagerAdapter {
    private ArrayList<View> mViewList = new ArrayList<>();

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

	@Override
	public void notifyDataSetChanged() {
        mViewList.clear();
        super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
        View itemView = findViewByPosition(container,position);
        container.addView(itemView);
        onBind(itemView,position);
		return itemView;
	}

    private View findViewByPosition(ViewGroup container,int position){
        for (View view : mViewList) {
            if (((int)view.getTag()) == position&&view.getParent()==null){
                return view;
            }
        }
        View view = getView(container,position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }


    public void onBind(View view,int position){
    }

	public abstract View getView(ViewGroup container, int position);

}
