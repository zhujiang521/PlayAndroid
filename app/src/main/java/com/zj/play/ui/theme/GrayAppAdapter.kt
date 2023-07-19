package com.zj.play.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import com.zj.utils.Lunar
import java.util.Calendar

/**
 * 是否需要黑白化应用
 * 如果是南京大屠杀死难者国家公祭日、清明节或中元节的话返回true，反之返回false
 *
 * @return 是否需要黑白化应用
 */
fun isNeedGray(): Boolean {
    val calendar = Calendar.getInstance()
    val month: Int = calendar.get(Calendar.MONTH) + 1
    val day: Int = calendar.get(Calendar.DATE)
    return if (month == 12 && day == 13) {
        true
    } else {
        // 由于清明节不确定，4月4、5、6日都有可能，所以这里都算
        // 中元节比较确定，就是每年的七月十五，所以也置灰
        month == 4 && day == 4 || month == 4 && day == 5 || month == 4 && day == 6
                || Lunar(calendar).isGhostFestival()
    }
}

/**
 * 适配黑白化应用
 *
 * @param isGray 是否置灰 true：置灰  false：正常颜色
 */
@Composable
fun GrayAppAdapter(isGray: Boolean = isNeedGray(), content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        color = MaterialTheme.colors.background
    ) {
        content()
        if (isGray) {
            // 黑白化
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.White,
                    blendMode = BlendMode.Saturation
                )
            }
        }
    }
}