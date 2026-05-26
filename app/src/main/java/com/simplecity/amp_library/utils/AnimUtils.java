package com.simplecity.amp_library.utils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public final class AnimUtils {

    public static float lerp(float a, float b, float v) {
        return a + (b - a) * v;
    }

    public static int lerp(int a, int b, int v) {
        return a + (b - a) * v;
    }

    public static double lerp(double a, double b, double v) {
        return a + (b - a) * v;
    }

    private AnimUtils() {
        throw new IllegalStateException("no instances");
    }
}
