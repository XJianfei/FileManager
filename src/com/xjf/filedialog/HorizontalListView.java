package com.xjf.filedialog;

import java.util.ArrayList;

import com.xjf.filedialog.TextGalleryAdapter.DataChangedListener;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorizontalListView extends HorizontalScrollView {
	Context context;
	LinearLayout layout;
	ArrayList<TextView> items = new ArrayList<TextView>();
	public HorizontalListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public HorizontalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context) {
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setPadding(20, 0, 5, 0);
		addView(layout);
		setHorizontalScrollBarEnabled(false);
        mLongPressRunnable = new Runnable() {  
              
            @Override  
            public void run() {   
                performLongClick();  
            }  
        };  
	}
	
	protected TextGalleryAdapter adapter = null;
	public void setAdapter(TextGalleryAdapter a) {
		adapter = a;
		a.setDataChangedListener(xOnDateChangedListener);
	}

	public void setSelection(int i) {
		adapter.setCurrentPosition(i);
		View v = layout.getChildAt(i);
		if (v == null) {
			return;
		}
		this.smoothScrollTo(v.getLeft(), 0);
		//this.smoothScrollTo(x, y)
	}
	
	OnLongClickListener longClickListener = null;
	public void setOnLongClickListener(OnLongClickListener l) {
		longClickListener = l;
		super.setOnLongClickListener(l);
	}
	public OnLongClickListener getLongClickListener() {
		return longClickListener;
	}
	private int visibleChildCount = 0;
	public DataChangedListener getDataChangedListener() {
		return xOnDateChangedListener;
	}

    //是否移动了  
    private boolean isMoved;  
    //长按的runnable  
    private Runnable mLongPressRunnable;  
    //移动的阈值  
    private static final int TOUCH_SLOP = 20;  
    private int mLastMotionX, mLastMotionY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event){
        int x = (int) event.getX();  
        int y = (int) event.getY();  
          
        switch(event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            mLastMotionX = x;  
            mLastMotionY = y;  
            isMoved = false;  
            postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());  
            break;
        case MotionEvent.ACTION_MOVE:  
            if(isMoved) break;  
            if(Math.abs(mLastMotionX-x) > TOUCH_SLOP   
                    || Math.abs(mLastMotionY-y) > TOUCH_SLOP) {  
                //移动超过阈值，则表示移动了  
                isMoved = true;  
                removeCallbacks(mLongPressRunnable);  
            }  
            break;  
        case MotionEvent.ACTION_UP:  
            //释放了  
            removeCallbacks(mLongPressRunnable);  
            break;  
        }  
		return super.dispatchTouchEvent(event);
	}

	
	/*
	private Rect mTouchFrame = null;

	
	public int pointToPosition(int x, int y) {
		Rect frame = mTouchFrame;
		if (frame == null) {
			mTouchFrame = new Rect();
			frame = mTouchFrame;
		}
		
		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			final View child = getChildAt(i);
			if (child.getVisibility() == View.VISIBLE) {
				child.getHitRect(frame);
				if (frame.contains(x, y)) {
					///return mFirstPosition + i;
				}
			}
		}
		return -1;
	}
	
	*/
	protected OnItemClickListener xOnItemClickListener = null;
	protected DataChangedListener xOnDateChangedListener 
			= new DataChangedListener() {
				
				@Override
				public void onViewRemove(int position) {
					// TODO Auto-generated method stub
					if (layout.getChildAt(position) != null) {
						layout.removeViewAt(position);
						items.remove(position);
					}
				}
				
				@Override
				public void onViewChange(int position, TextView v) {
					// TODO Auto-generated method stub
					//View v2 = HorizontalListView.this.getChildAt(position);
					//v2 = layout.getChildAt(position);
					if (position >= layout.getChildCount()) {
						layout.addView(v);
						items.add(v);
					} 
					/**
					else {
						layout.getChildAt(position).setVisibility(View.VISIBLE);
					}
					visibleChildCount++;*/
				}
				
				@Override
				public void onViewAdd(int position, TextView v) {
					// TODO Auto-generated method stub
					if (position > layout.getChildCount())
						position = layout.getChildCount();
					layout.addView(v, position);
					items.add(v);
					visibleChildCount++;
				}

				@Override
				public TextView getChildAt(int position) {
					// TODO Auto-generated method stub
					if (position >= layout.getChildCount())
						return null;
					return (TextView) layout.getChildAt(position);
				}
			};
	public int indexOfView(View v) {
		return items.indexOf(v);
	}
	public void setOnItemClickListener(OnItemClickListener l) {
		xOnItemClickListener = l;
	}
	public void performItemClick(View view, int position) {
		if (xOnItemClickListener != null) {
			xOnItemClickListener.onItemClick(view, position);
		}
	}
	
	public void performItemLongClickListener(View view, int position) {
		
	}
	
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}
	public int indexOfLayoutChild(View v) {
		return layout.indexOfChild(v);
	}
	public void removeLayoutViewAt(int index) {
		layout.removeViewAt(index);
	}
	
	public int getLayoutChildCount() {
		return layout.getChildCount();
	}
}
