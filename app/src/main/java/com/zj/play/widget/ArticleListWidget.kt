package com.zj.play.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * Implementation of App Widget functionality.
 */
class ArticleListWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ArticleListWidgetGlance()
}