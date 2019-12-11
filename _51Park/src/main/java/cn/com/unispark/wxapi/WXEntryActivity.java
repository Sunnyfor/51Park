package cn.com.unispark.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.com.unispark.R;

/**
 * <pre>
 * 功能说明：微信分享（发送给朋友或者朋友圈）
 * 日期：	2015年1月30日
 * 开发者：任建飞
 * </pre>
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this,
				cn.com.unispark.fragment.home.pay.wechatpay.Constants.APP_ID, false);
		api.handleIntent(getIntent(), this);

	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		// TODO 微信分享 成功之后调用接口
		this.finish();
	}

}
