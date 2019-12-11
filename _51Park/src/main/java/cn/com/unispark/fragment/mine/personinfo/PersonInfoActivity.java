package cn.com.unispark.fragment.mine.personinfo;

import java.util.HashMap;
import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 个人资料的详细
 * 日期：	2015年3月22日
 * 开发者：	任建飞
 * 
 * 历史记录
 *    修改内容：手机号中间四位用****代替；先生或女士显示出来
 *    修改人员：陈丶泳佐
 *    修改日期： 2015年3月29日
 * </pre>
 */
public class PersonInfoActivity extends BaseActivity {
	TextView username = null;
	TextView platenumber = null;
	TextView tel = null;
	private cn.com.unispark.define.ClearEditText name_et;
	private int sex = 1;
	private RadioButton male_rbtn;
	private RadioButton female_rbtn;
	private RadioGroup radiogroup;;
	Intent data;
	private ClearEditText phone_et;

	// 导航栏标题 //返回按钮//右侧保存按钮
	private TextView titleText, moreText;
	private LinearLayout backLLayout, moreLLayout;

	// 车牌号// 姓名// 性别
	private String mName;
	private int mSex;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.person_info_main);
		data = new Intent();
	}

	@Override
	public void initView() {

		// 导航栏标题“个人资料”
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(getResources().getString(R.string.ge_ren_zi_liao));

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 导航栏右侧保存按钮
		moreText = (TextView) findViewById(R.id.moreText);
		moreText.setVisibility(View.VISIBLE);
		moreText.setText("保存");
		moreText.setTextColor(getResources().getColor(R.color.red_font));
		moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);

		/*
		 * 姓名
		 */
		TextView name_tv = (TextView) findViewById(R.id.name_tv);
		ViewUtil.setTextSize(name_tv, 30);
		ViewUtil.setViewSize(name_tv, 88, 0);
		ViewUtil.setMarginLeft(name_tv, 20, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(name_tv, 62, ViewUtil.LINEARLAYOUT);

		// 输入框
		name_et = (ClearEditText) findViewById(R.id.name_et);
		ViewUtil.setTextSize(name_et, 30);

		String nameStr = ParkApplication.getmUserInfo().getName();
		if (!TextUtils.isEmpty(nameStr)) {
			name_et.setText(nameStr);
		}

		/*
		 * 性别选择
		 */
		TextView sex_tv = (TextView) findViewById(R.id.sex_tv);
		ViewUtil.setTextSize(sex_tv, 30);
		ViewUtil.setViewSize(sex_tv, 88, 0);
		ViewUtil.setMarginLeft(sex_tv, 20, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(sex_tv, 68, ViewUtil.LINEARLAYOUT);

		// 先生
		male_rbtn = (RadioButton) findViewById(R.id.male_rbtn);
		ViewUtil.setTextSize(male_rbtn, 30);
		ViewUtil.setMarginRight(male_rbtn, 30, ViewUtil.LINEARLAYOUT);

		// 女士
		female_rbtn = (RadioButton) findViewById(R.id.female_rbtn);
		ViewUtil.setTextSize(female_rbtn, 30);

		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == female_rbtn.getId()) {
					sex = 2;
				} else if (checkedId == male_rbtn.getId()) {
					sex = 1;
				}
				MobclickAgent.onEvent(context, "my_dataSex_click");
			}
		});

		/*
		 * 手机号
		 */
		TextView phone_tv = (TextView) findViewById(R.id.phone_tv);
		ViewUtil.setTextSize(phone_tv, 30);
		ViewUtil.setViewSize(phone_tv, 88, 0);
		ViewUtil.setMarginLeft(phone_tv, 20, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(phone_tv, 30, ViewUtil.LINEARLAYOUT);

		// 输入框
		phone_et = (ClearEditText) findViewById(R.id.phone_et);
		phone_et.setText(ParkApplication.getmUserInfo().getUsername());
		phone_et.setEnabled(false);
		ViewUtil.setTextSize(phone_et, 30);

		// 判断用户性别
		switch (ParkApplication.getmUserInfo().getSex()) {
		case 1:
			male_rbtn.setChecked(true);
			break;
		case 2:
			female_rbtn.setChecked(true);
			break;
		default:
			male_rbtn.setChecked(false);
			female_rbtn.setChecked(false);
			break;
		}
	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreLLayout:// 导航栏右侧保存按钮
			if (sex == 1) {
				mSex = 1;
			} else if (sex == 2) {
				mSex = 2;
			}
			mName = name_et.getText().toString().trim();
			parseModifyPersonInfo(mName, mSex);
			MobclickAgent.onEvent(context, "my_dataName_click");
			MobclickAgent.onEvent(context, "my_dataSave_click");
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * <pre>
	 * 功能说明：【解析】修改个人资料
	 * 日期：	2015年11月11日
	 * 开发者：	陈丶泳佐
	 * @param name  姓名
	 * @param sex 性别
	 * </pre>
	 */
	private void parseModifyPersonInfo(String name, int sex) {
		loadingProgress.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("name", name);
		params.put("sex", sex + "");
		httpUtil.parseno(httpUtil.POST, Constant.MODIFY_INFO_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						loadingProgress.dismiss();
						// {"data":[],"msg":"修改成功","code":200}
						if (code == httpUtil.SERVER_REQ_OK) {
							ParkApplication.getmUserInfo().setName(mName);
							ParkApplication.getmUserInfo().setSex(mSex);
							Intent intent = getIntent();
							intent.putExtra("namestr", mName);
							PersonInfoActivity.this.setResult(20, intent);
							PersonInfoActivity.this.finish();
							ToastUtil.showToast("修改成功");
						} else {
							ToastUtil.show(msg);
						}
					}
				});
	}

}
