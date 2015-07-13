package com.jude.rollviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 支持轮播和提示的的viewpager
 */
public class RollPagerView extends RelativeLayout implements OnPageChangeListener {

	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private long mRecentTouchTime;
	//播放延迟
	private int delay;
	
	//hint位置
	private int gravity;
	
	//hint颜色
	private int color;
	
	//hint透明度
	private int alpha;
	
	
	private View mHintView;
	private Timer timer;


	public RollPagerView(Context context){
		this(context,null);
	}

	public RollPagerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public RollPagerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(attrs);
	}


	/**
	 * 读取提示形式  和   提示位置   和    播放延迟
	 * @param attrs
	 */
	private void initView(AttributeSet attrs){
		if(mViewPager!=null){
			removeView(mViewPager);
		}

		TypedArray type = getContext().obtainStyledAttributes(attrs, R.styleable.viewpager);
		gravity = type.getInteger(R.styleable.viewpager_hint_gravity, 1);
		int mode = type.getInteger(R.styleable.viewpager_hint_mode, -1);
		delay = type.getInt(R.styleable.viewpager_auto_play, 0);
		color = type.getColor(R.styleable.viewpager_hint_color, Color.BLACK);
		alpha = type.getInt(R.styleable.viewpager_hint_alpha, 120);
		mViewPager = new ViewPager(getContext());
		mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(mViewPager);
		type.recycle();

		initHint(mode);
	}


	/**
	 * 开始播放
	 * 仅当view正在显示 且 触摸等待时间过后 播放
	 */
	private void startPlay(){
		if(delay<=0||mAdapter.getCount()<=1){
			return;
		}
		if (timer!=null){
			timer.cancel();
		}
		timer = new Timer();
		//用一个timer定时设置当前项为下一项
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
                if (isShown() && System.currentTimeMillis()-mRecentTouchTime>delay)
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						int cur = mViewPager.getCurrentItem()+1;
						if(cur>=mAdapter.getCount()){
							cur=0;
						}
						mViewPager.setCurrentItem(cur);
                        if(mHintView!=null)
						((HintView)mHintView).setCurrent(cur);
					}
				});
			}
		}, delay, delay);
	}



	/**
	 * 设置提示view
	 * @param mode
	 */
	private void initHint(int mode){
		switch(mode){
		case 1:
			initHint(new TextHintView(getContext()));
			break;
        case 0:
			initHint(new PointHintView(getContext()));
			break;
        default:
            break;
		}
	}

	private void initHint(HintView hintview){
		if(mHintView!=null){
			removeView(mHintView);
		}

		if(hintview == null||!(hintview instanceof HintView)){
			return;
		}

		mHintView = (View) hintview;
		loadHintView();
	}

	/**
	 * 加载hintview的容器
	 */
	private void loadHintView(){
		addView(mHintView);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(getContext(), 24));
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		((View) mHintView).setLayoutParams(lp);

		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		gd.setAlpha(alpha);
		mHintView.setBackgroundDrawable(gd);

		((HintView) mHintView).initView(mAdapter==null?0:mAdapter.getCount(), gravity);
	}


	/**
	 * 设置viewager滑动动画持续时间，貌似有效率问题
	 * @param during
	 */
	public void setAnimationDurtion(final int during){
		try {
			// viePager平移动画事件
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			Scroller mScroller = new Scroller(getContext(),
					new AccelerateInterpolator()){

				@Override
				public void startScroll(int startX, int startY, int dx,
						int dy, int duration) {
					super.startScroll(startX, startY, dx, dy, during);
				}

				@Override
				public void startScroll(int startX, int startY, int dx,
						int dy) {
					super.startScroll(startX, startY, dx, dy,during);
				}
			};
			mField.set(mViewPager, mScroller);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置提示view的透明度
	 * @param alpha 0为全透明  255为实心
	 */
	public void setHintAlpha(int alpha){
		this.alpha = alpha;
		initHint((HintView)mHintView);
	}

	/**
	 * 支持自定义hintview
	 * 只需new一个实现HintView的View传进来
	 * 会自动将你的view添加到本View里面。重新设置LayoutParams。
	 * @param hintview
	 */
	public void setHintView(HintView hintview){
		if (!(hintview instanceof View)){
			throw new IllegalArgumentException("HintView should be a View");
		}
		this.mHintView = (View) hintview;
		initHint(hintview);
	}

	/**
	 * 取真正的Viewpager
	 * @return
	 */
	public ViewPager getmViewPager() {
		return mViewPager;
	}

	/**
	 * 设置Adapter
	 * @param adapter
	 */
	public void setAdapter(PagerAdapter adapter){
		mViewPager.setAdapter(adapter);
		mViewPager.addOnPageChangeListener(this);
		mAdapter = adapter;
		dataSetChanged();
		adapter.registerDataSetObserver(new JPagerObserver());

	}

	/**
	 * 用来实现adapter的notifyDataSetChanged通知HintView变化
	 */
	private class JPagerObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			dataSetChanged();
		}

		@Override
		public void onInvalidated() {
			dataSetChanged();
		}
	}

	private void dataSetChanged(){
		startPlay();
		if(mHintView!=null)
			((HintView) mHintView).initView(mAdapter.getCount(), gravity);
	}


	/**
	 * 为了实现触摸时和过后一定时间内不滑动
	 * @param ev
	 * @return
	 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
		mRecentTouchTime = System.currentTimeMillis();
        return super.dispatchTouchEvent(ev);
    }

    @Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
        if(mHintView!=null)((HintView) mHintView).setCurrent(arg0);
	}

}