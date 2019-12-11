package cn.com.unispark.fragment.mine.setting.offlinemap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 自定义标题置顶的ListView
 * 
 * @author 任建飞
 * 
 */
public class PinnedHeaderListView2 extends ListView {

	private View mHeaderView;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private boolean mDrawFlag = true;
	private PinnedHeaderAdapter mPinnedHeaderAdapter;

	public PinnedHeaderListView2(Context context) {
		super(context);
	}

	public PinnedHeaderListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PinnedHeaderListView2(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setPinnedHeader(View pHeader) {
		mHeaderView = pHeader;
		requestLayout();
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

		mPinnedHeaderAdapter = (PinnedHeaderAdapter) adapter;
	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	// MeasureSpec.AT_MOST);
	// super.onMeasure(widthMeasureSpec, expandSpec);
	// if (null != mHeaderView) {
	// measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
	// mMeasuredWidth = mHeaderView.getMeasuredWidth();
	// mMeasuredHeight = mHeaderView.getMeasuredHeight();
	// }
	// }
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		 重写该方法，达到使ListView适应ScrollView的效果
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
//		
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (null != mHeaderView) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mMeasuredWidth = mHeaderView.getMeasuredWidth();
			mMeasuredHeight = mHeaderView.getMeasuredHeight();
		}
	}

	

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (null != mHeaderView) {
			mHeaderView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
			controlPinnedHeader(getFirstVisiblePosition());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (null != mHeaderView && mDrawFlag) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	public void controlPinnedHeader(int position) {
		if (null == mHeaderView) {
			return;
		}

		int pinnedHeaderState = mPinnedHeaderAdapter
				.getPinnedHeaderState(position);
		switch (pinnedHeaderState) {
		case PinnedHeaderAdapter.PINNED_HEADER_GONE:
			mDrawFlag = false;
			break;

		case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE:
			mPinnedHeaderAdapter
					.configurePinnedHeader(mHeaderView, position, 0);
			mDrawFlag = true;
			mHeaderView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
			break;

		case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP:
			mPinnedHeaderAdapter
					.configurePinnedHeader(mHeaderView, position, 0);
			mDrawFlag = true;
			View topItem = getChildAt(0);
			if (null != topItem) {
				int bottom = topItem.getBottom();
				int height = mHeaderView.getHeight();
				int y;
				if (bottom < height) {
					y = bottom - height;
				} else {
					y = 0;
				}

				if (mHeaderView.getTop() != y) {
					mHeaderView.layout(0, y, mMeasuredWidth, mMeasuredHeight
							+ y);
				}

			}
			break;
		}

	}

	public interface PinnedHeaderAdapter {

		public static final int PINNED_HEADER_GONE = 0;

		public static final int PINNED_HEADER_VISIBLE = 1;

		public static final int PINNED_HEADER_PUSHED_UP = 2;

		int getPinnedHeaderState(int position);

		void configurePinnedHeader(View headerView, int position, int alpaha);
	}

}
