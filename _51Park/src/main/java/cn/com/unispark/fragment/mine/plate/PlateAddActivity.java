package cn.com.unispark.fragment.mine.plate;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.ClearEditText;
import cn.com.unispark.fragment.mine.personinfo.ModifyCarPlateAdapter;
import cn.com.unispark.fragment.mine.plate.entity.PlateModifyEntity;
import cn.com.unispark.fragment.mine.plate.view.keyboard.KeyboardUtil;
import cn.com.unispark.util.DialogUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 车牌添加界面
 * 日期：	2015年11月19日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年12月3日
 * </pre>
 */
public class PlateAddActivity extends BaseActivity {

	// 导航栏标题 //返回按钮
	private TextView titleText;
	private LinearLayout backLLayout;
	// 省份
	private EditText province_tv;
	// 车牌号输入框
	private ClearEditText plate_et;

	/*
	 * 存放键盘的容器
	 */
	private LinearLayout keyboard_ll;
	private Button close_btn;// 键盘取消按钮
	private GridView grdv;
	private InputMethodManager imm;
	private Animation animation1;
	private Animation animation2;
	private String[] provinceCode_list;
	private String[] englishCode_list;
	// private Activity mActivity;
	private ModifyCarPlateAdapter adapter;
	private Button finish_btn;

	/*
	 * 判断是修改还是添加
	 */
	private boolean modify = false;
	private String modify_plate;
	private String platecard;// 车牌号
	private LinearLayout english_ll;

