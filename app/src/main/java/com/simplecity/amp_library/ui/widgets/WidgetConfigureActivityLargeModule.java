package com.simplecity.amp_library.ui.widgets;

import android.support.v7.app.AppCompatActivity;
import com.simplecity.amp_library.billing.BillingManager;
import com.simplecity.amp_library.di.app.activity.ActivityModule;
import com.simplecity.amp_library.di.app.activity.ActivityScope;
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope;
import com.simplecity.amp_library.ui.screens.widgets.WidgetFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module(includes = ActivityModule.class)
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public abstract class WidgetConfigureActivityLargeModule {

    @Binds
    @ActivityScope
    abstract AppCompatActivity appCompatActivity(WidgetConfigureActivityLarge activity);

    @Provides
    static BillingManager.BillingUpdatesListener provideBillingUpdatesListener(WidgetConfigureActivityLarge activity) {
        return activity;
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = WidgetFragmentModule.class)
    abstract WidgetFragment widgetFragmentInjector();
}
