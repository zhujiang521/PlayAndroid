package com.zj.core.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import java.util.*

/**
 * 版权：渤海新能 版权所有
 *
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 */
class FlowLayout : ViewGroup {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //   super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //遍历去调用所有子元素的measure方法（child.getMeasuredHeight()才能获取到值，否则为0）
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var measuredWidth = 0
        var measuredHeight = 0
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //由于计算子view所占宽度，这里传值需要自身减去PaddingRight宽度，PaddingLeft会在接下来计算子元素位置时加上
        val compute = compute(widthSize - paddingRight)

        //EXACTLY模式：对应于给定大小或者match_parent情况
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize
            //AT_MOS模式：对应wrap-content（需要手动计算大小，否则相当于match_parent）
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = compute["allChildWidth"] ?: error("")
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            measuredHeight = compute["allChildHeight"] ?: error("")
        }
        //设置flow的宽高
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val rect = getChildAt(i).tag as Rect
            child.layout(rect.left, rect.top, rect.right, rect.bottom)
        }
    }

    /**
     * 测量过程
     *
     * @param flowWidth 该view的宽度
     * @return 返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
     */
    private fun compute(flowWidth: Int): Map<String, Int> {
        //是否是单行
        var aRow = true
        var marginParams: MarginLayoutParams //子元素margin
        var rowsWidth = paddingLeft //当前行已占宽度(注意需要加上paddingLeft)
        var columnHeight = paddingTop //当前行顶部已占高度(注意需要加上paddingTop)
        var rowsMaxHeight = 0 //当前行所有子元素的最大高度（用于换行累加高度）
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            //获取元素测量宽度和高度
            val measuredWidth = child.measuredWidth
            val measuredHeight = child.measuredHeight
            //获取元素的margin
            marginParams = child.layoutParams as MarginLayoutParams
            //子元素所占宽度 = MarginLeft+ child.getMeasuredWidth+MarginRight  注意此时不能child.getWidth,因为界面没有绘制完成，此时wdith为0
            val childWidth = marginParams.leftMargin + marginParams.rightMargin + measuredWidth
            val childHeight = marginParams.topMargin + marginParams.bottomMargin + measuredHeight
            //判断是否换行： 该行已占大小+该元素大小>父容器宽度  则换行
            rowsMaxHeight = rowsMaxHeight.coerceAtLeast(childHeight)
            //换行
            if (rowsWidth + childWidth > flowWidth) {
                //重置行宽度
                rowsWidth = paddingLeft + paddingRight
                //累加上该行子元素最大高度
                columnHeight += rowsMaxHeight
                //重置该行最大高度
                rowsMaxHeight = childHeight
                aRow = false
            }
            //累加上该行子元素宽度
            rowsWidth += childWidth
            //判断时占的宽段时加上margin计算，设置顶点位置时不包括margin位置，不然margin会不起作用，这是给View设置tag,在onlayout给子元素设置位置再遍历取出
            child.tag = Rect(
                rowsWidth - childWidth + marginParams.leftMargin,
                columnHeight + marginParams.topMargin,
                rowsWidth - marginParams.rightMargin,
                columnHeight + childHeight - marginParams.bottomMargin
            )
        }

        //返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
        val flowMap: MutableMap<String, Int> = HashMap()
        //单行
        if (aRow) {
            flowMap["allChildWidth"] = rowsWidth
        } else {
            //多行
            flowMap["allChildWidth"] = flowWidth
        }
        //FlowLayout测量高度 = 当前行顶部已占高度 +当前行内子元素最大高度+FlowLayout的PaddingBottom
        flowMap["allChildHeight"] = columnHeight + rowsMaxHeight + paddingBottom
        return flowMap
    }
}