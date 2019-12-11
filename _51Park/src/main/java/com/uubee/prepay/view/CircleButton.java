package com.uubee.prepay.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import cn.com.unispark.R;

public class CircleButton extends AppCompatImageView {
	private static final int PRESSED_COLOR_LIGHTUP = 10;
	private static final int PRESSED_RING_ALPHA = 75;
	private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 4;
	private static final int ANIMATION_TIME_ID = 17694722;
	private int centerY;
	private int centerX;
	private int pressedRingRadius;
	private Paint circlePaint;
	private Paint focusPaint;
	private float animationProgress;
	private int pressedRingWidth;
	private int defaultColor = -16777216;
	private int pressedColor;
	private boolean isAnimStart;
	private ObjectAnimator pressedAnimator;

	public CircleButton(Context context) {
		super(context);
		this.init(context, (AttributeSet) null);
	}

	public CircleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context, attrs);
	}

	public CircleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init(context, attrs);
	}

	public void startRingAnim() {
		if (!this.isAnimStart) {
			if (this.circlePaint != null) {
				this.circlePaint.setColor(this.pressedColor);
			}

			this.isAnimStart = true;
			this.showPressedRing();
		}
	}

	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if (!this.isAnimStart) {
			if (this.circlePaint != null) {
//				this.circlePaint.setColor(pressed ? this.pressedColor: this.defaultColor);
			}

			if (pressed) {
				this.showPressedRing();
			} else {
				this.hidePressedRing();
			}

		}
	}

	protected void onDraw(Canvas canvas) {
		canvas.drawCircle((float) this.centerX, (float) this.centerY,
				(float) this.pressedRingRadius + this.animationProgress,
				this.focusPaint);
		super.onDraw(canvas);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.centerX = w / 2;
		this.centerY = h / 2;
		int outerRadius = Math.min(this.getDrawable().getIntrinsicWidth(), this
				.getDrawable().getIntrinsicHeight()) / 2;
		this.pressedRingRadius = outerRadius - this.pressedRingWidth;
	}

	public float getAnimationProgress() {
		return this.animationProgress;
	}

	public void setAnimationProgress(float animationProgress) {
		this.animationProgress = animationProgress;
		this.invalidate();
	}

	public void setColor(int color) {
		this.defaultColor = color;
		this.pressedColor = this.getHighlightColor(color, 10);
		this.circlePaint.setColor(this.defaultColor);
		this.focusPaint.setColor(this.defaultColor);
		this.focusPaint.setAlpha(75);
		this.invalidate();
	}

	private void hidePressedRing() {
		if (this.canAnimator()) {
			this.pressedAnimator.setFloatValues(new float[] {
					(float) this.pressedRingWidth, 0.0F });
			this.pressedAnimator.start();
		}
	}

	private void showPressedRing() {
		if (this.canAnimator()) {
			this.pressedAnimator.setFloatValues(new float[] {
					this.animationProgress, (float) this.pressedRingWidth });
			this.pressedAnimator.start();
		}
	}

	private void init(Context context, AttributeSet attrs) {
		this.setFocusable(true);
		this.setScaleType(ScaleType.CENTER_INSIDE);
		this.setClickable(true);
		this.circlePaint = new Paint(1);
		this.circlePaint.setStyle(Style.FILL);
		this.focusPaint = new Paint(1);
		this.focusPaint.setStyle(Style.STROKE);
		this.pressedRingWidth = (int) TypedValue.applyDimension(1, 4.0F, this
				.getResources().getDisplayMetrics());
		int color = -16777216;
//		int defaultAnimationTime = this.getResources().getInteger(17694722);
		int pressedAnimationTime = 0;
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CircleButton);
//			color = a.getColor(R.styleable.CircleButton_cb_color, color);
			this.pressedRingWidth = (int) a.getDimension(
					R.styleable.CircleButton_cb_pressedRingWidth,
					(float) this.pressedRingWidth);
//			pressedAnimationTime = a.getInt(R.styleable.CircleButton_cb_duration,
//					defaultAnimationTime);
			a.recycle();
		}

		this.setColor(color);
		this.focusPaint.setStrokeWidth((float) this.pressedRingWidth);
		if (this.canAnimator()) {
			this.pressedAnimator = ObjectAnimator.ofFloat(this,
					"animationProgress", new float[] { 0.0F, 0.0F });
			this.pressedAnimator.setDuration((long) pressedAnimationTime);
			this.pressedAnimator.setRepeatCount(-1);
		}

		this.isAnimStart = false;
	}

	private int getHighlightColor(int color, int amount) {
		return Color.argb(Math.min(255, Color.alpha(color)),
				Math.min(255, Color.red(color) + amount),
				Math.min(255, Color.green(color) + amount),
				Math.min(255, Color.blue(color) + amount));
	}

	protected boolean canAnimator() {
		return VERSION.SDK_INT > 10;
	}
}
