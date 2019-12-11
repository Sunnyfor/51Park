package cn.com.unispark.fragment.mine.msgpush;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.mine.msgpush.entity.MsgDetailEntity;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 消息通知详情页面
 * 日期：	2015年6月23日
 * 开发者：	陈丶泳佐
 * 版本信息：V4.3.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月23日
 * </pre>
 */
public class MsgDetailActivity extends BaseActivity {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;

	// 标题// 发布时间// 内容
	private TextView title_tv, time_tv, content_tv;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.msg_detail_main);

		ParkApplication.isMsgDetail = true;

		// 接收通知列表页面传递过来的id
		parseLoadDetail(getIntent().getStringExtra("id"));
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("无忧公告");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 标题
		title_tv = (TextView) findViewById(R.id.title_tv);
		ViewUtil.setTextSize(title_tv, 30);
//		ViewUtil.setPaddingLeft(title_tv, 30);
//		ViewUtil.setPaddingRight(title_tv, 30);
//		ViewUtil.setMarginTop(title_tv, 26, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMargin(title_tv, 26, 30, 0, 30, ViewUtil.RELATIVELAYOUT);

		// 发布时间
		time_tv = (TextView) findViewById(R.id.time_tv);
		ViewUtil.setTextSize(time_tv, 24);
		ViewUtil.setMarginTop(time_tv, 22, ViewUtil.RELATIVELAYOUT);

		// 内容
		content_tv = (TextView) findViewById(R.id.content_tv);
		ViewUtil.setTextSize(content_tv, 28);
		ViewUtil.setMarginTop(content_tv, 32, ViewUtil.RELATIVELAYOUT);
		ViewUtil.setMarginBottom(content_tv, 100, ViewUtil.RELATIVELAYOUT);

		// 无忧标签
		ImageView image = (ImageView) findViewById(R.id.image);
		ViewUtil.setViewSize(image, 75, 337);
		ViewUtil.setPaddingBottom(image, 10);
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		}
	}

	/**
	 * <pre>
	 * 功能说明：【解析】加载公告详情
	 * 日期：	2015年11月27日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param id
	 * </pre>
	 */
	public void parseLoadDetail(String id) {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("id", id);

		LogUtil.d("【加载公告详情URL】" + Constant.MSG_DETAIL_URL + params);

		httpUtil.parse(httpUtil.POST, Constant.MSG_DETAIL_URL,
				MsgDetailEntity.class, params, new onResult<MsgDetailEntity>() {
					@Override
					public void onSuccess(MsgDetailEntity result) {
						loadingProgress.dismiss();
						title_tv.setText(result.getData().getTitle());
						time_tv.setText(result.getData().getTime());
						content_tv.setText(result.getData().getBody());
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.dismiss();
						ToastUtil.show(errMsg);
					}
				});
	}

}
