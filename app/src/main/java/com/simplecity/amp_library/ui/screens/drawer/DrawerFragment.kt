@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.ui.screens.drawer

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.aesthetic.Aesthetic
import com.afollestad.aesthetic.Rx
import com.bignerdranch.expandablerecyclerview.model.Parent
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.simplecity.amp_library.R
import com.simplecity.amp_library.billing.BillingManager
import com.simplecity.amp_library.data.Repository
import com.simplecity.amp_library.model.AlbumArtist
import com.simplecity.amp_library.model.Playlist
import com.simplecity.amp_library.model.Song
import com.simplecity.amp_library.ui.common.BaseFragment
import com.simplecity.amp_library.ui.dialog.UpgradeDialog
import com.simplecity.amp_library.ui.dialog.WeekSelectorDialog
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerPresenter
import com.simplecity.amp_library.ui.screens.playlist.dialog.DeletePlaylistConfirmationDialog
import com.simplecity.amp_library.ui.screens.playlist.dialog.M3uPlaylistDialog
import com.simplecity.amp_library.ui.screens.playlist.dialog.RenamePlaylistDialog
import com.simplecity.amp_library.ui.views.PlayerViewAdapter
import com.simplecity.amp_library.utils.LogUtils
import com.simplecity.amp_library.utils.PlaceholderProvider
import com.simplecity.amp_library.utils.SettingsManager
import com.simplecity.amp_library.utils.SleepTimer
import com.simplecity.amp_library.utils.menu.playlist.PlaylistMenuUtils
import com.simplecity.amp_library.utils.playlists.FavoritesPlaylistManager
import com.simplecity.amp_library.utils.playlists.PlaylistManager
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.drawer_header.artist_image
import kotlinx.android.synthetic.main.drawer_header.background_image
import kotlinx.android.synthetic.main.drawer_header.line1
import kotlinx.android.synthetic.main.drawer_header.line2
import kotlinx.android.synthetic.main.drawer_header.placeholder_text
import kotlinx.android.synthetic.main.fragment_drawer.recyclerView
import java.util.ArrayList
import javax.inject.Inject

class DrawerFragment : BaseFragment(), DrawerView, View.OnCreateContextMenuListener, DrawerParent.ClickListener { //NOSONAR

    private lateinit var adapter: DrawerAdapter //NOSONAR

    private var drawerLayout: DrawerLayout? = null //NOSONAR

    private var playlistDrawerParent: DrawerParent? = null //NOSONAR

    @DrawerParent.Type //NOSONAR
    private var selectedDrawerParent = DrawerParent.Type.LIBRARY //NOSONAR

    private var currentSelectedPlaylist: Playlist? = null //NOSONAR

    @Inject lateinit var playerPresenter: PlayerPresenter //NOSONAR

    @Inject lateinit var drawerPresenter: DrawerPresenter //NOSONAR

    @Inject lateinit var billingManager: BillingManager //NOSONAR

    @Inject lateinit var songsRepository: Repository.SongsRepository //NOSONAR

    @Inject lateinit var playlistsRepository: Repository.PlaylistsRepository //NOSONAR

    @Inject lateinit var settingsManager: SettingsManager //NOSONAR

    @Inject lateinit var requestManager: RequestManager //NOSONAR

    @Inject lateinit var playlistManager: PlaylistManager //NOSONAR

    @Inject lateinit var favoritesPlaylistManager: FavoritesPlaylistManager //NOSONAR

    private var backgroundPlaceholder: Drawable? = null //NOSONAR

    private val disposables = CompositeDisposable() //NOSONAR

    private var drawerParents: MutableList<Parent<DrawerChild>>? = null //NOSONAR

    // Lifecycle

    override fun onAttach(context: Context?) { //NOSONAR
        AndroidSupportInjection.inject(this) //NOSONAR
        super.onAttach(context) //NOSONAR
    }

