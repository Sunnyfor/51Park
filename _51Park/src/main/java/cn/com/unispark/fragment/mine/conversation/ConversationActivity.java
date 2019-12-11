package cn.com.unispark.fragment.mine.conversation;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.util.HttpUtil.onResultTo;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.UserInfo;

/**
 * <pre>
 * 功能说明： 【意见反馈】界面
 * 日期：	2015年11月9日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月9日
 * </pre>
 */
public class ConversationActivity extends BaseActivity {

	// 导航栏标题//返回按钮//右侧提交
	private TextView titleText, moreText;
	private LinearLayout backLLayout, moreLLayout;

	private static final String KEY_UMENG_CONTACT_INFO_PLAIN_TEXT = "plain";
	private FeedbackAgent agent;
	private Conversation defaultConversation;

	// 意见框//邮箱手机号
	private EditText suggest_et, phone_email_et;
	private String content;
	private String contact_info;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.conversation_main);

	}

	@Override
	public void initView() {

		// 导航栏标题
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText("意见反馈");

		// 返回按钮
		backLLayout = (LinearLayout) findViewById(R.id.backLLayout);
		backLLayout.setOnClickListener(this);

		// 右侧提交按钮
		moreText = (TextView) findViewById(R.id.moreText);
		moreText.setVisibility(View.VISIBLE);
		moreText.setText("提交");
		moreLLayout = (LinearLayout) findViewById(R.id.moreLLayout);
		moreLLayout.setOnClickListener(this);
		moreLLayout.setClickable(false);

		agent = new FeedbackAgent(this);
		defaultConversation = agent.getDefaultConversation();

		// 意见框
		suggest_et = (EditText) findViewById(R.id.suggest_et);
		suggest_et.addTextChangedListener(watcher);

		// 手机号邮箱
		phone_email_et = (EditText) findViewById(R.id.phone_email_et);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.backLLayout:
			finish();
			break;
		case R.id.moreLLayout://提交
			contact_info = phone_email_et.getEditableText().toString().trim();
			parseUserConversation(contact_info);
			doSentSuggestion(contact_info);
			MobclickAgent.onEvent(context, "settings_feedbackSubmit_click");
			break;
		}

	}

	/*
	 * 设置当输入框有值时"提交"字样显示为红色
	 */
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			content = suggest_et.getEditableText().toString().trim();
			if (content.length() < 3) {
				moreLLayout.setClickable(false);
				moreText.setTextColor(getResources().getColor(
						R.color.gray_fontbb));
			} else {
				moreLLayout.setClickable(true);
				moreText.setTextColor(getResources().getColor(R.color.red_font));
			}

		}
	};

	/**
	 * <pre>
	 * 功能说明：发送建议
	 * 日期：	2015年11月23日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param contact_info
	 * </pre>
	 */
	private void doSentSuggestion(String contact_info) {
		defaultConversation.addUserReply(content);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
			imm.hideSoftInputFromWindow(suggest_et.getWindowToken(), 0);
		UserInfo info = agent.getUserInfo();
		if (info == null)
			info = new UserInfo();
		Map<String, String> contact = info.getContact();
		if (contact == null)
			contact = new HashMap<String, String>();
		contact.put(KEY_UMENG_CONTACT_INFO_PLAIN_TEXT, contact_info);
		info.setContact(contact);
		agent.setUserInfo(info);
		new Thread(new Runnable() {
			@Override
			public void run() {
				agent.updateUserInfo();
				finish();
			}
		}).start();
		suggest_et.getEditableText().clear();
		phone_email_et.getEditableText().clear();
	}

	/**
	 * <pre>
	 * 功能说明：【解析】用户意见反馈
	 * 日期：	2015年11月10日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param contact_info
	 * </pre>
	 */
	private void parseUserConversation(String contact_info) {
		loadingProgress.show();

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("contact", contact_info);
		params.put("content", content);

		LogUtil.showLog(
				3,
				"【用户意见反馈 URL】" + Constant.FEEDBACK_URL
						+ LogUtil.buildUrlParams(params));

		httpUtil.parseno(httpUtil.POST, Constant.FEEDBACK_URL, params,
				new onResultTo() {
					@Override
					public void onResult(int code, String msg, String json) {
						loadingProgress.dismiss();
						if (code == httpUtil.SERVER_REQ_OK) {
							ToastUtil.showToast("感谢您的意见！");
						} else {
							ToastUtil.showToast(msg);
						}
					}
				});
	}
}