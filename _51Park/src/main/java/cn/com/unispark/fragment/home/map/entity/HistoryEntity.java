package cn.com.unispark.fragment.home.map.entity;

/**
 * <pre>
 * 功能说明： 历史搜索记录，数据保存的实体类
 * 日期：	2015年11月5日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月5日
 * </pre>
 */
public class HistoryEntity {
	private String key;
	private String value;
	private Double WD;

	public HistoryEntity() {

	}

	public Double getWD() {
		return WD;
	}

	public void setWD(Double wD) {
		WD = wD;
	}

	private Double JD;

	public Double getJD() {
		return JD;
	}

	public void setJD(Double jD) {
		JD = jD;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "HistoryEntity [key=" + key + ", value=" + value + ", WD=" + WD
				+ ", JD=" + JD + "]";
	}

}
