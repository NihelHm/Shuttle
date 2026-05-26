@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app.activity.fragment

import android.support.v4.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module //NOSONAR
class FragmentModule { //NOSONAR

    @Provides //NOSONAR
    @FragmentScope //NOSONAR
    fun provideRequestManager(@Named(FRAGMENT) fragment: Fragment): RequestManager { //NOSONAR
        return Glide.with(fragment) //NOSONAR
    }

    companion object { //NOSONAR
        const val FRAGMENT = "FragmentModule.fragment" //NOSONAR
    }
}