	// private Intent data;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.plate_add_main);
		// data = getIntent();

	}

	@Override
	public void initView() {
		initData();
		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("车牌管理");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		/*
		 * 车牌号布局
		 */
		LinearLayout plate_ll = (LinearLayout) findViewById(R.id.plate_ll);
		ViewUtil.setViewSize(plate_ll, 88, 0);

		// 车牌号文字
		TextView test0_tv = (TextView) findViewById(R.id.test0_tv);
		ViewUtil.setTextSize(test0_tv, 30);
		ViewUtil.setMarginLeft(test0_tv, 20, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(test0_tv, 30, ViewUtil.LINEARLAYOUT);

		// 省份
		province_tv = (EditText) findViewById(R.id.province_tv);
		province_tv.setOnClickListener(this);
		ViewUtil.setTextSize(province_tv, 26);
		ViewUtil.setViewSize(province_tv, 50, 96);
		ViewUtil.setPaddingLeft(province_tv, 10);
		ViewUtil.setMarginRight(province_tv, 20, ViewUtil.LINEARLAYOUT);
		province_tv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				imm.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				new KeyboardUtil(activity, context, province_tv).showKeyboard();
				return false;
			}
		});

		province_tv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				imm.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				int inputback = province_tv.getInputType();
				province_tv.setInputType(InputType.TYPE_NULL);
				new KeyboardUtil(activity, context, province_tv).showKeyboard();
				province_tv.setInputType(inputback);
				return false;
			}
		});

		// 车牌号输入框
		plate_et = (ClearEditText) findViewById(R.id.plate_et);
		plate_et.setHint("请输入车牌号");
		plate_et.setOnClickListener(this);
		ViewUtil.setTextSize(plate_et, 30);
		ViewUtil.setViewSize(plate_et, 88, 0, ViewUtil.LINEARLAYOUT);
		ViewUtil.setMarginRight(plate_et, 20, ViewUtil.LINEARLAYOUT);
		plate_et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					new KeyboardUtil(activity, context, province_tv)
							.hideKeyboard();
				}

			}
		});
		plate_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				// new KeyboardUtil(activity, context,
				// province_tv).hideKeyboard();
			}

			@Override
			public void afterTextChanged(Editable editable) {

				final String temp = editable.toString();
				if (temp.length() != 0) {
					String tem = temp.substring(temp.length() - 1,
							temp.length());
					char[] temC = tem.toCharArray();
					int mid = temC[0];
					if (mid >= 97 && mid <= 122) {// 小写字母
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								plate_et.setText(temp.toUpperCase());
								plate_et.setSelection(temp.length());// 将光标移动到文字末尾
							}
						}, 300);
					}
				}

			}
		});

		/*
		 * 保存按钮
		 */
		finish_btn = (Button) findViewById(R.id.finish_btn);
		finish_btn.setOnClickListener(this);

		platecard = getIntent().getExtras().getString("platecard");
		if (!TextUtils.isEmpty(platecard)) {
			province_tv.setText(platecard.substring(0, 2));
			plate_et.setText(platecard.substring(2));
			modify = true;
		}
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.finish_btn:
			modify_plate = province_tv.getText().toString().trim()
					+ plate_et.getText().toString().trim();
			if (TextUtils.isEmpty(plate_et.getText().toString().trim())) {
				modify_plate = "";
				ToastUtil.show("车牌号不能为空");
			} else if (modify_plate.length() < 7) {
				ToastUtil.show("车牌号不合法");
			} else {
				if (modify) {
					parseModifyPlate(modify_plate);
				} else {
					parseAddPlate(modify_plate);
				}
			}
			break;
		// case R.id.province_tv:
		// imm.hideSoftInputFromWindow(activity.getCurrentFocus()
		// .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		// new Handler().postDelayed(new Runnable() {
		// public void run() {
		// keyboard_ll.setVisibility(View.VISIBLE); // 显示布局
		// keyboard_ll.startAnimation(animation1); // 开始动画
		// }
		// }, 100);
		// break;
		// case R.id.plate_et:
		// keyboard_ll.setVisibility(View.GONE); // 取出布局
		// keyboard_ll.startAnimation(animation2); // 开始退出动画
		// break;
		// case R.id.close_btn:
		// keyboard_ll.setVisibility(View.GONE); // 取出布局
		// keyboard_ll.startAnimation(animation2); // 开始退出动画
		// break;
		}
	}

	@Override
	protected void onDestroy() {
		loadingProgress.dismiss();
		super.onDestroy();
	}

	/**
	 * <pre>
	 * 功能说明：初始化自定义车牌省份选择键盘
	 * 日期：	2015年11月26日
	 * 开发者：	任建飞
	 * 
	 * </pre>
	 */
	private void initData() {
		imm = ((InputMethodManager) activity
				.getSystemService(INPUT_METHOD_SERVICE));
		// 加载动画
		animation1 = AnimationUtils.loadAnimation(this,
				R.anim.provice_query_enter_anim);
		animation2 = AnimationUtils.loadAnimation(this,
				R.anim.provice_query_exit_anim);
		provinceCode_list = new String[] { "京", "津", "冀", "鲁", "晋", "蒙", "辽",
				"吉", "黑", "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘", "粤",
				"桂", "渝", "川", "贵", "云", "藏", "陕", "甘", "青", "琼", "新", "港",
				"澳", "台", "宁" };// 初始化省的编码
		englishCode_list = new String[] { "1","2","3","4","5","6","7","8","9","0",
				"A", "B", "C", "D", "E", "F", "G",
				"H", "I", "J", "H", "I", "J", "K", "L", "M", "N", "O", "P",
				"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };// 初始化英文字符
	}

	/**
	 * <pre>
	 * 功能说明： 【解析】添加车牌号
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	private void parseAddPlate(final String modify_plateno_str) {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("plate", modify_plateno_str);
		params.put("type", "1");

		LogUtil.d("【添加车牌号URL】" + Constant.PLATEMANAGE_URL + params);

		httpUtil.parse(httpUtil.POST, Constant.PLATEMANAGE_URL,
				PlateModifyEntity.class, params,
				new onResult<PlateModifyEntity>() {
					@Override
					public void onSuccess(PlateModifyEntity result) {
						loadingProgress.hide();
						switch (result.getData().getRes()) {
						case 1:// 操作成功
							ToastUtil.show(result.getMsg());
							setResult(40, getIntent());
							finish();
							break;
						case 2:// 车牌已被绑定(type=1时有效)
							final DialogUtil mfindDialog = new DialogUtil(
									context);
							mfindDialog.setTitle(null);
							mfindDialog.setMessage("车牌已被绑定！\n是否找回？");
							mfindDialog.setPositiveButton("是",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mfindDialog.dismiss();
											// 找回车牌
											ToolUtil.IntentClass(activity,
													PlateBackActivity.class,
													"plate",
													modify_plateno_str, false);
										}
									});
							mfindDialog.setNegativeButton("否",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mfindDialog.dismiss();
										}
									});
							break;
						case 3:// 操作失败
							ToastUtil.show(result.getMsg());
							break;
						case 4:// 车牌正在审核中[自己或别人]
							ToolUtil.IntentClass(activity,
									PlateResultActivity.class, "msg",
									result.getMsg(), false);
							break;
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.hide();
						ToastUtil.show(errMsg);
					}
				});
	}

	/**
	 * <pre>
	 * 功能说明：【解析】 修改车牌号 
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	protected void parseModifyPlate(final String modify_plateno_str) {

		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("plate", platecard);
		params.put("platemodify", modify_plateno_str);

		LogUtil.d("【修改车牌号URL】" + Constant.PLATEMODIFY_URL + params);

		httpUtil.parse(httpUtil.POST, Constant.PLATEMODIFY_URL,
				PlateModifyEntity.class, params,
				new onResult<PlateModifyEntity>() {
					@Override
					public void onSuccess(PlateModifyEntity result) {
						loadingProgress.hide();
						// ToastUtil.show(result.getMsg());
						switch (result.getData().getRes()) {
						case 1:// 操作成功
							ToastUtil.show(result.getMsg());
							setResult(20, getIntent());
							finish();
							break;
						case 2:// 车牌已被绑定
							final DialogUtil mfindDialog = new DialogUtil(
									context);
							mfindDialog.setTitle(null);
							mfindDialog.setMessage("车牌已被绑定！\n是否找回？");
							mfindDialog.setPositiveButton("是",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mfindDialog.dismiss();
											// 找回车牌
											ToolUtil.IntentClass(activity,
													PlateBackActivity.class,
													"plate",
													modify_plateno_str, false);
										}
									});
							mfindDialog.setNegativeButton("否",
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											mfindDialog.dismiss();
										}
									});
							break;
						case 3:// 操作失败
							ToastUtil.show(result.getMsg());
							break;
						case 4:// 车牌正在审核中[自己或别人]
							ToolUtil.IntentClass(activity,
									PlateResultActivity.class, "msg",
									result.getMsg(), false);
							break;
						}
					}

					@Override
					public void onFailed(int errCode, String errMsg) {
						loadingProgress.hide();
						ToastUtil.show(errMsg);
					}
				});
	}

}
