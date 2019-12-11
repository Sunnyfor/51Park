package cn.com.unispark.fragment.unknown.parkcar;
//package cn.com.unispark.mine.collection.parkcar;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//import cn.com.unispark.R;
//
///**
// * <pre>
// * 功能说明： 【停车场】数据适配器
// * 日期：	2014年12月22日
// * 开发者：	陈丶泳佐
// * 
// * 历史记录
// *    修改内容：
// *    修改人员：
// *    修改日期： 2014年12月22日
// * </pre>
// */
//public class CollectParkCarAdapter extends BaseAdapter {
//	private List<ParkCarEntity> list;
//	private LayoutInflater inflater = null;
//
//	public CollectParkCarAdapter(Context context,  List<ParkCarEntity> list) {
//		super();
//		inflater = LayoutInflater.from(context);
//		this.list = list;
//	}
//
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		View view;
//		HolderView holderView;
//		if (convertView != null) {
//			view = convertView;
//			holderView = (HolderView) view.getTag();
//		} else {
//			view = inflater.inflate(R.layout.collection_item, null);
//			holderView = new HolderView();
//
//			holderView.store_park_name = (TextView) view.findViewById(R.id.store_park_name);
//			holderView.store_park_address = (TextView) view.findViewById(R.id.store_park_address);
//			view.setTag(holderView);
//		}
//		holderView.store_park_name.setText(list.get(position).getTitle());
//		holderView.store_park_address.setText(list.get(position).getAddress());
//		return view;
//	}
//	
//	private class HolderView {
//		TextView store_park_name;//停车场名称
//		TextView store_park_address;//停车场地址
//		
//	}
//	 /**  
//     * 添加数据列表项  
//     * @param newsitem  
//     */  
//    public void addNewsItem(ParkCarEntity point1){  
//    	list.add(point1);  
//    }  
//}
