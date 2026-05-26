package com.simplecity.amp_library.ui.screens.main;

import android.support.v7.app.AppCompatActivity;
import com.simplecity.amp_library.billing.BillingManager;
import com.simplecity.amp_library.di.app.activity.ActivityModule;
import com.simplecity.amp_library.di.app.activity.ActivityScope;
import com.simplecity.amp_library.di.app.activity.fragment.DialogFragmentModule;
import com.simplecity.amp_library.di.app.activity.fragment.FragmentModule;
import com.simplecity.amp_library.di.app.activity.fragment.FragmentScope;
import com.simplecity.amp_library.saf.SafManager;
import com.simplecity.amp_library.ui.common.EqualizerModule;
import com.simplecity.amp_library.ui.dialog.ChangelogDialog;
import com.simplecity.amp_library.ui.dialog.DeleteDialog;
import com.simplecity.amp_library.ui.dialog.InclExclDialog;
import com.simplecity.amp_library.ui.dialog.UpgradeDialog;
import com.simplecity.amp_library.ui.dialog.UpgradeNagDialog;
import com.simplecity.amp_library.ui.dialog.WeekSelectorDialog;
import com.simplecity.amp_library.ui.screens.album.detail.AlbumDetailFragment;
import com.simplecity.amp_library.ui.screens.album.detail.AlbumsDetailFragmentModule;
import com.simplecity.amp_library.ui.screens.album.list.AlbumListFragment;
import com.simplecity.amp_library.ui.screens.album.list.AlbumListFragmentModule;
import com.simplecity.amp_library.ui.screens.artist.detail.ArtistDetailFragment;
import com.simplecity.amp_library.ui.screens.artist.detail.ArtistsDetailFragmentModule;
import com.simplecity.amp_library.ui.screens.artist.list.AlbumArtistListFragment;
import com.simplecity.amp_library.ui.screens.artist.list.AlbumArtistListFragmentModule;
import com.simplecity.amp_library.ui.screens.drawer.DrawerFragment;
import com.simplecity.amp_library.ui.screens.drawer.DrawerFragmentModule;
import com.simplecity.amp_library.ui.screens.equalizer.EqualizerFragment;
import com.simplecity.amp_library.ui.screens.folders.FolderFragment;
import com.simplecity.amp_library.ui.screens.folders.FolderFragmentModule;
import com.simplecity.amp_library.ui.screens.genre.detail.GenreDetailFragment;
import com.simplecity.amp_library.ui.screens.genre.detail.GenreDetailFragmentModule;
import com.simplecity.amp_library.ui.screens.genre.list.GenreListFragment;
import com.simplecity.amp_library.ui.screens.genre.list.GenreListFragmentModule;
import com.simplecity.amp_library.ui.screens.lyrics.LyricsDialog;
import com.simplecity.amp_library.ui.screens.miniplayer.MiniPlayerFragment;
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerFragment;
import com.simplecity.amp_library.ui.screens.nowplaying.PlayerFragmentModule;
import com.simplecity.amp_library.ui.screens.playlist.detail.PlaylistDetailFragment;
import com.simplecity.amp_library.ui.screens.playlist.detail.PlaylistDetailFragmentModule;
import com.simplecity.amp_library.ui.screens.playlist.dialog.CreatePlaylistDialog;
import com.simplecity.amp_library.ui.screens.playlist.dialog.DeletePlaylistConfirmationDialog;
import com.simplecity.amp_library.ui.screens.playlist.dialog.M3uPlaylistDialog;
import com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListFragment;
import com.simplecity.amp_library.ui.screens.playlist.list.PlaylistListFragmentModule;
import com.simplecity.amp_library.ui.screens.queue.QueueFragment;
import com.simplecity.amp_library.ui.screens.queue.QueueFragmentModule;
import com.simplecity.amp_library.ui.screens.queue.pager.QueuePagerFragment;
import com.simplecity.amp_library.ui.screens.queue.pager.QueuePagerFragmentModule;
import com.simplecity.amp_library.ui.screens.search.SearchFragment;
import com.simplecity.amp_library.ui.screens.search.SearchFragmentModule;
import com.simplecity.amp_library.ui.screens.songs.list.SongListFragment;
import com.simplecity.amp_library.ui.screens.songs.list.SongsListFragmentModule;
import com.simplecity.amp_library.ui.screens.suggested.SuggestedFragment;
import com.simplecity.amp_library.ui.screens.suggested.SuggestedFragmentModule;
import com.simplecity.amp_library.ui.screens.tagger.TaggerDialog;
import com.simplecity.amp_library.ui.settings.SettingsFragmentModule;
import com.simplecity.amp_library.ui.settings.SettingsParentFragment;
import com.simplecity.amp_library.ui.settings.SettingsParentFragmentModule;
import com.simplecity.amp_library.ui.settings.TabChooserDialog;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module(includes = ActivityModule.class) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class MainActivityModule { //NOSONAR

    @Binds //NOSONAR
    @ActivityScope //NOSONAR
    abstract AppCompatActivity appCompatActivity(MainActivity mainActivity); //NOSONAR

    @Provides //NOSONAR
    static BillingManager.BillingUpdatesListener provideBillingUpdatesListener(MainActivity mainActivity) { //NOSONAR
        return mainActivity; //NOSONAR
    }

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

    // Dialog fragments

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
}
