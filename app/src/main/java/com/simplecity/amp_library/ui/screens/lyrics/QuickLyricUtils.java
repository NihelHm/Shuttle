package com.simplecity.amp_library.ui.screens.lyrics;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.model.Song;
import com.simplecity.amp_library.utils.ShuttleUtils;
import com.simplecity.amp_library.utils.TypefaceManager;

/**
 * QuickLyric helpers
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class QuickLyricUtils { //NOSONAR

    private static final String QUICKLYRIC_URL = "https://d3khd.app.goo.gl/jdF1"; //NOSONAR

    public static boolean isQLInstalled(Context context) { //NOSONAR

        PackageManager pm = context.getPackageManager(); //NOSONAR
        try { //NOSONAR
            pm.getPackageInfo("com.geecko.QuickLyric", PackageManager.GET_ACTIVITIES); //NOSONAR
            return true; //NOSONAR
        } catch (PackageManager.NameNotFoundException ignored) { //NOSONAR
            return false; //NOSONAR
        }
    }

    static void getLyricsFor(Context context, Song song) { //NOSONAR
        Intent intent = new Intent("com.geecko.QuickLyric.getLyrics"); //NOSONAR
        intent.putExtra("TAGS", new String[] { song.artistName, song.name }); //NOSONAR
        if (intent.resolveActivity(context.getApplicationContext().getPackageManager()) != null) { //NOSONAR
            context.startActivity(intent); //NOSONAR
        }
    }

    static Intent getQuickLyricIntent() { //NOSONAR
        return new Intent(Intent.ACTION_VIEW, Uri.parse(QUICKLYRIC_URL)); //NOSONAR
    }

    /**
     * @return true if the Play Store is available, and this QuickLyric can be downloaded.
     */
    static boolean canDownloadQuickLyric(Context context) { //NOSONAR
        if (ShuttleUtils.isAmazonBuild()) return false; //NOSONAR
        return getQuickLyricIntent().resolveActivity(context.getPackageManager()) != null; //NOSONAR
    }

    /**
     * @return a {@link SpannableStringBuilder}, using the correct font as per the QuickLyric branding.
     */
    static Spannable getSpannedString(Context context) { //NOSONAR
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(context.getString(R.string.quicklyric)); // "Quick" must use roboto light //NOSONAR
        ssBuilder.setSpan(new TypefaceSpan(TypefaceManager.SANS_SERIF_LIGHT), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE); //NOSONAR
        return ssBuilder; //NOSONAR
    }
}
