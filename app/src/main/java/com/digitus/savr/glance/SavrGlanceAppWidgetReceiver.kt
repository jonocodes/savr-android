
package com.digitus.savr.glance

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.digitus.savr.glance.ui.SavrGlanceAppWidget

class SavrGlanceAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SavrGlanceAppWidget()
}
