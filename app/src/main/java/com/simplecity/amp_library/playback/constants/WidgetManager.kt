@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.playback.constants

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.preference.PreferenceManager
import com.simplecity.amp_library.playback.MusicService
import com.simplecity.amp_library.ui.widgets.WidgetProviderExtraLarge
import com.simplecity.amp_library.ui.widgets.WidgetProviderLarge
import com.simplecity.amp_library.ui.widgets.WidgetProviderMedium
import com.simplecity.amp_library.ui.widgets.WidgetProviderSmall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //NOSONAR
class WidgetManager @Inject constructor( //NOSONAR
    private val widgetProviderMedium: WidgetProviderMedium, //NOSONAR
    private val widgetProviderSmall: WidgetProviderSmall, //NOSONAR
    private val widgetProviderLarge: WidgetProviderLarge, //NOSONAR
    private val widgetProviderExtraLarge: WidgetProviderExtraLarge //NOSONAR
) {

    fun processCommand(musicService: MusicService, intent: Intent, command: String) { //NOSONAR

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(musicService) //NOSONAR

        when (command) { //NOSONAR
            WidgetProviderSmall.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderSmall.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            }
            WidgetProviderMedium.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderMedium.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            }
            WidgetProviderLarge.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderLarge.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            }
            WidgetProviderExtraLarge.CMDAPPWIDGETUPDATE -> { //NOSONAR
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) //NOSONAR
                widgetProviderExtraLarge.update(musicService, sharedPreferences, appWidgetIds, true) //NOSONAR
            }
        }
    }

    fun notifyChange(musicService: MusicService, what: String) { //NOSONAR
        widgetProviderLarge.notifyChange(musicService, what) //NOSONAR
        widgetProviderMedium.notifyChange(musicService, what) //NOSONAR
        widgetProviderSmall.notifyChange(musicService, what) //NOSONAR
        widgetProviderExtraLarge.notifyChange(musicService, what) //NOSONAR
    }
}
