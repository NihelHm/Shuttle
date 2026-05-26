package com.simplecity.amp_library.utils.color; // NOSONAR

import android.animation.TypeEvaluator; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArgbEvaluator implements TypeEvaluator { //NOSONAR
        private static final ArgbEvaluator sInstance = new ArgbEvaluator(); //NOSONAR

        /** // NOSONAR
         * Returns an instance of <code>ArgbEvaluator</code> that may be used in // NOSONAR
         * {@link ValueAnimator#setEvaluator(TypeEvaluator)}. The same instance may // NOSONAR
         * be used in multiple <code>Animator</code>s because it holds no state. // NOSONAR
         * // NOSONAR
         * @return An instance of <code>ArgbEvalutor</code>. // NOSONAR
         * @hide // NOSONAR
         */ // NOSONAR
        public static ArgbEvaluator getInstance() { //NOSONAR
            return sInstance; //NOSONAR
        } // NOSONAR

        /** // NOSONAR
         * This function returns the calculated in-between value for a color // NOSONAR
         * given integers that represent the start and end values in the four // NOSONAR
         * bytes of the 32-bit int. Each channel is separately linearly interpolated // NOSONAR
         * and the resulting calculated values are recombined into the return value. // NOSONAR
         * // NOSONAR
         * @param fraction The fraction from the starting to the ending values // NOSONAR
         * @param startValue A 32-bit int value representing colors in the // NOSONAR
         * separate bytes of the parameter // NOSONAR
         * @param endValue A 32-bit int value representing colors in the // NOSONAR
         * separate bytes of the parameter // NOSONAR
         * @return A value that is calculated to be the linearly interpolated // NOSONAR
         * result, derived by separating the start and end values into separate // NOSONAR
         * color channels and interpolating each one separately, recombining the // NOSONAR
         * resulting values in the same way. // NOSONAR
         */ // NOSONAR
        public Object evaluate(float fraction, Object startValue, Object endValue) { //NOSONAR
            int startInt = (Integer) startValue; //NOSONAR
            float startA = ((startInt >> 24) & 0xff) / 255.0f; //NOSONAR
            float startR = ((startInt >> 16) & 0xff) / 255.0f; //NOSONAR
            float startG = ((startInt >> 8) & 0xff) / 255.0f; //NOSONAR
            float startB = (startInt & 0xff) / 255.0f; //NOSONAR

            int endInt = (Integer) endValue; //NOSONAR
            float endA = ((endInt >> 24) & 0xff) / 255.0f; //NOSONAR
            float endR = ((endInt >> 16) & 0xff) / 255.0f; //NOSONAR
            float endG = ((endInt >> 8) & 0xff) / 255.0f; //NOSONAR
            float endB = (endInt & 0xff) / 255.0f; //NOSONAR

            // convert from sRGB to linear // NOSONAR
            startR = (float) Math.pow(startR, 2.2); //NOSONAR
            startG = (float) Math.pow(startG, 2.2); //NOSONAR
            startB = (float) Math.pow(startB, 2.2); //NOSONAR

            endR = (float) Math.pow(endR, 2.2); //NOSONAR
            endG = (float) Math.pow(endG, 2.2); //NOSONAR
            endB = (float) Math.pow(endB, 2.2); //NOSONAR

            // compute the interpolated color in linear space // NOSONAR
            float a = startA + fraction * (endA - startA); //NOSONAR
            float r = startR + fraction * (endR - startR); //NOSONAR
            float g = startG + fraction * (endG - startG); //NOSONAR
            float b = startB + fraction * (endB - startB); //NOSONAR

            // convert back to sRGB in the [0..255] range // NOSONAR
            a = a * 255.0f; //NOSONAR
            r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f; //NOSONAR
            g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f; //NOSONAR
            b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f; //NOSONAR

            return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b); //NOSONAR
        } // NOSONAR
    } // NOSONAR
