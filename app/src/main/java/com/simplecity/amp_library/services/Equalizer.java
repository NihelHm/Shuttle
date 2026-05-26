package com.simplecity.amp_library.services; // NOSONAR

import android.content.BroadcastReceiver; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.IntentFilter; // NOSONAR
import android.content.SharedPreferences; // NOSONAR
import android.media.audiofx.AudioEffect; // NOSONAR
import android.media.audiofx.BassBoost; // NOSONAR
import android.media.audiofx.Virtualizer; // NOSONAR
import android.preference.PreferenceManager; // NOSONAR
import android.util.Log; // NOSONAR
import com.annimon.stream.Stream; // NOSONAR
import com.crashlytics.android.Crashlytics; // NOSONAR
import com.simplecity.amp_library.utils.SettingsManager; // NOSONAR
import java.util.Map; // NOSONAR
import java.util.concurrent.ConcurrentHashMap; // NOSONAR

/** // NOSONAR
 * <p>This calls listen to events that affect DSP function and responds to them.</p> // NOSONAR
 * <ol> // NOSONAR
 * <li>new audio session declarations</li> // NOSONAR
 * <li>headset plug / unplug events</li> // NOSONAR
 * <li>preference update events.</li> // NOSONAR
 * </ol> // NOSONAR
 * // NOSONAR
 * @author alankila // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class Equalizer { //NOSONAR

    private Context context; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    private static final String ACTION_OPEN_EQUALIZER_SESSION = "com.simplecity.amp_library.audiofx.OPEN_SESSION"; //NOSONAR
    private static final String ACTION_CLOSE_EQUALIZER_SESSION = "com.simplecity.amp_library.audiofx.CLOSE_SESSION"; //NOSONAR

    private SharedPreferences mPrefs; //NOSONAR

    public Equalizer(Context context, SettingsManager settingsManager) { //NOSONAR

        this.context = context; //NOSONAR

        this.settingsManager = settingsManager; //NOSONAR

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context); //NOSONAR

        IntentFilter audioFilter = new IntentFilter(); //NOSONAR
        audioFilter.addAction(ACTION_OPEN_EQUALIZER_SESSION); //NOSONAR
        audioFilter.addAction(ACTION_CLOSE_EQUALIZER_SESSION); //NOSONAR
        context.registerReceiver(mAudioSessionReceiver, audioFilter); //NOSONAR

        saveDefaults(); //NOSONAR
    } // NOSONAR

    public void release() { //NOSONAR
        releaseEffects(); //NOSONAR

        context.unregisterReceiver(mAudioSessionReceiver); //NOSONAR
    } // NOSONAR

    public void releaseEffects() { //NOSONAR
        Stream.of(mAudioSessions.values()) //NOSONAR
                .filter(effectSet -> effectSet != null) //NOSONAR
                .forEach(EffectSet::release); //NOSONAR
    } // NOSONAR

    public static String getZeroedBandsString(int length) { //NOSONAR
        StringBuilder stringBuilder = new StringBuilder(); //NOSONAR
        for (int i = 0; i < length; i++) { //NOSONAR
            stringBuilder.append("0;"); //NOSONAR
        } // NOSONAR
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); //NOSONAR
        return stringBuilder.toString(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Helper class representing the full complement of effects attached to one // NOSONAR
     * audio session. // NOSONAR
     * // NOSONAR
     * @author alankila // NOSONAR
     */ // NOSONAR
    private static class EffectSet { //NOSONAR
        /** // NOSONAR
         * Session-specific equalizer // NOSONAR
         */ // NOSONAR
        android.media.audiofx.Equalizer equalizer; //NOSONAR
        /** // NOSONAR
         * Session-specific bassboost // NOSONAR
         */ // NOSONAR
        private BassBoost bassBoost; //NOSONAR
        /** // NOSONAR
         * Session-specific virtualizer // NOSONAR
         */ // NOSONAR
        private Virtualizer virtualizer; //NOSONAR

        //        private final PresetReverb mPresetReverb; // NOSONAR

        private short mEqNumPresets = -1; //NOSONAR
        private short mEqNumBands = -1; //NOSONAR

        EffectSet(int sessionId) { //NOSONAR
            equalizer = new android.media.audiofx.Equalizer(1, sessionId); //NOSONAR
            bassBoost = new BassBoost(1, sessionId); //NOSONAR
            virtualizer = new Virtualizer(1, sessionId); //NOSONAR
            //            mPresetReverb = new PresetReverb(0, sessionId); // NOSONAR
        } // NOSONAR

        /* // NOSONAR
         * Take lots of care to not poke values that don't need // NOSONAR
         * to be poked- this can cause audible pops. // NOSONAR
         */ // NOSONAR

        void enableEqualizer(boolean enable) { //NOSONAR
            if (enable != equalizer.getEnabled()) { //NOSONAR
                if (!enable) { //NOSONAR
                    for (short i = 0; i < getNumEqualizerBands(); i++) { //NOSONAR
                        equalizer.setBandLevel(i, (short) 0); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
                equalizer.setEnabled(enable); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        void setEqualizerLevels(short[] levels) { //NOSONAR
            if (equalizer.getEnabled()) { //NOSONAR
                for (short i = 0; i < levels.length; i++) { //NOSONAR
                    if (equalizer.getBandLevel(i) != levels[i]) { //NOSONAR
                        equalizer.setBandLevel(i, levels[i]); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR

        short getNumEqualizerBands() { //NOSONAR
            if (mEqNumBands < 0) { //NOSONAR
                mEqNumBands = equalizer.getNumberOfBands(); //NOSONAR
            } // NOSONAR
            if (mEqNumBands > 6) { //NOSONAR
                mEqNumBands = 6; //NOSONAR
            } // NOSONAR
            return mEqNumBands; //NOSONAR
        } // NOSONAR

        short getNumEqualizerPresets() { //NOSONAR
            if (mEqNumPresets < 0) { //NOSONAR
                mEqNumPresets = equalizer.getNumberOfPresets(); //NOSONAR
            } // NOSONAR
            return mEqNumPresets; //NOSONAR
        } // NOSONAR

        void enableBassBoost(boolean enable) { //NOSONAR
            if (enable != bassBoost.getEnabled()) { //NOSONAR
                if (!enable) { //NOSONAR
                    bassBoost.setStrength((short) 1); //NOSONAR
                    bassBoost.setStrength((short) 0); //NOSONAR
                } // NOSONAR
                bassBoost.setEnabled(enable); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        void setBassBoostStrength(short strength) { //NOSONAR
            if (bassBoost.getEnabled() && bassBoost.getRoundedStrength() != strength) { //NOSONAR
                bassBoost.setStrength(strength); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        void enableVirtualizer(boolean enable) { //NOSONAR
            if (enable != virtualizer.getEnabled()) { //NOSONAR
                if (!enable) { //NOSONAR
                    virtualizer.setStrength((short) 1); //NOSONAR
                    virtualizer.setStrength((short) 0); //NOSONAR
                } // NOSONAR
                virtualizer.setEnabled(enable); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        void setVirtualizerStrength(short strength) { //NOSONAR
            if (virtualizer.getEnabled() && virtualizer.getRoundedStrength() != strength) { //NOSONAR
                virtualizer.setStrength(strength); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        //        public void enableReverb(boolean enable) { // NOSONAR
        //            if (enable != mPresetReverb.getEnabled()) { // NOSONAR
        //                if (!enable) { // NOSONAR
        //                    mPresetReverb.setPreset((short) 0); // NOSONAR
        //                } // NOSONAR
        //                mPresetReverb.setEnabled(enable); // NOSONAR
        //            } // NOSONAR
        //        } // NOSONAR

        //        public void setReverbPreset(short preset) { // NOSONAR
        //            if (mPresetReverb.getEnabled() && mPresetReverb.getPreset() != preset) { // NOSONAR
        //                mPresetReverb.setPreset(preset); // NOSONAR
        //            } // NOSONAR
        //        } // NOSONAR

        public void release() { //NOSONAR
            equalizer.release(); //NOSONAR
            bassBoost.release(); //NOSONAR
            virtualizer.release(); //NOSONAR
            //            mPresetReverb.release(); // NOSONAR
        } // NOSONAR
    } // NOSONAR

    protected static final String TAG = Equalizer.class.getSimpleName(); //NOSONAR

    /** // NOSONAR
     * Known audio sessions and their associated audioeffect suites. // NOSONAR
     */ // NOSONAR
    final Map<Integer, EffectSet> mAudioSessions = new ConcurrentHashMap<>(); //NOSONAR

    /** // NOSONAR
     * Receive new broadcast intents for adding DSP to session // NOSONAR
     */ // NOSONAR
    private final BroadcastReceiver mAudioSessionReceiver = new BroadcastReceiver() { //NOSONAR
        @Override //NOSONAR
        public void onReceive(Context context, Intent intent) { //NOSONAR
            String action = intent.getAction(); //NOSONAR
            int sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0); //NOSONAR
            if (action.equals(ACTION_OPEN_EQUALIZER_SESSION)) { //NOSONAR
                if (!mAudioSessions.containsKey(sessionId)) { //NOSONAR
                    try { //NOSONAR
                        EffectSet effectSet = new EffectSet(sessionId); //NOSONAR
                        mAudioSessions.put(sessionId, effectSet); //NOSONAR
                    } catch (Exception | ExceptionInInitializerError e) { //NOSONAR
                        Log.e(TAG, "Failed to open EQ session.. EffectSet error " + e); //NOSONAR
                    } // NOSONAR
                } // NOSONAR
            } // NOSONAR
            if (action.equals(ACTION_CLOSE_EQUALIZER_SESSION)) { //NOSONAR
                EffectSet gone = mAudioSessions.remove(sessionId); //NOSONAR
                if (gone != null) { //NOSONAR
                    gone.release(); //NOSONAR
                } // NOSONAR
            } // NOSONAR
            update(); //NOSONAR
        } // NOSONAR
    }; // NOSONAR

    private void saveDefaults() { //NOSONAR
        EffectSet temp; //NOSONAR
        try { //NOSONAR
            temp = new EffectSet(0); //NOSONAR
        } catch (Exception | ExceptionInInitializerError | UnsatisfiedLinkError e) { //NOSONAR
            releaseEffects(); //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        final int numBands = temp.getNumEqualizerBands(); //NOSONAR
        final int numPresets = temp.getNumEqualizerPresets(); //NOSONAR
        SharedPreferences.Editor editor = mPrefs.edit(); //NOSONAR
        editor.putString("equalizer.number_of_presets", String.valueOf(numPresets)).apply(); //NOSONAR
        editor.putString("equalizer.number_of_bands", String.valueOf(numBands)).apply(); //NOSONAR

        // range // NOSONAR
        short[] rangeShortArr = temp.equalizer.getBandLevelRange(); //NOSONAR

        editor.putString("equalizer.band_level_range", rangeShortArr[0] + ";" + rangeShortArr[1]).apply(); //NOSONAR

        // center freqs // NOSONAR
        StringBuilder centerFreqs = new StringBuilder(); //NOSONAR
        // audiofx.global.centerfreqs // NOSONAR
        for (short i = 0; i < numBands; i++) { //NOSONAR
            centerFreqs.append(temp.equalizer.getCenterFreq(i)); //NOSONAR
            centerFreqs.append(";"); //NOSONAR
        } // NOSONAR
        centerFreqs.deleteCharAt(centerFreqs.length() - 1); //NOSONAR
        editor.putString("equalizer.center_freqs", centerFreqs.toString()).apply(); //NOSONAR

        // populate preset names // NOSONAR
        StringBuilder presetNames = new StringBuilder(); //NOSONAR
        for (int i = 0; i < numPresets; i++) { //NOSONAR
            String presetName = temp.equalizer.getPresetName((short) i); //NOSONAR
            presetNames.append(presetName); //NOSONAR
            presetNames.append("|"); //NOSONAR

            // populate preset band values // NOSONAR
            StringBuilder presetBands = new StringBuilder(); //NOSONAR
            try { //NOSONAR
                temp.equalizer.usePreset((short) i); //NOSONAR
            } catch (RuntimeException e) { //NOSONAR
                Log.e(TAG, "equalizer.usePreset() failed"); //NOSONAR
            } // NOSONAR

            for (int j = 0; j < numBands; j++) { //NOSONAR
                // loop through preset bands // NOSONAR
                presetBands.append(temp.equalizer.getBandLevel((short) j)); //NOSONAR
                presetBands.append(";"); //NOSONAR
            } // NOSONAR
            presetBands.deleteCharAt(presetBands.length() - 1); //NOSONAR
            editor.putString("equalizer.preset." + i, presetBands.toString()).apply(); //NOSONAR
        } // NOSONAR
        if (presetNames.length() != 0) { //NOSONAR
            presetNames.deleteCharAt(presetNames.length() - 1); //NOSONAR
            editor.putString("equalizer.preset_names", presetNames.toString()).apply(); //NOSONAR
        } // NOSONAR
        temp.release(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Push new configuration to audio stack. // NOSONAR
     */ // NOSONAR
    public synchronized void update() { //NOSONAR
        try { //NOSONAR
            for (Integer sessionId : mAudioSessions.keySet()) { //NOSONAR
                updateDsp(mAudioSessions.get(sessionId)); //NOSONAR
            } // NOSONAR
        } catch (NoSuchMethodError e) { //NOSONAR
            Crashlytics.log("No such method error thrown when updating equalizer.. " + e.getMessage()); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void updateDsp(EffectSet session) { //NOSONAR
        final boolean globalEnabled = settingsManager.getEqualizerEnabled(); //NOSONAR

        try { //NOSONAR
            session.enableBassBoost(globalEnabled && mPrefs.getBoolean("audiofx.bass.enable", false)); //NOSONAR
            session.setBassBoostStrength(Short.valueOf(mPrefs.getString("audiofx.bass.strength", "0"))); //NOSONAR
        } catch (Exception e) { //NOSONAR
            Log.e(TAG, "Error enabling bass boost!", e); //NOSONAR
        } // NOSONAR

        //        try { // NOSONAR
        //            short preset = Short.decode(sharedPreferences.getString("audiofx.reverb.preset", String.valueOf(PresetReverb.PRESET_NONE))); // NOSONAR
        //            session.enableReverb(globalEnabled && (preset > 0)); // NOSONAR
        //            session.setReverbPreset(preset); // NOSONAR
        // // NOSONAR
        //        } catch (Exception e) { // NOSONAR
        //            Log.e(TAG, "Error enabling reverb preset", e); // NOSONAR
        //        } // NOSONAR

        try { //NOSONAR
            session.enableEqualizer(globalEnabled); //NOSONAR
            final int customPresetPos = session.getNumEqualizerPresets(); //NOSONAR
            final int preset = Integer.valueOf(mPrefs.getString("audiofx.eq.preset", String.valueOf(customPresetPos))); //NOSONAR
            final int bands = session.getNumEqualizerBands(); //NOSONAR

            /* // NOSONAR
             * Equalizer state is in a single string preference with all values // NOSONAR
             * separated by ; // NOSONAR
             */ // NOSONAR
            String[] levels; //NOSONAR

            if (preset == customPresetPos) { //NOSONAR
                levels = mPrefs.getString("audiofx.eq.bandlevels.custom", getZeroedBandsString(bands)).split(";"); //NOSONAR
            } else { //NOSONAR
                levels = mPrefs.getString("equalizer.preset." + preset, getZeroedBandsString(bands)).split(";"); //NOSONAR
            } // NOSONAR

            short[] equalizerLevels = new short[levels.length]; //NOSONAR
            for (int i = 0; i < levels.length; i++) { //NOSONAR
                equalizerLevels[i] = Short.parseShort(levels[i]); //NOSONAR
            } // NOSONAR

            session.setEqualizerLevels(equalizerLevels); //NOSONAR
        } catch (Exception e) { //NOSONAR
            Log.e(TAG, "Error enabling equalizer!", e); //NOSONAR
        } // NOSONAR

        try { //NOSONAR
            session.enableVirtualizer(globalEnabled && mPrefs.getBoolean("audiofx.virtualizer.enable", false)); //NOSONAR
            session.setVirtualizerStrength(Short.valueOf(mPrefs.getString("audiofx.virtualizer.strength", "0"))); //NOSONAR
        } catch (Exception e) { //NOSONAR
            Log.e(TAG, "Error enabling virtualizer!"); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sends a broadcast to close any existing audio effect sessions // NOSONAR
     */ // NOSONAR
    public void closeEqualizerSessions(boolean internal, int audioSessionId) { //NOSONAR

        if (internal) { //NOSONAR
            //Close the internal audio session // NOSONAR
            Intent intent = new Intent(Equalizer.ACTION_CLOSE_EQUALIZER_SESSION); //NOSONAR
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId); //NOSONAR
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.getPackageName()); //NOSONAR
            context.sendBroadcast(intent); //NOSONAR
        } else { //NOSONAR
            Intent intent = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION); //NOSONAR
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.getPackageName()); //NOSONAR
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId); //NOSONAR
            context.sendBroadcast(intent); //NOSONAR

            //Close any external audio sessions on session 0 // NOSONAR
            intent = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION); //NOSONAR
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.getPackageName()); //NOSONAR
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, 0); //NOSONAR
            context.sendBroadcast(intent); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public void openEqualizerSession(boolean internal, int audioSessionId) { //NOSONAR

        final Intent intent = new Intent(); //NOSONAR
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId); //NOSONAR
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.getPackageName()); //NOSONAR
        intent.setAction(internal ? Equalizer.ACTION_OPEN_EQUALIZER_SESSION : AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION); //NOSONAR
        context.sendBroadcast(intent); //NOSONAR
    } // NOSONAR
} // NOSONAR
