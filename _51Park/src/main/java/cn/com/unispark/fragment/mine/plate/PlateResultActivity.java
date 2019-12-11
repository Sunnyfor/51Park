package cn.com.unispark.fragment.mine.plate;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 车牌审核界面
 * 功能说明：
 * 添加车牌号
 * 日期：	2015年11月19日
 * 开发者：	任建飞
 * </pre>
 */
public class PlateResultActivity extends BaseActivity {

	private TextView titleText;
	private LinearLayout backLLayout;
	private TextView messageTextView;
	private ImageView image_tishi;
	private TextView tishiTextView;
	private TextView tishi2TextView;
	private TextView phoneTextView;
	private LinearLayout tishi_top;
	private ImageView tishi_image;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.plate_result_main);
	}

	@Override
	public void initView() {
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("找回车牌");
		// 返回按钮,点击进入主页
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		image_tishi = (ImageView) findViewById(R.id.image_tishi);
		ViewUtil.setMarginTop(image_tishi, 78, ViewUtil.LINEARLAYOUT);
		messageTextView = (TextView) findViewById(R.id.message);
		ViewUtil.setMarginTop(messageTextView, 32, ViewUtil.LINEARLAYOUT);
		ViewUtil.setTextSize(messageTextView, 30);
		String msg = getIntent().getStringExtra("msg");
		if (!TextUtils.isEmpty(msg)) {
			messageTextView.setText(msg);
		}

		tishi_top = (LinearLayout) findViewById(R.id.tishi_top);
		ViewUtil.setMarginTop(tishi_top, 82, ViewUtil.LINEARLAYOUT);
		tishi_image = (ImageView) findViewById(R.id.tishi_image);
		ViewUtil.setMarginLeft(tishi_image, 26, ViewUtil.LINEARLAYOUT);

		tishiTextView = (TextView) findViewById(R.id.tishi_tv);
		ViewUtil.setTextSize(tishiTextView, 26);
		tishi2TextView = (TextView) findViewById(R.id.tishi2);
		ViewUtil.setTextSize(tishi2TextView, 26);
		ViewUtil.setMarginLeft(tishi2TextView, 26, ViewUtil.LINEARLAYOUT);
		// 客服电话
		phoneTextView = (TextView) findViewById(R.id.phone);
		phoneTextView.setOnClickListener(this);
		ViewUtil.setTextSize(phoneTextView, 26);
		SpannableString noYhText = new SpannableString("400-700-5151");
		phoneTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		phoneTextView.setText(noYhText, TextView.BufferType.SPANNABLE);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.phone:
			String dhh = "4007095151";
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ dhh));
			startActivity(intent);
			break;

		}
	}

}
