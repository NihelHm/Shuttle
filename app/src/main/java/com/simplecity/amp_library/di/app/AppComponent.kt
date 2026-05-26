@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app // NOSONAR

import com.simplecity.amp_library.ShuttleApplication // NOSONAR
import dagger.Component // NOSONAR
import dagger.android.AndroidInjector // NOSONAR
import dagger.android.support.AndroidSupportInjectionModule // NOSONAR
import javax.inject.Singleton // NOSONAR

@Singleton //NOSONAR
@Component( //NOSONAR
    modules = [ //NOSONAR
        AndroidSupportInjectionModule::class, //NOSONAR
        AppModule::class, //NOSONAR
        AppAssistedModule::class, //NOSONAR
        RepositoryModule::class //NOSONAR
    ] // NOSONAR
) // NOSONAR
interface AppComponent : AndroidInjector<ShuttleApplication> { //NOSONAR
    @Component.Builder //NOSONAR
    abstract class Builder : AndroidInjector.Builder<ShuttleApplication>() //NOSONAR
} // NOSONAR
