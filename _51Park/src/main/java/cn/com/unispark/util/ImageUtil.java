package cn.com.unispark.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * <pre>
 * 功能说明： 加载图片的工具类
 * 日期：	2015年11月9日
 * 开发者：	陈丶泳佐
 * 版本信息：V5.0.0
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年11月9日
 * </pre>
 */
public class ImageUtil {

	public static void loadImage(Context context, ImageView imageView,
			String url) {
		imageView.setScaleType(ScaleType.FIT_XY);
		/**
		 * 开始图片加载
		 */
		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
				.crossFade().into(imageView);

	}

	/**
	 * <pre>
	 * 功能说明： Bitmap 计算图片的压缩比例
	 * 日期：	2015年11月19日
	 * 开发者：	任建飞
	 * </pre>
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的宽度
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;
		if (width > reqWidth || height > reqHeight) {
			// 计算出实际宽度和目标宽度的比率
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			inSampleSize = widthRatio > heightRatio ? widthRatio : heightRatio;
		}
		return inSampleSize;
	}

}
