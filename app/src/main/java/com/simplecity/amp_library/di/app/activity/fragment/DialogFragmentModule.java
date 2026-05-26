package com.simplecity.amp_library.di.app.activity.fragment; // NOSONAR

import android.support.v4.app.DialogFragment; // NOSONAR
import dagger.Binds; // NOSONAR
import dagger.Module; // NOSONAR
import javax.inject.Named; // NOSONAR

@Module //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class DialogFragmentModule { //NOSONAR

    @Binds //NOSONAR
    @Named(FragmentModule.FRAGMENT) //NOSONAR
    @FragmentScope //NOSONAR
    abstract DialogFragment fragment(DialogFragment dialogFragment); //NOSONAR
} // NOSONAR
