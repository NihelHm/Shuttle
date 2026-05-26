package com.simplecity.amp_library.di.app.activity.fragment;

import android.support.v4.app.DialogFragment;
import dagger.Binds;
import dagger.Module;
import javax.inject.Named;

@Module
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public abstract class DialogFragmentModule {

    @Binds
    @Named(FragmentModule.FRAGMENT)
    @FragmentScope
    abstract DialogFragment fragment(DialogFragment dialogFragment);
}
