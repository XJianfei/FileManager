package com.xjf.filedialog;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * µØÖ·À¸ GalleryµÄadapter
 * */
public class TextGalleryAdapter{
	private static String tag = "TextGalleryAdapter";
	private static boolean D = FileManager.D;
	
	private Context context;
	private int currentPosition = 0;
	private Resources res;
	private String absolutePath;
	@SuppressWarnings("unused")
	private final int TEXT_WIDTH;
	private final int TEXT_SIZE;
	private final int TEXT_HEIGHT;
	@SuppressWarnings("unused")
	private final int TEXT_MAX_WIDTH ;
	private final int TEXT_MIN_WIDTH ;
	private final float PIX_SCALE;
	private static final int TEXT_COLOR = 0xff05294d;
	private static final int CUREENT_TEXT_COLOR = 0xffffffff;
	private HorizontalListView vg = null;
	private String[] pathBar;
	
	private OnLongClickListener lcl = null;
	public static final void dbg(String msg) {
		//if (D) {
		//	Log.d(tag, msg);
		//}
		FileManager.dbg(msg);
	}
	
	public TextGalleryAdapter(Context ct, String[] path, HorizontalListView v,
			DataChangedListener l){
		vg = v;
		context = ct;
		res = context.getResources();
		PIX_SCALE = res.getDisplayMetrics().density;
		TEXT_HEIGHT = (int)(32*PIX_SCALE+0.5f);
		TEXT_MAX_WIDTH = (int)(350*PIX_SCALE+0.5f);
		TEXT_MIN_WIDTH = (int)(30*PIX_SCALE+0.5f);
		TEXT_SIZE = (int)(30*PIX_SCALE+0.5f); 
		TEXT_WIDTH  = (int)(15*PIX_SCALE+0.5f);
		xDataChangedListener = l;
		pathBar = path;
		refreshPath();
	}

    public void setPathArray(String[] s) { pathBar = s;}
	public int getCurrentPosition() {return currentPosition;}
	public void setCurrentPosition(int i) {
		if (currentPosition == i)
			return;
		TextView v = (TextView) xDataChangedListener.getChildAt(currentPosition);
		if (v != null)
			v.setTextColor(TEXT_COLOR);
		v = (TextView) xDataChangedListener.getChildAt(i);
		v.setTextColor(CUREENT_TEXT_COLOR);
		currentPosition = i;
	}
	public String getAbsolutePath() {return absolutePath;}
	public void setAbsolutePath(String path) { absolutePath = path;}
	public void setItemLongClickListener(OnLongClickListener olcl) {
		lcl = olcl;
	}
	
	public String getPath(int position){
		String path = new String("/");
		if (position == 0 ||
				position > pathBar.length) {
			//FileManager.error("(TextGalleryAdapter)getPath: " + position);
			return path;
		}
		for (int i = 1; i < position; i++)
			path = path + pathBar[i] + "/";
		
		return path + pathBar[position];
	}
	
	/** Refresh path, reset {@code paths}*/
	public void refreshPath(){
		TextView v;
		int i;
		for (i = 0; i < pathBar.length; i++) {
			v = (TextView) xDataChangedListener.getChildAt(i);
			if (v == null) {
				v = getView(i, v, null, pathBar[i]);
				xDataChangedListener.onViewChange(i, v);
			} else {
				if (v.getText().toString().equals(pathBar[i])) continue;
				v.setText(pathBar[i]);
			}
		}
		while ((v = xDataChangedListener.getChildAt(i)) != null) {
			if (i >= pathBar.length || !v.getText().toString().equals(pathBar[i])) {
				do {
					vg.removeLayoutViewAt(i);
				} while (xDataChangedListener.getChildAt(i) != null);
			}
			i++;
		}
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return pathBar.length;
	}

	/**Return String*/
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pathBar[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

	public void notifyDataSetChanged() {
		refreshPath();
	}
	public interface DataChangedListener {
		/**
		 * remove view on position
		 * */
		public void onViewRemove(int position);
		/**
		 * add view on position
		 * */
		public void onViewAdd(int position, TextView v);
		/**
		 * Change view on position
		 * */
		public void onViewChange(int position, TextView v);
		
		public TextView getChildAt(int position);
	}
	private DataChangedListener xDataChangedListener = null;
	public void setDataChangedListener(DataChangedListener d) {
		xDataChangedListener = d;
	}

	View.OnClickListener itemOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.setBackgroundDrawable(null);
			vg.performItemClick(v, vg.indexOfLayoutChild(v));
		}
	};
	
	OnFocusChangeListener itemFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (hasFocus) {
				//v.setBackgroundColor(Color.GREEN);
				v.setBackgroundResource(R.drawable.grid_selector_background_pressed);
			} else {
				v.setBackgroundDrawable(null);
			}
		}
	};
	View.OnTouchListener itemOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			long time = event.getDownTime();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// v.setBackgroundColor(Color.GREEN);
				v.setBackgroundResource(R.drawable.grid_selector_background_pressed);

				return false;
			case MotionEvent.ACTION_UP:
				vg.performItemClick(v, vg.indexOfLayoutChild(v));
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
				v.setBackgroundDrawable(null);
				break;
			default:
				break;
			}
			return false;
		}
	};
	public TextView getView(int position, TextView convertView, 
			ViewGroup parent, String text) {
		// TODO Auto-generated method stub
		TextView v = (TextView)convertView;
		//Log.d(tag, "gallery adapter getView, position:" + position);
		if (v == null){
			v = new TextView(context);
    		v.setSingleLine(true);
    		v.setTextColor(Color.GRAY);
    		v.setEllipsize(TruncateAt.MARQUEE);
    		v.setMaxWidth(TEXT_MAX_WIDTH);
    		v.setMinWidth(TEXT_MIN_WIDTH);
    		v.setTextSize(TEXT_SIZE);
    		v.setGravity(Gravity.CENTER);

    		v.setOnClickListener(itemOnClickListener);
    		v.setOnTouchListener(itemOnTouchListener);
    		v.setOnFocusChangeListener(itemFocusChangeListener);
    		//v.setOnLongClickListener(vg.getLongClickListener());
    		//v.setBackgroundResource(R.drawable.path_text_bg);
    		v.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, TEXT_HEIGHT));
    		v.setPadding(10, 0, 10, 0);
    		v.setFocusable(true);
    		v.setClickable(true);
		}
		v.setText(text);
		return v;
	}

}
