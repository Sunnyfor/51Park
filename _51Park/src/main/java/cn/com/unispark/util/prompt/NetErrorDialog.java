package cn.com.unispark.util.prompt;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import cn.com.unispark.R;
import cn.com.unispark.util.ViewUtil;

/**
 * <pre>
 * 功能说明： 网络状态不好时展示的对话框
 * 日期：	2015年11月23日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年12月16日
 * </pre>
 */
public class NetErrorDialog {
	private static Dialog dialog;

	private static ImageView clear_iv;

	/**
	 * <pre>
	 * 功能说明：网络状态不好时展示的对话框
	 * 日期：	2015年11月23日
	 * 开发者：	陈丶泳佐
	 * </pre>
	 */
	private static void initDialog(Context context) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		View view = View.inflate(context, R.layout.net_error_dialog, null);
		ViewUtil.setPadding(view, 88);

		clear_iv = (ImageView) view.findViewById(R.id.clear_iv);

		dialog.setContentView(view);
		
		Window window = dialog.getWindow();  
		window.setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams params = window.getAttributes();  
        params.dimAmount = 0f;  
//		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(false);

		// dialog.getWindow().setBackgroundDrawableResource(R.color.gray_bg);

		// /////
		// Window window = dialog.getWindow();
		// WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// // 设置透明度为0.3
		// lp.alpha = 0.6f;
		// dialog.getWindow().setAttributes(lp);

		// /
		//
		//
		// dialog.setContentView(view);
	}

	public static void show(Context context, final onReload reload) {
		if (dialog != null) {
			dialog.dismiss();
		}

		initDialog(context);

		clear_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				reload.onOption();
			}
		});

		if (!dialog.isShowing()) {
			dialog.show();
		}

	}

	public static void dismiss() {
		dialog.dismiss();
	}

	public interface onReload {
		void onOption();
	}
}
