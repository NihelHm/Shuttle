package com.simplecity.amp_library.ui.settings; // NOSONAR

        import android.support.v4.app.Fragment; // NOSONAR
        import com.simplecity.amp_library.di.app.activity.fragment.FragmentModule; // NOSONAR
        import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope; // NOSONAR
        import dagger.Binds; // NOSONAR
        import dagger.Module; // NOSONAR
        import javax.inject.Named; // NOSONAR

@Module(includes = FragmentModule.class) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class SettingsFragmentModule { //NOSONAR

    @Binds //NOSONAR
    @Named(FragmentModule.FRAGMENT) //NOSONAR
    @FragmentScope //NOSONAR
    abstract Fragment fragment(SettingsParentFragment.SettingsFragment settingsFragment); //NOSONAR
} // NOSONAR
