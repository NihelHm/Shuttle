@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app

import android.content.Context
import com.simplecity.amp_library.data.AlbumArtistsRepository
import com.simplecity.amp_library.data.AlbumsRepository
import com.simplecity.amp_library.data.BlacklistRepository
import com.simplecity.amp_library.data.GenresRepository
import com.simplecity.amp_library.data.PlaylistsRepository
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.data.SongsRepository
import com.simplecity.amp_library.data.WhitelistRepository
import com.simplecity.amp_library.di.app.RepositoryModule.AbsRepositoryModule
import com.simplecity.amp_library.sql.databases.BlacklistWhitelistDbOpenHelper
import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module(includes = [AbsRepositoryModule::class]) //NOSONAR
class RepositoryModule { //NOSONAR

    @Provides //NOSONAR
    @Singleton //NOSONAR
    fun provideInclExclDatabase(context: Context): BriteDatabase { //NOSONAR
        return SqlBrite.Builder() //NOSONAR
            .build() //NOSONAR
            .wrapDatabaseHelper(BlacklistWhitelistDbOpenHelper(context), Schedulers.io()) //NOSONAR
    }

    @Module //NOSONAR
    abstract class AbsRepositoryModule { //NOSONAR

        @Binds //NOSONAR
        @Singleton //NOSONAR
        abstract fun bindSongsRepository(songsRepository: SongsRepository): Repository.SongsRepository //NOSONAR

        @Binds //NOSONAR
        @Singleton //NOSONAR
        abstract fun bindAlbumsRepository(albumsRepository: AlbumsRepository): Repository.AlbumsRepository //NOSONAR

        @Binds //NOSONAR
        @Singleton //NOSONAR
        abstract fun bindAlbumArtistsRepository(albumArtistsRepository: AlbumArtistsRepository): Repository.AlbumArtistsRepository //NOSONAR

        @Binds //NOSONAR
        @Singleton //NOSONAR
        abstract fun bindGenresRepository(genresRepository: GenresRepository): Repository.GenresRepository //NOSONAR

        @Binds //NOSONAR
        @Singleton //NOSONAR
        abstract fun bindPlaylistsRepository(playlistsRepository: PlaylistsRepository): Repository.PlaylistsRepository //NOSONAR

        @Binds //NOSONAR
        @Singleton //NOSONAR
        abstract fun bindBlacklistRepository(blacklistRepository: BlacklistRepository): Repository.BlacklistRepository //NOSONAR

        @Binds //NOSONAR
        @Singleton //NOSONAR
        abstract fun bindWhitelistRepository(whitelistRepository: WhitelistRepository): Repository.WhitelistRepository //NOSONAR
    }
}
