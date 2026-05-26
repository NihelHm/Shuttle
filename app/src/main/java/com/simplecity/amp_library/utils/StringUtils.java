package com.simplecity.amp_library.utils; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.res.Resources; // NOSONAR
import android.support.annotation.NonNull; // NOSONAR
import android.support.annotation.Nullable; // NOSONAR
import android.text.TextUtils; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import java.text.Normalizer; // NOSONAR
import java.util.Arrays; // NOSONAR
import java.util.Formatter; // NOSONAR
import java.util.Locale; // NOSONAR
import java.util.regex.Pattern; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class StringUtils { //NOSONAR

    private static final String TAG = "StringUtils"; //NOSONAR

    private static StringBuilder sFormatBuilder = new StringBuilder(); //NOSONAR

    private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault()); //NOSONAR

    private static Pattern pattern = Pattern.compile("^(?i)\\s*(?:the |an |a )|(?:, the|, an|, a)\\s*$|[\\[\\]()!?.,']"); //NOSONAR

    private StringUtils() { //NOSONAR
        // Intentionally left empty. // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Method makeTimeString. // NOSONAR
     * <p> // NOSONAR
     * To do later: Move to StringUtils or somewhere else // NOSONAR
     * // NOSONAR
     * @param context Context // NOSONAR
     * @param secs long // NOSONAR
     * @return String // NOSONAR
     */ // NOSONAR
    public static String makeTimeString(@NonNull Context context, long secs) { //NOSONAR
        sFormatBuilder.setLength(0); //NOSONAR
        //return (secs < 0 ? "- " : "") + (Math.abs(secs) < 3600 ? makeShortTimeString(context, Math.abs(secs)) : makeLongTimeString(context, Math.abs(secs))); // NOSONAR
        return Math.abs(secs) < 3600 ? makeShortTimeString(context, secs) : makeLongTimeString(context, secs); //NOSONAR
    } // NOSONAR

    private static String makeLongTimeString(@NonNull Context context, long secs) { //NOSONAR
        return makeTimeString(context.getString(R.string.durationformatlong), secs); //NOSONAR
    } // NOSONAR

    private static String makeShortTimeString(@NonNull Context context, long secs) { //NOSONAR
        return makeTimeString(context.getString(R.string.durationformatshort), secs); //NOSONAR
    } // NOSONAR

    private static String makeTimeString(String formatString, long secs) { //NOSONAR
        long absSeconds = Math.abs(secs); //NOSONAR
        sFormatBuilder.setLength(0); //NOSONAR
        return sFormatter.format(formatString, //NOSONAR
                secs < 0 ? "- " : "", //NOSONAR
                absSeconds / 3600, //NOSONAR
                absSeconds / 60, //NOSONAR
                absSeconds / 60 % 60, //NOSONAR
                absSeconds, //NOSONAR
                absSeconds % 60) //NOSONAR
                .toString(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Method makeSubfoldersLabel. // NOSONAR
     * // NOSONAR
     * @param context context // NOSONAR
     * @param numSubfolders the number of subFolders for this folder // NOSONAR
     * @param numSubfiles the number of subFiles for this folder // NOSONAR
     * @return a label in the vein of "5 folders | 3 files" // NOSONAR
     */ // NOSONAR
    public static String makeSubfoldersLabel(Context context, int numSubfolders, int numSubfiles) { //NOSONAR

        final StringBuilder string = new StringBuilder(); //NOSONAR

        final Resources r = context.getResources(); //NOSONAR

        if (numSubfolders != 0) { //NOSONAR
            if (numSubfolders == 1) { //NOSONAR
                string.append(context.getString(R.string.onefolder)); //NOSONAR
            } else { //NOSONAR
                final String f = r.getQuantityText(R.plurals.Nfolders, numSubfolders) //NOSONAR
                        .toString(); //NOSONAR
                sFormatBuilder.setLength(0); //NOSONAR
                sFormatter.format(f, numSubfolders); //NOSONAR
                string.append(sFormatBuilder); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (numSubfiles > 0 && numSubfolders > 0) { //NOSONAR
            string.append(" | "); //NOSONAR
        } // NOSONAR

        if (numSubfiles != 0) { //NOSONAR
            if (numSubfiles == 1) { //NOSONAR
                string.append(context.getString(R.string.onesong)); //NOSONAR
            } else { //NOSONAR
                final String f = r.getQuantityText(R.plurals.Nsongs, numSubfiles).toString(); //NOSONAR
                sFormatBuilder.setLength(0); //NOSONAR
                sFormatter.format(f, numSubfiles); //NOSONAR
                string.append(sFormatBuilder); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        if (numSubfiles == 0 && numSubfolders == 0) { //NOSONAR
            string.append("-"); //NOSONAR
        } // NOSONAR

        return string.toString(); //NOSONAR
    } // NOSONAR

    public static String makeAlbumAndSongsLabel(Context context, int numalbums, int numsongs) { //NOSONAR

        final StringBuilder stringBuilder = new StringBuilder(); //NOSONAR
        final Resources r = context.getResources(); //NOSONAR

        String f; //NOSONAR
        if (numalbums > 0) { //NOSONAR
            f = r.getQuantityText(R.plurals.Nalbums, numalbums).toString(); //NOSONAR
            sFormatBuilder.setLength(0); //NOSONAR
            sFormatter.format(f, numalbums); //NOSONAR
            stringBuilder.append(sFormatBuilder); //NOSONAR
        } // NOSONAR

        if (numalbums > 0 && numsongs > 0) { //NOSONAR
            stringBuilder.append(" | "); //NOSONAR
        } // NOSONAR
        if (numsongs == 1) { //NOSONAR
            stringBuilder.append(context.getString(R.string.onesong)); //NOSONAR
        } else if (numsongs > 0) { //NOSONAR
            f = r.getQuantityText(R.plurals.Nsongs, numsongs).toString(); //NOSONAR
            sFormatBuilder.setLength(0); //NOSONAR
            sFormatter.format(f, numsongs); //NOSONAR
            stringBuilder.append(sFormatBuilder); //NOSONAR
        } // NOSONAR
        return stringBuilder.toString(); //NOSONAR
    } // NOSONAR

    public static String makeAlbumsLabel(Context context, int numAlbums) { //NOSONAR
        final StringBuilder stringBuilder = new StringBuilder(); //NOSONAR
        String formatString = context.getResources().getQuantityText(R.plurals.Nalbums, numAlbums).toString(); //NOSONAR
        sFormatBuilder.setLength(0); //NOSONAR
        sFormatter.format(formatString, numAlbums); //NOSONAR
        stringBuilder.append(sFormatBuilder); //NOSONAR

        return stringBuilder.toString(); //NOSONAR
    } // NOSONAR

    public static String makeSongsLabel(Context context, int numSongs) { //NOSONAR
        final StringBuilder stringBuilder = new StringBuilder(); //NOSONAR
        String formatString = context.getResources().getQuantityText(R.plurals.Nsongs, numSongs).toString(); //NOSONAR
        sFormatBuilder.setLength(0); //NOSONAR
        sFormatter.format(formatString, numSongs); //NOSONAR
        stringBuilder.append(sFormatBuilder); //NOSONAR

        return stringBuilder.toString(); //NOSONAR
    } // NOSONAR

    public static String makeYearLabel(Context context, int year) { //NOSONAR

        if (year <= 0) { //NOSONAR
            return context.getResources().getString(R.string.unknown_year); //NOSONAR
        } // NOSONAR

        return String.format("%s", year); //NOSONAR
    } // NOSONAR

    public static String makeSongsAndTimeLabel(Context context, int numSongs, long secs) { //NOSONAR
        return context.getResources().getString(R.string.songs_time_label, makeSongsLabel(context, numSongs), makeLongTimeString(context, secs)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Converts a name to a "key" that can be used for grouping, sorting // NOSONAR
     * and searching. // NOSONAR
     * The rules that govern this conversion are: // NOSONAR
     * - remove 'special' characters like ()[]'!?., // NOSONAR
     * - remove leading/trailing spaces // NOSONAR
     * - convert everything to lowercase // NOSONAR
     * - remove leading "the ", "an " and "a " // NOSONAR
     * - remove trailing ", the|an|a" // NOSONAR
     * - remove accents. This step leaves us with CollationKey data, // NOSONAR
     * which is not human readable // NOSONAR
     * // NOSONAR
     * @param name The artist or album name to convert // NOSONAR
     * @return The "key" for the given name. // NOSONAR
     */ // NOSONAR
    public static String keyFor(String name) { //NOSONAR

        if (!TextUtils.isEmpty(name)) { //NOSONAR
            name = pattern.matcher(name) //NOSONAR
                    .replaceAll("") //NOSONAR
                    .trim() //NOSONAR
                    .toLowerCase(); //NOSONAR
        } else { //NOSONAR
            name = ""; //NOSONAR
        } // NOSONAR

        return name; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return true if String s1 contains String s2, ignoring case. // NOSONAR
     */ // NOSONAR
    public static boolean containsIgnoreCase(String s1, String s2) { //NOSONAR
        return s1.toLowerCase().contains(s2.toLowerCase()); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Find the Jaro Winkler Similarity which indicates the similarity score between two Strings. // NOSONAR
     * <p> // NOSONAR
     * Note: This method splits the {@param first} string at whitespaces, and returns the best Jaro-Winkler // NOSONAR
     * score between {@param second} and the 'split' strings. // NOSONAR
     */ // NOSONAR
    public static double getAdjustedJaroWinklerSimilarity(@Nullable String first, @Nullable String second) { //NOSONAR

        if (TextUtils.isEmpty(first) || TextUtils.isEmpty(second)) { //NOSONAR
            return 0; //NOSONAR
        } // NOSONAR

        String[] split = first.split("\\s"); //NOSONAR
        if (split.length > 1) { //NOSONAR
            double score = 0; //NOSONAR
            for (String str : split) { //NOSONAR
                double curScore = getJaroWinklerSimilarity(str, second); //NOSONAR
                if (curScore > score) { //NOSONAR
                    score = curScore; //NOSONAR
                } // NOSONAR
            } // NOSONAR
            //Make sure we do a normal (non-adjusted) test as well, in case that comes out as our best match. // NOSONAR
            return Math.max(getJaroWinklerSimilarity(first, second), score); //NOSONAR
        } else { //NOSONAR
            return getJaroWinklerSimilarity(first, second); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * <p>Find the Jaro Winkler Similarity which indicates the similarity score between two Strings.</p> // NOSONAR
     * <p> // NOSONAR
     * <p>The Jaro measure is the weighted sum of percentage of matched characters from each file and transposed characters. // NOSONAR
     * Winkler increased this measure for matching initial characters.</p> // NOSONAR
     * <p> // NOSONAR
     * <p>This implementation is based on the Jaro Winkler similarity algorithm // NOSONAR
     * from <a href="http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance">http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance</a>.</p> // NOSONAR
     * <p> // NOSONAR
     * // NOSONAR
     * @param first the first String, must not be null // NOSONAR
     * @param second the second String, must not be null // NOSONAR
     * @return result similarity // NOSONAR
     */ // NOSONAR
    public static double getJaroWinklerSimilarity(@NonNull String first, @NonNull String second) { //NOSONAR

        final double DEFAULT_SCALING_FACTOR = 0.1; //NOSONAR

        first = first.toLowerCase(); //NOSONAR
        second = second.toLowerCase(); //NOSONAR
        first = Normalizer.normalize(first, Normalizer.Form.NFD); //NOSONAR
        second = Normalizer.normalize(second, Normalizer.Form.NFD); //NOSONAR

        final int[] mtp = matches(first, second); //NOSONAR
        final double m = mtp[0]; //NOSONAR
        if (m == 0) { //NOSONAR
            return 0D; //NOSONAR
        } // NOSONAR
        final double j = ((m / first.length() + m / second.length() + (m - mtp[1]) / m)) / 3; //NOSONAR
        final double jw = j < 0.7D ? j : j + Math.min(DEFAULT_SCALING_FACTOR, 1D / mtp[3]) * mtp[2] * (1D - j); //NOSONAR
        return Math.round(jw * 100.0D) / 100.0D; //NOSONAR
    } // NOSONAR

    private static int[] matches(final CharSequence first, final CharSequence second) { //NOSONAR
        CharSequence max, min; //NOSONAR
        if (first.length() > second.length()) { //NOSONAR
            max = first; //NOSONAR
            min = second; //NOSONAR
        } else { //NOSONAR
            max = second; //NOSONAR
            min = first; //NOSONAR
        } // NOSONAR
        final int range = Math.max(max.length() / 2 - 1, 0); //NOSONAR
        final int[] matchIndexes = new int[min.length()]; //NOSONAR
        Arrays.fill(matchIndexes, -1); //NOSONAR
        final boolean[] matchFlags = new boolean[max.length()]; //NOSONAR
        int matches = 0; //NOSONAR
        for (int mi = 0; mi < min.length(); mi++) { //NOSONAR
            final char c1 = min.charAt(mi); //NOSONAR
            for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) { //NOSONAR
                if (!matchFlags[xi] && c1 == max.charAt(xi)) { //NOSONAR
                    matchIndexes[mi] = xi; //NOSONAR
                    matchFlags[xi] = true; //NOSONAR
                    matches++; //NOSONAR
                    break; //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
        final char[] ms1 = new char[matches]; //NOSONAR
        final char[] ms2 = new char[matches]; //NOSONAR
        for (int i = 0, si = 0; i < min.length(); i++) { //NOSONAR
            if (matchIndexes[i] != -1) { //NOSONAR
                ms1[si] = min.charAt(i); //NOSONAR
                si++; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        for (int i = 0, si = 0; i < max.length(); i++) { //NOSONAR
            if (matchFlags[i]) { //NOSONAR
                ms2[si] = max.charAt(i); //NOSONAR
                si++; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        int transpositions = 0; //NOSONAR
        for (int mi = 0; mi < ms1.length; mi++) { //NOSONAR
            if (ms1[mi] != ms2[mi]) { //NOSONAR
                transpositions++; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        int prefix = 0; //NOSONAR
        for (int mi = 0; mi < min.length(); mi++) { //NOSONAR
            if (first.charAt(mi) == second.charAt(mi)) { //NOSONAR
                prefix++; //NOSONAR
            } else { //NOSONAR
                break; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return new int[] { matches, transpositions / 2, prefix, max.length() }; //NOSONAR
    } // NOSONAR

    public static int parseInt(@Nullable String string) { //NOSONAR
        if (string != null) { //NOSONAR
            try { //NOSONAR
                return Integer.parseInt(string); //NOSONAR
            } catch (NumberFormatException ignored) { //NOSONAR
                // Intentionally left empty. // NOSONAR
            } // NOSONAR
        } // NOSONAR
        return -1; //NOSONAR
    } // NOSONAR
} // NOSONAR
