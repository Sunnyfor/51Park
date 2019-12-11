package cn.com.unispark.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.unispark.application.Constant;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.map.entity.InOutEntity;
import cn.com.unispark.fragment.home.map.entity.ParkInOutEntity;
import cn.com.unispark.fragment.home.map.entity.ParkInOutEntity.DataObject.ParkInOutInfo;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import android.content.Context;

/**
 * <pre>
 * 功能说明： 导航到入口工具类
 * 日期：	2016年12月22日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2016年12月22日
 * </pre>
 */
public class NavEntryUtil {

	private Context context;
	// 停车场id
	private String parkId;
	private OnInOutResult onResult;

	private List<InOutEntity> inOutList = new ArrayList<InOutEntity>();

	public NavEntryUtil(Context context, String parkId) {
		this.context = context;
		this.parkId = parkId;
	}

	HttpUtil httpUtil = new HttpUtil(context);

	/**
	 * <pre>
	 * 功能说明：获取车场最近的经纬度
	 * 日期：	2016年12月22日
	 * 开发者：	陈丶泳佐
	 *
	 * </pre>
	 */
	public void getLatLng(OnInOutResult onResult) {

		this.onResult = onResult;
		parseLoadInOut();
	}

	/**
	 * <pre>
	 * 功能说明：解析加载出入口
	 * 日期：	2016年12月22日
	 * 开发者：	陈丶泳佐
	 * 
	 * @param parkId
	 * </pre>
	 */
	private void parseLoadInOut() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", ParkApplication.getmUserInfo().getUid());
		params.put("parkid", parkId);

		LogUtil.d("【停车场出入口URL】" + Constant.ENEXIT_URL, params);

		httpUtil.parse(httpUtil.POST, Constant.ENEXIT_URL,
				ParkInOutEntity.class, params,
				new HttpUtil.onResult<ParkInOutEntity>() {

					@Override
					public void onSuccess(ParkInOutEntity result) {

						List<ParkInOutInfo> list = result.getData().getList();

						int size = list.size();
						for (int i = 0; i < size; i++) {
							InOutEntity inOutEntity = new InOutEntity();
							// type=0入口,type=2出入口
							int type = list.get(i).getIseneixt();

							if (type == 0 || type == 2) {
								inOutEntity.setLongitude(Double.valueOf(list
										.get(i).getLongitude()));
								inOutEntity.setLatitude(Double.valueOf(list
										.get(i).getLatitude()));

							}
							inOutList.add(inOutEntity);
						}

//						for (int j = 0; j < inOutList.size(); j++) {
//							System.out.println("NavEntryUtil:"
//									+ inOutList.get(j));
//						}

						if (onResult != null) {
							onResult.onResult(inOutList);
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
	 * 功能说明： 当集合赋值后返回结果的回调
	 * 日期：	2016年12月22日
	 * 开发者：	陈丶泳佐
	 * 版本信息：V
	 * 版权声明：版权所有@北京百会易泊科技有限公司
	 * 
	 * 历史记录
	 *    修改内容：
	 *    修改人员：
	 *    修改日期： 2016年12月22日
	 * </pre>
	 */
	public interface OnInOutResult {
		void onResult(List<InOutEntity> inOutList);
	}
}
