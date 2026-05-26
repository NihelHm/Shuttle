package com.simplecity.amp_library.ui.screens.artist.list;

import android.support.v4.app.Fragment;
import com.simplecity.amp_library.di.app.activity.fragment.FragmentModule;
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope;
import dagger.Binds;
import dagger.Module;
import javax.inject.Named;

@Module(includes = FragmentModule.class) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class AlbumArtistListFragmentModule { //NOSONAR

    @Binds //NOSONAR
    @Named(FragmentModule.FRAGMENT) //NOSONAR
    @FragmentScope //NOSONAR
    abstract Fragment fragment(AlbumArtistListFragment artistFragment); //NOSONAR
}
