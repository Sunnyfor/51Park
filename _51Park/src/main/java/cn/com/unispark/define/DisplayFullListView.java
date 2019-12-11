package cn.com.unispark.define;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * <pre>
 * 功能说明： 自定义ListView，解决ScrollView嵌套ListView列表显示不全的问题
 * 日期：	2015年6月24日
 * 开发者：	陈丶泳佐
 * 版本信息：V
 * 版权声明：版权所有@北京百会易泊科技有限公司
 * 
 * 历史记录
 *    修改内容：
 *    修改人员：
 *    修改日期： 2015年6月24日
 * </pre>
 */
public class DisplayFullListView extends ListView {
	
    public DisplayFullListView(Context context) {
        super(context);
    }

    public DisplayFullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayFullListView(Context context, AttributeSet attrs,
        int defStyle) {
        super(context, attrs, defStyle);
    }
   

	@Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
