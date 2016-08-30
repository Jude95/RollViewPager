package com.jude.rollviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 支持轮播和提示的的viewpager
 */
public class RollPagerView extends RelativeLayout implements OnPageChangeListener {

	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private OnItemClickListener mOnItemClickListener;
    private GestureDetector mGestureDetector;

	private long mRecentTouchTime;
	//播放延迟
	private int delay;
	
	//hint位置
	private int gravity;
	
	//hint颜色
	private int color;
	
	//hint透明度
	private int alpha;

	private int paddingLeft;
	private int paddingTop;
	private int paddingRight;
	private int paddingBottom;

	private View mHintView;
	private Timer timer;

	public interface HintViewDelegate{
        void setCurrentPosition(int position,HintView hintView);
        void initView(int length, int gravity,HintView hintView);
    }

    private HintViewDelegate mHintViewDelegate = new HintViewDelegate() {
        @Override
        public void setCurrentPosition(int position,HintView hintView) {
            if(hintView!=null)
                hintView.setCurrent(position);
        }

        @Override
        public void initView(int length, int gravity,HintView hintView) {
            if (hintView!=null)
            hintView.initView(length,gravity);
        }
    };


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

		TypedArray type = getContext().obtainStyledAttributes(attrs, R.styleable.RollViewPager);
		gravity = type.getInteger(R.styleable.RollViewPager_rollviewpager_hint_gravity, 1);
		delay = type.getInt(R.styleable.RollViewPager_rollviewpager_play_delay, 0);
		color = type.getColor(R.styleable.RollViewPager_rollviewpager_hint_color, Color.BLACK);
		alpha = type.getInt(R.styleable.RollViewPager_rollviewpager_hint_alpha, 0);
		paddingLeft = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingLeft, 0);
		paddingRight = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingRight, 0);
		paddingTop = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingTop, 0);
		paddingBottom = (int) type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingBottom, Util.dip2px(getContext(),4));

		mViewPager = new ViewPager(getContext());
		mViewPager.setId(R.id.viewpager_inner);
		mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(mViewPager);
		type.recycle();
		initHint(new ColorPointHintView(getContext(),Color.parseColor("#E3AC42"),Color.parseColor("#88ffffff")));
        //手势处理
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemClickListener!=null){
                    if (mAdapter instanceof LoopPagerAdapter){//原谅我写了这么丑的代码
                        mOnItemClickListener.onItemClick(mViewPager.getCurrentItem()%((LoopPagerAdapter) mAdapter).getRealCount());
                    }else {
                        mOnItemClickListener.onItemClick(mViewPager.getCurrentItem());
                    }
                }
                return super.onSingleTapUp(e);
            }
        });
	}

    private final static class TimeTaskHandler extends Handler{
        private WeakReference<RollPagerView> mRollPagerViewWeakReference;

        public TimeTaskHandler(RollPagerView rollPagerView) {
            this.mRollPagerViewWeakReference = new WeakReference<>(rollPagerView);
        }

        @Override
        public void handleMessage(Message msg) {
            RollPagerView rollPagerView = mRollPagerViewWeakReference.get();
            int cur = rollPagerView.getViewPager().getCurrentItem()+1;
            if(cur>=rollPagerView.mAdapter.getCount()){
                cur=0;
            }
            rollPagerView.getViewPager().setCurrentItem(cur);
            rollPagerView.mHintViewDelegate.setCurrentPosition(cur, (HintView) rollPagerView.mHintView);
			if (rollPagerView.mAdapter.getCount()<=1)rollPagerView.stopPlay();

        }
    }
    private TimeTaskHandler mHandler = new TimeTaskHandler(this);

    private static class WeakTimerTask extends TimerTask{
        private WeakReference<RollPagerView> mRollPagerViewWeakReference;

        public WeakTimerTask(RollPagerView mRollPagerView) {
            this.mRollPagerViewWeakReference = new WeakReference<>(mRollPagerView);
        }

        @Override
        public void run() {
            RollPagerView rollPagerView = mRollPagerViewWeakReference.get();
            if (rollPagerView!=null){
                if(rollPagerView.isShown() && System.currentTimeMillis()-rollPagerView.mRecentTouchTime>rollPagerView.delay){
                    rollPagerView.mHandler.sendEmptyMessage(0);
                }
            }else{
                cancel();
            }
        }
    }

	/**
	 * 开始播放
	 * 仅当view正在显示 且 触摸等待时间过后 播放
	 */
	private void startPlay(){
		if(delay<=0||mAdapter==null||mAdapter.getCount()<=1){
			return;
		}
		if (timer!=null){
			timer.cancel();
		}
		timer = new Timer();
		//用一个timer定时设置当前项为下一项
		timer.schedule(new WeakTimerTask(this), delay, delay);
	}

    private void stopPlay(){
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }


    public void setHintViewDelegate(HintViewDelegate delegate){
        this.mHintViewDelegate = delegate;
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
		mHintView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		((View) mHintView).setLayoutParams(lp);

		GradientDrawable gd = new GradientDrawable();
		gd.setColor(color);
		gd.setAlpha(alpha);
		mHintView.setBackgroundDrawable(gd);

        mHintViewDelegate.initView(mAdapter == null ? 0 : mAdapter.getCount(), gravity, (HintView) mHintView);
	}


	/**
	 * 设置viewager滑动动画持续时间
	 * @param during
	 */
	public void setAnimationDurtion(final int during){
		try {
			// viePager平移动画事件
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			Scroller mScroller = new Scroller(getContext(),
					// 动画效果与ViewPager的一致
                    new Interpolator() {
                        public float getInterpolation(float t) {
                            t -= 1.0f;
                            return t * t * t * t * t + 1.0f;
                        }
                    }) {

                @Override
                public void startScroll(int startX, int startY, int dx,
                                        int dy, int duration) {
                    // 如果手工滚动,则加速滚动
                    if (System.currentTimeMillis() - mRecentTouchTime > delay) {
                        duration = during;
                    } else {
                        duration /= 2;
                    }
                    super.startScroll(startX, startY, dx, dy, duration);
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

    public void setPlayDelay(int delay){
        this.delay = delay;
        startPlay();
    }


    public void pause(){
        stopPlay();
    }

    public void resume(){
        startPlay();
    }

    public boolean isPlaying(){
        return timer!=null;
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

	/**
	 * 设置提示view的位置
	 *
	 */
	public void setHintPadding(int left,int top,int right,int bottom){
		paddingLeft = left;
		paddingTop = top;
		paddingRight = right;
		paddingBottom = bottom;
		mHintView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
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

		if (mHintView != null) {
			removeView(mHintView);
		}
		this.mHintView = (View) hintview;
		if (hintview!=null&&hintview instanceof View){
			initHint(hintview);
		}
	}

	/**
	 * 取真正的Viewpager
	 * @return
	 */
	public ViewPager getViewPager() {
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
		if(mHintView!=null) {
			mHintViewDelegate.initView(mAdapter.getCount(), gravity, (HintView) mHintView);
			mHintViewDelegate.setCurrentPosition(mViewPager.getCurrentItem(), (HintView) mHintView);
		}
        startPlay();
    }

	/**
	 * 为了实现触摸时和过后一定时间内不滑动,这里拦截
	 * @param ev
	 * @return
	 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
		mRecentTouchTime = System.currentTimeMillis();
        mGestureDetector.onTouchEvent(ev);
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
        mHintViewDelegate.setCurrentPosition(arg0, (HintView) mHintView);
	}

}
