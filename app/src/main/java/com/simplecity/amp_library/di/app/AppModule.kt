@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app // NOSONAR

import android.content.Context // NOSONAR
import android.content.SharedPreferences // NOSONAR
import android.preference.PreferenceManager // NOSONAR
import com.simplecity.amp_library.ShuttleApplication // NOSONAR
import com.simplecity.amp_library.di.app.activity.ActivityScope // NOSONAR
import com.simplecity.amp_library.playback.MusicService // NOSONAR
import com.simplecity.amp_library.services.ArtworkDownloadService // NOSONAR
import com.simplecity.amp_library.ui.screens.main.MainActivity // NOSONAR
import com.simplecity.amp_library.ui.screens.main.MainActivityModule // NOSONAR
import com.simplecity.amp_library.ui.screens.shortcut.ShortcutTrampolineActivity // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityExtraLarge // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityExtraLargeModule // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityLarge // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityLargeModule // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityMedium // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivityMediumModule // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivitySmall // NOSONAR
import com.simplecity.amp_library.ui.widgets.WidgetConfigureActivitySmallModule // NOSONAR
import com.simplecity.amp_library.utils.MediaButtonIntentReceiver // NOSONAR
import dagger.Module // NOSONAR
import dagger.Provides // NOSONAR
import dagger.android.ContributesAndroidInjector // NOSONAR
import dagger.android.support.AndroidSupportInjectionModule // NOSONAR
import javax.inject.Singleton // NOSONAR

@Module(includes = [AppModuleBinds::class]) //NOSONAR
class AppModule { //NOSONAR

    @Provides //NOSONAR
    fun provideContext(application: ShuttleApplication): Context = application.applicationContext //NOSONAR

    @Provides //NOSONAR
    @Singleton //NOSONAR
    fun provideSharedPreferences(context: Context): SharedPreferences { //NOSONAR
        return PreferenceManager.getDefaultSharedPreferences(context) //NOSONAR
    } // NOSONAR
} // NOSONAR

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
} // NOSONAR
