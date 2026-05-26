@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app // NOSONAR

import com.squareup.inject.assisted.dagger2.AssistedModule // NOSONAR
import dagger.Module // NOSONAR

@AssistedModule //NOSONAR
@Module(includes = [AssistedInject_AppAssistedModule::class]) //NOSONAR
abstract class AppAssistedModule //NOSONAR
