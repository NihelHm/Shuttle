package com.simplecity.amp_library.ui.screens.main; // NOSONAR

import android.support.v7.app.AppCompatActivity; // NOSONAR
import com.simplecity.amp_library.billing.BillingManager; // NOSONAR
import com.simplecity.amp_library.di.app.activity.ActivityModule; // NOSONAR
import com.simplecity.amp_library.di.app.activity.ActivityScope; // NOSONAR
import com.simplecity.amp_library.di.app.activity.fragment.DialogFragmentModule; // NOSONAR
import com.simplecity.amp_library.di.app.activity.fragment.FragmentModule; // NOSONAR
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope; // NOSONAR
import com.simplecity.amp_library.saf.SafManager; // NOSONAR
import com.simplecity.amp_library.ui.common.EqualizerModule; // NOSONAR
import com.simplecity.amp_library.ui.dialog.ChangelogDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.DeleteDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.InclExclDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.UpgradeDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.UpgradeNagDialog; // NOSONAR
import com.simplecity.amp_library.ui.dialog.WeekSelectorDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.detail.AlbumDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.detail.AlbumsDetailFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.list.AlbumListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.album.list.AlbumListFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.detail.ArtistDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.detail.ArtistsDetailFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.list.AlbumArtistListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.artist.list.AlbumArtistListFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.equalizer.EqualizerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.folders.FolderFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.folders.FolderFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.detail.GenreDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.detail.GenreDetailFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.list.GenreListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.genre.list.GenreListFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.lyrics.LyricsDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.miniplayer.MiniPlayerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.detail.PlaylistDetailFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.detail.PlaylistDetailFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.DeletePlaylistConfirmationDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.dialog.M3uPlaylistDialog; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.QueueFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.pager.QueuePagerFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.queue.pager.QueuePagerFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.search.SearchFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.search.SearchFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.list.SongListFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.songs.list.SongsListFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.suggested.SuggestedFragment; // NOSONAR
import com.simplecity.amp_library.ui.screens.suggested.SuggestedFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog; // NOSONAR
import com.simplecity.amp_library.ui.settings.SettingsFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.settings.SettingsParentFragment; // NOSONAR
import com.simplecity.amp_library.ui.settings.SettingsParentFragmentModule; // NOSONAR
import com.simplecity.amp_library.ui.settings.TabChooserDialog; // NOSONAR
import dagger.Binds; // NOSONAR
import dagger.Module; // NOSONAR
import dagger.Provides; // NOSONAR
import dagger.android.ContributesAndroidInjector; // NOSONAR

@Module(includes = ActivityModule.class) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class MainActivityModule { //NOSONAR

    @Binds //NOSONAR
    @ActivityScope //NOSONAR
    abstract AppCompatActivity appCompatActivity(MainActivity mainActivity); //NOSONAR

    @Provides //NOSONAR
    static BillingManager.BillingUpdatesListener provideBillingUpdatesListener(MainActivity mainActivity) { //NOSONAR
        return mainActivity; //NOSONAR
    } // NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = LibraryFragmentModule.class) //NOSONAR
    abstract LibraryController libraryControllerInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DrawerFragmentModule.class) //NOSONAR
    abstract DrawerFragment drawerFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = MainControllerModule.class) //NOSONAR
    abstract MainController mainControllerInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = AlbumArtistListFragmentModule.class) //NOSONAR
    abstract AlbumArtistListFragment artistsFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = AlbumListFragmentModule.class) //NOSONAR
    abstract AlbumListFragment albumsFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = SongsListFragmentModule.class) //NOSONAR
    abstract SongListFragment songsFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = PlayerFragmentModule.class) //NOSONAR
    abstract PlayerFragment playerFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = QueueFragmentModule.class) //NOSONAR
    abstract QueueFragment queueFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = QueuePagerFragmentModule.class) //NOSONAR
    abstract QueuePagerFragment queuePagerFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = FragmentModule.class) //NOSONAR
    abstract MiniPlayerFragment miniPlayerFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = SuggestedFragmentModule.class) //NOSONAR
    abstract SuggestedFragment suggestedFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = FolderFragmentModule.class) //NOSONAR
    abstract FolderFragment folderFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = AlbumsDetailFragmentModule.class) //NOSONAR
    abstract AlbumDetailFragment albumDetailFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = ArtistsDetailFragmentModule.class) //NOSONAR
    abstract ArtistDetailFragment artistDetailFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = PlaylistListFragmentModule.class) //NOSONAR
    abstract PlaylistListFragment playlistFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = PlaylistDetailFragmentModule.class) //NOSONAR
    abstract PlaylistDetailFragment playlistDetailFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = GenreListFragmentModule.class) //NOSONAR
    abstract GenreListFragment genresFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = GenreDetailFragmentModule.class) //NOSONAR
    abstract GenreDetailFragment genreDetailFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = SettingsParentFragmentModule.class) //NOSONAR
    abstract SettingsParentFragment settingsParentFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = SettingsFragmentModule.class) //NOSONAR
    abstract SettingsParentFragment.SettingsFragment settingsFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = SearchFragmentModule.class) //NOSONAR
    abstract SearchFragment searchFragmentInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = EqualizerModule.class) //NOSONAR
    abstract EqualizerFragment equalizerFragmentInjector(); //NOSONAR

    // Dialog fragments // NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract CreatePlaylistDialog createPlaylistdialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract DeletePlaylistConfirmationDialog deletePlaylistConfirmationDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract M3uPlaylistDialog m3uPlaylistDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract TaggerDialog taggerDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract DeleteDialog deleteDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract InclExclDialog inclExclDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract UpgradeDialog upgradeDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract UpgradeNagDialog UpgradeNagDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract WeekSelectorDialog weekSelectorDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract ChangelogDialog changelogDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract TabChooserDialog tabChooseerDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract LyricsDialog lyricsDialogInjector(); //NOSONAR

    @FragmentScope //NOSONAR
    @ContributesAndroidInjector(modules = DialogFragmentModule.class) //NOSONAR
    abstract SafManager.SafDialog safDialogInjector(); //NOSONAR
} // NOSONAR