    override fun onCreate(savedInstanceState: Bundle?) { //NOSONAR
        super.onCreate(savedInstanceState) //NOSONAR

        if (savedInstanceState != null) { //NOSONAR
            selectedDrawerParent = savedInstanceState.getInt(STATE_SELECTED_DRAWER_PARENT, DrawerParent.Type.LIBRARY) //NOSONAR
            currentSelectedPlaylist = savedInstanceState.get(STATE_SELECTED_PLAYLIST) as Playlist? //NOSONAR
        }

        backgroundPlaceholder = ContextCompat.getDrawable(context!!, R.drawable.ic_drawer_header_placeholder) //NOSONAR

        playlistDrawerParent = DrawerParent.getPlaylistsParent(settingsManager) //NOSONAR

        drawerParents = ArrayList() //NOSONAR
        drawerParents!!.add(DrawerParent.getLibraryParent(settingsManager)) //NOSONAR
        drawerParents!!.add(DrawerParent.getFolderParent(context!!, settingsManager)) //NOSONAR
        drawerParents!!.add(playlistDrawerParent!!) //NOSONAR
        drawerParents!!.add(DrawerDivider()) //NOSONAR
        drawerParents!!.add(DrawerParent.getSleepTimerParent(settingsManager)) //NOSONAR
        drawerParents!!.add(DrawerParent.getEqualizerParent(settingsManager)) //NOSONAR
        drawerParents!!.add(DrawerParent.getSettingsParent(settingsManager)) //NOSONAR
        drawerParents!!.add(DrawerParent.getSupportParent(settingsManager)) //NOSONAR

        adapter = DrawerAdapter(drawerParents!!) //NOSONAR
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { //NOSONAR
        return inflater.inflate(R.layout.fragment_drawer, container, false) //NOSONAR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //NOSONAR
        super.onViewCreated(view, savedInstanceState) //NOSONAR

        recyclerView!!.layoutManager = LinearLayoutManager(context) //NOSONAR
        recyclerView!!.adapter = adapter //NOSONAR

        setDrawerItemSelected(selectedDrawerParent) //NOSONAR

        drawerPresenter.bindView(this) //NOSONAR
        playerPresenter.bindView(playerViewAdapter) //NOSONAR

        drawerLayout = getParentDrawerLayout(view) //NOSONAR
    }

    override fun onResume() { //NOSONAR
        super.onResume() //NOSONAR

        // To do later: Move this crap to presenter
        disposables.add(Aesthetic.get(context) //NOSONAR
            .colorPrimary() //NOSONAR
            .compose(Rx.distinctToMainThread()) //NOSONAR
            .subscribe { color -> //NOSONAR
                backgroundPlaceholder!!.setColorFilter(color!!, PorterDuff.Mode.MULTIPLY) //NOSONAR
                if (mediaManager.song == null) { //NOSONAR
                    background_image.setImageDrawable(backgroundPlaceholder) //NOSONAR
                }
            })

        playerPresenter.updateTrackInfo() //NOSONAR

        disposables.add( //NOSONAR
            SleepTimer.getInstance().currentTimeObservable //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe({ aLong -> //NOSONAR
                    drawerParents!! //NOSONAR
                        .forEachIndexed { i, drawerParent -> //NOSONAR
                            if (aLong > 0 && drawerParent is DrawerParent && drawerParent.type == DrawerParent.Type.SLEEP_TIMER) { //NOSONAR
                                drawerParent.setTimeRemaining(aLong!!) //NOSONAR
                                adapter.notifyParentChanged(i) //NOSONAR
                            }
                        }
                }, { throwable -> LogUtils.logException(TAG, "Error observing sleep time", throwable) }) //NOSONAR
        )

        disposables.add( //NOSONAR
            SleepTimer.getInstance().timerActiveSubject //NOSONAR
                .observeOn(AndroidSchedulers.mainThread()) //NOSONAR
                .subscribe({ active -> //NOSONAR
                    drawerParents!! //NOSONAR
                        .forEachIndexed { i, drawerParent -> //NOSONAR
                            if (drawerParent is DrawerParent && drawerParent.type == DrawerParent.Type.SLEEP_TIMER) { //NOSONAR
                                drawerParent.setTimerActive(active!!) //NOSONAR
                                adapter.notifyParentChanged(i) //NOSONAR
                            }
                        }
                },
                    { throwable -> LogUtils.logException(TAG, "Error observing sleep state", throwable) }) //NOSONAR
        )

