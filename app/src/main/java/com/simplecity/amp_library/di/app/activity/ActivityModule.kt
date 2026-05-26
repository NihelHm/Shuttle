@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app.activity // NOSONAR

import android.app.Activity // NOSONAR
import android.support.v4.app.FragmentManager // NOSONAR
import android.support.v7.app.AppCompatActivity // NOSONAR
import dagger.Module // NOSONAR
import dagger.Provides // NOSONAR

@Module //NOSONAR
class ActivityModule { //NOSONAR

    @Provides //NOSONAR
    @ActivityScope //NOSONAR
    fun activity(activity: AppCompatActivity): Activity { //NOSONAR
        return activity //NOSONAR
    } // NOSONAR

    @Provides //NOSONAR
    @ActivityScope //NOSONAR
    fun fragmentManager(activity: AppCompatActivity): FragmentManager { //NOSONAR
        return activity.supportFragmentManager //NOSONAR
    } // NOSONAR
} // NOSONAR
