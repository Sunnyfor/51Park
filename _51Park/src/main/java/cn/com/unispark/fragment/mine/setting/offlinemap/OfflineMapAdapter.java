package cn.com.unispark.fragment.mine.setting.offlinemap;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.setting.offlinemap.view.PinnedHeaderListView2;
import cn.com.unispark.fragment.mine.setting.offlinemap.view.PinnedHeaderListView2.PinnedHeaderAdapter;
import cn.com.unispark.util.ReckonUtil;
import cn.com.unispark.util.ViewUtil;

import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapStatus;

/**
 * <pre>
 * 功能说明： 【离线地图】适配器
 * 日期：	2014年12月5日
 * 开发者：	任建飞
 * 版本信息：V5.1.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * </pre>
 */
public class OfflineMapAdapter extends BaseAdapter implements OnScrollListener,
		PinnedHeaderAdapter {

	private Context context;
	private List<CityMapItemEntity> list;
	private ViewHolder holder;
	private OfflineMapCity mMapCity;// 离线下载城市
	private OfflineMapManager amapManager;

	public OfflineMapAdapter(Context context, List<CityMapItemEntity> list,
			OfflineMapManager offlineMapManager) {
		this.context = context;
		this.list = list;
		this.amapManager = offlineMapManager;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		if (null == convertView) {
			convertView = View
					.inflate(context, R.layout.offline_map_item, null);
			holder = new ViewHolder();
			// 字母
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);
			ViewUtil.setTextSize(holder.title_tv, 24);
			ViewUtil.setViewSize(holder.title_tv, 42, 0);
			ViewUtil.setPaddingLeft(holder.title_tv, 20);
			holder.city_ll = (RelativeLayout) convertView
					.findViewById(R.id.city_ll);
			ViewUtil.setViewSize(holder.city_ll, 100, 0);
			ViewUtil.setPaddingRight(holder.city_ll, 20);

			// 城市
			holder.content_tv = (TextView) convertView
					.findViewById(R.id.content_tv);
			ViewUtil.setTextSize(holder.content_tv, 30);
			ViewUtil.setViewSize(holder.content_tv, 55, 0);
			ViewUtil.setPaddingLeft(holder.content_tv, 20);

			holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			ViewUtil.setTextSize(holder.tv_size, 24);
			ViewUtil.setViewSize(holder.tv_size, 45, 0);
			ViewUtil.setPaddingLeft(holder.tv_size, 20);

			holder.tv_download = (TextView) convertView	
					.findViewById(R.id.tv_download);
			ViewUtil.setTextSize(holder.tv_download, 30);
			ViewUtil.setPaddingRight(holder.tv_download, 30);
			ViewUtil.setViewSize(holder.tv_download, 100, 0);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CityMapItemEntity itemEntity = list.get(position);
		mMapCity = amapManager.getItemByCityName(itemEntity
				.getmOfflineMapCity().getCity());
		if (needTitle(position)) {
			holder.title_tv.setText(itemEntity.getTitle());
			ViewUtil.setTextSize(holder.title_tv, 30);
			holder.title_tv.setVisibility(View.VISIBLE);
		} else {
			holder.title_tv.setVisibility(View.GONE);
		}
		holder.content_tv.setText(mMapCity.getCity());
		Log.e("slx", "1");
		holder.tv_size.setText(ReckonUtil.getTwoPointFormat(String.valueOf(
				mMapCity.getSize() / (1024 * 1024f)).toString())
				+ "MB");
		int state = mMapCity.getState();
		int completeCode = mMapCity.getcompleteCode();

		if (state == OfflineMapStatus.SUCCESS) {
			holder.tv_download.setText("安装完成");
		} else if (state == OfflineMapStatus.LOADING) {
			holder.tv_download.setText("正在下载" + completeCode + "%");
		} else if (state == OfflineMapStatus.WAITING) {
			holder.tv_download.setText("等待中");
		} else if (state == OfflineMapStatus.UNZIP) {
			holder.tv_download.setText("正在解压" + completeCode + "%");
		} else if (state == OfflineMapStatus.LOADING) {
			holder.tv_download.setText("下载");
		} else if (state == OfflineMapStatus.PAUSE) {
			holder.tv_download.setText("暂停中");
		} else {
			holder.tv_download.setText("未下载");
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (view instanceof PinnedHeaderListView2) {
			((PinnedHeaderListView2) view).controlPinnedHeader(firstVisibleItem);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public int getPinnedHeaderState(int position) {
		if (getCount() == 0 || position < 0) {
			return PinnedHeaderAdapter.PINNED_HEADER_GONE;
		}

		if (isMove(position) == true) {
			return PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
		}

		return PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View headerView, int position, int alpaha) {
		CityMapItemEntity itemEntity = (CityMapItemEntity) getItem(position);
		String headerValue = itemEntity.getTitle();

		// 设置浮条的样式
		if (!TextUtils.isEmpty(headerValue)) {
			TextView headerText = (TextView) headerView
					.findViewById(R.id.header);
			headerText.setText(headerValue);
			headerText.setBackgroundResource(R.color.white);
			ViewUtil.setTextSize(headerText, 30);
			ViewUtil.setViewSize(headerText, 44, 0);

		}

	}

	private boolean needTitle(int position) {
		if (position == 0) {
			return true;
		}

		if (position < 0) {
			return false;
		}

		CityMapItemEntity currentEntity = (CityMapItemEntity) getItem(position);
		CityMapItemEntity previousEntity = (CityMapItemEntity) getItem(position - 1);
		if (null == currentEntity || null == previousEntity) {
			return false;
		}

		String currentTitle = currentEntity.getTitle();
		String previousTitle = previousEntity.getTitle();
		if (null == previousTitle || null == currentTitle) {
			return false;
		}
		if (currentTitle.equals(previousTitle)) {
			return false;
		}

		return true;
	}

	private boolean isMove(int position) {
		CityMapItemEntity currentEntity = (CityMapItemEntity) getItem(position);
		CityMapItemEntity nextEntity = (CityMapItemEntity) getItem(position + 1);
		if (null == currentEntity || null == nextEntity) {
			return false;
		}
		String currentTitle = currentEntity.getTitle();
		String nextTitle = nextEntity.getTitle();
		if (null == currentTitle || null == nextTitle) {
			return false;
		}
		if (!currentTitle.equals(nextTitle)) {
			return true;
		}

		return false;
	}

	private class ViewHolder {
		RelativeLayout city_ll;
		TextView tv_size;
		TextView title_tv;
		TextView content_tv;
		TextView tv_download;

	}

	public int getSectionForPosition(int position) {
		return list.get(position).getmTitle().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getTitle();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}
}
