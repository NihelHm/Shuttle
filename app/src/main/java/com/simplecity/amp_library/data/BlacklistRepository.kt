@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.data // NOSONAR

import android.content.ContentValues // NOSONAR
import com.jakewharton.rxrelay2.BehaviorRelay // NOSONAR
import com.simplecity.amp_library.model.InclExclItem // NOSONAR
import com.simplecity.amp_library.model.Song // NOSONAR
import com.simplecity.amp_library.sql.databases.BlacklistWhitelistDbOpenHelper // NOSONAR
import com.squareup.sqlbrite2.BriteDatabase // NOSONAR
import io.reactivex.Observable // NOSONAR
import io.reactivex.disposables.Disposable // NOSONAR
import io.reactivex.schedulers.Schedulers // NOSONAR
import javax.inject.Inject // NOSONAR

open class InclExclRepository @Inject constructor( //NOSONAR
    private val inclExclDatabase: BriteDatabase, //NOSONAR
    @InclExclItem.Type private val type: Int //NOSONAR
) : Repository.InclExclRepository { //NOSONAR

    override fun add(inclExclItem: InclExclItem) { //NOSONAR
        val values = ContentValues(2) //NOSONAR
        values.put(BlacklistWhitelistDbOpenHelper.COLUMN_PATH, inclExclItem.path) //NOSONAR
        values.put(BlacklistWhitelistDbOpenHelper.COLUMN_TYPE, inclExclItem.type) //NOSONAR
        inclExclDatabase.insert(BlacklistWhitelistDbOpenHelper.TABLE_NAME, values) //NOSONAR
    } // NOSONAR

    override fun addAll(inclExclItems: List<InclExclItem>) { //NOSONAR
        val transaction = inclExclDatabase.newTransaction() //NOSONAR
        try { //NOSONAR
            inclExclItems.map { inclExclItem -> //NOSONAR
                val contentValues = ContentValues(2) //NOSONAR
                contentValues.put(BlacklistWhitelistDbOpenHelper.COLUMN_PATH, inclExclItem.path) //NOSONAR
                contentValues.put(BlacklistWhitelistDbOpenHelper.COLUMN_TYPE, inclExclItem.type) //NOSONAR
                contentValues //NOSONAR
            }.forEach { contentValues -> inclExclDatabase.insert(BlacklistWhitelistDbOpenHelper.TABLE_NAME, contentValues) } //NOSONAR
            transaction.markSuccessful() //NOSONAR
        } finally { //NOSONAR
            transaction.end() //NOSONAR
        } // NOSONAR
    } // NOSONAR

    override fun delete(inclExclItem: InclExclItem) { //NOSONAR
        inclExclDatabase.delete( //NOSONAR
            BlacklistWhitelistDbOpenHelper.TABLE_NAME, //NOSONAR
            BlacklistWhitelistDbOpenHelper.COLUMN_PATH + " = '" + inclExclItem.path.replace("'".toRegex(), "\''") + "'" + //NOSONAR
                " AND " + BlacklistWhitelistDbOpenHelper.COLUMN_TYPE + " = " + inclExclItem.type //NOSONAR
        ) // NOSONAR
    } // NOSONAR

    override fun addSong(song: Song) { //NOSONAR
        add(InclExclItem(song.path, type)) //NOSONAR
    } // NOSONAR

    override fun addAllSongs(songs: List<Song>) { //NOSONAR
        addAll(songs.map { song -> InclExclItem(song.path, type) }.toList()) //NOSONAR
    } // NOSONAR

    override fun deleteAll() { //NOSONAR
        inclExclDatabase.delete(BlacklistWhitelistDbOpenHelper.TABLE_NAME, BlacklistWhitelistDbOpenHelper.COLUMN_TYPE + " = " + type) //NOSONAR
    } // NOSONAR

} // NOSONAR

class WhitelistRepository @Inject constructor(private val inclExclDatabase: BriteDatabase) : InclExclRepository(inclExclDatabase, InclExclItem.Type.INCLUDE), Repository.WhitelistRepository { //NOSONAR

    private var inclSubscription: Disposable? = null //NOSONAR
    private val inclRelay = BehaviorRelay.create<List<InclExclItem>>() //NOSONAR

    private fun getIncludeItems(): Observable<List<InclExclItem>> { //NOSONAR
        return inclExclDatabase.createQuery( //NOSONAR
            BlacklistWhitelistDbOpenHelper.TABLE_NAME, //NOSONAR
            "SELECT * FROM " + BlacklistWhitelistDbOpenHelper.TABLE_NAME + " WHERE " + BlacklistWhitelistDbOpenHelper.COLUMN_TYPE + " = " + InclExclItem.Type.INCLUDE //NOSONAR
        ).mapToList { InclExclItem(it) } //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return a **continuous** stream of type [InclExclItem.Type.INCLUDE] , backed by a behavior relay for caching query results. // NOSONAR
     */ // NOSONAR
    override fun getWhitelistItems(songsRepository: Repository.SongsRepository): Observable<List<InclExclItem>> { //NOSONAR
        if (inclSubscription == null || inclSubscription?.isDisposed == true) { //NOSONAR
            inclSubscription = getIncludeItems().subscribe(inclRelay) //NOSONAR
        } // NOSONAR
        return inclRelay.subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR
} // NOSONAR

class BlacklistRepository @Inject constructor(private val inclExclDatabase: BriteDatabase) : InclExclRepository(inclExclDatabase, InclExclItem.Type.EXCLUDE), Repository.BlacklistRepository { //NOSONAR

    private var exclSubscription: Disposable? = null //NOSONAR
    private val exclRelay = BehaviorRelay.create<List<InclExclItem>>() //NOSONAR

    private fun getExcludeItems(): Observable<List<InclExclItem>> { //NOSONAR
        return inclExclDatabase.createQuery( //NOSONAR
            BlacklistWhitelistDbOpenHelper.TABLE_NAME, //NOSONAR
            "SELECT * FROM " + BlacklistWhitelistDbOpenHelper.TABLE_NAME + " WHERE " + BlacklistWhitelistDbOpenHelper.COLUMN_TYPE + " = " + InclExclItem.Type.EXCLUDE //NOSONAR
        ) // NOSONAR
            .mapToList { InclExclItem(it) } //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return a **continuous** stream of type [InclExclItem.Type.EXCLUDE], backed by a behavior relay for caching query results. // NOSONAR
     */ // NOSONAR
    override fun getBlacklistItems(songsRepository: Repository.SongsRepository): Observable<List<InclExclItem>> { //NOSONAR
        if (exclSubscription == null || exclSubscription?.isDisposed == true) { //NOSONAR
            exclSubscription = getExcludeItems() //NOSONAR
                .subscribe(exclRelay) //NOSONAR
        } // NOSONAR
        return exclRelay.subscribeOn(Schedulers.io()) //NOSONAR
    } // NOSONAR
} // NOSONAR
