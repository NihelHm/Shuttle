package com.simplecity.amp_library.ui.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.playback.MediaManager;
import com.simplecity.amp_library.playback.constants.MediaButtonCommand;
import com.simplecity.amp_library.playback.constants.ServiceCommand;
import com.simplecity.amp_library.ui.common.BaseActivity;
import com.simplecity.amp_library.ui.screens.widgets.WidgetFragment;
import com.simplecity.amp_library.ui.views.SizableSeekBar;
import com.simplecity.amp_library.utils.ColorUtils;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

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

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
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
        }

        setContentView(R.layout.activity_widget_config); //NOSONAR

        Bundle extras = this.getIntent().getExtras(); //NOSONAR
        if (extras != null) { //NOSONAR
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID); //NOSONAR
        }
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) { //NOSONAR
            finish(); //NOSONAR
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this); //NOSONAR
        layoutId = prefs.getInt(getLayoutIdString() + appWidgetId, getWidgetLayouts()[0]); //NOSONAR
        backgroundColor = prefs.getInt(BaseWidgetProvider.ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, ContextCompat.getColor(this, R.color.white)); //NOSONAR
        textColor = prefs.getInt(BaseWidgetProvider.ARG_WIDGET_TEXT_COLOR + appWidgetId, Color.WHITE); //NOSONAR
        showAlbumArt = prefs.getBoolean(BaseWidgetProvider.ARG_WIDGET_SHOW_ARTWORK + appWidgetId, true); //NOSONAR

        Toolbar toolbar = findViewById(R.id.toolbar); //NOSONAR
        setSupportActionBar(toolbar); //NOSONAR

        layouts = getWidgetLayouts(); //NOSONAR

        // Instantiate a ViewPager and a PagerAdapter.
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
    }

    @Override //NOSONAR
    public void onBackPressed() { //NOSONAR
        if (pager.getCurrentItem() == 0) { //NOSONAR
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed(); //NOSONAR
        } else { //NOSONAR
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) { //NOSONAR

        if (compoundButton.getId() == R.id.checkBox1) { //NOSONAR
            showAlbumArt = checked; //NOSONAR
            prefs.edit().putBoolean(BaseWidgetProvider.ARG_WIDGET_SHOW_ARTWORK + appWidgetId, showAlbumArt).apply(); //NOSONAR
        }
        if (compoundButton.getId() == R.id.checkBox2) { //NOSONAR
            invertIcons = checked; //NOSONAR
            prefs.edit().putBoolean(BaseWidgetProvider.ARG_WIDGET_INVERT_ICONS + appWidgetId, invertIcons).apply(); //NOSONAR
        }
        updateWidgetUI(); //NOSONAR
    }

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

            // Send broadcast intent to any running MediaPlaybackService so it can
            // wrap around with an immediate update.
            Intent updateIntent = new Intent(ServiceCommand.COMMAND); //NOSONAR
            updateIntent.putExtra(MediaButtonCommand.CMD_NAME, getUpdateCommandString()); //NOSONAR
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId }); //NOSONAR
            updateIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY); //NOSONAR
            sendBroadcast(updateIntent); //NOSONAR

            finish(); //NOSONAR
        }

        if (view.getId() == R.id.btn_background_color) { //NOSONAR
            backgroundColorDialog = new ColorChooserDialog.Builder(this, R.string.color_pick) //NOSONAR
                    .allowUserColorInputAlpha(true) //NOSONAR
                    .show(getSupportFragmentManager()); //NOSONAR
        }
        if (view.getId() == R.id.btn_text_color) { //NOSONAR
            textColorDialog = new ColorChooserDialog.Builder(this, R.string.color_pick) //NOSONAR
                    .allowUserColorInputAlpha(true) //NOSONAR
                    .show(getSupportFragmentManager()); //NOSONAR
        }
    }

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
            }
        }
    }

    @Override //NOSONAR
    public void onStartTrackingTouch(SeekBar seekBar) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onStopTrackingTouch(SeekBar seekBar) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onPageScrolled(int i, float v, int i2) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onPageSelected(int position) { //NOSONAR
        layoutId = layouts[position]; //NOSONAR
        prefs.edit().putInt(getLayoutIdString() + appWidgetId, layoutId).apply(); //NOSONAR
        updateWidgetUI(); //NOSONAR
    }

    @Override //NOSONAR
    public void onPageScrollStateChanged(int i) { //NOSONAR
        // Intentionally left empty.
    }

    @Override //NOSONAR
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) { //NOSONAR
        updateWidgetUI(); //NOSONAR
        super.onServiceConnected(componentName, iBinder); //NOSONAR
    }

    private class WidgetPagerAdapter extends FragmentStatePagerAdapter { //NOSONAR

        public WidgetPagerAdapter(FragmentManager fm) { //NOSONAR
            super(fm); //NOSONAR
        }

        @Override //NOSONAR
        public Fragment getItem(int position) { //NOSONAR
            return WidgetFragment.newInstance(layouts[position]); //NOSONAR
        }

        @Override //NOSONAR
        public int getCount() { //NOSONAR
            return layouts.length; //NOSONAR
        }

        @Override //NOSONAR
        public CharSequence getPageTitle(int position) { //NOSONAR
            return "Layout " + String.valueOf(position + 1); //NOSONAR
        }

        @Override //NOSONAR
        public Object instantiateItem(ViewGroup container, int position) { //NOSONAR
            Fragment fragment = (Fragment) super.instantiateItem(container, position); //NOSONAR
            registeredFragments.put(position, fragment); //NOSONAR
            return fragment; //NOSONAR
        }

        @Override //NOSONAR
        public void destroyItem(ViewGroup container, int position, Object object) { //NOSONAR
            registeredFragments.remove(position); //NOSONAR
            super.destroyItem(container, position, object); //NOSONAR
        }

        public Fragment getRegisteredFragment(int position) { //NOSONAR
            return registeredFragments.get(position); //NOSONAR
        }
    }

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
                }
                if (trackName != null && text1 != null) { //NOSONAR
                    text1.setText(trackName); //NOSONAR
                    text1.setTextColor(textColor); //NOSONAR
                }
                if (artistName != null && albumName != null && text2 != null && text3 == null) { //NOSONAR
                    text2.setText(artistName + " • " + albumName); //NOSONAR
                    text2.setTextColor(textColor); //NOSONAR
                } else if (artistName != null && albumName != null && text2 != null) { //NOSONAR
                    text2.setText(albumName); //NOSONAR
                    text2.setTextColor(textColor); //NOSONAR
                    text3.setText(artistName); //NOSONAR
                    text3.setTextColor(textColor); //NOSONAR
                }

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
                        }
                    }

                    Glide.with(this) //NOSONAR
                            .load(mediaManager.getSong()) //NOSONAR
                            .diskCacheStrategy(DiskCacheStrategy.ALL) //NOSONAR
                            .placeholder(R.drawable.ic_placeholder_light_medium) //NOSONAR
                            .into(albumArt); //NOSONAR
                }
            }
        }
    }

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
                    }
                    TextView text2 = widgetView.findViewById(R.id.text2); //NOSONAR
                    if (text2 != null) { //NOSONAR
                        text2.setTextColor(textColor); //NOSONAR
                    }
                    TextView text3 = widgetView.findViewById(R.id.text3); //NOSONAR
                    if (text3 != null) { //NOSONAR
                        text3.setTextColor(textColor); //NOSONAR
                    }
                }
            }
        } else if (dialog == backgroundColorDialog) { //NOSONAR
            backgroundColor = selectedColor; //NOSONAR
            prefs.edit().putInt(BaseWidgetProvider.ARG_WIDGET_BACKGROUND_COLOR + appWidgetId, selectedColor).apply(); //NOSONAR

            Fragment fragment = adapter.getRegisteredFragment(pager.getCurrentItem()); //NOSONAR
            if (fragment != null) { //NOSONAR
                View fragmentView = fragment.getView(); //NOSONAR
                if (fragmentView != null) { //NOSONAR
                    View layout = fragmentView.findViewById(getRootViewId()); //NOSONAR
                    layout.setBackgroundColor(ColorUtils.adjustAlpha(backgroundColor, alpha)); //NOSONAR
                }
            }
        }
    }

    @Override //NOSONAR
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) { //NOSONAR
        // Intentionally left empty.
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public String key() { //NOSONAR
        return "widget_activity"; //NOSONAR
    }
}
