package com.jude.rollviewpager.hintview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jude.rollviewpager.HintView;

public abstract class ShapeHintView extends LinearLayout implements HintView {
	private ImageView[] mDots;
	private int length = 0;
	private int lastPosition = 0;
	
	private Drawable dot_normal;
	private Drawable dot_focus;
	
	public ShapeHintView(Context context){
		super(context);
	}
	
	public ShapeHintView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


    public abstract Drawable makeFocusDrawable();

    public abstract Drawable makeNormalDrawable();

	@Override
	public void initView(int length, int gravity) {
        removeAllViews();
		lastPosition = 0;
		setOrientation(HORIZONTAL);
		switch (gravity) {
		case 0:
			setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
			break;
		case 1:
			setGravity(Gravity.CENTER);
			break;
		case 2:
			setGravity(Gravity.RIGHT| Gravity.CENTER_VERTICAL);
			break;
		}
		
		this.length = length;
		mDots = new ImageView[length];
		
		dot_focus = makeFocusDrawable();
		dot_normal = makeNormalDrawable();
		
        for (int i = 0; i < length; i++) {  
        	mDots[i]=new ImageView(getContext());
        	LayoutParams dotlp = new LayoutParams(
        			LayoutParams.WRAP_CONTENT,
        			LayoutParams.WRAP_CONTENT);
        	dotlp.setMargins(10, 0, 10, 0); 
        	mDots[i].setLayoutParams(dotlp);
        	mDots[i].setBackgroundDrawable(dot_normal);
        	addView(mDots[i]);
        }

        setCurrent(0);
	}

	@Override
	public void setCurrent(int current) {
		if (current < 0 || current > length - 1) {  
            return;  
        } 
        mDots[lastPosition].setBackgroundDrawable(dot_normal);
        mDots[current].setBackgroundDrawable(dot_focus);
        lastPosition = current;  
	}
}
