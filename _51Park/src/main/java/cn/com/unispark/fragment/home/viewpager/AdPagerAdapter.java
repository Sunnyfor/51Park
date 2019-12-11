package cn.com.unispark.fragment.home.viewpager;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.com.unispark.R;
import cn.com.unispark.application.ParkApplication;
import cn.com.unispark.fragment.home.viewpager.entity.AdPagerEntity.Data.ImageInfo;
import cn.com.unispark.util.ToastUtil;
import cn.com.unispark.util.ToolUtil;

import com.bumptech.glide.Glide;

/**
 * <pre>
 * 功能说明： 轮播图适配器
 * 日期：	2015年10月20日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年10月23日
 * </pre>
 */
public class AdPagerAdapter extends PagerAdapter {

	private Context context;
	private List<ImageInfo> list;

	public AdPagerAdapter(Context context, List<ImageInfo> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		View itemview = LayoutInflater.from(context).inflate(
				R.layout.home_adpager_item, null);
		ImageView imageView = (ImageView) itemview.findViewById(R.id.image);

		final String imgUrl = list.get(position).getImg();
		final String srcUrl = list.get(position).getSrc();

		Glide.with(context).load(imgUrl).placeholder(R.drawable.bg_51park)
				.into(imageView);

		itemview.findViewById(R.id.loading_text).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!ToolUtil.isNetConn()) {
							ToastUtil.showToastNetError();
						} else {
							if (!srcUrl.equals("")) {
								Intent intent = null;
								intent = new Intent(context,
										WebActiveActivity.class);
								intent.putExtra("url", srcUrl
										+ "?uid="
										+ ParkApplication.getmUserInfo()
												.getUid());
								context.startActivity(intent);
							}
						}
					}
				});
		view.addView(itemview);
		return itemview;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
