package cn.com.unispark.util;

import cn.com.unispark.application.BaseActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 百分百设置组件大小（以苹果5C的屏幕比例进行设置大小）
 * 
 */
public class ViewSetting {
	private static final int IPHONE5CHEIGHT = 1136; // Iphone5C的屏幕高度像素 - 状态栏高度
	private static final int IPHONE5CWEIGHT = 640; // Iphone5C的屏幕长度像素
	public static final int RELATIVELAYOUT = 1; // 相对布局
	public static final int LINEARLAYOUT = 2; // 线性布局
	public static final int VIEWGROUP = 3;
	public static final int RADIOGROUP = 4;
	public static final int ABSLISTVIEW = 5; // listView中子条目类型

	public static final int HEIGHT_1920 = 1920;
	public static final int WIDTH_1080 = 1080;

	/**
	 * 获取上下边距百分比高度
	 * 
	 * @param px
	 * @return
	 */
	public static int getHeight(int px) {
		int screenHeight = BaseActivity.getScreenHeight();
		if (BaseActivity.getScreenWidth() == WIDTH_1080
				&& screenHeight != HEIGHT_1920) {
			screenHeight = HEIGHT_1920;
		}
		double iPhoneproportion = px / (double) (IPHONE5CHEIGHT / (double) 100);
		int result = (int) ((screenHeight / (double) 100) * iPhoneproportion);
		return result;

	}

	/**
	 * 获取左右边距百分比长度
	 * 
	 * @param px
	 * @return
	 */
	public static int getWidth(int px) {
		double iPhoneproportion = px / (double) (IPHONE5CWEIGHT / (double) 100);
		int result = (int) ((BaseActivity.getScreenWidth() / (double) 100) * iPhoneproportion);
		return result;
	}

	/**
	 * 获取比例视图的长度
	 * 
	 * @param height
	 * @param width
	 * @return
	 */
	public static int getWidth(int height, int width) {

		float proportion = (height / (float) width);

		return (int) (height / proportion);

	}

