package cn.com.unispark.fragment.mine.vipcard.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DrawImageView extends ImageView {

	public DrawImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	Paint paint = new Paint();
	{
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2.5f);// 设置线宽
		paint.setAlpha(100);
	};

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(new Rect(200, 200, 550, 550), paint);// 绘制矩形
	}
}
