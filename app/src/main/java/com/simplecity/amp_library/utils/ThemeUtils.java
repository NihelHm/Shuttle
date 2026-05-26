package com.simplecity.amp_library.utils; // NOSONAR

import android.support.annotation.ColorRes; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.List; // NOSONAR
import java.util.Random; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ThemeUtils { //NOSONAR

    private ThemeUtils() { //NOSONAR
        //no instance // NOSONAR
    } // NOSONAR

    public static Theme getRandom() { //NOSONAR

        List<Theme> themes = new ArrayList<>(); //NOSONAR

        themes.add(new Theme(0, "blue_500", "amber_300", false, R.color.md_blue_500, R.color.md_amber_300)); //NOSONAR
        themes.add(new Theme(1, "blue_500", "amber_300", true, R.color.md_blue_500, R.color.md_amber_300)); //NOSONAR

        themes.add(new Theme(2, "blue_grey_500", "red_A400", false, R.color.md_blue_grey_500, R.color.md_red_A400)); //NOSONAR
        themes.add(new Theme(3, "blue_grey_500", "red_A400", true, R.color.md_blue_grey_500, R.color.md_red_A400)); //NOSONAR

        themes.add(new Theme(4, "red_600", "light_blue_600", false, R.color.md_red_600, R.color.md_light_blue_600)); //NOSONAR
        themes.add(new Theme(5, "red_600", "light_blue_600", true, R.color.md_red_600, R.color.md_light_blue_600)); //NOSONAR

        themes.add(new Theme(6, "grey_900", "teal_A700", false, R.color.md_grey_900, R.color.md_teal_A700)); //NOSONAR
        themes.add(new Theme(7, "grey_900", "teal_A700", true, R.color.md_grey_900, R.color.md_teal_A700)); //NOSONAR

        return themes.get(new Random().nextInt(themes.size())); //NOSONAR
    } // NOSONAR

    public static class Theme { //NOSONAR

        @SuppressWarnings("java:S1104") //NOSONAR

        public int id; //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public String primaryColorName; //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public String accentColorName; //NOSONAR

        @SuppressWarnings("java:S1104") //NOSONAR

        public boolean isDark; //NOSONAR

        @ColorRes //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public int primaryColor; //NOSONAR

        @ColorRes //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public int accentColor; //NOSONAR

        Theme(int id, String primaryColorName, String accentColorName, boolean isDark, int primaryColor, int accentColor) { //NOSONAR
            this.id = id; //NOSONAR
            this.primaryColorName = primaryColorName; //NOSONAR
            this.accentColorName = accentColorName; //NOSONAR
            this.isDark = isDark; //NOSONAR
            this.primaryColor = primaryColor; //NOSONAR
            this.accentColor = accentColor; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
