package com.uubee.prepay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class SoftCloseEditText extends EditText {
	private SoftCloseEditText.SoftCloseListener mListener;

	public SoftCloseEditText(Context context) {
		super(context);
	}

	public SoftCloseEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SoftCloseEditText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setOnCloseListener(SoftCloseEditText.SoftCloseListener listener) {
		this.mListener = listener;
	}

	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == 4 && this.mListener != null) {
			this.mListener.onClose();
		}

		return super.dispatchKeyEvent(event);
	}

	public interface SoftCloseListener {
		void onClose();
	}
}
