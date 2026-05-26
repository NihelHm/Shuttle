package com.simplecity.amp_library.format; // NOSONAR

import android.content.Context; // NOSONAR
import android.text.SpannableString; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.text.style.ForegroundColorSpan; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import javax.inject.Inject; // NOSONAR

/** // NOSONAR
 * Highlights the text in a text field // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PrefixHighlighter { //NOSONAR

    private final int mPrefixHighlightColor; //NOSONAR

    private ForegroundColorSpan mPrefixColorSpan; //NOSONAR

    @Inject //NOSONAR
    public PrefixHighlighter(Context context) { //NOSONAR
        mPrefixHighlightColor = Aesthetic.get(context).colorAccent().blockingFirst(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text on the given {@link TextView}, highlighting the word that // NOSONAR
     * matches the given prefix // NOSONAR
     * // NOSONAR
     * @param view The {@link TextView} on which to set the text // NOSONAR
     * @param prefix The prefix to look for // NOSONAR
     */ // NOSONAR
    public void setText(TextView view, char[] prefix) { //NOSONAR
        setText(view, view.getText().toString(), prefix); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the text on the given {@link TextView}, highlighting the word that // NOSONAR
     * matches the given prefix // NOSONAR
     * // NOSONAR
     * @param view The {@link TextView} on which to set the text // NOSONAR
     * @param text The string to use as the text // NOSONAR
     * @param prefix The prefix to look for // NOSONAR
     */ // NOSONAR
    public void setText(TextView view, String text, char[] prefix) { //NOSONAR
        if (view == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if ((prefix == null || prefix.length == 0)) { //NOSONAR
            view.setText(text); //NOSONAR
        } else if (!TextUtils.isEmpty(text)) { //NOSONAR
            view.setText(apply(text, prefix)); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns a {@link CharSequence} which highlights the given prefix if found // NOSONAR
     * in the given text // NOSONAR
     * // NOSONAR
     * @param text the text to which to apply the highlight // NOSONAR
     * @param prefix the prefix to look for // NOSONAR
     */ // NOSONAR
    private CharSequence apply(CharSequence text, char[] prefix) { //NOSONAR
        final int index = indexOfWordPrefix(text, prefix); //NOSONAR
        if (index != -1) { //NOSONAR
            if (mPrefixColorSpan == null) { //NOSONAR
                mPrefixColorSpan = new ForegroundColorSpan(mPrefixHighlightColor); //NOSONAR
            } // NOSONAR
            final SpannableString result = new SpannableString(text); //NOSONAR
            result.setSpan(mPrefixColorSpan, index, index + prefix.length, 0); //NOSONAR
            return result; //NOSONAR
        } else { //NOSONAR
            return text; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Finds the index of the first word that starts with the given prefix. If // NOSONAR
     * not found, returns -1 // NOSONAR
     * // NOSONAR
     * @param text the text in which to search for the prefix // NOSONAR
     * @param prefix the text to find, in upper case letters // NOSONAR
     */ // NOSONAR
    private static int indexOfWordPrefix(CharSequence text, char[] prefix) { //NOSONAR
        if (TextUtils.isEmpty(text) || prefix == null) { //NOSONAR
            return -1; //NOSONAR
        } // NOSONAR

        final int tlen = text.length(); //NOSONAR
        final int plen = prefix.length; //NOSONAR

        if (plen == 0 || tlen < plen) { //NOSONAR
            return -1; //NOSONAR
        } // NOSONAR

        int i = 0; //NOSONAR
        while (i < tlen) { //NOSONAR
            // Skip non-word characters // NOSONAR
            while (i < tlen && !Character.isLetterOrDigit(text.charAt(i))) { //NOSONAR
                i++; //NOSONAR
            } // NOSONAR

            if (i + plen > tlen) { //NOSONAR
                return -1; //NOSONAR
            } // NOSONAR

            //  Compare the prefixes // NOSONAR
            int j; //NOSONAR
            for (j = 0; j < plen; j++) { //NOSONAR
                if (Character.toUpperCase(text.charAt(i + j)) != prefix[j]) { //NOSONAR
                    break; //NOSONAR
                } // NOSONAR
            } // NOSONAR
            if (j == plen) { //NOSONAR
                return i; //NOSONAR
            } // NOSONAR

            // Skip this word // NOSONAR
            while (i < tlen && Character.isLetterOrDigit(text.charAt(i))) { //NOSONAR
                i++; //NOSONAR
            } // NOSONAR
        } // NOSONAR
        return -1; //NOSONAR
    } // NOSONAR
} // NOSONAR
