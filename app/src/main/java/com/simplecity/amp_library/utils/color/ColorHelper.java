package com.simplecity.amp_library.utils.color; // NOSONAR

/* // NOSONAR
 * Copyright (C) 2014 The Android Open Source Project // NOSONAR
 * // NOSONAR
 * Licensed under the Apache License, Version 2.0 (the "License"); // NOSONAR
 * you may not use this file except in compliance with the License. // NOSONAR
 * You may obtain a copy of the License at // NOSONAR
 * // NOSONAR
 *      http://www.apache.org/licenses/LICENSE-2.0 // NOSONAR
 * // NOSONAR
 * Unless required by applicable law or agreed to in writing, software // NOSONAR
 * distributed under the License is distributed on an "AS IS" BASIS, // NOSONAR
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. // NOSONAR
 * See the License for the specific language governing permissions and // NOSONAR
 * limitations under the License // NOSONAR
 */ // NOSONAR

import android.app.Notification; // NOSONAR
import android.content.Context; // NOSONAR
import android.graphics.Color; // NOSONAR
import android.support.annotation.ColorInt; // NOSONAR
import android.support.annotation.FloatRange; // NOSONAR
import android.support.annotation.IntRange; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.v4.util.Pair; // NOSONAR
import android.util.Log; // NOSONAR

