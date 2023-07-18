package com.zj.play.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.zj.play.R

/**
 * Implementation of App Widget functionality.
 */
class ArticleListWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ArticleListWidgetGlance()
}