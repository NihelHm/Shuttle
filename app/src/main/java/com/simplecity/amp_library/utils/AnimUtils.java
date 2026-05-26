package com.simplecity.amp_library.utils; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class AnimUtils { //NOSONAR

    public static float lerp(float a, float b, float v) { //NOSONAR
        return a + (b - a) * v; //NOSONAR
    } // NOSONAR

    public static int lerp(int a, int b, int v) { //NOSONAR
        return a + (b - a) * v; //NOSONAR
    } // NOSONAR

    public static double lerp(double a, double b, double v) { //NOSONAR
        return a + (b - a) * v; //NOSONAR
    } // NOSONAR

    private AnimUtils() { //NOSONAR
        throw new IllegalStateException("no instances"); //NOSONAR
    } // NOSONAR
} // NOSONAR
