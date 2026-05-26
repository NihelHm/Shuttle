package com.simplecity.amp_library.ui.screens.equalizer; // NOSONAR

import android.annotation.SuppressLint; // NOSONAR
import android.annotation.TargetApi; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.media.audiofx.AudioEffect; // NOSONAR
import android.os.Build; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.support.v7.widget.SwitchCompat; // NOSONAR
import android.support.v7.widget.Toolbar; // NOSONAR
import android.util.Log; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.MenuItem; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.AdapterView; // NOSONAR
import android.widget.CompoundButton; // NOSONAR
import android.widget.SeekBar; // NOSONAR
import android.widget.Spinner; // NOSONAR
import android.widget.TextView; // NOSONAR
import butterknife.BindView; // NOSONAR
import butterknife.ButterKnife; // NOSONAR
import butterknife.Unbinder; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.constants.OpenSLESConstants; // NOSONAR
import com.simplecity.amp_library.services.Equalizer; // NOSONAR
import com.simplecity.amp_library.ui.adapters.RobotoSpinnerAdapter; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.DrawerLockManager; // NOSONAR
import com.simplecity.amp_library.ui.screens.drawer.MiniPlayerLockManager; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseFragment; // NOSONAR
import com.simplecity.amp_library.ui.views.SizableSeekBar; // NOSONAR
import java.util.Formatter; // NOSONAR
import java.util.Locale; // NOSONAR
import java.util.UUID; // NOSONAR

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

    /** // NOSONAR
     * Max number of EQ bands supported // NOSONAR
     */ // NOSONAR
    private final static int EQUALIZER_MAX_BANDS = 6; //NOSONAR

    /** // NOSONAR
     * Indicates if Equalizer effect is supported. // NOSONAR
     */ // NOSONAR
    private boolean equalizerSupported; //NOSONAR
    /** // NOSONAR
     * Indicates if BassBoost effect is supported. // NOSONAR
     */ // NOSONAR
    private boolean bassBoostSupported; //NOSONAR
    /** // NOSONAR
     * Indicates if Virtualizer effect is supported. // NOSONAR
     */ // NOSONAR
    private boolean virtualizerSupported; //NOSONAR

    // Equalizer fields // NOSONAR
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

    /** // NOSONAR
     * Mapping for the EQ widget ids per band // NOSONAR
     */ // NOSONAR
    static final int[][] eqViewElementIds = { //NOSONAR
            { R.id.EqBand0TopTextView, R.id.EqBand0SeekBar }, //NOSONAR
            { R.id.EqBand1TopTextView, R.id.EqBand1SeekBar }, //NOSONAR
            { R.id.EqBand2TopTextView, R.id.EqBand2SeekBar }, //NOSONAR
            { R.id.EqBand3TopTextView, R.id.EqBand3SeekBar }, //NOSONAR
            { R.id.EqBand4TopTextView, R.id.EqBand4SeekBar }, //NOSONAR
            { R.id.EqBand5TopTextView, R.id.EqBand5SeekBar } //NOSONAR
    }; // NOSONAR

    /** // NOSONAR
     * Mapping for the EQ widget ids per band // NOSONAR
     */ // NOSONAR
    private static final int[][] eqViewTextElementIds = { //NOSONAR
            { R.id.EqBand0LeftTextView, R.id.EqBand0RightTextView }, //NOSONAR
            { R.id.EqBand1LeftTextView, R.id.EqBand1RightTextView }, //NOSONAR
            { R.id.EqBand2LeftTextView, R.id.EqBand2RightTextView }, //NOSONAR
            { R.id.EqBand3LeftTextView, R.id.EqBand3RightTextView }, //NOSONAR
            { R.id.EqBand4LeftTextView, R.id.EqBand4RightTextView }, //NOSONAR
            { R.id.EqBand5LeftTextView, R.id.EqBand5RightTextView } //NOSONAR
    }; // NOSONAR

    @Override //NOSONAR
    public boolean onMenuItemClick(MenuItem item) { //NOSONAR
        switch (item.getItemId()) { //NOSONAR
            case R.id.menu_dsp: //NOSONAR
                Intent openDSP = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL); //NOSONAR
                if (getActivity().getPackageManager().resolveActivity(openDSP, 0) != null) { //NOSONAR
                    startActivityForResult(openDSP, 1000); //NOSONAR
                } // NOSONAR
                break; //NOSONAR
        } // NOSONAR
        return true; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //NOSONAR
        mediaManager.closeEqualizerSessions(!isChecked, mediaManager.getAudioSessionId()); //NOSONAR

        mediaManager.openEqualizerSession(isChecked, mediaManager.getAudioSessionId()); //NOSONAR

        // set parameter and state // NOSONAR
        prefs.edit().putBoolean("audiofx.global.enable", isChecked).apply(); //NOSONAR
        mediaManager.updateEqualizer(); //NOSONAR
    } // NOSONAR

    public static EqualizerFragment newInstance() { //NOSONAR
        Bundle args = new Bundle(); //NOSONAR
        EqualizerFragment fragment = new EqualizerFragment(); //NOSONAR
        fragment.setArguments(args); //NOSONAR
        return fragment; //NOSONAR
    } // NOSONAR

    @SuppressLint("InlinedApi") //NOSONAR
    @TargetApi(Build.VERSION_CODES.GINGERBREAD) //NOSONAR
    @Override //NOSONAR
    public void onCreate(Bundle savedInstanceState) { //NOSONAR

        super.onCreate(savedInstanceState); //NOSONAR

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext()); //NOSONAR

        try { //NOSONAR
            //Query available effects // NOSONAR
            final AudioEffect.Descriptor[] effects = AudioEffect.queryEffects(); //NOSONAR

            //Determine available/supported effects // NOSONAR
            if (effects != null && effects.length != 0) { //NOSONAR
                for (final AudioEffect.Descriptor effect : effects) { //NOSONAR
                    //Equalizer // NOSONAR
                    if (effect.type.equals(UUID.fromString(EFFECT_TYPE_EQUALIZER))) { //NOSONAR
                        equalizerSupported = true; //NOSONAR
                    } else if (effect.type.equals(UUID.fromString(EFFECT_TYPE_BASS_BOOST))) { //NOSONAR
                        bassBoostSupported = true; //NOSONAR
                    } else if (effect.type.equals(UUID.fromString(EFFECT_TYPE_VIRTUALIZER))) { //NOSONAR
                        virtualizerSupported = true; //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } catch (NoClassDefFoundError ignored) { //NOSONAR
            //The user doesn't have the AudioEffect/AudioEffect.Descriptor class. How sad. // NOSONAR
        } // NOSONAR
    } // NOSONAR

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

        //Hide the 'open DSP' button if DSP/Other audio effects aren't available // NOSONAR
        final Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL); //NOSONAR
        if (getContext().getPackageManager().resolveActivity(intent, 0) == null) { //NOSONAR
            MenuItem openDSPItem = toolbar.getMenu().findItem(R.id.menu_dsp); //NOSONAR
            if (openDSPItem != null) { //NOSONAR
                openDSPItem.setVisible(false); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //NOSONAR

            @Override //NOSONAR
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //NOSONAR
                eqPreset = position; //NOSONAR
                equalizerSetPreset(position); //NOSONAR
            } // NOSONAR

            @Override //NOSONAR
            public void onNothingSelected(AdapterView<?> parent) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        }); // NOSONAR

        setupPresets(); //NOSONAR
        if (spinnerAdapter != null && spinnerAdapter.getCount() > eqPreset) { //NOSONAR
            spinner.setSelection(eqPreset); //NOSONAR
        } // NOSONAR

        //Initialize the equalizer elements // NOSONAR
        numberEqualizerBands = Integer.parseInt(prefs.getString("equalizer.number_of_bands", "5")); //NOSONAR
        final int[] centerFreqs = getCenterFreqs(); //NOSONAR
        final int[] bandLevelRange = getBandLevelRange(); //NOSONAR

        for (int band = 0; band < numberEqualizerBands; band++) { //NOSONAR
            //Unit conversion from mHz to Hz and use k prefix if necessary to display // NOSONAR
            float centerFreqHz = centerFreqs[band] / 1000; //NOSONAR
            String unitPrefix = ""; //NOSONAR
            if (centerFreqHz >= 1000) { //NOSONAR
                centerFreqHz = centerFreqHz / 1000; //NOSONAR
                unitPrefix = "k"; //NOSONAR
            } // NOSONAR
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
                        //Determine which band changed // NOSONAR
                        int seekbarId = seekBar.getId(); //NOSONAR
                        int band = 0; //NOSONAR
                        for (int i = 0; i < eqViewElementIds.length; i++) { //NOSONAR
                            if (eqViewElementIds[i][1] == seekbarId) { //NOSONAR
                                band = i; //NOSONAR
                            } // NOSONAR
                        } // NOSONAR

                        if (eqPreset != eqCustomPresetPosition) { //NOSONAR
                            equalizerCopyToCustom(); //NOSONAR
                            if (spinnerAdapter != null && spinnerAdapter.getCount() > eqCustomPresetPosition) { //NOSONAR
                                spinner.setSelection(eqCustomPresetPosition); //NOSONAR
                            } // NOSONAR
                        } else { //NOSONAR
                            int level = getBandLevelRange()[0] + (progress * 100); //NOSONAR
                            equalizerBandUpdate(band, level); //NOSONAR
                        } // NOSONAR
                    } // NOSONAR
                } // NOSONAR

                @Override //NOSONAR
                public void onStartTrackingTouch(SeekBar seekBar) { //NOSONAR
                    // Intentionally left empty. // NOSONAR
                } // NOSONAR

                @Override //NOSONAR
                public void onStopTrackingTouch(SeekBar seekBar) { //NOSONAR
                    mediaManager.updateEqualizer(); //NOSONAR
                } // NOSONAR
            }); // NOSONAR
        } // NOSONAR

        // Initialize the Bass Boost elements. // NOSONAR
        // Set the SeekBar listener. // NOSONAR
        if (bassBoostSupported) { //NOSONAR

            baseBoostSeekbar.setMax(OpenSLESConstants.BASSBOOST_MAX_STRENGTH - OpenSLESConstants.BASSBOOST_MIN_STRENGTH); //NOSONAR

            baseBoostSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //NOSONAR

                @Override //NOSONAR
                public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) { //NOSONAR
                    // set parameter and state // NOSONAR
                    if (fromUser) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.bass.enable", true).apply(); //NOSONAR
                        prefs.edit().putString("audiofx.bass.strength", String.valueOf(progress)).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR

                // If slider pos was 0 when starting re-enable effect // NOSONAR
                @Override //NOSONAR
                public void onStartTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.bass.enable", true).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR

                // If slider pos = 0 when stopping disable effect // NOSONAR
                @Override //NOSONAR
                public void onStopTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        // disable // NOSONAR
                        prefs.edit().putBoolean("audiofx.bass.enable", false).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            }); // NOSONAR
        } // NOSONAR

        // Initialize the Virtualizer elements. // NOSONAR
        // Set the SeekBar listener. // NOSONAR
        if (virtualizerSupported) { //NOSONAR

            virtualizerSeekbar.setMax(OpenSLESConstants.VIRTUALIZER_MAX_STRENGTH - OpenSLESConstants.VIRTUALIZER_MIN_STRENGTH); //NOSONAR

            virtualizerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //NOSONAR

                @Override //NOSONAR
                public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) { //NOSONAR
                    // set parameter and state // NOSONAR
                    if (fromUser) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.virtualizer.enable", true).apply(); //NOSONAR
                        prefs.edit().putString("audiofx.virtualizer.strength", String.valueOf(progress)).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR

                // If slider pos was 0 when starting re-enable effect // NOSONAR
                @Override //NOSONAR
                public void onStartTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        prefs.edit().putBoolean("audiofx.virtualizer.enable", true).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR

                // If slider pos = 0 when stopping disable effect // NOSONAR
                @Override //NOSONAR
                public void onStopTrackingTouch(final SeekBar seekBar) { //NOSONAR
                    if (seekBar.getProgress() == 0) { //NOSONAR
                        // disable // NOSONAR
                        prefs.edit().putBoolean("audiofx.virtualizer.enable", false).apply(); //NOSONAR
                        mediaManager.updateEqualizer(); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            }); // NOSONAR
        } // NOSONAR

        return rootView; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onResume() { //NOSONAR
        super.onResume(); //NOSONAR
        DrawerLockManager.getInstance().addDrawerLock(this); //NOSONAR
        MiniPlayerLockManager.getInstance().addMiniPlayerLock(this); //NOSONAR

        updateUI(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onPause() { //NOSONAR
        DrawerLockManager.getInstance().removeDrawerLock(this); //NOSONAR
        MiniPlayerLockManager.getInstance().removeMiniPlayerLock(this); //NOSONAR

        super.onPause(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onDestroyView() { //NOSONAR
        super.onDestroyView(); //NOSONAR

        unbinder.unbind(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the given EQ preset. // NOSONAR
     * // NOSONAR
     * @param preset EQ preset id. // NOSONAR
     */ // NOSONAR
    void equalizerSetPreset(final int preset) { //NOSONAR
        eqPreset = preset; //NOSONAR
        prefs.edit().putString("audiofx.eq.preset", String.valueOf(preset)).apply(); //NOSONAR

        String newLevels; //NOSONAR
        if (preset == eqCustomPresetPosition) { //NOSONAR
            // load custom if possible // NOSONAR
            newLevels = prefs.getString("audiofx.eq.bandlevels.custom", Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
        } else { //NOSONAR
            newLevels = prefs.getString("equalizer.preset." + preset, Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
        } // NOSONAR
        prefs.edit().putString("audiofx.eq.bandlevels", newLevels).apply(); //NOSONAR
        updateUI(); //NOSONAR

        mediaManager.updateEqualizer(); //NOSONAR
    } // NOSONAR

    void updateUI() { //NOSONAR

        setupPresets(); //NOSONAR

        if (equalizerSupported) { //NOSONAR
            equalizerUpdateDisplay(); //NOSONAR
        } // NOSONAR
        if (bassBoostSupported) { //NOSONAR
            baseBoostSeekbar.setProgress(Integer.valueOf(prefs.getString("audiofx.bass.strength", "0"))); //NOSONAR
        } // NOSONAR
        if (virtualizerSupported) { //NOSONAR
            virtualizerSeekbar.setProgress(Integer.valueOf(prefs.getString("audiofx.virtualizer.strength", "0"))); //NOSONAR
        } // NOSONAR

        // Initialize the Equalizer elements. // NOSONAR
        if (equalizerSupported) { //NOSONAR
            String preset = String.valueOf(numberEqualizerBands); //NOSONAR
            eqPreset = Integer.valueOf(prefs.getString("audiofx.eq.preset", preset)); //NOSONAR
            if (spinnerAdapter != null && spinnerAdapter.getCount() > eqPreset) { //NOSONAR
                spinner.setSelection(eqPreset); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Updates the EQ by getting the parameters. // NOSONAR
     */ // NOSONAR
    private void equalizerUpdateDisplay() { //NOSONAR

        String levelsString; //NOSONAR
        float[] floats; //NOSONAR

        if (eqPreset == eqCustomPresetPosition) { //NOSONAR
            // load custom preset for current device // NOSONAR
            // here mEQValues needs to be pre-populated with the user's preset values. // NOSONAR
            String[] customEq = prefs.getString("audiofx.eq.bandlevels.custom", Equalizer.getZeroedBandsString(numberEqualizerBands)).split(";"); //NOSONAR
            floats = new float[numberEqualizerBands]; //NOSONAR
            for (int band = 0; band < floats.length; band++) { //NOSONAR
                final float level = Float.parseFloat(customEq[band]); //NOSONAR
                floats[band] = level / 100.0f; //NOSONAR
                mEqualizerSeekBar[band].setProgress((int) ((getBandLevelRange()[1] / 100.0f) + (level / 100.0f))); //NOSONAR
            } // NOSONAR
        } else { //NOSONAR
            // try to load preset // NOSONAR
            levelsString = prefs.getString("equalizer.preset." + eqPreset, Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
            String[] bandLevels = levelsString.split(";"); //NOSONAR
            floats = new float[bandLevels.length]; //NOSONAR
            for (int band = 0; band < bandLevels.length; band++) { //NOSONAR
                final float level = Float.parseFloat(bandLevels[band]); //NOSONAR
                floats[band] = level / 100.0f; //NOSONAR
                mEqualizerSeekBar[band].setProgress((int) ((getBandLevelRange()[1] / 100.0f) + (level / 100.0f))); //NOSONAR
            } // NOSONAR
        } // NOSONAR
    } // NOSONAR

    void equalizerBandUpdate(final int band, final int level) { //NOSONAR

        String[] currentCustomLevels = prefs.getString("audiofx.eq.bandlevels.custom", Equalizer.getZeroedBandsString(numberEqualizerBands)).split(";"); //NOSONAR

        currentCustomLevels[band] = String.valueOf(level); //NOSONAR

        // save // NOSONAR
        StringBuilder builder = new StringBuilder(); //NOSONAR
        for (int i = 0; i < numberEqualizerBands; i++) { //NOSONAR
            builder.append(currentCustomLevels[i]); //NOSONAR
            builder.append(";"); //NOSONAR
        } // NOSONAR
        builder.deleteCharAt(builder.length() - 1); //NOSONAR
        prefs.edit().putString("audiofx.eq.bandlevels", builder.toString()).apply(); //NOSONAR
        prefs.edit().putString("audiofx.eq.bandlevels.custom", builder.toString()).apply(); //NOSONAR

        mediaManager.updateEqualizer(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Called when user starts touch eq on a preset // NOSONAR
     */ // NOSONAR
    void equalizerCopyToCustom() { //NOSONAR
        Log.d(TAG, "equalizerCopyToCustom()"); //NOSONAR
        StringBuilder bandLevels = new StringBuilder(); //NOSONAR
        for (int band = 0; band < numberEqualizerBands; band++) { //NOSONAR
            final float level = (getBandLevelRange()[0] / 100) + mEqualizerSeekBar[band].getProgress(); //NOSONAR
            bandLevels.append(level * 100); //NOSONAR
            bandLevels.append(";"); //NOSONAR
        } // NOSONAR
        // remove trailing ";" // NOSONAR
        bandLevels.deleteCharAt(bandLevels.length() - 1); //NOSONAR
        prefs.edit().putString("audiofx.eq.bandlevels.custom", bandLevels.toString()).apply(); //NOSONAR
        prefs.edit().putString("audiofx.eq.preset", String.valueOf(eqCustomPresetPosition)).apply(); //NOSONAR
    } // NOSONAR

    private String format(String format, Object... args) { //NOSONAR
        formatBuilder.setLength(0); //NOSONAR
        formatter.format(format, args); //NOSONAR
        return formatBuilder.toString(); //NOSONAR
    } // NOSONAR

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
            } // NOSONAR
            return freqs; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private int[] getCenterFreqs() { //NOSONAR
        String savedCenterFreqs = prefs.getString("equalizer.center_freqs", Equalizer.getZeroedBandsString(numberEqualizerBands)); //NOSONAR
        String[] split = savedCenterFreqs.split(";"); //NOSONAR
        int[] freqs = new int[split.length]; //NOSONAR
        for (int i = 0; i < split.length; i++) { //NOSONAR
            freqs[i] = Integer.valueOf(split[i]); //NOSONAR
        } // NOSONAR
        return freqs; //NOSONAR
    } // NOSONAR

    private void setupPresets() { //NOSONAR
        // setup equalizer presets // NOSONAR
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
        } // NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR
} // NOSONAR
