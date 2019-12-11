package cn.com.unispark.util.prompt;

import java.util.Set;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.application.BaseActivity;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.login.LoginActivity;
import cn.com.unispark.util.ShareUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class ExitLoginDialog {

	private static Dialog dialog;

	private static void initDialog(final Context context) {
		if (dialog == null) {
			dialog = new Dialog(context);
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.common_dialog);
			
			// 对话框布局
			RelativeLayout dialog_rl = (RelativeLayout) dialog
					.findViewById(R.id.dialog_rl);
			ViewUtil.setViewSize(dialog_rl, 304, 540);

			// 标题和内容所在的布局
			View test0_rl = dialog.findViewById(R.id.test0_rl);
			ViewUtil.setViewSize(test0_rl, 216, 540);

			// 标题
			TextView title_tv = (TextView) dialog.findViewById(R.id.title_tv);
			title_tv.setVisibility(View.VISIBLE);
			ViewUtil.setTextSize(title_tv, 32);
			title_tv.setText("下线通知");

			// 内容
			TextView content_tv = (TextView) dialog
					.findViewById(R.id.content_tv);
			ViewUtil.setTextSize(content_tv, 32);
			ViewUtil.setMarginTop(content_tv, 10, ViewUtil.RELATIVELAYOUT);
			content_tv.setText("您当前登录的账号已下线，\n为账户安全请谨慎操作！");

			// 确认按钮
			Button sureBtn = (Button) dialog.findViewById(R.id.sure_btn);
			sureBtn.setText("确定");
			sureBtn.setVisibility(View.VISIBLE);
			ViewUtil.setTextSize(sureBtn, 32);
			ViewUtil.setViewSize(sureBtn, 88, 0);
			ViewUtil.setPaddingBottom(sureBtn, 5);
			sureBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.hide();
					// 别名置空
					JPushInterface.setAlias(context, "",
							new TagAliasCallback() {
								@Override
								public void gotResult(int code, String alias,
										Set<String> tags) {
								}
							});
					
//					ParkApplication.getmUserInfo().setUid("");
					ShareUtil.setSharedString("token", "");
					ShareUtil.setSharedString("uid", "");
					ShareUtil.setSharedBoolean("islogin", false);
					ParkApplication.setmUserInfo(null);
					
					ParkApplication.quitActivity();
					ToolUtil.IntentClass(BaseActivity.activity,
							LoginActivity.class,"imlogin",true, true);
			
				}
			});

			// 横线
			View line_view = (View) dialog.findViewById(R.id.line_view);
			line_view.setVisibility(View.VISIBLE);
			ViewUtil.setViewSize(line_view, 2, 540);

		}

	}

	public static void show(Context context) {
		initDialog(context);
		
		if (!dialog.isShowing() && context != null) {
			dialog.show();
		}
	}
}
