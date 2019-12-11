package cn.com.unispark.fragment.mine.plate.view.keyboard;

import java.util.List;

import cn.com.unispark.R;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * 自定义键盘 省份键盘 英文大写键盘
 * 
 * @author renjianfei
 * 
 */
public class KeyboardUtil {
	private Context ctx;
	private Activity act;
	private KeyboardView keyboardView;
	private Keyboard k1;// 省份键盘
	private Keyboard k2;// 英文大写键盘
	public boolean isnun = true;
	public boolean isupper = false;// 是否大写

	private EditText ed;

	public KeyboardUtil(Activity act, Context ctx, EditText edit) {
		this.act = act;
		this.ctx = ctx;
		this.ed = edit;
		k1 = new Keyboard(ctx, R.xml.plateqwerty);
		k2 = new Keyboard(ctx, R.xml.qwerty);
		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
			Log.e("slx", "text" + text);
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(k1);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
					keyboardView.setKeyboard(k2);
				}
			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else {
				// k1.getKeys();
				if (isnun) {
					isnun = false;
					Log.e("slx", "editable.length()------" + editable.length());
					if (editable.length() == 2) {
						editable.replace(0, 1,
								k1.getKeys().get(primaryCode).label.toString());
					} else {
						editable.insert(0,
								k1.getKeys().get(primaryCode).label.toString());

					}
					keyboardView.setKeyboard(k2);
				} else {
					isnun = true;
					Log.e("slx",
							"editable.length()------->>>>>>"
									+ editable.length());
					if (editable.length() == 2) {
						editable.replace(1, 2,
								Character.toString((char) primaryCode));
					} else {
						editable.insert(1,
								Character.toString((char) primaryCode));

					}
					hideKeyboard();
				}
			}
		}
	};

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

}
