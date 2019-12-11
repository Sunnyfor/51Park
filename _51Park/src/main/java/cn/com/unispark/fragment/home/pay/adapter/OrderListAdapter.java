package cn.com.unispark.fragment.home.pay.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.pay.PayFeeActivity;
import cn.com.unispark.fragment.home.pay.PayResultActivity;
import cn.com.unispark.fragment.home.pay.entity.OrderListEntity.DataObject.OrderList;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ReckonUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 功能说明： 账单布局和支付完成布局适配器
 * 日期：	2015年12月14日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年12月14日
 * </pre>
 */
public class OrderListAdapter extends PagerAdapter {
	private Context context;
	private Activity activity;
	public List<OrderList> orderList;
	public List<View> viewList;

	// 停车场名称 // 车牌号 // 停车时长 // 停车费用
	private TextView name_tv, plate_tv, long_tv, park_fee_tv;
	private View line_view;
	private ImageView statusImg;
	private ImageView more_iv;

	// 支付成功//离场时间
	private TextView pay_success_tv, leave_time_tv;

	// 账单布局// 支付成功布局
	private RelativeLayout order_bill_rl, order_pay_rl;

	public OrderListAdapter(Context context, Activity activity,
			List<OrderList> list) {
		this.context = context;
		this.activity = activity;
		this.orderList = list;
		initView();
	}

