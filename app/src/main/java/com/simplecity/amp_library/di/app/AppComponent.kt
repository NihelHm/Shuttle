@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app

import com.simplecity.amp_library.ShuttleApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton //NOSONAR
@Component( //NOSONAR
    modules = [ //NOSONAR
        AndroidSupportInjectionModule::class, //NOSONAR
        AppModule::class, //NOSONAR
        AppAssistedModule::class, //NOSONAR
        RepositoryModule::class //NOSONAR
    ]
)
interface AppComponent : AndroidInjector<ShuttleApplication> { //NOSONAR
    @Component.Builder //NOSONAR
    abstract class Builder : AndroidInjector.Builder<ShuttleApplication>() //NOSONAR
}
