package com.zj.play.widget

import android.content.Context
import androidx.compose.runtime.key
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.zj.model.ArticleModel
import com.zj.network.PlayAndroidNetwork
import com.zj.play.R
import com.zj.play.ui.theme.GlanceTextStyles
import com.zj.utils.XLog

class ArticleListWidgetGlance : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val articleList = getArticleList()
        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.surface)
                        .appWidgetBackgroundCornerRadius(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (articleList.isNotEmpty()) {
                        key(LocalSize.current) {
                            Text(
                                text = stringResource(id = R.string.widget_name),
                                modifier = GlanceModifier.padding(10.dp),
                                style = GlanceTextStyles.bodyLarge.copy(color = GlanceTheme.colors.onBackground)
                            )
                            LazyColumn(
                                modifier = GlanceModifier.fillMaxSize().padding(horizontal = 10.dp)
                            ) {
                                items(articleList) { data ->
                                    GlanceArticleItem(context, data)
                                }
                            }
                        }

                    } else {
                        CircularProgressIndicator(
                            modifier = GlanceModifier.clickable(
                                actionRunCallback<RefreshAction>()
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun getArticleList(): List<ArticleModel> {
        XLog.i("Obtain the home page list data")
        val apiResponse = PlayAndroidNetwork.getArticle(0)
        return apiResponse.data.datas
    }

    companion object {
        const val ARTICLE_DATA: String = "ARTICLE_DATA"
    }

}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Clear the state to show loading screen
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs.clear()
        }
        ArticleListWidgetGlance().update(context, glanceId)
    }
}