package com.github.florent37.glidepalette; // NOSONAR

import android.support.v4.util.Pair; // NOSONAR
import android.view.View; // NOSONAR
import android.widget.TextView; // NOSONAR

import java.util.ArrayList; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PaletteTarget { //NOSONAR

    @BitmapPalette.Profile //NOSONAR
    protected int paletteProfile = GlidePalette.Profile.VIBRANT; //NOSONAR

    protected ArrayList<Pair<View, Integer>> targetsBackground = new ArrayList<>(); //NOSONAR
    protected ArrayList<Pair<TextView, Integer>> targetsText = new ArrayList<>(); //NOSONAR

    protected boolean targetCrossfade = false; //NOSONAR
    protected int targetCrossfadeSpeed = DEFAULT_CROSSFADE_SPEED; //NOSONAR
    protected static final int DEFAULT_CROSSFADE_SPEED = 300; //NOSONAR

    public PaletteTarget(@BitmapPalette.Profile int paletteProfile) { //NOSONAR
        this.paletteProfile = paletteProfile; //NOSONAR
    } // NOSONAR

    public void clear() { //NOSONAR
        targetsBackground.clear(); //NOSONAR
        targetsText.clear(); //NOSONAR

        targetsBackground = null; //NOSONAR
        targetsText = null; //NOSONAR

        targetCrossfade = false; //NOSONAR
        targetCrossfadeSpeed = DEFAULT_CROSSFADE_SPEED; //NOSONAR
    } // NOSONAR
} // NOSONAR
