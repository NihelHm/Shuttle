package com.simplecity.amp_library.utils;

import android.support.annotation.Nullable;
import java.text.Collator;
import java.util.Locale;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ComparisonUtils { //NOSONAR

    private static Collator collator = Collator.getInstance(Locale.getDefault()); //NOSONAR

    private ComparisonUtils() { //NOSONAR
        // Intentionally left empty.
    }

    public static int compareLong(long x, long y) { //NOSONAR
        return (x < y) ? -1 : ((x == y) ? 0 : 1); //NOSONAR
    }

    public static int compareInt(int x, int y) { //NOSONAR
        return (x < y) ? -1 : ((x == y) ? 0 : 1); //NOSONAR
    }

    /**
     * Null-safe string comparison. Uses a Collator, which is slower than normal string comparison.
     */
    public static int compare(@Nullable String str1, @Nullable String str2) { //NOSONAR

        if (str1 == null) { //NOSONAR
            return -1; //NOSONAR
        } else if (str2 == null) { //NOSONAR
            return 1; //NOSONAR
        } else if (str1.equals(str2)) { //NOSONAR
            return 0; //NOSONAR
        } else { //NOSONAR
            return collator.compare(str1, str2); //NOSONAR
        }
    }
}