	/**
	 * 设置TextView文字的大小
	 * 
	 * @param view
	 * @param px
	 */
	public static void setTextSize(TextView view, int px) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getWidth(px));
	}

	/**
	 * 设置editText文字大小
	 * 
	 * @param view
	 * @param px
	 */
	public static void setTextSize(EditText view, int px) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getHeight(px));
	}

	public static void setTextSize(Button button, int px) {
		button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getHeight(px));
	}

	/**
	 * 设置组件大小方法
	 * 
	 * @param view
	 *            组件对象
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 */
	public static void setViewProportionSize(View view, int height, int width) {
		if (view != null) {
			if (view.getLayoutParams() != null) {
				if (height != 0) {
					view.getLayoutParams().height = getHeight(height);
				}

				if (width != 0) {
					view.getLayoutParams().width = getWidth(width);
				}

			} else {

				int mHeight = LayoutParams.WRAP_CONTENT;
				int mWidth = LayoutParams.WRAP_CONTENT;
				if (height != 0) {
					mHeight = getHeight(height);
				}

				if (width != 0) {
					mWidth = getWidth(width);
				}
				LayoutParams params = new LayoutParams(mWidth, mHeight);
				view.setLayoutParams(params);

				LogUtil.e("LayoutParams对象为null，创建新的布局参数");
			}

		} else {
			LogUtil.e("View对象为null，无法获取布局参数");
		}
	}

	/**
	 * 设置组件大小方法
	 * 
	 * @param view
	 *            组件对象
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 */
	public static void setViewSize(View view, int height, int width) {
		if (view != null) {
			if (view.getLayoutParams() != null) {
				if (height != 0) {
					view.getLayoutParams().height = getHeight(height);
				}

				if (width != 0) {
					view.getLayoutParams().width = getWidth(width);
				}

			} else {

				int mHeight = LayoutParams.WRAP_CONTENT;
				int mWidth = LayoutParams.WRAP_CONTENT;
				if (height != 0) {
					mHeight = getHeight(height);
				}

				if (width != 0) {
					mWidth = getWidth(width);
				}
				LayoutParams params = new LayoutParams(mWidth, mHeight);
				view.setLayoutParams(params);

				LogUtil.e("LayoutParams对象为null，创建新的布局参数");
			}

		} else {
			LogUtil.e("View对象为null，无法获取布局参数");
		}
	}

	/**
	 * 
	 * @param view
	 * @param height
	 * @param width
	 */
	public static void setViewSize(View view, int height, int width, int type) {
		if (view != null) {
			if (view.getLayoutParams() != null) {
				if (height != 0) {
					view.getLayoutParams().height = getHeight(height);
				}

				if (width != 0) {
					view.getLayoutParams().width = getWidth(width);
				}

			} else {

				if (type == LINEARLAYOUT) {
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							getWidth(width), getHeight(height));
					view.setLayoutParams(params);
				}

				if (type == RELATIVELAYOUT) {
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							getWidth(width), getHeight(height));
					view.setLayoutParams(params);
				}

				if (type == ABSLISTVIEW) {
					AbsListView.LayoutParams params = new AbsListView.LayoutParams(
							getWidth(width), getHeight(height));
					view.setLayoutParams(params);
				}

				LogUtil.e("LayoutParams对象为null，创建新的布局参数");
			}

		} else {
			LogUtil.e("View对象为null，无法获取布局参数");
		}
	}

	public static void setViewMargin(View view, int left, int top, int right,
			int bottom, int type) {
		if (left != 0) {
			setViewLeftMargin(view, left, type);
		}

		if (top != 0) {
			setViewTopMargin(view, top, type);
		}

		if (right != 0) {
			setViewRightMargin(view, right, type);
		}

		if (bottom != 0) {
			setViewButtomMargin(view, bottom, type);
		}
	}

	/**
	 * 按比例设置组件左边距
	 * 
	 * @param view
	 * @param margin
	 * @param type
	 */
	public static void setViewLeftMargin(View view, int margin, int type) {
		if (type == RELATIVELAYOUT) {

			if (view.getLayoutParams() == null) {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.leftMargin = getWidth(margin);
				view.setLayoutParams(params);
			} else {
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
						.getLayoutParams();
				params.leftMargin = getWidth(margin);
			}

		}
		if (type == LINEARLAYOUT) {

			if (view.getLayoutParams() == null) {
				LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.leftMargin = getWidth(margin);
				view.setLayoutParams(params);
			} else {
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) view
						.getLayoutParams();
				params.leftMargin = getWidth(margin);
			}

		}

		if (type == VIEWGROUP) {
			if (view.getLayoutParams() == null) {
				setViewLeftMargin(view, margin, RELATIVELAYOUT);
			} else {
				LayoutParams layoutParams = view.getLayoutParams();
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.height = layoutParams.height;
				params.width = layoutParams.width;
				params.leftMargin = getWidth(margin);
				view.setLayoutParams(params);
			}
		}

		if (type == RADIOGROUP) {
			if (view.getLayoutParams() == null) {
				RadioGroup.LayoutParams layoutParmas = new RadioGroup.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParmas.leftMargin = getWidth(margin);
				view.setLayoutParams(layoutParmas);
			} else {
				RadioGroup.LayoutParams layoutParams = (android.widget.RadioGroup.LayoutParams) view
						.getLayoutParams();
				layoutParams.leftMargin = getWidth(margin);
			}
		}

	}

	/**
	 * 按比例设置组件右边距
	 * 
	 * @param view
	 * @param margin
	 * @param type
	 */
	public static void setViewRightMargin(View view, int margin, int type) {
		if (type == RELATIVELAYOUT) {
			if (view.getLayoutParams() == null) {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.rightMargin = getWidth(margin);
				view.setLayoutParams(params);
			} else {
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
						.getLayoutParams();
				params.rightMargin = getWidth(margin);
			}
		}
		if (type == LINEARLAYOUT) {

			if (view.getLayoutParams() == null) {
				LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.rightMargin = getWidth(margin);
				view.setLayoutParams(params);
			} else {
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) view
						.getLayoutParams();
				params.rightMargin = getWidth(margin);
			}

		}

		if (type == VIEWGROUP) {
			if (view.getLayoutParams() == null) {
				setViewRightMargin(view, margin, RELATIVELAYOUT);
			} else {
				LayoutParams layoutParams = view.getLayoutParams();
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.height = layoutParams.height;
				params.width = layoutParams.width;
				params.rightMargin = getWidth(margin);
				view.setLayoutParams(params);
			}
		}
	}

	/**
	 * 按比例设置组件的上边距
	 * 
	 * @param view
	 * @param margin
	 * @param type
	 */
	public static void setViewTopMargin(View view, int margin, int type) {
		if (type == RELATIVELAYOUT) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			params.topMargin = getHeight(margin);
		}
		if (type == LINEARLAYOUT) {
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) view
					.getLayoutParams();
			params.topMargin = getHeight(margin);
		}
	}

	/**
	 * 按比例设置组件的下边距
	 * 
	 * @param view
	 * @param margin
	 * @param type
	 */
	public static void setViewButtomMargin(View view, int margin, int type) {

		if (view.getLayoutParams() != null) {

			if (type == RELATIVELAYOUT) {
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
						.getLayoutParams();
				params.bottomMargin = getHeight(margin);
			}
			if (type == LINEARLAYOUT) {
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) view
						.getLayoutParams();
				params.bottomMargin = getHeight(margin);
			}
		} else {
			if (type == RELATIVELAYOUT) {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.bottomMargin = getHeight(margin);
				view.setLayoutParams(params);
			}
			if (type == LINEARLAYOUT) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.bottomMargin = getHeight(margin);
				view.setLayoutParams(params);
			}
		}
	}

	/**
	 * 按比例设置组件左边距
	 * 
	 * @param view
	 * @param margin
	 * @param type
	 */
	public static void setViewLeftPadding(View view, int margin) {
		view.setPadding(getWidth(margin), view.getPaddingTop(),
				view.getPaddingRight(), view.getPaddingBottom());
	}

	public static void setViewButtomPadding(View view, int margin) {
		view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
				view.getPaddingRight(), getHeight(margin));
	}

	public static void setViewRightPadding(View view, int margin) {
		view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
				getWidth(margin), view.getPaddingBottom());
	}

	public static void setViewTopPadding(View view, int margin) {
		view.setPadding(view.getPaddingLeft(), getWidth(margin),
				view.getPaddingRight(), view.getPaddingBottom());
	}

	public static void setViewPadding(View view, int margin) {
		view.setPadding(getWidth(margin), getWidth(margin), getWidth(margin),
				getWidth(margin));
	}

}
