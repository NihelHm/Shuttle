@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier")

package com.simplecity.amp_library.di.app.activity

import android.app.Activity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    @ActivityScope
    fun activity(activity: AppCompatActivity): Activity {
        return activity
    }

    @Provides
    @ActivityScope
    fun fragmentManager(activity: AppCompatActivity): FragmentManager {
        return activity.supportFragmentManager
    }
}
