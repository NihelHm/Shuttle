package com.simplecity.amp_library.ui.widgets; // NOSONAR

import android.support.v7.app.AppCompatActivity; // NOSONAR
import com.simplecity.amp_library.billing.BillingManager; // NOSONAR
import com.simplecity.amp_library.di.app.activity.ActivityModule; // NOSONAR
import com.simplecity.amp_library.di.app.activity.ActivityScope; // NOSONAR
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope; // NOSONAR
import com.simplecity.amp_library.ui.screens.widgets.WidgetFragment; // NOSONAR
import dagger.Binds; // NOSONAR
import dagger.Module; // NOSONAR
import dagger.Provides; // NOSONAR
import dagger.android.ContributesAndroidInjector; // NOSONAR

@Module(includes = ActivityModule.class) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class WidgetConfigureActivityExtraLargeModule { //NOSONAR

    @Binds //NOSONAR
    @ActivityScope //NOSONAR
    abstract AppCompatActivity appCompatActivity(WidgetConfigureActivityExtraLarge activity); //NOSONAR

    @Provides //NOSONAR
    static BillingManager.BillingUpdatesListener provideBillingUpdatesListener(WidgetConfigureActivityExtraLarge activity) { //NOSONAR
        return activity; //NOSONAR
    } // NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = WidgetFragmentModule.class) //NOSONAR
    abstract WidgetFragment widgetFragmentInjector(); //NOSONAR
} // NOSONAR
