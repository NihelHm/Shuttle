package com.simplecity.amp_library.ui.widgets; // NOSONAR

import android.appwidget.AppWidgetManager; // NOSONAR
import android.content.ComponentName; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.os.IBinder; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.design.widget.TabLayout; // NOSONAR
import android.support.v4.app.Fragment; // NOSONAR
import android.support.v4.app.FragmentManager; // NOSONAR
import android.support.v4.app.FragmentStatePagerAdapter; // NOSONAR
import android.support.v4.content.ContextCompat; // NOSONAR
import android.support.v4.graphics.drawable.DrawableCompat; // NOSONAR
import android.support.v4.view.ViewPager; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.util.SparseArray; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.Button; // NOSONAR
import android.widget.CheckBox; // NOSONAR
import android.widget.CompoundButton; // NOSONAR
import android.widget.ImageButton; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.RemoteViews; // NOSONAR
import android.widget.SeekBar; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.materialdialogs.color.ColorChooserDialog; // NOSONAR
import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.load.engine.DiskCacheStrategy; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.playback.constants.MediaButtonCommand; // NOSONAR
import com.simplecity.amp_library.playback.constants.ServiceCommand; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseActivity; // NOSONAR
import com.simplecity.amp_library.ui.screens.widgets.WidgetFragment; // NOSONAR
import com.simplecity.amp_library.ui.views.SizableSeekBar; // NOSONAR
import com.simplecity.amp_library.utils.ColorUtils; // NOSONAR
import dagger.android.AndroidInjection; // NOSONAR
import javax.inject.Inject; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public abstract class BaseWidgetConfigureActivity extends BaseActivity implements //NOSONAR
        View.OnClickListener, //NOSONAR
        CheckBox.OnCheckedChangeListener, //NOSONAR
        SeekBar.OnSeekBarChangeListener, //NOSONAR
        ViewPager.OnPageChangeListener, //NOSONAR
        ColorChooserDialog.ColorCallback { //NOSONAR

    private ColorChooserDialog textColorDialog; //NOSONAR
    private ColorChooserDialog backgroundColorDialog; //NOSONAR

    abstract int[] getWidgetLayouts(); //NOSONAR

    abstract String getLayoutIdString(); //NOSONAR

    abstract String getUpdateCommandString(); //NOSONAR

    abstract int getRootViewId(); //NOSONAR

    int[] layouts; //NOSONAR
    private int layoutId; //NOSONAR
    private int appWidgetId; //NOSONAR

    private float alpha = 0.15f; //NOSONAR

    private ViewPager pager; //NOSONAR

    /** // NOSONAR
     * The pager adapter, which provides the pages to the view pager widget. // NOSONAR
     */ // NOSONAR
    private WidgetPagerAdapter adapter; //NOSONAR

    private SharedPreferences prefs; //NOSONAR

    private Button backgroundColorButton; //NOSONAR
    private Button textColorButton; //NOSONAR
    private SizableSeekBar seekBar; //NOSONAR

    private int backgroundColor; //NOSONAR
    private int textColor; //NOSONAR
    private boolean showAlbumArt; //NOSONAR
    private boolean invertIcons; //NOSONAR

    SparseArray<Fragment> registeredFragments = new SparseArray<>(); //NOSONAR

    @Inject //NOSONAR
    MediaManager mediaManager; //NOSONAR

    @Override //NOSONAR
    protected void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        if (Aesthetic.isFirstTime(this)) { //NOSONAR
            Aesthetic.get(this) //NOSONAR
                    .activityTheme(R.style.WallpaperTheme) //NOSONAR
                    .isDark(false) //NOSONAR
                    .colorPrimaryRes(R.color.md_blue_500) //NOSONAR
                    .colorAccentRes(R.color.md_amber_300) //NOSONAR
                    .colorStatusBarAuto() //NOSONAR
                    .apply(); //NOSONAR
        } // NOSONAR

        setContentView(R.layout.activity_widget_config); //NOSONAR

        Bundle extras = this.getIntent().getExtras(); //NOSONAR
        if (extras != null) { //NOSONAR
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID); //NOSONAR
        } // NOSONAR
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) { //NOSONAR
            finish(); //NOSONAR
        } // NOSONAR

        prefs = PreferenceManager.getDefaultSharedPreferences(this); //NOSONAR
        layoutId = prefs.getInt(getLayoutIdString() + appWidgetId, getWidgetLayouts()[0]); //NOSONAR
        backgroundColor = prefs.getInt(BaseWidgetProvider.ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, ContextCompat.getColor(this, R.color.white)); //NOSONAR
        textColor = prefs.getInt(BaseWidgetProvider.ARG_WIDGET_TEXT_COLOR + appWidgetId, Color.WHITE); //NOSONAR
        showAlbumArt = prefs.getBoolean(BaseWidgetProvider.ARG_WIDGET_SHOW_ARTWORK + appWidgetId, true); //NOSONAR

        Toolbar toolbar = findViewById(R.id.toolbar); //NOSONAR
        setSupportActionBar(toolbar); //NOSONAR

        layouts = getWidgetLayouts(); //NOSONAR

        // Instantiate a ViewPager and a PagerAdapter. // NOSONAR
        pager = findViewById(R.id.pager); //NOSONAR
        adapter = new WidgetPagerAdapter(getSupportFragmentManager()); //NOSONAR
        pager.setAdapter(adapter); //NOSONAR

        TabLayout tabLayout = findViewById(R.id.tabs); //NOSONAR
        tabLayout.setupWithViewPager(pager); //NOSONAR
        pager.addOnPageChangeListener(this); //NOSONAR

        Button doneButton = findViewById(R.id.btn_done); //NOSONAR
        doneButton.setOnClickListener(this); //NOSONAR

        backgroundColorButton = findViewById(R.id.btn_background_color); //NOSONAR
        backgroundColorButton.setOnClickListener(this); //NOSONAR

        textColorButton = findViewById(R.id.btn_text_color); //NOSONAR
        textColorButton.setOnClickListener(this); //NOSONAR

        CheckBox showAlbumArtCheckbox = findViewById(R.id.checkBox1); //NOSONAR
        showAlbumArtCheckbox.setOnCheckedChangeListener(this); //NOSONAR

        CheckBox invertedIconsCheckbox = findViewById(R.id.checkBox2); //NOSONAR
        invertedIconsCheckbox.setOnCheckedChangeListener(this); //NOSONAR

        seekBar = findViewById(R.id.seekBar1); //NOSONAR
        seekBar.setOnSeekBarChangeListener(this); //NOSONAR

        updateWidgetUI(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onBackPressed() { //NOSONAR
        if (pager.getCurrentItem() == 0) { //NOSONAR
            // If the user is currently looking at the first step, allow the system to handle the // NOSONAR
            // Back button. This calls finish() on this activity and pops the back stack. // NOSONAR
            super.onBackPressed(); //NOSONAR
        } else { //NOSONAR
            // Otherwise, select the previous step. // NOSONAR
            pager.setCurrentItem(pager.getCurrentItem() - 1); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) { //NOSONAR

        if (compoundButton.getId() == R.id.checkBox1) { //NOSONAR
            showAlbumArt = checked; //NOSONAR
            prefs.edit().putBoolean(BaseWidgetProvider.ARG_WIDGET_SHOW_ARTWORK + appWidgetId, showAlbumArt).apply(); //NOSONAR
        } // NOSONAR
        if (compoundButton.getId() == R.id.checkBox2) { //NOSONAR
            invertIcons = checked; //NOSONAR
            prefs.edit().putBoolean(BaseWidgetProvider.ARG_WIDGET_INVERT_ICONS + appWidgetId, invertIcons).apply(); //NOSONAR
        } // NOSONAR
        updateWidgetUI(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onClick(View view) { //NOSONAR
        if (view.getId() == R.id.btn_done) { //NOSONAR

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this); //NOSONAR

            RemoteViews remoteViews = new RemoteViews(this.getPackageName(), layoutId); //NOSONAR
            BaseWidgetProvider.setupButtons(this, remoteViews, appWidgetId, getRootViewId()); //NOSONAR
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews); //NOSONAR

            Intent resultValue = new Intent(); //NOSONAR
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId); //NOSONAR
            setResult(RESULT_OK, resultValue); //NOSONAR

            // Send broadcast intent to any running MediaPlaybackService so it can // NOSONAR
            // wrap around with an immediate update. // NOSONAR
            Intent updateIntent = new Intent(ServiceCommand.COMMAND); //NOSONAR
            updateIntent.putExtra(MediaButtonCommand.CMD_NAME, getUpdateCommandString()); //NOSONAR
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId }); //NOSONAR
            updateIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY); //NOSONAR
            sendBroadcast(updateIntent); //NOSONAR

            finish(); //NOSONAR
        } // NOSONAR

        if (view.getId() == R.id.btn_background_color) { //NOSONAR
            backgroundColorDialog = new ColorChooserDialog.Builder(this, R.string.color_pick) //NOSONAR
                    .allowUserColorInputAlpha(true) //NOSONAR
                    .show(getSupportFragmentManager()); //NOSONAR
        } // NOSONAR
        if (view.getId() == R.id.btn_text_color) { //NOSONAR
            textColorDialog = new ColorChooserDialog.Builder(this, R.string.color_pick) //NOSONAR
                    .allowUserColorInputAlpha(true) //NOSONAR
                    .show(getSupportFragmentManager()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onProgressChanged(SeekBar seekBar, int progress, boolean byUser) { //NOSONAR
        Fragment fragment = adapter.getRegisteredFragment(pager.getCurrentItem()); //NOSONAR
        if (fragment != null) { //NOSONAR
            View view = fragment.getView(); //NOSONAR
            if (view != null) { //NOSONAR
                View layout = view.findViewById(getRootViewId()); //NOSONAR
                alpha = 1 - (progress / 255f); //NOSONAR
                int adjustedColor = ColorUtils.adjustAlpha(backgroundColor, alpha); //NOSONAR
                layout.setBackgroundColor(adjustedColor); //NOSONAR
                prefs.edit() //NOSONAR
                        .putInt(BaseWidgetProvider.ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, adjustedColor) //NOSONAR
                        .apply(); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onStartTrackingTouch(SeekBar seekBar) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onStopTrackingTouch(SeekBar seekBar) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPageScrolled(int i, float v, int i2) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPageSelected(int position) { //NOSONAR
        layoutId = layouts[position]; //NOSONAR
        prefs.edit().putInt(getLayoutIdString() + appWidgetId, layoutId).apply(); //NOSONAR
        updateWidgetUI(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPageScrollStateChanged(int i) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) { //NOSONAR
        updateWidgetUI(); //NOSONAR
        super.onServiceConnected(componentName, iBinder); //NOSONAR
    } // NOSONAR

    private class WidgetPagerAdapter extends FragmentStatePagerAdapter { //NOSONAR

        public WidgetPagerAdapter(FragmentManager fm) { //NOSONAR
            super(fm); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public Fragment getItem(int position) { //NOSONAR
            return WidgetFragment.newInstance(layouts[position]); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public int getCount() { //NOSONAR
            return layouts.length; //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public CharSequence getPageTitle(int position) { //NOSONAR
            return "Layout " + String.valueOf(position + 1); //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public Object instantiateItem(ViewGroup container, int position) { //NOSONAR
            Fragment fragment = (Fragment) super.instantiateItem(container, position); //NOSONAR
            registeredFragments.put(position, fragment); //NOSONAR
            return fragment; //NOSONAR
        } // NOSONAR

        @Override //NOSONAR
        public void destroyItem(ViewGroup container, int position, Object object) { //NOSONAR
            registeredFragments.remove(position); //NOSONAR
            super.destroyItem(container, position, object); //NOSONAR
        } // NOSONAR

        public Fragment getRegisteredFragment(int position) { //NOSONAR
            return registeredFragments.get(position); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void updateWidgetUI() { //NOSONAR

        backgroundColor = prefs.getInt(BaseWidgetProvider.ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, ContextCompat.getColor(this, R.color.white)); //NOSONAR
        textColor = prefs.getInt(BaseWidgetProvider.ARG_WIDGET_TEXT_COLOR + appWidgetId, ContextCompat.getColor(this, R.color.white)); //NOSONAR

        Drawable backgroundButtonDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.bg_rounded)); //NOSONAR
        backgroundButtonDrawable.setBounds(0, 0, 60, 60); //NOSONAR
        backgroundColorButton.setCompoundDrawables(backgroundButtonDrawable, null, null, null); //NOSONAR

        Drawable textButtonDrawable = ContextCompat.getDrawable(this, R.drawable.bg_rounded); //NOSONAR
        textButtonDrawable.setBounds(0, 0, 60, 60); //NOSONAR
        textColorButton.setCompoundDrawables(textButtonDrawable, null, null, null); //NOSONAR

        Fragment fragment = adapter.getRegisteredFragment(pager.getCurrentItem()); //NOSONAR
        if (fragment != null) { //NOSONAR
            View view = fragment.getView(); //NOSONAR
            if (view != null) { //NOSONAR
                View widgetLayout = view.findViewById(getRootViewId()); //NOSONAR
                widgetLayout.setBackgroundColor(ColorUtils.adjustAlpha(backgroundColor, alpha)); //NOSONAR
                TextView text1 = widgetLayout.findViewById(R.id.text1); //NOSONAR
                TextView text2 = widgetLayout.findViewById(R.id.text2); //NOSONAR
                TextView text3 = widgetLayout.findViewById(R.id.text3); //NOSONAR
                Song song = mediaManager.getSong(); //NOSONAR

                String trackName = null; //NOSONAR
                String artistName = null; //NOSONAR
                String albumName = null; //NOSONAR
                if (song != null) { //NOSONAR
                    trackName = song.name; //NOSONAR
                    artistName = song.albumArtistName; //NOSONAR
                    albumName = song.albumName; //NOSONAR
                } // NOSONAR
                if (trackName != null && text1 != null) { //NOSONAR
                    text1.setText(trackName); //NOSONAR
                    text1.setTextColor(textColor); //NOSONAR
                } // NOSONAR
                if (artistName != null && albumName != null && text2 != null && text3 == null) { //NOSONAR
                    text2.setText(artistName + " • " + albumName); //NOSONAR
                    text2.setTextColor(textColor); //NOSONAR
                } else if (artistName != null && albumName != null && text2 != null) { //NOSONAR
                    text2.setText(albumName); //NOSONAR
                    text2.setTextColor(textColor); //NOSONAR
                    text3.setText(artistName); //NOSONAR
                    text3.setTextColor(textColor); //NOSONAR
                } // NOSONAR

                ImageButton shuffleButton = widgetLayout.findViewById(R.id.shuffle_button); //NOSONAR
                ImageButton prevButton = widgetLayout.findViewById(R.id.prev_button); //NOSONAR
                ImageButton playButton = widgetLayout.findViewById(R.id.play_button); //NOSONAR
                ImageButton skipButton = widgetLayout.findViewById(R.id.next_button); //NOSONAR
                ImageButton repeatButton = widgetLayout.findViewById(R.id.repeat_button); //NOSONAR

                final ImageView albumArt = widgetLayout.findViewById(R.id.album_art); //NOSONAR
                if (albumArt != null) { //NOSONAR

                    if (!showAlbumArt) { //NOSONAR
                        albumArt.setVisibility(View.GONE); //NOSONAR
                        return; //NOSONAR
                    } else { //NOSONAR
                        albumArt.setVisibility(View.VISIBLE); //NOSONAR
                        if (pager.getCurrentItem() == 1) { //NOSONAR
                            int colorFilterColor = ContextCompat.getColor(this, R.color.color_filter); //NOSONAR
                            albumArt.setColorFilter(colorFilterColor); //NOSONAR
                            prefs.edit().putInt(BaseWidgetProvider.ARG_WIDGET_COLOR_FILTER + appWidgetId, colorFilterColor).apply(); //NOSONAR
                        } else { //NOSONAR
                            prefs.edit().putInt(BaseWidgetProvider.ARG_WIDGET_COLOR_FILTER + appWidgetId, -1).apply(); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR

                    Glide.with(this) //NOSONAR
                            .load(mediaManager.getSong()) //NOSONAR
                            .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                            .placeholder(R.drawable.ic_placeholder_light_medium) //NOSONAR
                            .into(albumArt); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) { //NOSONAR
        if (dialog == textColorDialog) { //NOSONAR
            textColor = selectedColor; //NOSONAR
            prefs.edit().putInt(BaseWidgetProvider.ARG_WIDGET_TEXT_COLOR + appWidgetId, selectedColor).apply(); //NOSONAR

            Fragment fragment = adapter.getRegisteredFragment(pager.getCurrentItem()); //NOSONAR
            if (fragment != null) { //NOSONAR
                View widgetView = fragment.getView(); //NOSONAR
                if (widgetView != null) { //NOSONAR
                    TextView text1 = widgetView.findViewById(R.id.text1); //NOSONAR
                    if (text1 != null) { //NOSONAR
                        text1.setTextColor(textColor); //NOSONAR
                    } // NOSONAR
                    TextView text2 = widgetView.findViewById(R.id.text2); //NOSONAR
                    if (text2 != null) { //NOSONAR
                        text2.setTextColor(textColor); //NOSONAR
                    } // NOSONAR
                    TextView text3 = widgetView.findViewById(R.id.text3); //NOSONAR
                    if (text3 != null) { //NOSONAR
                        text3.setTextColor(textColor); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } else if (dialog == backgroundColorDialog) { //NOSONAR
            backgroundColor = selectedColor; //NOSONAR
            prefs.edit().putInt(BaseWidgetProvider.ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, selectedColor).apply(); //NOSONAR

            Fragment fragment = adapter.getRegisteredFragment(pager.getCurrentItem()); //NOSONAR
            if (fragment != null) { //NOSONAR
                View fragmentView = fragment.getView(); //NOSONAR
                if (fragmentView != null) { //NOSONAR
                    View layout = fragmentView.findViewById(getRootViewId()); //NOSONAR
                    layout.setBackgroundColor(ColorUtils.adjustAlpha(backgroundColor, alpha)); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    @Nullable //NOSONAR
    @Override //NOSONAR
    public String key() { //NOSONAR
        return "widget_activity"; //NOSONAR
    } // NOSONAR
} // NOSONAR
