@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.simplecity.amp_library.ShuttleApplication
import com.simplecity.amp_library.di.app.activity.ActivityScope
import com.simplecity.amp_library.playback.MusicService
import com.simplecity.amp_library.services.ArtworkDownloadService
import com.simplecity.amp_library.ui.screens.main.MainActivity
import com.simplecity.amp_library.ui.screens.main.MainActivityModule
import com.simplecity.amp_library.ui.screens.shortcut.ShortcutTrampolineActivity
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityExtraLarge
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityExtraLargeModule
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityLarge
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityLargeModule
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityMedium
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityMediumModule
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivitySmall
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivitySmallModule
import com.simplecity.amp_library.utils.MediaButtonIntentReceiver
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module(includes = [AppModuleBinds::class]) //NOSONAR
class AppModule { //NOSONAR

    @Provides //NOSONAR
    fun provideContext(application: ShuttleApplication): Context = application.applicationContext //NOSONAR

    @Provides //NOSONAR
    @Singleton //NOSONAR
    fun provideSharedPreferences(context: Context): SharedPreferences { //NOSONAR
        return PreferenceManager.getDefaultSharedPreferences(context) //NOSONAR
    }
}

@Module(includes = [AndroidSupportInjectionModule::class]) //NOSONAR
abstract class AppModuleBinds { //NOSONAR

    @ActivityScope //NOSONAR
    @ContributesAndroidInjector(modules = [MainActivityModule::class]) //NOSONAR
    abstract fun mainActivityInjector(): MainActivity //NOSONAR

    @ContributesAndroidInjector //NOSONAR
    abstract fun musicServiceInjector(): MusicService //NOSONAR

    @ContributesAndroidInjector //NOSONAR
    abstract fun artworkServiceInjector(): ArtworkDownloadService //NOSONAR

    @ContributesAndroidInjector //NOSONAR
    abstract fun mediaButtonIntentReceiverInjector(): MediaButtonIntentReceiver //NOSONAR

    @ActivityScope //NOSONAR
    @ContributesAndroidInjector //NOSONAR
    abstract fun shortcutTrampolineActivityInjector(): ShortcutTrampolineActivity //NOSONAR

    @ActivityScope //NOSONAR
    @ContributesAndroidInjector(modules = [WidgetConfigureActivitySmallModule::class]) //NOSONAR
    abstract fun widgetConfigureActivitySmallInjector(): WidgetConfigureActivitySmall //NOSONAR

    @ActivityScope //NOSONAR
    @ContributesAndroidInjector(modules = [WidgetConfigureActivityMediumModule::class]) //NOSONAR
    abstract fun widgetConfigureActivityMediumInjector(): WidgetConfigureActivityMedium //NOSONAR

    @ActivityScope //NOSONAR
    @ContributesAndroidInjector(modules = [WidgetConfigureActivityLargeModule::class]) //NOSONAR
    abstract fun widgetConfigureActivityLargeInjector(): WidgetConfigureActivityLarge //NOSONAR

    @ActivityScope //NOSONAR
    @ContributesAndroidInjector(modules = [WidgetConfigureActivityExtraLargeModule::class]) //NOSONAR
    abstract fun widgetConfigureActivityExtraLargeInjector(): WidgetConfigureActivityExtraLarge //NOSONAR
}
