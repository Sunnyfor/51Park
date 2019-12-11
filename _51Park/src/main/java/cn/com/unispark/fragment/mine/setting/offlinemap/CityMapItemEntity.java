package cn.com.unispark.fragment.mine.setting.offlinemap;

import java.io.Serializable;
import java.util.List;

import com.amap.api.maps.offlinemap.OfflineMapCity;

import cn.com.unispark.application.BaseEntity;
import cn.com.unispark.fragment.mine.setting.offlinemap.CityListEntity.DataObject.CityGroupInfo.CityInfo;

/**
 * <pre>
 * 功能说明： 【城市地图带索引】实体类
 * 日期：	2015年12月31日
 * 开发者：	任建飞
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class CityMapItemEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mTitle;
	private CityInfo mCityInfo;
	private OfflineMapCity mOfflineMapCity;

	public OfflineMapCity getmOfflineMapCity() {
		return mOfflineMapCity;
	}

	public void setmOfflineMapCity(OfflineMapCity mOfflineMapCity) {
		this.mOfflineMapCity = mOfflineMapCity;
	}

	private String mCityMapSize;

	public String getmCityMapSize() {
		return mCityMapSize;
	}

	public void setmCityMapSize(String mCityMapSize) {
		this.mCityMapSize = mCityMapSize;
	}

	public CityMapItemEntity(String pTitle, CityInfo mCityInfo) {
		mTitle = pTitle;
		this.mCityInfo = mCityInfo;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public CityInfo getmCityInfo() {
		return mCityInfo;
	}

	public void setmCityInfo(CityInfo mCityInfo) {
		this.mCityInfo = mCityInfo;
	}

}