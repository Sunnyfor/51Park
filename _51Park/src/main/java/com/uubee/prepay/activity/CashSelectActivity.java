package com.uubee.prepay.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.unispark.R;
import com.uubee.prepay.model.CashGift;

public class CashSelectActivity extends BaseActivity {
	private ArrayList<CashGift> mCashGifts;
	private ImageView mNoChooseImg;
	private CashSelectActivity.CashAdapter mAdapter;
	private int mIndex;
	private OnItemClickListener mClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CashGift cash = (CashGift) CashSelectActivity.this.mCashGifts
					.get(position);
			switch (cash.sta_cashgift) {
			case 0:
				CashSelectActivity.this.mNoChooseImg.setImageResource(0);
				CashSelectActivity.this.mNoChooseImg.invalidate();
				CashSelectActivity.CashAdapter.Holder holder = (CashSelectActivity.CashAdapter.Holder) view
						.getTag();
				holder.mChooseImg.setImageResource(R.drawable.ico_choose);
				CashSelectActivity.this.mIndex = position;
				CashSelectActivity.this.finish();
				break;
			case 1:
				Toast.makeText(CashSelectActivity.this.getApplicationContext(),
						R.string.cash_used_yet, 0).show();
				break;
			case 2:
				Toast.makeText(CashSelectActivity.this.getApplicationContext(),
						R.string.cash_invalid_yet, 0).show();
			}

		}
	};

	public CashSelectActivity() {
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mIndex = -1;
		this.setContentView(R.layout.cashlist_select_layout);
		this.mCashGifts = (ArrayList) this.getIntent().getSerializableExtra(
				"cash_list");
		if (this.mCashGifts == null) {
			this.finish();
		}

		this.mNoChooseImg = (ImageView) this
				.findViewById(R.id.cash_no_choose_img);
		View noChooseLayout = this.findViewById(R.id.no_choose_layout);
		ListView mCashListView = (ListView) this.findViewById(R.id.lv_cash_gift);
		this.mIndex = this.getIntent().getIntExtra("cash_select_index", -1);
		this.mAdapter = new CashSelectActivity.CashAdapter(this.mCashGifts,
				this, this.mIndex);
		mCashListView.setAdapter(this.mAdapter);
		mCashListView.setOnItemClickListener(this.mClickListener);
		if (this.mIndex == -1) {
			this.mNoChooseImg.setImageResource(R.drawable.ico_choose);
		}

		noChooseLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CashSelectActivity.this.mIndex = -1;
				CashSelectActivity.this.mNoChooseImg
						.setImageResource(R.drawable.ico_choose);
				CashSelectActivity.this.mAdapter
						.setSelected(CashSelectActivity.this.mIndex);
				CashSelectActivity.this.mAdapter.notifyDataSetChanged();
				CashSelectActivity.this.finish();
			}
		});
	}

	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("cash_select_index", this.mIndex);
		this.setResult(-1, intent);
		super.finish();
		this.overridePendingTransition(R.anim.uubee_slide_in_left,
				R.anim.uubee_slide_out_right);
	}

	private class CashAdapter extends BaseAdapter {
		private ArrayList<CashGift> cashGifts;
		private Context mContext;
		private String[] mTypes;
		private int selected;
		private int[] drawables;

		public CashAdapter(ArrayList<CashGift> list, Context context,
				int selected) {
			this.drawables = new int[] { R.drawable.uubee_cash_item_type_bg_red,
					R.drawable.uubee_cash_item_type_bg_blue,
					R.drawable.uubee_cash_item_type_bg_red };
			this.cashGifts = list;
			this.mContext = context;
			this.selected = selected;
			this.mTypes = context.getResources()
					.getStringArray(R.array.cash_type);
		}

		public int getCount() {
			return this.cashGifts == null ? 0 : this.cashGifts.size();
		}

		public CashGift getItem(int position) {
			return this.cashGifts == null ? null : (CashGift) this.cashGifts
					.get(position);
		}

		public long getItemId(int position) {
			return 0L;
		}

		public void setSelected(int selected) {
			this.selected = selected;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			CashSelectActivity.CashAdapter.Holder mHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(this.mContext).inflate(
						R.layout.uubee_cash_item, parent, false);
				mHolder = new CashSelectActivity.CashAdapter.Holder();
				mHolder.mTypeBg = convertView
						.findViewById(R.id.cash_item_type_layout);
				mHolder.mCashValueView = (TextView) convertView
						.findViewById(R.id.cash_item_value);
				mHolder.mCashTypeView = (TextView) convertView
						.findViewById(R.id.cash_item_type);
				mHolder.mCashValidView = (TextView) convertView
						.findViewById(R.id.cash_valid_until);
				mHolder.mChooseImg = (ImageView) convertView
						.findViewById(R.id.cash_choose_img);
				mHolder.mInvalidImg = (ImageView) convertView
						.findViewById(R.id.cash_invalid_img);
				convertView.setTag(mHolder);
			} else {
				mHolder = (CashSelectActivity.CashAdapter.Holder) convertView
						.getTag();
			}

			this.setItemInfo(position, mHolder);
			return convertView;
		}

		private void setItemInfo(int position,
				CashSelectActivity.CashAdapter.Holder holder) {
			CashGift cash = (CashGift) this.cashGifts.get(position);
			int type = cash.type_cashgift - 1;
			holder.mCashTypeView.setText(this.mTypes[type]);
			if (cash.sta_cashgift == 2) {
				holder.mTypeBg
						.setBackgroundResource(R.drawable.uubee_cash_item_type_bg_gray);
				holder.mInvalidImg.setImageResource(R.drawable.ico_pay_pass);
			} else {
				holder.mTypeBg.setBackgroundResource(this.drawables[type]);
				holder.mInvalidImg.setImageResource(0);
			}

			if (position == this.selected) {
				holder.mChooseImg.setImageResource(R.drawable.ico_choose);
			} else {
				holder.mChooseImg.setImageResource(0);
			}

			String money = CashSelectActivity.this
					.getString(R.string.pay_money_unit_text)
					+ " "
					+ cash.amt_cashgift;
			int end = money.indexOf(".");
			SpannableString moneySpan = new SpannableString(money);
			moneySpan.setSpan(new RelativeSizeSpan(2.0F), 2, end, 33);
			holder.mCashValueView.setText(moneySpan);
			String validStr = "";
			if (TextUtils.isEmpty(validStr)) {
				validStr = cash.dt_end.substring(0, 4) + "."
						+ cash.dt_end.substring(4, 6) + "."
						+ cash.dt_end.substring(6, 8);
			}

			holder.mCashValidView.setText(CashSelectActivity.this.getString(
					R.string.cash_valid_util_text, new Object[] { validStr }));
		}

		private class Holder {
			private View mTypeBg;
			private TextView mCashValueView;
			private TextView mCashTypeView;
			private TextView mCashValidView;
			private ImageView mChooseImg;
			private ImageView mInvalidImg;

			private Holder() {

			}
		}
	}
}
