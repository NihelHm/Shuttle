package com.afollestad.aesthetic;

import android.support.annotation.ColorInt;
import android.support.annotation.RestrictTo;

import io.reactivex.functions.Function3;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * @author Aidan Follestad (afollestad)
 */
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class LightDarkColorState { //NOSONAR

    private final int lightColor; //NOSONAR
    private final int darkColor; //NOSONAR
    private final boolean isDark; //NOSONAR

    public LightDarkColorState(int lightColor, int darkColor, boolean isDark) { //NOSONAR
        this.lightColor = lightColor; //NOSONAR
        this.darkColor = darkColor; //NOSONAR
        this.isDark = isDark; //NOSONAR
    }

    static LightDarkColorState create(int lightColor, int darkColor, boolean isDark) { //NOSONAR
        return new LightDarkColorState(lightColor, darkColor, isDark); //NOSONAR
    }

    public static Function3<Integer, Integer, Boolean, LightDarkColorState> creator() { //NOSONAR
        return new Function3<Integer, Integer, Boolean, LightDarkColorState>() { //NOSONAR
            @Override //NOSONAR
            public LightDarkColorState apply(Integer lightcolor, Integer darkColor, Boolean aBoolean) { //NOSONAR
                return LightDarkColorState.create(lightcolor, darkColor, aBoolean); //NOSONAR
            }
        };
    }

    @ColorInt //NOSONAR
    public int color() { //NOSONAR
        return isDark() ? darkColor : lightColor; //NOSONAR
    }

    public boolean isDark() { //NOSONAR
        return isDark; //NOSONAR
    }
}
