package com.simplecity.amp_library.utils;

import android.support.annotation.ColorRes;
import com.simplecity.amp_library.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class ThemeUtils {

    private ThemeUtils() {
        //no instance
    }

    public static Theme getRandom() {

        List<Theme> themes = new ArrayList<>();

        themes.add(new Theme(0, "blue_500", "amber_300", false, R.color.md_blue_500, R.color.md_amber_300));
        themes.add(new Theme(1, "blue_500", "amber_300", true, R.color.md_blue_500, R.color.md_amber_300));

        themes.add(new Theme(2, "blue_grey_500", "red_A400", false, R.color.md_blue_grey_500, R.color.md_red_A400));
        themes.add(new Theme(3, "blue_grey_500", "red_A400", true, R.color.md_blue_grey_500, R.color.md_red_A400));

        themes.add(new Theme(4, "red_600", "light_blue_600", false, R.color.md_red_600, R.color.md_light_blue_600));
        themes.add(new Theme(5, "red_600", "light_blue_600", true, R.color.md_red_600, R.color.md_light_blue_600));

        themes.add(new Theme(6, "grey_900", "teal_A700", false, R.color.md_grey_900, R.color.md_teal_A700));
        themes.add(new Theme(7, "grey_900", "teal_A700", true, R.color.md_grey_900, R.color.md_teal_A700));

        return themes.get(new Random().nextInt(themes.size()));
    }

    public static class Theme {

        @SuppressWarnings("java:S1104")

        public int id;
        @SuppressWarnings("java:S1104")
        public String primaryColorName;
        @SuppressWarnings("java:S1104")
        public String accentColorName;

        @SuppressWarnings("java:S1104")

        public boolean isDark;

        @ColorRes
        @SuppressWarnings("java:S1104")
        public int primaryColor;

        @ColorRes
        @SuppressWarnings("java:S1104")
        public int accentColor;

        Theme(int id, String primaryColorName, String accentColorName, boolean isDark, int primaryColor, int accentColor) {
            this.id = id;
            this.primaryColorName = primaryColorName;
            this.accentColorName = accentColorName;
            this.isDark = isDark;
            this.primaryColor = primaryColor;
            this.accentColor = accentColor;
        }
    }
}