        drawerParents!! //NOSONAR
            .filter { parent -> parent is DrawerParent } //NOSONAR
            .forEach { parent -> (parent as DrawerParent).setListener(this) } //NOSONAR
    }

    override fun onPause() { //NOSONAR
        disposables.clear() //NOSONAR

        drawerParents!! //NOSONAR
            .filter { parent -> parent is DrawerParent } //NOSONAR
            .forEach { parent -> (parent as DrawerParent).setListener(null) } //NOSONAR

        super.onPause() //NOSONAR
    }

    override fun onDestroyView() { //NOSONAR
        drawerPresenter.unbindView(this) //NOSONAR
        playerPresenter.unbindView(playerViewAdapter) //NOSONAR

        super.onDestroyView() //NOSONAR
    }

    private val playerViewAdapter: PlayerViewAdapter = object : PlayerViewAdapter() { //NOSONAR
        override fun trackInfoChanged(song: Song?) { //NOSONAR

            if (song == null) { //NOSONAR
                return //NOSONAR
            }

            line1.text = song.name //NOSONAR
            line2.text = String.format("%s - %s", song.albumArtistName, song.albumName) //NOSONAR
            placeholder_text.setText(R.string.app_name) //NOSONAR

            requestManager.load(song) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                .centerCrop() //NOSONAR
                .error(backgroundPlaceholder) //NOSONAR
                .into(background_image) //NOSONAR

            requestManager.load<AlbumArtist>(song.albumArtist) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                .placeholder(PlaceholderProvider.getInstance(context).mediumPlaceHolderResId) //NOSONAR
                .into(artist_image) //NOSONAR

            if (song.name == null || song.albumName == null && song.albumArtistName == null) { //NOSONAR
                placeholder_text.visibility = View.VISIBLE //NOSONAR
                line1.visibility = View.GONE //NOSONAR
                line2.visibility = View.GONE //NOSONAR
            } else { //NOSONAR
                placeholder_text.visibility = View.GONE //NOSONAR
                line1.visibility = View.VISIBLE //NOSONAR
                line2.visibility = View.VISIBLE //NOSONAR
            }
        }
    }

    override fun onClick(drawerParent: DrawerParent) { //NOSONAR
        drawerPresenter.onDrawerItemClicked(drawerParent) //NOSONAR
    }

    override fun onSaveInstanceState(outState: Bundle) { //NOSONAR
        super.onSaveInstanceState(outState) //NOSONAR
        outState.putSerializable(STATE_SELECTED_DRAWER_PARENT, selectedDrawerParent) //NOSONAR
        outState.putSerializable(STATE_SELECTED_PLAYLIST, currentSelectedPlaylist) //NOSONAR
    }

    internal fun onPlaylistClicked(playlist: Playlist) { //NOSONAR
        drawerPresenter.onPlaylistClicked(playlist) //NOSONAR
    }

    override fun setPlaylistItems(playlists: List<Playlist>) { //NOSONAR

        val parentPosition = adapter.parentList.indexOf(playlistDrawerParent) //NOSONAR

        val prevItemCount = playlistDrawerParent!!.children.size //NOSONAR
        playlistDrawerParent!!.children.clear() //NOSONAR
        adapter.notifyChildRangeRemoved(parentPosition, 0, prevItemCount) //NOSONAR

        val drawerChildren = playlists //NOSONAR
            .map { playlist -> //NOSONAR
                val drawerChild = DrawerChild(playlist) //NOSONAR
                drawerChild.setListener(object : DrawerChild.ClickListener { //NOSONAR
                    override fun onClick(playlist: Playlist) { //NOSONAR
                        onPlaylistClicked(playlist) //NOSONAR
                    }

                    override fun onOverflowClick(view: View, playlist: Playlist) { //NOSONAR
                        val popupMenu = PopupMenu(view.context, view) //NOSONAR
                        PlaylistMenuUtils.setupPlaylistMenu(popupMenu, playlist) //NOSONAR
                        popupMenu.setOnMenuItemClickListener(PlaylistMenuUtils.getPlaylistPopupMenuClickListener(playlist, drawerPresenter)) //NOSONAR
                        popupMenu.show() //NOSONAR
                    }
                })
                drawerChild //NOSONAR
            }.toList() //NOSONAR

        playlistDrawerParent!!.children.addAll(drawerChildren) //NOSONAR
        adapter.notifyChildRangeInserted(parentPosition, 0, drawerChildren.size) //NOSONAR

        adapter.notifyParentChanged(parentPosition) //NOSONAR
    }

    override fun closeDrawer() { //NOSONAR
        drawerLayout?.closeDrawer(Gravity.START) //NOSONAR
    }

    override fun setDrawerItemSelected(@DrawerParent.Type type: Int) { //NOSONAR
        adapter.parentList //NOSONAR
            .forEachIndexed { i, drawerParent -> //NOSONAR
                if (drawerParent is DrawerParent) { //NOSONAR
                    if (drawerParent.type == type) { //NOSONAR
                        if (!drawerParent.isSelected) { //NOSONAR
                            drawerParent.isSelected = true //NOSONAR
                            adapter.notifyParentChanged(i) //NOSONAR
                        }
                    } else { //NOSONAR
                        if (drawerParent.isSelected) { //NOSONAR
                            drawerParent.isSelected = false //NOSONAR
                            adapter.notifyParentChanged(i) //NOSONAR
                        }
                    }
                }
            }
    }

    override fun showUpgradeDialog() { //NOSONAR
        UpgradeDialog().show(childFragmentManager) //NOSONAR
    }

    // PlaylistMenuContract.View Implementation

    override fun onPlaybackFailed() { //NOSONAR
        // To do later: Improve error message
        Toast.makeText(context, R.string.empty_playlist, Toast.LENGTH_SHORT).show() //NOSONAR
    }

    override fun presentEditDialog(playlist: Playlist) { //NOSONAR
        WeekSelectorDialog().show(childFragmentManager) //NOSONAR
    }

    override fun presentRenameDialog(playlist: Playlist) { //NOSONAR
        RenamePlaylistDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    }

    override fun presentM3uDialog(playlist: Playlist) { //NOSONAR
        M3uPlaylistDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    }

    override fun presentDeletePlaylistDialog(playlist: Playlist) { //NOSONAR
        DeletePlaylistConfirmationDialog.newInstance(playlist).show(childFragmentManager) //NOSONAR
    }

    override fun onSongsAddedToQueue(numSongs: Int) { //NOSONAR
        Toast.makeText(context, context!!.resources.getQuantityString(R.plurals.NNNtrackstoqueue, numSongs, numSongs), Toast.LENGTH_SHORT).show() //NOSONAR
    }

    // BaseDetailFragment Implementation

    override fun screenName(): String { //NOSONAR
        return TAG //NOSONAR
    }

    // Static

    companion object { //NOSONAR

        private const val TAG = "DrawerFragment" //NOSONAR

        private const val STATE_SELECTED_DRAWER_PARENT = "selected_drawer_parent" //NOSONAR

        private const val STATE_SELECTED_PLAYLIST = "selected_drawer_playlist" //NOSONAR

        fun getParentDrawerLayout(v: View?): DrawerLayout? { //NOSONAR
            if (v == null) return null //NOSONAR

            if (v is DrawerLayout) { //NOSONAR
                return v //NOSONAR
            }

            return if (v.parent is View) { //NOSONAR
                getParentDrawerLayout(v.parent as View) //NOSONAR
            } else null //NOSONAR
        }
    }
}