/** // NOSONAR
 * Helper class to process legacy (Holo) notifications to make them look like material notifications. // NOSONAR
 * // NOSONAR
 * @hide // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ColorHelper { //NOSONAR

    private static final String TAG = "ColorHelper"; //NOSONAR

    private static final Object sLock = new Object(); //NOSONAR

    private static ColorHelper sInstance; //NOSONAR

    public static ColorHelper getInstance() { //NOSONAR
        synchronized (sLock) { //NOSONAR
            if (sInstance == null) { //NOSONAR
                sInstance = new ColorHelper(); //NOSONAR
            } // NOSONAR
            return sInstance; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Finds a suitable color such that there's enough contrast. // NOSONAR
     * // NOSONAR
     * @param color the color to start searching from. // NOSONAR
     * @param other the color to ensure contrast against. Assumed to be lighter than {@param color} // NOSONAR
     * @param findFg if true, we assume {@param color} is a foreground, otherwise a background. // NOSONAR
     * @param minRatio the minimum contrast ratio required. // NOSONAR
     * @return a color with the same hue as {@param color}, potentially darkened to meet the // NOSONAR
     *          contrast ratio. // NOSONAR
     */ // NOSONAR
    private static int findContrastColor(int color, int other, boolean findFg, double minRatio) { //NOSONAR
        int fg = findFg ? color : other; //NOSONAR
        int bg = findFg ? other : color; //NOSONAR
        if (ColorUtilsFromCompat.calculateContrast(fg, bg) >= minRatio) { //NOSONAR
            return color; //NOSONAR
        } // NOSONAR

        double[] lab = new double[3]; //NOSONAR
        ColorUtilsFromCompat.colorToLAB(findFg ? fg : bg, lab); //NOSONAR

        double low = 0, high = lab[0]; //NOSONAR
        final double a = lab[1], b = lab[2]; //NOSONAR
        for (int i = 0; i < 15 && high - low > 0.00001; i++) { //NOSONAR
            final double l = (low + high) / 2; //NOSONAR
            if (findFg) { //NOSONAR
                fg = ColorUtilsFromCompat.LABToColor(l, a, b); //NOSONAR
            } else { //NOSONAR
                bg = ColorUtilsFromCompat.LABToColor(l, a, b); //NOSONAR
            } // NOSONAR
            if (ColorUtilsFromCompat.calculateContrast(fg, bg) > minRatio) { //NOSONAR
                low = l; //NOSONAR
            } else { //NOSONAR
                high = l; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return ColorUtilsFromCompat.LABToColor(low, a, b); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Finds a suitable alpha such that there's enough contrast. // NOSONAR
     * // NOSONAR
     * @param color the color to start searching from. // NOSONAR
     * @param backgroundColor the color to ensure contrast against. // NOSONAR
     * @param minRatio the minimum contrast ratio required. // NOSONAR
     * @return the same color as {@param color} with potentially modified alpha to meet contrast // NOSONAR
     */ // NOSONAR
    private static int findAlphaToMeetContrast(int color, int backgroundColor, double minRatio) { //NOSONAR
        int fg = color; //NOSONAR
        int bg = backgroundColor; //NOSONAR
        if (ColorUtilsFromCompat.calculateContrast(fg, bg) >= minRatio) { //NOSONAR
            return color; //NOSONAR
        } // NOSONAR
        int startAlpha = Color.alpha(color); //NOSONAR
        int r = Color.red(color); //NOSONAR
        int g = Color.green(color); //NOSONAR
        int b = Color.blue(color); //NOSONAR

        int low = startAlpha, high = 255; //NOSONAR
        for (int i = 0; i < 15 && high - low > 0; i++) { //NOSONAR
            final int alpha = (low + high) / 2; //NOSONAR
            fg = Color.argb(alpha, r, g, b); //NOSONAR
            if (ColorUtilsFromCompat.calculateContrast(fg, bg) > minRatio) { //NOSONAR
                high = alpha; //NOSONAR
            } else { //NOSONAR
                low = alpha; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return Color.argb(high, r, g, b); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Finds a suitable color such that there's enough contrast. // NOSONAR
     * // NOSONAR
     * @param color the color to start searching from. // NOSONAR
     * @param other the color to ensure contrast against. Assumed to be darker than {@param color} // NOSONAR
     * @param findFg if true, we assume {@param color} is a foreground, otherwise a background. // NOSONAR
     * @param minRatio the minimum contrast ratio required. // NOSONAR
     * @return a color with the same hue as {@param color}, potentially darkened to meet the // NOSONAR
     *          contrast ratio. // NOSONAR
     */ // NOSONAR
    private static int findContrastColorAgainstDark(int color, int other, boolean findFg, //NOSONAR
            double minRatio) { //NOSONAR
        int fg = findFg ? color : other; //NOSONAR
        int bg = findFg ? other : color; //NOSONAR
        if (ColorUtilsFromCompat.calculateContrast(fg, bg) >= minRatio) { //NOSONAR
            return color; //NOSONAR
        } // NOSONAR

        float[] hsl = new float[3]; //NOSONAR
        ColorUtilsFromCompat.colorToHSL(findFg ? fg : bg, hsl); //NOSONAR

        float low = hsl[2], high = 1; //NOSONAR
        for (int i = 0; i < 15 && high - low > 0.00001; i++) { //NOSONAR
            final float l = (low + high) / 2; //NOSONAR
            hsl[2] = l; //NOSONAR
            if (findFg) { //NOSONAR
                fg = ColorUtilsFromCompat.HSLToColor(hsl); //NOSONAR
            } else { //NOSONAR
                bg = ColorUtilsFromCompat.HSLToColor(hsl); //NOSONAR
            } // NOSONAR
            if (ColorUtilsFromCompat.calculateContrast(fg, bg) > minRatio) { //NOSONAR
                high = l; //NOSONAR
            } else { //NOSONAR
                low = l; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return findFg ? fg : bg; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Change a color by a specified value // NOSONAR
     * @param baseColor the base color to lighten // NOSONAR
     * @param amount the amount to lighten the color from 0 to 100. This corresponds to the L // NOSONAR
     *               increase in the LAB color space. A negative value will darken the color and // NOSONAR
     *               a positive will lighten it. // NOSONAR
     * @return the changed color // NOSONAR
     */ // NOSONAR
    private static int changeColorLightness(int baseColor, int amount) { //NOSONAR
        final double[] result = ColorUtilsFromCompat.getTempDouble3Array(); //NOSONAR
        ColorUtilsFromCompat.colorToLAB(baseColor, result); //NOSONAR
        result[0] = Math.max(Math.min(100, result[0] + amount), 0); //NOSONAR
        return ColorUtilsFromCompat.LABToColor(result[0], result[1], result[2]); //NOSONAR
    } // NOSONAR

    public static int resolvePrimaryColor(Context context, int backgroundColor) { //NOSONAR
        boolean useDark = shouldUseDark(backgroundColor); //NOSONAR
        if (useDark) { //NOSONAR
            return context.getResources().getColor( //NOSONAR
                    android.R.color.primary_text_light); //NOSONAR
        } else { //NOSONAR
            return context.getResources().getColor( //NOSONAR
                   android.R.color.primary_text_dark); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    public static int resolveSecondaryColor(Context context, int backgroundColor) { //NOSONAR
        boolean useDark = shouldUseDark(backgroundColor); //NOSONAR
        if (useDark) { //NOSONAR
            return context.getResources().getColor( //NOSONAR
                    android.R.color.secondary_text_light); //NOSONAR
        } else { //NOSONAR
            return context.getResources().getColor( //NOSONAR
                    android.R.color.secondary_text_dark); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private static boolean shouldUseDark(int backgroundColor) { //NOSONAR
        boolean useDark = backgroundColor == Notification.COLOR_DEFAULT; //NOSONAR
        if (!useDark) { //NOSONAR
            useDark = ColorUtilsFromCompat.calculateLuminance(backgroundColor) > 0.5; //NOSONAR
        } // NOSONAR
        return useDark; //NOSONAR
    } // NOSONAR

    private static double calculateLuminance(int backgroundColor) { //NOSONAR
        return ColorUtilsFromCompat.calculateLuminance(backgroundColor); //NOSONAR
    } // NOSONAR

    private static double calculateContrast(int foregroundColor, int backgroundColor) { //NOSONAR
        return ColorUtilsFromCompat.calculateContrast(foregroundColor, backgroundColor); //NOSONAR
    } // NOSONAR

    private static boolean satisfiesTextContrast(int backgroundColor, int foregroundColor) { //NOSONAR
        return ColorHelper.calculateContrast(foregroundColor, backgroundColor) >= 4.5; //NOSONAR
    } // NOSONAR

    static boolean isColorLight(int backgroundColor) { //NOSONAR
        return calculateLuminance(backgroundColor) > 0.5f; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Framework copy of functions needed from android.support.v4.graphics.ColorUtils. // NOSONAR
     */ // NOSONAR
    private static class ColorUtilsFromCompat { //NOSONAR
        private static final double XYZ_WHITE_REFERENCE_X = 95.047; //NOSONAR
        private static final double XYZ_WHITE_REFERENCE_Y = 100; //NOSONAR
        private static final double XYZ_WHITE_REFERENCE_Z = 108.883; //NOSONAR
        private static final double XYZ_EPSILON = 0.008856; //NOSONAR
        private static final double XYZ_KAPPA = 903.3; //NOSONAR

        private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal<>(); //NOSONAR

        private ColorUtilsFromCompat() { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Composite two potentially translucent colors over each other and returns the result. // NOSONAR
         */ // NOSONAR
        static int compositeColors(@ColorInt int foreground, @ColorInt int background) { //NOSONAR
            int bgAlpha = Color.alpha(background); //NOSONAR
            int fgAlpha = Color.alpha(foreground); //NOSONAR
            int a = compositeAlpha(fgAlpha, bgAlpha); //NOSONAR

            int r = compositeComponent(Color.red(foreground), fgAlpha, //NOSONAR
                    Color.red(background), bgAlpha, a); //NOSONAR
            int g = compositeComponent(Color.green(foreground), fgAlpha, //NOSONAR
                    Color.green(background), bgAlpha, a); //NOSONAR
            int b = compositeComponent(Color.blue(foreground), fgAlpha, //NOSONAR
                    Color.blue(background), bgAlpha, a); //NOSONAR

            return Color.argb(a, r, g, b); //NOSONAR
        } // NOSONAR

        private static int compositeAlpha(int foregroundAlpha, int backgroundAlpha) { //NOSONAR
            return 0xFF - (((0xFF - backgroundAlpha) * (0xFF - foregroundAlpha)) / 0xFF); //NOSONAR
        } // NOSONAR

        private static int compositeComponent(int fgC, int fgA, int bgC, int bgA, int a) { //NOSONAR
            if (a == 0) return 0; //NOSONAR
            return ((0xFF * fgC * fgA) + (bgC * bgA * (0xFF - fgA))) / (a * 0xFF); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Returns the luminance of a color as a float between {@code 0.0} and {@code 1.0}. // NOSONAR
         * <p>Defined as the Y component in the XYZ representation of {@code color}.</p> // NOSONAR
         */ // NOSONAR
        @FloatRange(from = 0.0, to = 1.0) //NOSONAR
        public static double calculateLuminance(@ColorInt int color) { //NOSONAR
            final double[] result = getTempDouble3Array(); //NOSONAR
            colorToXYZ(color, result); //NOSONAR
            // Luminance is the Y component // NOSONAR
            return result[1] / 100; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Returns the contrast ratio between {@code foreground} and {@code background}. // NOSONAR
         * {@code background} must be opaque. // NOSONAR
         * <p> // NOSONAR
         * Formula defined // NOSONAR
         * <a href="http://www.w3.org/TR/2008/REC-WCAG20-20081211/#contrast-ratiodef">here</a>. // NOSONAR
         */ // NOSONAR
        static double calculateContrast(@ColorInt int foreground, @ColorInt int background) { //NOSONAR
            if (Color.alpha(background) != 255) { //NOSONAR
                Log.wtf(TAG, "background can not be translucent: #" //NOSONAR
                        + Integer.toHexString(background)); //NOSONAR
            } // NOSONAR
            if (Color.alpha(foreground) < 255) { //NOSONAR
                // If the foreground is translucent, composite the foreground over the background // NOSONAR
                foreground = compositeColors(foreground, background); //NOSONAR
            } // NOSONAR

            final double luminance1 = calculateLuminance(foreground) + 0.05; //NOSONAR
            final double luminance2 = calculateLuminance(background) + 0.05; //NOSONAR

            // Now return the lighter luminance divided by the darker luminance // NOSONAR
            return Math.max(luminance1, luminance2) / Math.min(luminance1, luminance2); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Convert the ARGB color to its CIE Lab representative components. // NOSONAR
         * // NOSONAR
         * @param color  the ARGB color to convert. The alpha component is ignored // NOSONAR
         * @param outLab 3-element array which holds the resulting LAB components // NOSONAR
         */ // NOSONAR
        static void colorToLAB(@ColorInt int color, @NonNull double[] outLab) { //NOSONAR
            RGBToLAB(Color.red(color), Color.green(color), Color.blue(color), outLab); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Convert RGB components to its CIE Lab representative components. // NOSONAR
         * // NOSONAR
         * <ul> // NOSONAR
         * <li>outLab[0] is L [0 ...100)</li> // NOSONAR
         * <li>outLab[1] is a [-128...127)</li> // NOSONAR
         * <li>outLab[2] is b [-128...127)</li> // NOSONAR
         * </ul> // NOSONAR
         * // NOSONAR
         * @param r      red component value [0..255] // NOSONAR
         * @param g      green component value [0..255] // NOSONAR
         * @param b      blue component value [0..255] // NOSONAR
         * @param outLab 3-element array which holds the resulting LAB components // NOSONAR
         */ // NOSONAR
        static void RGBToLAB(@IntRange(from = 0x0, to = 0xFF) int r, //NOSONAR
                @IntRange(from = 0x0, to = 0xFF) int g, @IntRange(from = 0x0, to = 0xFF) int b, //NOSONAR
                @NonNull double[] outLab) { //NOSONAR
            // First we convert RGB to XYZ // NOSONAR
            RGBToXYZ(r, g, b, outLab); //NOSONAR
            // outLab now contains XYZ // NOSONAR
            XYZToLAB(outLab[0], outLab[1], outLab[2], outLab); //NOSONAR
            // outLab now contains LAB representation // NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Convert the ARGB color to it's CIE XYZ representative components. // NOSONAR
         * // NOSONAR
         * <p>The resulting XYZ representation will use the D65 illuminant and the CIE // NOSONAR
         * 2° Standard Observer (1931).</p> // NOSONAR
         * // NOSONAR
         * <ul> // NOSONAR
         * <li>outXyz[0] is X [0 ...95.047)</li> // NOSONAR
         * <li>outXyz[1] is Y [0...100)</li> // NOSONAR
         * <li>outXyz[2] is Z [0...108.883)</li> // NOSONAR
         * </ul> // NOSONAR
         * // NOSONAR
         * @param color  the ARGB color to convert. The alpha component is ignored // NOSONAR
         * @param outXyz 3-element array which holds the resulting LAB components // NOSONAR
         */ // NOSONAR
        static void colorToXYZ(@ColorInt int color, @NonNull double[] outXyz) { //NOSONAR
            RGBToXYZ(Color.red(color), Color.green(color), Color.blue(color), outXyz); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Convert RGB components to it's CIE XYZ representative components. // NOSONAR
         * // NOSONAR
         * <p>The resulting XYZ representation will use the D65 illuminant and the CIE // NOSONAR
         * 2° Standard Observer (1931).</p> // NOSONAR
         * // NOSONAR
         * <ul> // NOSONAR
         * <li>outXyz[0] is X [0 ...95.047)</li> // NOSONAR
         * <li>outXyz[1] is Y [0...100)</li> // NOSONAR
         * <li>outXyz[2] is Z [0...108.883)</li> // NOSONAR
         * </ul> // NOSONAR
         * // NOSONAR
         * @param r      red component value [0..255] // NOSONAR
         * @param g      green component value [0..255] // NOSONAR
         * @param b      blue component value [0..255] // NOSONAR
         * @param outXyz 3-element array which holds the resulting XYZ components // NOSONAR
         */ // NOSONAR
        static void RGBToXYZ(@IntRange(from = 0x0, to = 0xFF) int r, //NOSONAR
                @IntRange(from = 0x0, to = 0xFF) int g, @IntRange(from = 0x0, to = 0xFF) int b, //NOSONAR
                @NonNull double[] outXyz) { //NOSONAR
            if (outXyz.length != 3) { //NOSONAR
                throw new IllegalArgumentException("outXyz must have a length of 3."); //NOSONAR
            } // NOSONAR

            double sr = r / 255.0; //NOSONAR
            sr = sr < 0.04045 ? sr / 12.92 : Math.pow((sr + 0.055) / 1.055, 2.4); //NOSONAR
            double sg = g / 255.0; //NOSONAR
            sg = sg < 0.04045 ? sg / 12.92 : Math.pow((sg + 0.055) / 1.055, 2.4); //NOSONAR
            double sb = b / 255.0; //NOSONAR
            sb = sb < 0.04045 ? sb / 12.92 : Math.pow((sb + 0.055) / 1.055, 2.4); //NOSONAR

            outXyz[0] = 100 * (sr * 0.4124 + sg * 0.3576 + sb * 0.1805); //NOSONAR
            outXyz[1] = 100 * (sr * 0.2126 + sg * 0.7152 + sb * 0.0722); //NOSONAR
            outXyz[2] = 100 * (sr * 0.0193 + sg * 0.1192 + sb * 0.9505); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Converts a color from CIE XYZ to CIE Lab representation. // NOSONAR
         * // NOSONAR
         * <p>This method expects the XYZ representation to use the D65 illuminant and the CIE // NOSONAR
         * 2° Standard Observer (1931).</p> // NOSONAR
         * // NOSONAR
         * <ul> // NOSONAR
         * <li>outLab[0] is L [0 ...100)</li> // NOSONAR
         * <li>outLab[1] is a [-128...127)</li> // NOSONAR
         * <li>outLab[2] is b [-128...127)</li> // NOSONAR
         * </ul> // NOSONAR
         * // NOSONAR
         * @param x      X component value [0...95.047) // NOSONAR
         * @param y      Y component value [0...100) // NOSONAR
         * @param z      Z component value [0...108.883) // NOSONAR
         * @param outLab 3-element array which holds the resulting Lab components // NOSONAR
         */ // NOSONAR
        static void XYZToLAB(@FloatRange(from = 0f, to = XYZ_WHITE_REFERENCE_X) double x, //NOSONAR
                @FloatRange(from = 0f, to = XYZ_WHITE_REFERENCE_Y) double y, //NOSONAR
                @FloatRange(from = 0f, to = XYZ_WHITE_REFERENCE_Z) double z, //NOSONAR
                @NonNull double[] outLab) { //NOSONAR
            if (outLab.length != 3) { //NOSONAR
                throw new IllegalArgumentException("outLab must have a length of 3."); //NOSONAR
            } // NOSONAR
            x = pivotXyzComponent(x / XYZ_WHITE_REFERENCE_X); //NOSONAR
            y = pivotXyzComponent(y / XYZ_WHITE_REFERENCE_Y); //NOSONAR
            z = pivotXyzComponent(z / XYZ_WHITE_REFERENCE_Z); //NOSONAR
            outLab[0] = Math.max(0, 116 * y - 16); //NOSONAR
            outLab[1] = 500 * (x - y); //NOSONAR
            outLab[2] = 200 * (y - z); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Converts a color from CIE Lab to CIE XYZ representation. // NOSONAR
         * // NOSONAR
         * <p>The resulting XYZ representation will use the D65 illuminant and the CIE // NOSONAR
         * 2° Standard Observer (1931).</p> // NOSONAR
         * // NOSONAR
         * <ul> // NOSONAR
         * <li>outXyz[0] is X [0 ...95.047)</li> // NOSONAR
         * <li>outXyz[1] is Y [0...100)</li> // NOSONAR
         * <li>outXyz[2] is Z [0...108.883)</li> // NOSONAR
         * </ul> // NOSONAR
         * // NOSONAR
         * @param l      L component value [0...100) // NOSONAR
         * @param a      A component value [-128...127) // NOSONAR
         * @param b      B component value [-128...127) // NOSONAR
         * @param outXyz 3-element array which holds the resulting XYZ components // NOSONAR
         */ // NOSONAR
        static void LABToXYZ(@FloatRange(from = 0f, to = 100) final double l, //NOSONAR
                @FloatRange(from = -128, to = 127) final double a, //NOSONAR
                @FloatRange(from = -128, to = 127) final double b, //NOSONAR
                @NonNull double[] outXyz) { //NOSONAR
            final double fy = (l + 16) / 116; //NOSONAR
            final double fx = a / 500 + fy; //NOSONAR
            final double fz = fy - b / 200; //NOSONAR

            double tmp = Math.pow(fx, 3); //NOSONAR
            final double xr = tmp > XYZ_EPSILON ? tmp : (116 * fx - 16) / XYZ_KAPPA; //NOSONAR
            final double yr = l > XYZ_KAPPA * XYZ_EPSILON ? Math.pow(fy, 3) : l / XYZ_KAPPA; //NOSONAR

            tmp = Math.pow(fz, 3); //NOSONAR
            final double zr = tmp > XYZ_EPSILON ? tmp : (116 * fz - 16) / XYZ_KAPPA; //NOSONAR

            outXyz[0] = xr * XYZ_WHITE_REFERENCE_X; //NOSONAR
            outXyz[1] = yr * XYZ_WHITE_REFERENCE_Y; //NOSONAR
            outXyz[2] = zr * XYZ_WHITE_REFERENCE_Z; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Converts a color from CIE XYZ to its RGB representation. // NOSONAR
         * // NOSONAR
         * <p>This method expects the XYZ representation to use the D65 illuminant and the CIE // NOSONAR
         * 2° Standard Observer (1931).</p> // NOSONAR
         * // NOSONAR
         * @param x X component value [0...95.047) // NOSONAR
         * @param y Y component value [0...100) // NOSONAR
         * @param z Z component value [0...108.883) // NOSONAR
         * @return int containing the RGB representation // NOSONAR
         */ // NOSONAR
        @ColorInt //NOSONAR
        static int XYZToColor(@FloatRange(from = 0f, to = XYZ_WHITE_REFERENCE_X) double x, //NOSONAR
                @FloatRange(from = 0f, to = XYZ_WHITE_REFERENCE_Y) double y, //NOSONAR
                @FloatRange(from = 0f, to = XYZ_WHITE_REFERENCE_Z) double z) { //NOSONAR
            double r = (x * 3.2406 + y * -1.5372 + z * -0.4986) / 100; //NOSONAR
            double g = (x * -0.9689 + y * 1.8758 + z * 0.0415) / 100; //NOSONAR
            double b = (x * 0.0557 + y * -0.2040 + z * 1.0570) / 100; //NOSONAR

            r = r > 0.0031308 ? 1.055 * Math.pow(r, 1 / 2.4) - 0.055 : 12.92 * r; //NOSONAR
            g = g > 0.0031308 ? 1.055 * Math.pow(g, 1 / 2.4) - 0.055 : 12.92 * g; //NOSONAR
            b = b > 0.0031308 ? 1.055 * Math.pow(b, 1 / 2.4) - 0.055 : 12.92 * b; //NOSONAR

            return Color.rgb( //NOSONAR
                    constrain((int) Math.round(r * 255), 0, 255), //NOSONAR
                    constrain((int) Math.round(g * 255), 0, 255), //NOSONAR
                    constrain((int) Math.round(b * 255), 0, 255)); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Converts a color from CIE Lab to its RGB representation. // NOSONAR
         * // NOSONAR
         * @param l L component value [0...100] // NOSONAR
         * @param a A component value [-128...127] // NOSONAR
         * @param b B component value [-128...127] // NOSONAR
         * @return int containing the RGB representation // NOSONAR
         */ // NOSONAR
        @ColorInt //NOSONAR
        static int LABToColor(@FloatRange(from = 0f, to = 100) final double l, //NOSONAR
                @FloatRange(from = -128, to = 127) final double a, //NOSONAR
                @FloatRange(from = -128, to = 127) final double b) { //NOSONAR
            final double[] result = getTempDouble3Array(); //NOSONAR
            LABToXYZ(l, a, b, result); //NOSONAR
            return XYZToColor(result[0], result[1], result[2]); //NOSONAR
        } // NOSONAR

        private static int constrain(int amount, int low, int high) { //NOSONAR
            return amount < low ? low : (amount > high ? high : amount); //NOSONAR
        } // NOSONAR

        private static float constrain(float amount, float low, float high) { //NOSONAR
            return amount < low ? low : (amount > high ? high : amount); //NOSONAR
        } // NOSONAR

        private static double pivotXyzComponent(double component) { //NOSONAR
            return component > XYZ_EPSILON //NOSONAR
                    ? Math.pow(component, 1 / 3.0) //NOSONAR
                    : (XYZ_KAPPA * component + 16) / 116; //NOSONAR
        } // NOSONAR

        static double[] getTempDouble3Array() { //NOSONAR
            double[] result = TEMP_ARRAY.get(); //NOSONAR
            if (result == null) { //NOSONAR
                result = new double[3]; //NOSONAR
                TEMP_ARRAY.set(result); //NOSONAR
            } // NOSONAR
            return result; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Convert HSL (hue-saturation-lightness) components to a RGB color. // NOSONAR
         * <ul> // NOSONAR
         * <li>hsl[0] is Hue [0 .. 360)</li> // NOSONAR
         * <li>hsl[1] is Saturation [0...1]</li> // NOSONAR
         * <li>hsl[2] is Lightness [0...1]</li> // NOSONAR
         * </ul> // NOSONAR
         * If hsv values are out of range, they are pinned. // NOSONAR
         * // NOSONAR
         * @param hsl 3-element array which holds the input HSL components // NOSONAR
         * @return the resulting RGB color // NOSONAR
         */ // NOSONAR
        @ColorInt //NOSONAR
        static int HSLToColor(@NonNull float[] hsl) { //NOSONAR
            final float h = hsl[0]; //NOSONAR
            final float s = hsl[1]; //NOSONAR
            final float l = hsl[2]; //NOSONAR

            final float c = (1f - Math.abs(2 * l - 1f)) * s; //NOSONAR
            final float m = l - 0.5f * c; //NOSONAR
            final float x = c * (1f - Math.abs((h / 60f % 2f) - 1f)); //NOSONAR

            final int hueSegment = (int) h / 60; //NOSONAR

            int r = 0, g = 0, b = 0; //NOSONAR

            switch (hueSegment) { //NOSONAR
                case 0: //NOSONAR
                    r = Math.round(255 * (c + m)); //NOSONAR
                    g = Math.round(255 * (x + m)); //NOSONAR
                    b = Math.round(255 * m); //NOSONAR
                    break; //NOSONAR
                case 1: //NOSONAR
                    r = Math.round(255 * (x + m)); //NOSONAR
                    g = Math.round(255 * (c + m)); //NOSONAR
                    b = Math.round(255 * m); //NOSONAR
                    break; //NOSONAR
                case 2: //NOSONAR
                    r = Math.round(255 * m); //NOSONAR
                    g = Math.round(255 * (c + m)); //NOSONAR
                    b = Math.round(255 * (x + m)); //NOSONAR
                    break; //NOSONAR
                case 3: //NOSONAR
                    r = Math.round(255 * m); //NOSONAR
                    g = Math.round(255 * (x + m)); //NOSONAR
                    b = Math.round(255 * (c + m)); //NOSONAR
                    break; //NOSONAR
                case 4: //NOSONAR
                    r = Math.round(255 * (x + m)); //NOSONAR
                    g = Math.round(255 * m); //NOSONAR
                    b = Math.round(255 * (c + m)); //NOSONAR
                    break; //NOSONAR
                case 5: //NOSONAR
                case 6: //NOSONAR
                    r = Math.round(255 * (c + m)); //NOSONAR
                    g = Math.round(255 * m); //NOSONAR
                    b = Math.round(255 * (x + m)); //NOSONAR
                    break; //NOSONAR
            } // NOSONAR

            r = constrain(r, 0, 255); //NOSONAR
            g = constrain(g, 0, 255); //NOSONAR
            b = constrain(b, 0, 255); //NOSONAR

            return Color.rgb(r, g, b); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Convert the ARGB color to its HSL (hue-saturation-lightness) components. // NOSONAR
         * <ul> // NOSONAR
         * <li>outHsl[0] is Hue [0 .. 360)</li> // NOSONAR
         * <li>outHsl[1] is Saturation [0...1]</li> // NOSONAR
         * <li>outHsl[2] is Lightness [0...1]</li> // NOSONAR
         * </ul> // NOSONAR
         * // NOSONAR
         * @param color  the ARGB color to convert. The alpha component is ignored // NOSONAR
         * @param outHsl 3-element array which holds the resulting HSL components // NOSONAR
         */ // NOSONAR
        static void colorToHSL(@ColorInt int color, @NonNull float[] outHsl) { //NOSONAR
            RGBToHSL(Color.red(color), Color.green(color), Color.blue(color), outHsl); //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * Convert RGB components to HSL (hue-saturation-lightness). // NOSONAR
         * <ul> // NOSONAR
         * <li>outHsl[0] is Hue [0 .. 360)</li> // NOSONAR
         * <li>outHsl[1] is Saturation [0...1]</li> // NOSONAR
         * <li>outHsl[2] is Lightness [0...1]</li> // NOSONAR
         * </ul> // NOSONAR
         * // NOSONAR
         * @param r      red component value [0..255] // NOSONAR
         * @param g      green component value [0..255] // NOSONAR
         * @param b      blue component value [0..255] // NOSONAR
         * @param outHsl 3-element array which holds the resulting HSL components // NOSONAR
         */ // NOSONAR
        static void RGBToHSL(@IntRange(from = 0x0, to = 0xFF) int r, //NOSONAR
                @IntRange(from = 0x0, to = 0xFF) int g, @IntRange(from = 0x0, to = 0xFF) int b, //NOSONAR
                @NonNull float[] outHsl) { //NOSONAR
            final float rf = r / 255f; //NOSONAR
            final float gf = g / 255f; //NOSONAR
            final float bf = b / 255f; //NOSONAR

            final float max = Math.max(rf, Math.max(gf, bf)); //NOSONAR
            final float min = Math.min(rf, Math.min(gf, bf)); //NOSONAR
            final float deltaMaxMin = max - min; //NOSONAR

            float h, s; //NOSONAR
            float l = (max + min) / 2f; //NOSONAR

            if (max == min) { //NOSONAR
                // Monochromatic // NOSONAR
                h = s = 0f; //NOSONAR
            } else { //NOSONAR
                if (max == rf) { //NOSONAR
                    h = ((gf - bf) / deltaMaxMin) % 6f; //NOSONAR
                } else if (max == gf) { //NOSONAR
                    h = ((bf - rf) / deltaMaxMin) + 2f; //NOSONAR
                } else { //NOSONAR
                    h = ((rf - gf) / deltaMaxMin) + 4f; //NOSONAR
                } // NOSONAR

                s = deltaMaxMin / (1f - Math.abs(2f * l - 1f)); //NOSONAR
            } // NOSONAR

            h = (h * 60f) % 360f; //NOSONAR
            if (h < 0) { //NOSONAR
                h += 360f; //NOSONAR
            } // NOSONAR

            outHsl[0] = constrain(h, 0f, 360f); //NOSONAR
            outHsl[1] = constrain(s, 0f, 1f); //NOSONAR
            outHsl[2] = constrain(l, 0f, 1f); //NOSONAR
        } // NOSONAR

    } // NOSONAR


    /** // NOSONAR
     * The lightness difference that has to be added to the primary text color to obtain the // NOSONAR
     * secondary text color when the background is light. // NOSONAR
     */ // NOSONAR
    private static final int LIGHTNESS_TEXT_DIFFERENCE_LIGHT = 20; //NOSONAR

    /** // NOSONAR
     * The lightness difference that has to be added to the primary text color to obtain the // NOSONAR
     * secondary text color when the background is dark. // NOSONAR
     * A bit less then the above value, since it looks better on dark backgrounds. // NOSONAR
     */ // NOSONAR
    private static final int LIGHTNESS_TEXT_DIFFERENCE_DARK = -10; //NOSONAR

    public Pair<Integer, Integer> ensureColors(Context context, boolean hasForegroundColor, int backgroundColor, int foregroundColor) { //NOSONAR
        int primaryTextColor; //NOSONAR
        int secondaryTextColor; //NOSONAR
        if (!hasForegroundColor) { //NOSONAR
            primaryTextColor = ColorHelper.resolvePrimaryColor(context, backgroundColor); //NOSONAR
            secondaryTextColor = ColorHelper.resolveSecondaryColor(context, backgroundColor); //NOSONAR
            int COLOR_DEFAULT = 0; //NOSONAR
            if (backgroundColor != COLOR_DEFAULT) { //NOSONAR
                primaryTextColor = ColorHelper.findAlphaToMeetContrast(primaryTextColor, backgroundColor, 4.5); //NOSONAR
                secondaryTextColor = ColorHelper.findAlphaToMeetContrast(secondaryTextColor, backgroundColor, 4.5); //NOSONAR
            } // NOSONAR
        } else { //NOSONAR
            double backLum = ColorHelper.calculateLuminance(backgroundColor); //NOSONAR
            double textLum = ColorHelper.calculateLuminance(foregroundColor); //NOSONAR
            double contrast = ColorHelper.calculateContrast(foregroundColor, //NOSONAR
                    backgroundColor); //NOSONAR
            // We only respect the given colors if worst case Black or White still has // NOSONAR
            // contrast // NOSONAR
            boolean backgroundLight = backLum > textLum && ColorHelper.satisfiesTextContrast(backgroundColor, Color.BLACK) //NOSONAR
                    || backLum <= textLum && !ColorHelper.satisfiesTextContrast(backgroundColor, Color.WHITE); //NOSONAR
            if (contrast < 4.5f) { //NOSONAR
                if (backgroundLight) { //NOSONAR
                    secondaryTextColor = ColorHelper.findContrastColor( //NOSONAR
                            foregroundColor, //NOSONAR
                            backgroundColor, //NOSONAR
                            true /* findFG */, //NOSONAR
                            4.5f); //NOSONAR
                    primaryTextColor = ColorHelper.changeColorLightness( //NOSONAR
                            secondaryTextColor, -LIGHTNESS_TEXT_DIFFERENCE_LIGHT); //NOSONAR
                } else { //NOSONAR
                    secondaryTextColor = //NOSONAR
                            ColorHelper.findContrastColorAgainstDark( //NOSONAR
                                    foregroundColor, //NOSONAR
                                    backgroundColor, //NOSONAR
                                    true /* findFG */, //NOSONAR
                                    4.5f); //NOSONAR
                    primaryTextColor = ColorHelper.changeColorLightness( //NOSONAR
                            secondaryTextColor, -LIGHTNESS_TEXT_DIFFERENCE_DARK); //NOSONAR
                } // NOSONAR
            } else { //NOSONAR
                primaryTextColor = foregroundColor; //NOSONAR
                secondaryTextColor = ColorHelper.changeColorLightness( //NOSONAR
                        primaryTextColor, backgroundLight ? LIGHTNESS_TEXT_DIFFERENCE_LIGHT //NOSONAR
                                : LIGHTNESS_TEXT_DIFFERENCE_DARK); //NOSONAR
                if (ColorHelper.calculateContrast(secondaryTextColor, //NOSONAR
                        backgroundColor) < 4.5f) { //NOSONAR
                    // oh well the secondary is not good enough // NOSONAR
                    if (backgroundLight) { //NOSONAR
                        secondaryTextColor = ColorHelper.findContrastColor( //NOSONAR
                                secondaryTextColor, //NOSONAR
                                backgroundColor, //NOSONAR
                                true /* findFG */, //NOSONAR
                                4.5f); //NOSONAR
                    } else { //NOSONAR
                        secondaryTextColor //NOSONAR
                                = ColorHelper.findContrastColorAgainstDark( //NOSONAR
                                secondaryTextColor, //NOSONAR
                                backgroundColor, //NOSONAR
                                true /* findFG */, //NOSONAR
                                4.5f); //NOSONAR
                    } // NOSONAR
                    primaryTextColor = ColorHelper.changeColorLightness( //NOSONAR
                            secondaryTextColor, backgroundLight //NOSONAR
                                    ? -LIGHTNESS_TEXT_DIFFERENCE_LIGHT //NOSONAR
                                    : -LIGHTNESS_TEXT_DIFFERENCE_DARK); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
        return new Pair<>(primaryTextColor, secondaryTextColor); //NOSONAR
    } // NOSONAR
} // NOSONAR
