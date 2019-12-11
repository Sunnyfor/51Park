package cn.com.unispark.fragment.mine.personinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.unispark.R;

public class ModifyCarPlateAdapter extends BaseAdapter {
	private String[]list;
	private LayoutInflater inflater = null;
	private LinearLayout ll_Popup;
	private TextView plateno_tv;
	private Animation animation2;
	public ModifyCarPlateAdapter(Context context, String[] list, LinearLayout ll_Popup, TextView plateno_tv,Animation animation2) {
		super();
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.ll_Popup = ll_Popup;
		this.plateno_tv =plateno_tv;
		this.animation2 = animation2;
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		HolderView holderView;
		if (convertView != null) {
			view = convertView;
			holderView = (HolderView) view.getTag();
		} else {
			view = inflater.inflate(R.layout.province_item, null);
			holderView = new HolderView();
			holderView.btn = (Button) view.findViewById(R.id.btn_province_item);
			view.setTag(holderView);
		}
		holderView.btn.setText(list[position]);
		holderView.btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				首先布局消失
				 ll_Popup.setVisibility(View.GONE);   // 取出布局  
	             ll_Popup.startAnimation(animation2); // 开始退出动画  
	             plateno_tv.setText("    " + list[position]);
//	             plateno_tv.setSelection(plateno_edit.getText().length());// 将光标移动到文字末尾
			}
		});
		return view;
	}
	
	private class HolderView {
		Button btn;
	}

}
