package com.simplecity.amp_library.ui.screens.equalizer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.constants.OpenSLESConstants;
import com.simplecity.amp_library.services.Equalizer;
import com.simplecity.amp_library.ui.adapters.RobotoSpinnerAdapter;
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager;
import com.simplecity.amp_library.ui.screens.drawer.MiniPlayerLockManager;
import com.simplecity.amp_library.ui.common.BaseFragment;
import com.simplecity.amp_library.ui.views.SizableSeekBar;
import java.util.Formatter;
import java.util.Locale;
import java.util.UUID;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class EqualizerFragment extends BaseFragment implements //NOSONAR
        Toolbar.OnMenuItemClickListener, //NOSONAR
        CompoundButton.OnCheckedChangeListener, //NOSONAR
        DrawerLockManager.DrawerLock, //NOSONAR
        MiniPlayerLockManager.MiniPlayerLock { //NOSONAR

    private static final String TAG = "EqualizerFragment"; //NOSONAR

    private static final String EFFECT_TYPE_EQUALIZER = "47382d60-ddd8-11db-bf3a-0002a5d5c51b"; //NOSONAR

    private static final String EFFECT_TYPE_BASS_BOOST = "0634f220-ddd4-11db-a0fc-0002a5d5c51b"; //NOSONAR

    private static final String EFFECT_TYPE_VIRTUALIZER = "37cc2c00-dddd-11db-8577-0002a5d5c51b"; //NOSONAR

    SharedPreferences prefs; //NOSONAR

    /**
     * Max number of EQ bands supported
     */
    private final static int EQUALIZER_MAX_BANDS = 6; //NOSONAR

    /**
     * Indicates if Equalizer effect is supported.
     */
    private boolean equalizerSupported; //NOSONAR
    /**
     * Indicates if BassBoost effect is supported.
     */
    private boolean bassBoostSupported; //NOSONAR
    /**
     * Indicates if Virtualizer effect is supported.
     */
    private boolean virtualizerSupported; //NOSONAR

    // Equalizer fields
    private int numberEqualizerBands; //NOSONAR
    int eqCustomPresetPosition = 1; //NOSONAR
    int eqPreset; //NOSONAR
    private String[] eqPresetNames; //NOSONAR

    private final SizableSeekBar[] mEqualizerSeekBar = new SizableSeekBar[EQUALIZER_MAX_BANDS]; //NOSONAR

    Unbinder unbinder; //NOSONAR

    @BindView(R.id.toolbar) //NOSONAR
    Toolbar toolbar; //NOSONAR

    @BindView(R.id.eqSpinner) //NOSONAR
    Spinner spinner; //NOSONAR

    @BindView(R.id.eqContainer) //NOSONAR
    View eqContainer; //NOSONAR

    @BindView(R.id.bb_strength) //NOSONAR
    SizableSeekBar baseBoostSeekbar; //NOSONAR

    @BindView(R.id.virtualizer_strength) //NOSONAR
    SizableSeekBar virtualizerSeekbar; //NOSONAR

    RobotoSpinnerAdapter spinnerAdapter; //NOSONAR

    private StringBuilder formatBuilder = new StringBuilder(); //NOSONAR
    private Formatter formatter = new Formatter(formatBuilder, Locale.getDefault()); //NOSONAR

    /**
     * Mapping for the EQ widget ids per band
     */
    static final int[][] eqViewElementIds = { //NOSONAR
            { R.id.EqBand0TopTextView, R.id.EqBand0SeekBar }, //NOSONAR
            { R.id.EqBand1TopTextView, R.id.EqBand1SeekBar }, //NOSONAR
            { R.id.EqBand2TopTextView, R.id.EqBand2SeekBar }, //NOSONAR
            { R.id.EqBand3TopTextView, R.id.EqBand3SeekBar }, //NOSONAR
            { R.id.EqBand4TopTextView, R.id.EqBand4SeekBar }, //NOSONAR
            { R.id.EqBand5TopTextView, R.id.EqBand5SeekBar } //NOSONAR
    };

    /**
     * Mapping for the EQ widget ids per band
     */
    private static final int[][] eqViewTextElementIds = { //NOSONAR
            { R.id.EqBand0LeftTextView, R.id.EqBand0RightTextView }, //NOSONAR
            { R.id.EqBand1LeftTextView, R.id.EqBand1RightTextView }, //NOSONAR
            { R.id.EqBand2LeftTextView, R.id.EqBand2RightTextView }, //NOSONAR
            { R.id.EqBand3LeftTextView, R.id.EqBand3RightTextView }, //NOSONAR
            { R.id.EqBand4LeftTextView, R.id.EqBand4RightTextView }, //NOSONAR
            { R.id.EqBand5LeftTextView, R.id.EqBand5RightTextView } //NOSONAR
    };

    @Override //NOSONAR
    public boolean onMenuItemClick(MenuItem item) { //NOSONAR
        switch (item.getItemId()) { //NOSONAR
            case R.id.menu_dsp: //NOSONAR
                Intent openDSP = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL); //NOSONAR
                if (getActivity().getPackageManager().resolveActivity(openDSP, 0) != null) { //NOSONAR
                    startActivityForResult(openDSP, 1000); //NOSONAR
                }
                break; //NOSONAR
        }
        return true; //NOSONAR
    }

    @Override //NOSONAR
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //NOSONAR
        mediaManager.closeEqualizerSessions(!isChecked, mediaManager.getAudioSessionId()); //NOSONAR

        mediaManager.openEqualizerSession(isChecked, mediaManager.getAudioSessionId()); //NOSONAR

        // set parameter and state
        prefs.edit().putBoolean("audiofx.global.enable", isChecked).apply(); //NOSONAR
        mediaManager.updateEqualizer(); //NOSONAR
    }

    public static EqualizerFragment newInstance() { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        EqualizerFragment fragment = new EqualizerFragment(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    }

    @SuppressLint("InlinedApi") //NOSONAR
    @TargetApi(Build.VERSION_CODES.GINGERBREAD) //NOSONAR
    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR

        super.onCreate(savedInstanceState); //NOSONAR

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext()); //NOSONAR

        try { //NOSONAR
            //Query available effects
            final AudioEffect.Descriptor[] effects = AudioEffect.queryEffects(); //NOSONAR

            //Determine available/supported effects
            if (effects != null && effects.length != 0) { //NOSONAR
                for (final AudioEffect.Descriptor effect : effects) { //NOSONAR
                    //Equalizer
                    if (effect.type.equals(UUID.fromString(EFFECT_TYPE_EQUALIZER))) { //NOSONAR
                        equalizerSupported = true; //NOSONAR
                    } else if (effect.type.equals(UUID.fromString(EFFECT_TYPE_BASS_BOOST))) { //NOSONAR
                        bassBoostSupported = true; //NOSONAR
                    } else if (effect.type.equals(UUID.fromString(EFFECT_TYPE_VIRTUALIZER))) { //NOSONAR
                        virtualizerSupported = true; //NOSONAR
                    }
                }
            }
        } catch (NoClassDefFoundError ignored) { //NOSONAR
            //The user doesn't have the AudioEffect/AudioEffect.Descriptor class. How sad.
        }
    }

    @Nullable //NOSONAR
    @Override //NOSONAR
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //NOSONAR

        View rootView = inflater.inflate(R.layout.fragment_equalizer, container, false); //NOSONAR

        unbinder = ButterKnife.bind(this, rootView); //NOSONAR

        toolbar.inflateMenu(R.menu.menu_equalizer); //NOSONAR
        toolbar.setNavigationOnClickListener(v -> getNavigationController().popViewController()); //NOSONAR
        toolbar.setOnMenuItemClickListener(this); //NOSONAR

        MenuItem item = toolbar.getMenu().findItem(R.id.action_equalizer); //NOSONAR
        SwitchCompat switchItem = (SwitchCompat) item.getActionView(); //NOSONAR

        boolean isEnabled = prefs.getBoolean("audiofx.global.enable", false); //NOSONAR
        switchItem.setChecked(isEnabled); //NOSONAR
        switchItem.setOnCheckedChangeListener(this); //NOSONAR

        //Hide the 'open DSP' button if DSP/Other audio effects aren't available
        final Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL); //NOSONAR
        if (getContext().getPackageManager().resolveActivity(intent, 0) == null) { //NOSONAR
            MenuItem openDSPItem = toolbar.getMenu().findItem(R.id.menu_dsp); //NOSONAR
            if (openDSPItem != null) { //NOSONAR
                openDSPItem.setVisible(false); //NOSONAR
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //NOSONAR

            @Override //NOSONAR
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //NOSONAR
                eqPreset = position; //NOSONAR
                equalizerSetPreset(position); //NOSONAR
            }

            @Override //NOSONAR
            public void onNothingSelected(AdapterView<?> parent) { //NOSONAR
                // Intentionally left empty.
            }
        });

        setupPresets(); //NOSONAR
        if (spinnerAdapter != null && spinnerAdapter.getCount() > eqPreset) { //NOSONAR
            spinner.setSelection(eqPreset); //NOSONAR
        }

        //Initialize the equalizer elements
        numberEqualizerBands = Integer.parseInt(prefs.getString("equalizer.number_of_bands", "5")); //NOSONAR
        final int[] centerFreqs = getCenterFreqs(); //NOSONAR
        final int[] bandLevelRange = getBandLevelRange(); //NOSONAR

        for (int band = 0; band < numberEqualizerBands; band++) { //NOSONAR
            //Unit conversion from mHz to Hz and use k prefix if necessary to display
            float centerFreqHz = centerFreqs[band] / 1000; //NOSONAR
            String unitPrefix = ""; //NOSONAR
            if (centerFreqHz >= 1000) { //NOSONAR
                centerFreqHz = centerFreqHz / 1000; //NOSONAR
                unitPrefix = "k"; //NOSONAR
            }
            (eqContainer.findViewById(eqViewElementIds[band][0])).setVisibility(View.VISIBLE); //NOSONAR
            (eqContainer.findViewById(eqViewTextElementIds[band][0])).setVisibility(View.VISIBLE); //NOSONAR
            (eqContainer.findViewById(eqViewElementIds[band][1])).setVisibility(View.VISIBLE); //NOSONAR
            (eqContainer.findViewById(eqViewTextElementIds[band][1])).setVisibility(View.VISIBLE); //NOSONAR
            ((TextView) eqContainer.findViewById(eqViewElementIds[band][0])).setText(format("%.0f ", centerFreqHz) + unitPrefix + "Hz"); //NOSONAR
            mEqualizerSeekBar[band] = eqContainer.findViewById(eqViewElementIds[band][1]); //NOSONAR
            mEqualizerSeekBar[band].setMax((bandLevelRange[1] / 100) - (bandLevelRange[0] / 100)); //NOSONAR
            mEqualizerSeekBar[band].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //NOSONAR
                @Override //NOSONAR
                public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) { //NOSONAR

                    if (fromUser) { //NOSONAR
                        //Determine which band changed
                        int seekbarId = seekBar.getId(); //NOSONAR
                        int band = 0; //NOSONAR
                        for (int i = 0; i < eqViewElementIds.length; i++) { //NOSONAR
                            if (eqViewElementIds[i][1] == seekbarId) { //NOSONAR
                                band = i; //NOSONAR
                            }
                        }

                        if (eqPreset != eqCustomPresetPosition) { //NOSONAR
                            equalizerCopyToCustom(); //NOSONAR
                            if (spinnerAdapter != null && spinnerAdapter.getCount() > eqCustomPresetPosition) { //NOSONAR
                                spinner.setSelection(eqCustomPresetPosition); //NOSONAR
                            }
                        } else { //NOSONAR
                            int level = getBandLevelRange()[0] + (progress * 100); //NOSONAR
                            equalizerBandUpdate(band, level); //NOSONAR
                        }
                    }
                }

                @Override //NOSONAR
                public void onStartTrackingTouch(SeekBar seekBar) { //NOSONAR
                    // Intentionally left empty.
                }

                @Override //NOSONAR
                public void onStopTrackingTouch(SeekBar seekBar) { //NOSONAR
                    mediaManager.updateEqualizer(); //NOSONAR
                }
            });
        }

        // Initialize the Bass Boost elements.
        // Set the SeekBar listener.
        if (bassBoostSupported) { //NOSONAR

            baseBoostSeekbar.setMax(OpenSLESConstants.BASSBOOST_MAX_STRENGTH - OpenSLESConstants.BASSBOOST_MIN_STRENGTH); //NOSONAR

            baseBoostSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //NOSONAR

                @Override //NOSONAR
                public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) { //NOSONAR
                    // set parameter and state
                    if (fromUser) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.bass.enable", true).apply(); //NOSONAR
                        prefs.edit().putString("audiofx.bass.strength", String.valueOf(progress)).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    }
                }

                // If slider pos was 0 when starting re-enable effect
                @Override //NOSONAR
                public void onStartTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.bass.enable", true).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    }
                }

                // If slider pos = 0 when stopping disable effect
                @Override //NOSONAR
                public void onStopTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        // disable
                        prefs.edit().putBoolean("audiofx.bass.enable", false).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    }
                }
            });
        }

        // Initialize the Virtualizer elements.
        // Set the SeekBar listener.
        if (virtualizerSupported) { //NOSONAR

            virtualizerSeekbar.setMax(OpenSLESConstants.VIRTUALIZER_MAX_STRENGTH - OpenSLESConstants.VIRTUALIZER_MIN_STRENGTH); //NOSONAR

            virtualizerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //NOSONAR

                @Override //NOSONAR
                public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) { //NOSONAR
                    // set parameter and state
                    if (fromUser) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.virtualizer.enable", true).apply(); //NOSONAR
                        prefs.edit().putString("audiofx.virtualizer.strength", String.valueOf(progress)).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    }
                }

                // If slider pos was 0 when starting re-enable effect
                @Override //NOSONAR
                public void onStartTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.virtualizer.enable", true).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    }
                }

                // If slider pos = 0 when stopping disable effect
                @Override //NOSONAR
                public void onStopTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        // disable
                        prefs.edit().putBoolean("audiofx.virtualizer.enable", false).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    }
                }
            });
        }

        return rootView; //NOSONAR
    }

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR
        DrawerLockManager.getInstance().addDrawerLock(this); //NOSONAR
        MiniPlayerLockManager.getInstance().addMiniPlayerLock(this); //NOSONAR

        updateUI(); //NOSONAR
    }

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        DrawerLockManager.getInstance().removeDrawerLock(this); //NOSONAR
        MiniPlayerLockManager.getInstance().removeMiniPlayerLock(this); //NOSONAR

        super.onPause(); //NOSONAR
    }

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        super.onDestroyView(); //NOSONAR

        unbinder.unbind(); //NOSONAR
    }

    /**
     * Sets the given EQ preset.
     *
     * @param preset EQ preset id.
     */
    void equalizerSetPreset(final int preset) { //NOSONAR
        eqPreset = preset; //NOSONAR
        prefs.edit().putString("audiofx.eq.preset", String.valueOf(preset)).apply(); //NOSONAR

        String newLevels; //NOSONAR
        if (preset == eqCustomPresetPosition) { //NOSONAR
            // load custom if possible
            newLevels = prefs.getString("audiofx.eq.bandlevels.custom", Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
        } else { //NOSONAR
            newLevels = prefs.getString("equalizer.preset." + preset, Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
        }
        prefs.edit().putString("audiofx.eq.bandlevels", newLevels).apply(); //NOSONAR
        updateUI(); //NOSONAR

        mediaManager.updateEqualizer(); //NOSONAR
    }

    void updateUI() { //NOSONAR

        setupPresets(); //NOSONAR

        if (equalizerSupported) { //NOSONAR
            equalizerUpdateDisplay(); //NOSONAR
        }
        if (bassBoostSupported) { //NOSONAR
            baseBoostSeekbar.setProgress(Integer.valueOf(prefs.getString("audiofx.bass.strength", "0"))); //NOSONAR
        }
        if (virtualizerSupported) { //NOSONAR
            virtualizerSeekbar.setProgress(Integer.valueOf(prefs.getString("audiofx.virtualizer.strength", "0"))); //NOSONAR
        }

        // Initialize the Equalizer elements.
        if (equalizerSupported) { //NOSONAR
            String preset = String.valueOf(numberEqualizerBands); //NOSONAR
            eqPreset = Integer.valueOf(prefs.getString("audiofx.eq.preset", preset)); //NOSONAR
            if (spinnerAdapter != null && spinnerAdapter.getCount() > eqPreset) { //NOSONAR
                spinner.setSelection(eqPreset); //NOSONAR
            }
        }
    }

    /**
     * Updates the EQ by getting the parameters.
     */
    private void equalizerUpdateDisplay() { //NOSONAR

        String levelsString; //NOSONAR
        float[] floats; //NOSONAR

        if (eqPreset == eqCustomPresetPosition) { //NOSONAR
            // load custom preset for current device
            // here mEQValues needs to be pre-populated with the user's preset values.
            String[] customEq = prefs.getString("audiofx.eq.bandlevels.custom", Equalizer.getZeroedBandsString(numberEqualizerBands)).split(";"); //NOSONAR
            floats = new float[numberEqualizerBands]; //NOSONAR
            for (int band = 0; band < floats.length; band++) { //NOSONAR
                final float level = Float.parseFloat(customEq[band]); //NOSONAR
                floats[band] = level / 100.0f; //NOSONAR
                mEqualizerSeekBar[band].setProgress((int) ((getBandLevelRange()[1] / 100.0f) + (level / 100.0f))); //NOSONAR
            }
        } else { //NOSONAR
            // try to load preset
            levelsString = prefs.getString("equalizer.preset." + eqPreset, Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
            String[] bandLevels = levelsString.split(";"); //NOSONAR
            floats = new float[bandLevels.length]; //NOSONAR
            for (int band = 0; band < bandLevels.length; band++) { //NOSONAR
                final float level = Float.parseFloat(bandLevels[band]); //NOSONAR
                floats[band] = level / 100.0f; //NOSONAR
                mEqualizerSeekBar[band].setProgress((int) ((getBandLevelRange()[1] / 100.0f) + (level / 100.0f))); //NOSONAR
            }
        }
    }

    void equalizerBandUpdate(final int band, final int level) { //NOSONAR

        String[] currentCustomLevels = prefs.getString("audiofx.eq.bandlevels.custom", Equalizer.getZeroedBandsString(numberEqualizerBands)).split(";"); //NOSONAR

        currentCustomLevels[band] = String.valueOf(level); //NOSONAR

        // save
        StringBuilder builder = new StringBuilder(); //NOSONAR
        for (int i = 0; i < numberEqualizerBands; i++) { //NOSONAR
            builder.append(currentCustomLevels[i]); //NOSONAR
            builder.append(";"); //NOSONAR
        }
        builder.deleteCharAt(builder.length() - 1); //NOSONAR
        prefs.edit().putString("audiofx.eq.bandlevels", builder.toString()).apply(); //NOSONAR
        prefs.edit().putString("audiofx.eq.bandlevels.custom", builder.toString()).apply(); //NOSONAR

        mediaManager.updateEqualizer(); //NOSONAR
    }

    /**
     * Called when user starts touch eq on a preset
     */
    void equalizerCopyToCustom() { //NOSONAR
        Log.d(TAG, "equalizerCopyToCustom()"); //NOSONAR
        StringBuilder bandLevels = new StringBuilder(); //NOSONAR
        for (int band = 0; band < numberEqualizerBands; band++) { //NOSONAR
            final float level = (getBandLevelRange()[0] / 100) + mEqualizerSeekBar[band].getProgress(); //NOSONAR
            bandLevels.append(level * 100); //NOSONAR
            bandLevels.append(";"); //NOSONAR
        }
        // remove trailing ";"
        bandLevels.deleteCharAt(bandLevels.length() - 1); //NOSONAR
        prefs.edit().putString("audiofx.eq.bandlevels.custom", bandLevels.toString()).apply(); //NOSONAR
        prefs.edit().putString("audiofx.eq.preset", String.valueOf(eqCustomPresetPosition)).apply(); //NOSONAR
    }

    private String format(String format, Object... args) { //NOSONAR
        formatBuilder.setLength(0); //NOSONAR
        formatter.format(format, args); //NOSONAR
        return formatBuilder.toString(); //NOSONAR
    }

    private static final int MSG_UPDATE_EQUALIZER = 1; //NOSONAR

    int[] getBandLevelRange() { //NOSONAR
        String savedCenterFreqs = prefs.getString("equalizer.band_level_range", null); //NOSONAR
        if (savedCenterFreqs == null || savedCenterFreqs.isEmpty()) { //NOSONAR
            return new int[] { -1500, 1500 }; //NOSONAR
        } else { //NOSONAR
            String[] split = savedCenterFreqs.split(";"); //NOSONAR
            int[] freqs = new int[split.length]; //NOSONAR
            for (int i = 0; i < split.length; i++) { //NOSONAR
                freqs[i] = Integer.valueOf(split[i]); //NOSONAR
            }
            return freqs; //NOSONAR
        }
    }

    private int[] getCenterFreqs() { //NOSONAR
        String savedCenterFreqs = prefs.getString("equalizer.center_freqs", Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
        String[] split = savedCenterFreqs.split(";"); //NOSONAR
        int[] freqs = new int[split.length]; //NOSONAR
        for (int i = 0; i < split.length; i++) { //NOSONAR
            freqs[i] = Integer.valueOf(split[i]); //NOSONAR
        }
        return freqs; //NOSONAR
    }

    private void setupPresets() { //NOSONAR
        // setup equalizer presets
        final int numPresets = Integer.parseInt(prefs.getString("equalizer.number_of_presets", "0")); //NOSONAR
        eqPresetNames = new String[numPresets + 1]; //NOSONAR

        String[] presetNames = prefs.getString("equalizer.preset_names", "").split("\\|"); //NOSONAR
        System.arraycopy(presetNames, 0, eqPresetNames, 0, numPresets); //NOSONAR
        eqPresetNames[numPresets] = getString(R.string.custom); //NOSONAR
        eqCustomPresetPosition = numPresets; //NOSONAR

        if (spinnerAdapter == null || spinnerAdapter.getCount() != eqPresetNames.length) { //NOSONAR
            spinnerAdapter = new RobotoSpinnerAdapter<>(getContext(), R.layout.spinner_item, eqPresetNames); //NOSONAR
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //NOSONAR
            spinner.setAdapter(spinnerAdapter); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    }
}
