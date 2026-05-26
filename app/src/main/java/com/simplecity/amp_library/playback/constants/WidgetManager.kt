@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback.constants // NOSONAR

import android.appwidget.AppWidgetManager // NOSONAR
import android.content.Intent // NOSONAR
import android.preference.PreferenceManager // NOSONAR
import com.simplecity.amp_library.playback.MusicService // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetProviderExtraLarge // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetProviderLarge // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetProviderMedium // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetProviderSmall // NOSONAR
import javax.inject.Inject // NOSONAR
import javax.inject.Singleton // NOSONAR

@Singleton //NOSONAR
class WidgetManager @Inject constructor( //NOSONAR
    private val widgetProviderMedium: WidgetProviderMedium, //NOSONAR
    private val widgetProviderSmall: WidgetProviderSmall, //NOSONAR
    private val widgetProviderLarge: WidgetProviderLarge, //NOSONAR
    private val widgetProviderExtraLarge: WidgetProviderExtraLarge //NOSONAR
) { // NOSONAR

    fun processCommand(musicService: MusicService, intent: Intent, command: String) { //NOSONAR

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(musicService) //NOSONAR

        when (command) { //NOSONAR
            WidgetProviderSmall.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderSmall.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            } // NOSONAR
            WidgetProviderMedium.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderMedium.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            } // NOSONAR
            WidgetProviderLarge.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderLarge.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            } // NOSONAR
            WidgetProviderExtraLarge.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderExtraLarge.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    fun notifyChange(musicService: MusicService, what: String) { //NOSONAR
        widgetProviderLarge.notifyChange(musicService, what) //NOSONAR
        widgetProviderMedium.notifyChange(musicService, what) //NOSONAR
        widgetProviderSmall.notifyChange(musicService, what) //NOSONAR
        widgetProviderExtraLarge.notifyChange(musicService, what) //NOSONAR
    } // NOSONAR
} // NOSONAR
