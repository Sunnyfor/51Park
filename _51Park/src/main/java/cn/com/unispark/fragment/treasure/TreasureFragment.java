package cn.com.unispark.fragment.treasure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import cn.com.unispark.R;
import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.define.LoadingProgress;
import cn.com.unispark.fragment.home.viewpager.WebActiveActivity;
import cn.com.unispark.fragment.treasure.entity.CheMiEntity;
import cn.com.unispark.fragment.treasure.entity.TreasureEntity;
import cn.com.unispark.fragment.treasure.lease.LeaseCarActivity;
import cn.com.unispark.util.HttpUtil;
import cn.com.unispark.util.HttpUtil.onResult;
import cn.com.unispark.util.LogUtil;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;
import cn.com.unispark.util.ViewUtil;

public class TreasureFragment extends Fragment implements OnClickListener {

    // 当前点击的View的ID,防止多次重复点击使用
    private int currentViewId;

    public View view;

    // 导航栏停车场详情页标题
    private TextView titleText;

    // 车位租赁//汽车专家//活动中心//摇一摇
    private RelativeLayout carLeaseRLayout, car_home_rl, cheXingYiRLayout;

    private Context context;
    private Activity activity;
    private HttpUtil httpUtil;
    private LoadingProgress loadingProgress;
    protected int mCouponsCount;

    //	private TextView car_lease_detail;
//	private TextView car_home_detail;
//	private TextView active_center_detail;
//	private TextView yao_yi_yao_detail;
//	private TextView che_xing_yi_detail;
//
    private ImageView car_lease_iv;
    private ImageView car_home_iv;
    //	private ImageView active_center_iv;
//	private ImageView yao_yi_yao_iv;
    private ImageView che_xing_yi_iv;
//
//	protected enum ListType {
//		CARLEASE, CARHOME, ACTIVECENTER, YAOYIYAO
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        activity = getActivity();

        view = View.inflate(context, R.layout.treasure_mian, null);
        httpUtil = new HttpUtil(context);
        loadingProgress = new LoadingProgress(context);
        initView();

//		parseGetTreasureList();

        return view;
    }


    /**
     * <pre>
     * 功能说明：设置详情和图标
     * 日期：	2016年9月29日
     * 开发者：	陈丶泳佐
     * </pre>
     */
