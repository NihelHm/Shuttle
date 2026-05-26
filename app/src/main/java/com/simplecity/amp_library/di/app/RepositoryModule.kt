@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.di.app // NOSONAR

import android.content.Context // NOSONAR
import com.simplecity.amp_library.data.AlbumArtistsRepository // NOSONAR
import com.simplecity.amp_library.data.AlbumsRepository // NOSONAR
import com.simplecity.amp_library.data.BlacklistRepository // NOSONAR
import com.simplecity.amp_library.data.GenresRepository // NOSONAR
import com.simplecity.amp_library.data.PlaylistsRepository // NOSONAR
import com.simplecity.amp_library.data.Repository // NOSONAR
import com.simplecity.amp_library.data.SongsRepository // NOSONAR
import com.simplecity.amp_library.data.WhitelistRepository // NOSONAR
import com.simplecity.amp_library.di.app.RepositoryModule.AbsRepositoryModule // NOSONAR
import com.simplecity.amp_library.sql.databases.BlacklistWhitelistDbOpenHelper // NOSONAR
import com.squareup.sqlbrite2.BriteDatabase // NOSONAR
import com.squareup.sqlbrite2.SqlBrite // NOSONAR
import dagger.Binds // NOSONAR
import dagger.Module // NOSONAR
import dagger.Provides // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Singleton // NOSONAR

@Module(includes = [AbsRepositoryModule::class]) //NOSONAR
class RepositoryModule { //NOSONAR

    @Provides //NOSONAR
    @Singleton //NOSONAR
    fun provideInclExclDatabase(context: Context): BriteDatabase { //NOSONAR
        return SqlBrite.Builder() //NOSONAR
            .build() //NOSONAR
            .wrapDatabaseHelper(BlacklistWhitelistDbOpenHelper(context), Schedulers.io()) //NOSONAR
    } // NOSONAR

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
    } // NOSONAR
} // NOSONAR
