package cn.com.unispark.fragment.home.map.navigation;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.fragment.home.map.entity.RouteEntity;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.navi.model.AMapNaviGuide;

/**
 * <pre>
 * 功能说明： 【文字描述驾车路线详情】界面，通过拼接文字图标而成。
 * 日期：	2015年11月15日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月16日
 * </pre>
 */
public class NavDetailActivity extends BaseActivity {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	private RelativeLayout content_rl;
	private View line_view;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.nav_detail_mian);
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("驾车路线");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		line_view = findViewById(R.id.line_view);

		content_rl = (RelativeLayout) findViewById(R.id.content_rl);

		/**
		 * 初始化路线数据
		 */
		int size = NavVoiceActivity.naviGuideList.size() + 1;
		for (int i = 0; i < size; i++) {
			LinearLayout llayout = new LinearLayout(this);
			llayout.setOrientation(LinearLayout.HORIZONTAL);
			llayout.setGravity(Gravity.CENTER_VERTICAL);
			llayout.setId(100 + i);
			LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			if (i != 0) {
				layoutParams.addRule(RelativeLayout.BELOW, 100 + i - 1);

				/**
				 * 线条
				 */
				View view = new View(this);
				view.setBackgroundColor(getResources().getColor(
						R.color.gray_line));
				RelativeLayout.LayoutParams viewlayoutParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				viewlayoutParams.width = ViewUtil.getWidth(556);
				viewlayoutParams.height = ViewUtil.getHeight(1);
				viewlayoutParams.addRule(RelativeLayout.BELOW, 100 + i - 1);
				viewlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

				/**
				 * 添加线条
				 */
				content_rl.addView(view, viewlayoutParams);
			}

			/**
			 * 图标
			 */
			ImageView icon = new ImageView(this);
			llayout.addView(icon);
			ViewUtil.setMarginLeft(icon, 25, ViewUtil.LINEARLAYOUT);
			ViewUtil.setMarginRight(icon, 25, ViewUtil.LINEARLAYOUT);

			/**
			 * 显示文字
			 */
			TextView title = new TextView(this);
			title.setTextColor(Color.parseColor("#787878"));
			ViewUtil.setTextSize(title, 30);
			llayout.addView(title);
			ViewUtil.setViewSize(title, 0, 483);
			ViewUtil.setMarginTop(title, 30, ViewUtil.LINEARLAYOUT);
			ViewUtil.setMarginBottom(title, 30, ViewUtil.LINEARLAYOUT);

			/**
			 * 添加item
			 */
			content_rl.addView(llayout, layoutParams);

			if (i == 0) {
				icon.setBackgroundResource(R.drawable.icon_map_start);
				title.setText("从我的位置出发");

			} else {
				RouteEntity routeEntity = getRouteEntity(NavVoiceActivity.naviGuideList
						.get(i - 1));

				/**
				 * 逻辑判断不全，过滤掉其他标志。
				 */
				if (routeEntity == null) {
					llayout.setVisibility(View.GONE);
				} else {
					icon.setBackgroundResource(routeEntity.getIcon());
					title.setText(routeEntity.getTitle());
				}
			}

		}

		content_rl.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {

						content_rl.getViewTreeObserver()
								.removeOnPreDrawListener(this);
						RelativeLayout.LayoutParams layoutParams = (LayoutParams) line_view
								.getLayoutParams();
						layoutParams.leftMargin = ViewUtil.getWidth(50);
						layoutParams.height = content_rl.getHeight()
								- ViewUtil.getHeight(50);
						layoutParams.width = ViewUtil.getWidth(1);
						layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

						return false;
					}
				});

	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}

	}

	/**
	 * <pre>
	 * 功能说明：拼接路线方案
	 * 日期：	2015年11月16日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param guide
	 * @return
	 * </pre>
	 */
	private RouteEntity getRouteEntity(AMapNaviGuide guide) {

		RouteEntity entity = new RouteEntity();
		int iconType = guide.getIconType();
		int resId = 0;
		String title = null;
		if (iconType == 2 || iconType == 4 || iconType == 6) {
			resId = R.drawable.icon_map_left;
			title = "向左行驶" + guide.getLength();

			if (iconType == 4) {
				title = "向左前方行驶" + guide.getLength();
			}

			if (iconType == 6) {
				title = "向左后方行驶" + guide.getLength();
			}

		}

		if (iconType == 3 || iconType == 5 || iconType == 7) {
			resId = R.drawable.icon_map_right;
			title = "向右行驶" + guide.getLength();

			if (iconType == 5) {
				title = "向右前方行驶" + guide.getLength();
			}

			if (iconType == 7) {
				title = "向右后方行驶" + guide.getLength();
			}

		}

		if (iconType == 9) {
			resId = R.drawable.icon_map_straight;
			title = "直行" + guide.getLength();
		}

		if (iconType == 15) {
			resId = R.drawable.icon_map_stop;
			title = "到达终点";
		}

		entity.setIcon(resId);
		entity.setTitle(title + (iconType == 15 ? "" : "米到达" + guide.getName()));

		if (resId == 0 || title == null) {
			return null;
		}

		return entity;

	}

}