//	private void setDetailAndIcon(int i, String picStr, String detailStr) {
//		switch (i) {
//		case 1:
//			car_lease_detail.setText(detailStr);
//			ImageUtil.loadImage(context, car_lease_iv, picStr);
//			break;
//		case 2:
//			car_home_detail.setText(detailStr);
//			ImageUtil.loadImage(context, car_home_iv, picStr);
//			break;
////		case 3:
////			active_center_detail.setText(detailStr);
////			ImageUtil.loadImage(context, active_center_iv, picStr);
////			break;
////		case 4:
////			yao_yi_yao_detail.setText(detailStr);
////			ImageUtil.loadImage(context, yao_yi_yao_iv, picStr);
////			break;
//		case 5:
//			che_xing_yi_detail.setText(detailStr);
//			ImageUtil.loadImage(context, che_xing_yi_iv, picStr);
//			break;
//		}
//	}
    public void initView() {
        // 导航栏标题
        titleText = (TextView) view.findViewById(R.id.titleText);
        titleText.setText("百宝箱");

        // 导航栏返回按钮隐藏
        view.findViewById(R.id.backImgView).setVisibility(View.GONE);

//		car_lease_detail = (TextView) view.findViewById(R.id.car_lease_detail);
//		car_home_detail = (TextView) view.findViewById(R.id.car_home_detail);
//		che_xing_yi_detail = (TextView) view.findViewById(R.id.che_xing_yi_detail);


		/*
		 * 车位租赁
		 */
        carLeaseRLayout = (RelativeLayout) view.findViewById(R.id.car_lease_rl);
        carLeaseRLayout.setOnClickListener(this);

        // 图标
        car_lease_iv = (ImageView) view.findViewById(R.id.car_lease_iv);
        ViewUtil.setViewSize(car_lease_iv, 65, 65);
        ViewUtil.setMargin(car_lease_iv, 5, 20, ViewUtil.RELATIVELAYOUT);

		/*
		 * 汽车专家
		 */
        car_home_rl = (RelativeLayout) view.findViewById(R.id.car_home_rl);
        car_home_rl.setOnClickListener(this);

        // 图标
        car_home_iv = (ImageView) view.findViewById(R.id.car_home_iv);
        ViewUtil.setViewSize(car_home_iv, 65, 65);
        ViewUtil.setMargin(car_home_iv, 5, 20, ViewUtil.RELATIVELAYOUT);
		
		/*
		 * 车行易 违章查询
		 */
        cheXingYiRLayout = (RelativeLayout) view.findViewById(R.id.che_xing_yi_rl);
        cheXingYiRLayout.setOnClickListener(this);

        //图标
        che_xing_yi_iv = (ImageView) view.findViewById(R.id.che_xing_yi_iv);
        ViewUtil.setViewSize(che_xing_yi_iv, 65, 65);
        ViewUtil.setMargin(che_xing_yi_iv, 5, 20, ViewUtil.RELATIVELAYOUT);


    }

    @Override
    public void onClick(View v) {
        // 防止按钮多次点击
        if (currentViewId == v.getId()) {
            return;
        }
        currentViewId = v.getId();

        switch (v.getId()) {
            case R.id.car_lease_rl:// 车位租赁
                ToolUtil.IntentClass(activity, LeaseCarActivity.class, false);
                MobclickAgent.onEvent(context, "MagicBox_leaseBtn_click");
                break;
            case R.id.car_home_rl:// 汽车之家
                if (ParkApplication.isLogin(activity)) {
                    parseCarHomeUrl();
                }
                MobclickAgent.onEvent(context, "MagicBox_carSpecialist_click");
                break;
            case R.id.che_xing_yi_rl:// 车行易
                if (ParkApplication.isLogin(activity)) {
                    parseCheXingYiUrl();
                }
//			MobclickAgent.onEvent(context, "MagicBox_activityBtn_click");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        currentViewId = 0;
    }


    /**
     * <pre>
     * 功能说明：获取百宝箱列表
     * 日期：	2016年3月28日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    private void parseGetTreasureList() {
        Map<String, String> params = new HashMap<String, String>();
        httpUtil.parse(httpUtil.POST, Constant.TREASURE_LIST_URL,
                TreasureEntity.class, params, new onResult<TreasureEntity>() {
                    // 1:包租车位;2:汽车专家;3:活动中心;4:摇一摇;
                    @Override
                    public void onSuccess(TreasureEntity result) {
                        mCouponsCount = result.getData().getList().size();
                        if (mCouponsCount != 0) {
                            for (int i = 0; i < mCouponsCount; i++) {
                                int type = result.getData().getList().get(i)
                                        .getType();
                                String picStr = result.getData().getList()
                                        .get(i).getImg();

                                String detailStr = result.getData().getList()
                                        .get(i).getDescribe();
//								setDetailAndIcon(type, picStr, detailStr);
                            }
                        }
                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        ToastUtil.showToast(errMsg);
                    }
                });
    }

    /**
     * <pre>
     * 功能说明：【解析】汽车专家，车秘
     * 日期：	2015年10月16日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public void parseCarHomeUrl() {

        loadingProgress.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());

        LogUtil.d("【汽车专家URL】" + Constant.CHEMI_URL, params);

        httpUtil.parse(httpUtil.POST, Constant.CHEMI_URL, CheMiEntity.class,
                params, new onResult<CheMiEntity>() {

                    @Override
                    public void onSuccess(CheMiEntity result) {

                        String chemiUrl = result.getData().getUrl();
                        Intent intent = new Intent(getActivity(),
                                WebActiveActivity.class);
                        intent.putExtra("url", chemiUrl);
                        intent.putExtra("title", "汽车专家");
                        startActivity(intent);

                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        ToastUtil.showToast(errMsg);
                    }
                });

        loadingProgress.dismiss();

    }


    /**
     * <pre>
     * 功能说明：【解析】车行易
     * 日期：	2016年9月30日
     * 开发者：	陈丶泳佐
     *
     * </pre>
     */
    public void parseCheXingYiUrl() {

        loadingProgress.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", ParkApplication.getmUserInfo().getUid());

        LogUtil.d("【车行易URL】" + Constant.CHEXINGYI_URL, params);

        httpUtil.parse(httpUtil.POST, Constant.CHEXINGYI_URL, CheMiEntity.class,
                params, new onResult<CheMiEntity>() {

                    @Override
                    public void onSuccess(CheMiEntity result) {

                        String chexingyiUrl = result.getData().getUrl();
                        Intent intent = new Intent(getActivity(),
                                WebActiveActivity.class);
                        intent.putExtra("url", chexingyiUrl);
                        intent.putExtra("title", "违章查询");
                        startActivity(intent);

                    }

                    @Override
                    public void onFailed(int errCode, String errMsg) {
                        ToastUtil.showToast(errMsg);
                    }
                });

        loadingProgress.dismiss();

    }

}