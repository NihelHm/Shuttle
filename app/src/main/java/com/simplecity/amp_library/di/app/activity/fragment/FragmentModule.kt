@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app.activity.fragment // NOSONAR

import android.support.v4.app.Fragment // NOSONAR
import com.bumptech.glide.Glide // NOSONAR
import com.bumptech.glide.RequestManager // NOSONAR
import dagger.Module // NOSONAR
import dagger.Provides // NOSONAR
import javax.inject.Named // NOSONAR

@Module //NOSONAR
class FragmentModule { //NOSONAR

    @Provides //NOSONAR
    @FragmentScope //NOSONAR
    fun provideRequestManager(@Named(FRAGMENT) fragment: Fragment): RequestManager { //NOSONAR
        return Glide.with(fragment) //NOSONAR
    } // NOSONAR

    companion object { //NOSONAR
        const val FRAGMENT = "FragmentModule.fragment" //NOSONAR
    } // NOSONAR
} // NOSONAR
