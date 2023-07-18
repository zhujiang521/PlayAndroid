package com.zj.play.widget

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.Text
import com.zj.model.ArticleModel
import com.zj.play.MainActivity
import com.zj.play.R
import com.zj.play.ui.theme.GlanceTextStyles
import com.zj.play.widget.ArticleListWidgetGlance.Companion.ARTICLE_DATA

@Composable
fun GlanceArticleItem(context: Context, articleModel: ArticleModel) {
    Column {
        Row(
            modifier = GlanceModifier.padding(8.dp)
                .clickable(onClick = openArticle(context, articleModel))
                .cornerRadius(10.dp)
                .background(GlanceTheme.colors.onTertiary),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            GlanceImageLoader(
                modifier = GlanceModifier.size(60.dp).cornerRadius(8.dp),
                data = articleModel.envelopePic.ifBlank { R.drawable.img_default },
            )

            Column(
                modifier = GlanceModifier.padding(start = 10.dp)
            ) {

                Text(
                    text = articleModel.title,
                    maxLines = 1,
                    style = GlanceTextStyles.bodyLarge.copy(color = GlanceTheme.colors.onBackground)
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = articleModel.superChapterName,
                        maxLines = 1,
                        modifier = GlanceModifier
                            .wrapContentWidth()
                    )
                    Text(
                        text = if (TextUtils.isEmpty(articleModel.author)) articleModel.shareUser else articleModel.author,
                        maxLines = 1,
                        modifier = GlanceModifier
                            .padding(start = 5.dp)
                            .wrapContentWidth()
                    )
                }
            }
        }

        Spacer(
            modifier = GlanceModifier.fillMaxWidth().height(10.dp)
        )
    }

}

private fun openArticle(context: Context, article: ArticleModel): Action {
    // actionStartActivity is the preferred way to start activities.
    return actionStartActivity(
        Intent(context, MainActivity::class.java).apply {
            putExtra(ARTICLE_DATA, article)
        }.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    )
}