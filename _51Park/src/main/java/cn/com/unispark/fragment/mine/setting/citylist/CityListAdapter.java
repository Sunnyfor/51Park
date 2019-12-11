package cn.com.unispark.fragment.mine.setting.citylist;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.unispark.R;
import cn.com.unispark.fragment.mine.setting.citylist.CityListActivity.CityItemEntity;
import cn.com.unispark.fragment.mine.setting.citylist.view.PinnedHeaderListView;
import cn.com.unispark.fragment.mine.setting.citylist.view.PinnedHeaderListView.PinnedHeaderAdapter;
import cn.com.unispark.util.ViewUtil;

public class CityListAdapter extends BaseAdapter implements OnScrollListener,
		PinnedHeaderAdapter {

	private Context context;
	private List<CityItemEntity> list;
	private ViewHolder holder;

	public CityListAdapter(Context context, List<CityItemEntity> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		if (null == convertView) {
			convertView = View.inflate(context, R.layout.offline_map_item, null);

			holder = new ViewHolder();

			// 字母
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);
			ViewUtil.setTextSize(holder.title_tv, 24);
			ViewUtil.setViewSize(holder.title_tv, 42, 0);
			ViewUtil.setPaddingLeft(holder.title_tv, 20);

			// 城市
			holder.content_tv = (TextView) convertView
					.findViewById(R.id.content_tv);
			ViewUtil.setTextSize(holder.content_tv, 30);
			ViewUtil.setViewSize(holder.content_tv, 88, 0);
			ViewUtil.setPaddingLeft(holder.content_tv, 20);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CityItemEntity itemEntity = (CityItemEntity) getItem(position);

		holder.content_tv.setText(itemEntity.getmCityInfo().getName());
		if (needTitle(position)) {
			holder.title_tv.setText(itemEntity.getTitle());
			ViewUtil.setTextSize(holder.title_tv, 30);
			holder.title_tv.setVisibility(View.VISIBLE);
		} else {
			holder.title_tv.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public int getCount() {
		// if (null != mData) {
		return list.size();
		// }
		// return 0;
	}

	@Override
	public Object getItem(int position) {
		// if (null != mData && position < getCount()) {
		return list.get(position);
		// }
		// return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
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
		CityItemEntity itemEntity = (CityItemEntity) getItem(position);
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

		CityItemEntity currentEntity = (CityItemEntity) getItem(position);
		CityItemEntity previousEntity = (CityItemEntity) getItem(position - 1);
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
		CityItemEntity currentEntity = (CityItemEntity) getItem(position);
		CityItemEntity nextEntity = (CityItemEntity) getItem(position + 1);
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
		TextView title_tv;
		TextView content_tv;
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
