package cn.com.unispark.fragment.mine.vipcard;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.util.ViewUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * <pre>
 * 功能说明：生成用户的二维码
 * 日期：	2015年4月19日
 * 开发者：	任丶建飞
 * 版本信息：V4.2.1
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容： 继承统一父类
 *    修改人员：  陈丶泳佐
 *    修改日期： 2015年8月12日
 * </pre>
 */
public class UserQrActivity extends BaseActivity {

	// 标题栏以及返回按钮
	private TextView titleText;
	private ImageView backImgView;
	private LinearLayout backLLayout;

	/*
	 * 个人名片
	 */
	private LinearLayout person_info_ll;
	private ImageView qrcode_iv;// 二维码
	private TextView name_tv;// 姓名
	private TextView phone_tv;// 手机号
	private TextView prompt_tv;// 提示语

	@Override
	public void setContentLayout() {
		setContentView(R.layout.user_qr_main);
	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("我的二维码");

		// 导航栏返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);
		backImgView = (ImageView) findViewById(R.id.backImgView);
		backImgView.setImageResource(R.drawable.btn_close_red);

		// 个人名片
		person_info_ll = (LinearLayout) findViewById(R.id.person_info_ll);
		ViewUtil.setViewSize(person_info_ll, 760 + 20, 582);
		ViewUtil.setPadding(person_info_ll, 0, 65);
		ViewUtil.setMargin(person_info_ll, 82, 28, 0, 28, ViewUtil.LINEARLAYOUT);

		// 姓名
		name_tv = (TextView) findViewById(R.id.name_tv);
		ViewUtil.setTextSize(name_tv, 30);
		ViewUtil.setMarginTop(name_tv, 10, ViewUtil.LINEARLAYOUT);
		if (!TextUtils.isEmpty(ParkApplication.getmUserInfo().getName())) {
			String name = ParkApplication.getmUserInfo().getName()
					.substring(0, 1)
					+ "** ";
			String sex = ParkApplication.getmUserInfo().getSex() == 2 ? " (女士)"
					: " (先生)";
			name_tv.setText(name + sex);
		} else {
			name_tv.setText(getResources().getString(R.string.app_name));
		}

		// 手机号
		phone_tv = (TextView) findViewById(R.id.phone_tv);
		ViewUtil.setTextSize(phone_tv, 30);
		ViewUtil.setMarginTop(name_tv, 22, ViewUtil.LINEARLAYOUT);

		String phone = ParkApplication.getmUserInfo().getUsername();
		if (!TextUtils.isEmpty(phone)) {
			phone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
			phone_tv.setText(phone);
		} else {
			phone_tv.setVisibility(View.GONE);
		}

		// 生成二维码图片
		qrcode_iv = (ImageView) findViewById(R.id.qrcode_iv);
		qrcode_iv.setBackgroundResource(R.color.blue);
		qrcode_iv.setScaleType(ScaleType.FIT_XY);
		ViewUtil.setViewSize(qrcode_iv, 452 + 88, 452 + 88);
		ViewUtil.setMarginTop(qrcode_iv, 24, ViewUtil.LINEARLAYOUT);
		doCreateQrcode(ParkApplication.getmUserInfo().getQr(), qrcode_iv, 500,
				500);

		// 提示语
		prompt_tv = (TextView) findViewById(R.id.prompt_tv);
		ViewUtil.setTextSize(prompt_tv, 30);
		ViewUtil.setMarginTop(prompt_tv, 28, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginBottom(prompt_tv, 42, ViewUtil.LINEARLAYOUT);
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
	 * 功能说明：二维码生成器
	 * 日期：	2015年8月12日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param userQrcode	用户名
	 * @param userQrimgView	用户二维码显示的组件
	 * @param QrWidth	二维码的宽度
	 * @param QrHeight	二维码的高度
	 * </pre>
	 */
	public void doCreateQrcode(String userQrcode, ImageView userQrimgView,
			int QrWidth, int QrHeight) {
		try {
			Bitmap bitmap;
			if (TextUtils.isEmpty(userQrcode)) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(userQrcode,
					BarcodeFormat.QR_CODE, QrWidth, QrHeight, hints);
			int[] pixels = new int[QrWidth * QrHeight];
			for (int y = 0; y < QrHeight; y++) {
				for (int x = 0; x < QrWidth; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QrWidth + x] = 0xff000000;
					} else {
						pixels[y * QrWidth + x] = 0xffffffff;
					}

				}
			}
			bitmap = Bitmap.createBitmap(QrWidth, QrHeight,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QrWidth, 0, 0, QrWidth, QrHeight);
			userQrimgView.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}

	}

}
