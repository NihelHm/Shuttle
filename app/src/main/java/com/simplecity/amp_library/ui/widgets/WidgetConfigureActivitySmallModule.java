package com.simplecity.amp_library.ui.widgets;

import android.support.v7.app.AppCompatActivity;
import com.simplecity.amp_library.billing.BillingManager;
import com.simplecity.amp_library.di.app.activity.ActivityModule;
import com.simplecity.amp_library.di.app.activity.ActivityScope;
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope;
import com.simplecity.amp_library.ui.screens.main.LibraryController;
import com.simplecity.amp_library.ui.screens.main.LibraryFragmentModule;
import com.simplecity.amp_library.ui.screens.widgets.WidgetFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module(includes = ActivityModule.class) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class WidgetConfigureActivitySmallModule { //NOSONAR

    @Binds //NOSONAR
    @ActivityScope //NOSONAR
    abstract AppCompatActivity appCompatActivity(WidgetConfigureActivitySmall activity); //NOSONAR

    @Provides //NOSONAR
    static BillingManager.BillingUpdatesListener provideBillingUpdatesListener(WidgetConfigureActivitySmall activity) { //NOSONAR
        return activity; //NOSONAR
    }

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = WidgetFragmentModule.class) //NOSONAR
    abstract WidgetFragment widgetFragmentInjector(); //NOSONAR
}
