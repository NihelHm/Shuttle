package com.simplecity.amp_library.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.ArrayMap;
import java.util.Map;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class TypefaceManager { //NOSONAR

    public static final String SANS_SERIF = "sans-serif"; //NOSONAR

    public static final String SANS_SERIF_MEDIUM = "sans-serif-medium"; //NOSONAR

    public static final String SANS_SERIF_LIGHT = "sans-serif-light"; //NOSONAR

    private final Map<String, Typeface> mCache = new ArrayMap<>(); //NOSONAR
    private static TypefaceManager sInstance = null; //NOSONAR

    /**
     * Only initialize through {@link #getInstance()}
     */
    private TypefaceManager() { //NOSONAR
        // Intentionally left empty.
    }

    public static TypefaceManager getInstance() { //NOSONAR
        if (sInstance == null) { //NOSONAR
            sInstance = new TypefaceManager(); //NOSONAR
        }
        return sInstance; //NOSONAR
    }

    /**
     * @param typeface The name of the type face asset
     * @return The {@link android.graphics.Typeface} that matches
     * <code>typeface</code>
     */
    public Typeface getTypeface(Context context, String typeface) { //NOSONAR
        Typeface result = mCache.get(typeface); //NOSONAR
        if (result == null) { //NOSONAR

            switch (typeface) { //NOSONAR
                case SANS_SERIF: //NOSONAR
                    result = Typeface.create("sans-serif", Typeface.NORMAL); //NOSONAR
                    break; //NOSONAR
                case SANS_SERIF_MEDIUM: //NOSONAR
                    result = Typeface.create("sans-serif-medium", Typeface.NORMAL); //NOSONAR
                    break; //NOSONAR
                case SANS_SERIF_LIGHT: //NOSONAR
                    result = Typeface.create("sans-serif-light", Typeface.NORMAL); //NOSONAR
                    break; //NOSONAR
                default: //NOSONAR
                    result = Typeface.createFromAsset(context.getAssets(), "fonts/" + typeface); //NOSONAR
                    break; //NOSONAR
            }
            mCache.put(typeface, result); //NOSONAR
        }
        return result; //NOSONAR
    }
}

