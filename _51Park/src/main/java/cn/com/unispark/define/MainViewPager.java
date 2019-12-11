package cn.com.unispark.define;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 让viewpager不可滑动
 * 
 * @author renjianfei
 *
 */
public class MainViewPager extends ViewPager {

	private boolean isCanScroll = false;

	public MainViewPager(Context context) {
		super(context);
	}

	public MainViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			return false;
		} else {
			return super.onTouchEvent(arg0);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			return false;
		} else {
			return super.onInterceptTouchEvent(arg0);
		}
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}

}