	public void initView() {
		viewList = new ArrayList<View>();

		for (int i = 0, size = orderList.size(); i < size; i++) {
			View contentView = null;
			
			final int status = orderList.get(i).getStatus();
			
		

			/*
			 * 订单状态：1 待支付 2 停车中 (显示账单)
			 */
//			if (status == 1 || status == 2) {

				contentView = View.inflate(context, R.layout.home_order_bill,
						null);
				
				// 订单状态：停车中、已交费、固定车
				statusImg = (ImageView) contentView.findViewById(R.id.iv_status);
				
				switch (status) {
//				case 1://待支付
//					statusImg.setImageResource(R.drawable.label_order_monthcard);
//					break;
				case 2://停车中
					statusImg.setImageResource(R.drawable.label_order_parking);
					break;
				case 3://待出场改为已缴费
					statusImg.setImageResource(R.drawable.label_order_alreadypay);
					break;
				case 4://固定车
					statusImg.setImageResource(R.drawable.label_order_monthcard);
					break;

				}

				/*
				 * order_bill_rl = (RelativeLayout) contentView
				 * .findViewById(R.id.order_bill_rl);
				 * ViewUtil.setPaddingTop(order_bill_rl, 84);
				 */

				// 停车场名称
				name_tv = (TextView) contentView.findViewById(R.id.name_tv);
				name_tv.setText(orderList.get(i).getParkname());
				ViewUtil.setTextSize(name_tv, 27);
				ViewUtil.setDrawablePadding(name_tv, 18);
				ViewUtil.setMarginTop(name_tv, 76, ViewUtil.RELATIVELAYOUT);
				ViewUtil.setMarginLeft(name_tv, 43, ViewUtil.RELATIVELAYOUT);

				// 车牌号
				plate_tv = (TextView) contentView.findViewById(R.id.plate_tv);
				ViewUtil.setTextSize(plate_tv, 27);
				ViewUtil.setDrawablePadding(plate_tv, 18);
				ViewUtil.setMarginTop(plate_tv, 29, ViewUtil.RELATIVELAYOUT);

				// 首页显示车牌位置，当无车牌时显示手机号
				if (!TextUtils.isEmpty(orderList.get(i).getCarno())) {
					plate_tv.setText(orderList.get(i).getCarno());
				} else {
					Drawable drawable = context.getResources().getDrawable(
							R.drawable.icon_bill_user_phone);
					drawable.setBounds(0, 0, ViewUtil.getWidth(40),
							ViewUtil.getWidth(40));
					plate_tv.setCompoundDrawables(drawable, null, null, null);

					String phone = ParkApplication.getmUserInfo().getUsername();
					if (!TextUtils.isEmpty(phone)) {
						phone = phone.substring(0, 3) + "****"
								+ phone.substring(7, 11);
						plate_tv.setText(phone);
					}

				}

				// 停车时长
				long_tv = (TextView) contentView.findViewById(R.id.long_tv);
				long_tv.setText(orderList.get(i).getParklength());
				ViewUtil.setTextSize(long_tv, 27);
				ViewUtil.setDrawablePadding(long_tv, 18);
				ViewUtil.setMarginTop(long_tv, 29, ViewUtil.RELATIVELAYOUT);

				// 停车费用
				park_fee_tv = (TextView) contentView
						.findViewById(R.id.park_fee_tv);

				park_fee_tv.setText(TextUtils.isEmpty(orderList.get(i)
						.getShparkfee()) == true ? "0.00" : ReckonUtil
						.getMoneyFormat(orderList.get(i).getShparkfee()));

				// ViewUtil.setViewSize(park_fee_tv, 150, 226);
				ViewUtil.setTextSize(park_fee_tv, 44);
				ViewUtil.setDrawablePadding(park_fee_tv, 23);
				// ViewUtil.setMarginTop(park_fee_tv, 92,
				// ViewUtil.RELATIVELAYOUT);

				// 分割线
				line_view = contentView.findViewById(R.id.line_view);
				ViewUtil.setViewSize(line_view, 100, 1);
				ViewUtil.setMarginRight(line_view, 20, ViewUtil.RELATIVELAYOUT);

//			}

			/**
			 * 订单状态：3 待出场(显示支付成功)
			 */
			/*
			 * if (status == 3) {
			 * 
			 * contentView = View.inflate(context, R.layout.home_order_payok,
			 * null);
			 * 
			 * order_pay_rl = (RelativeLayout) contentView
			 * .findViewById(R.id.order_pay_rl);
			 * ViewUtil.setPaddingTop(order_pay_rl, 50);
			 * 
			 * // 支付成功 pay_success_tv = (TextView) contentView
			 * .findViewById(R.id.pay_success_tv);
			 * ViewUtil.setTextSize(pay_success_tv, 30);
			 * 
			 * // 离场时间 leave_time_tv = (TextView) contentView
			 * .findViewById(R.id.leave_time_tv);
			 * ViewUtil.setTextSize(leave_time_tv, 20);
			 * ViewUtil.setMarginTop(leave_time_tv, 30,
			 * ViewUtil.RELATIVELAYOUT);
			 * 
			 * // 动态将出场时间改为红色放大效果 if
			 * (!TextUtils.isEmpty(orderList.get(i).getExitnote())) { String
			 * exitnote = "提示：" + orderList.get(i).getExitnote();
			 * SpannableStringBuilder style = new SpannableStringBuilder(
			 * exitnote); style.setSpan( new
			 * ForegroundColorSpan(Color.parseColor("#ec4f39")), 6, 13,
			 * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); style.setSpan(new
			 * AbsoluteSizeSpan(ViewUtil.getWidth(24), false), 6, 13,
			 * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			 * 
			 * leave_time_tv.setText(style); }
			 * 
			 * }
			 */

			// 更多按钮
			more_iv = (ImageView) contentView.findViewById(R.id.more_iv);
			ViewUtil.setMarginLeft(more_iv, 20, ViewUtil.RELATIVELAYOUT);
			ViewUtil.setMarginRight(more_iv, 20, ViewUtil.RELATIVELAYOUT);

			viewList.add(contentView);

			/*
			 * 注册点击事件
			 */
			contentView.setTag(i);
//			if (status == 4) {
//				contentView.setClickable(false);
//				return;
//			}
			contentView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int position = ((Integer) v.getTag());
					String orderNo = orderList.get(position)
							.getOrderno();
					
					
					LogUtil.i("点击订单号："+ orderList.get(position)
							.getOrderno());
					if (status == 1 || status == 2 || status == 4) {
						ToolUtil.IntentClass(activity, PayFeeActivity.class,
								"orderNo",orderNo,false);
					} else if (status == 3) {
						ToolUtil.IntentClass(activity, PayResultActivity.class,
								false);
					}

					MobclickAgent.onEvent(activity, "home_ParkingFee_click");
				}
			});

		}
	}

	@Override
	public int getCount() {
		return orderList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {

		view.addView(viewList.get(position));

		return viewList.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
