package com.simplecity.amp_library.format;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.afollestad.aesthetic.Aesthetic;
import javax.inject.Inject;

/**
 * Highlights the text in a text field
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PrefixHighlighter { //NOSONAR

    private final int mPrefixHighlightColor; //NOSONAR

    private ForegroundColorSpan mPrefixColorSpan; //NOSONAR

    @Inject //NOSONAR
    public PrefixHighlighter(Context context) { //NOSONAR
        mPrefixHighlightColor = Aesthetic.get(context).colorAccent().blockingFirst(); //NOSONAR
    }

    /**
     * Sets the text on the given {@link TextView}, highlighting the word that
     * matches the given prefix
     *
     * @param view The {@link TextView} on which to set the text
     * @param prefix The prefix to look for
     */
    public void setText(TextView view, char[] prefix) { //NOSONAR
        setText(view, view.getText().toString(), prefix); //NOSONAR
    }

    /**
     * Sets the text on the given {@link TextView}, highlighting the word that
     * matches the given prefix
     *
     * @param view The {@link TextView} on which to set the text
     * @param text The string to use as the text
     * @param prefix The prefix to look for
     */
    public void setText(TextView view, String text, char[] prefix) { //NOSONAR
        if (view == null) { //NOSONAR
            return; //NOSONAR
        }
        if ((prefix == null || prefix.length == 0)) { //NOSONAR
            view.setText(text); //NOSONAR
        } else if (!TextUtils.isEmpty(text)) { //NOSONAR
            view.setText(apply(text, prefix)); //NOSONAR
        }
    }

    /**
     * Returns a {@link CharSequence} which highlights the given prefix if found
     * in the given text
     *
     * @param text the text to which to apply the highlight
     * @param prefix the prefix to look for
     */
    private CharSequence apply(CharSequence text, char[] prefix) { //NOSONAR
        final int index = indexOfWordPrefix(text, prefix); //NOSONAR
        if (index != -1) { //NOSONAR
            if (mPrefixColorSpan == null) { //NOSONAR
                mPrefixColorSpan = new ForegroundColorSpan(mPrefixHighlightColor); //NOSONAR
            }
            final SpannableString result = new SpannableString(text); //NOSONAR
            result.setSpan(mPrefixColorSpan, index, index + prefix.length, 0); //NOSONAR
            return result; //NOSONAR
        } else { //NOSONAR
            return text; //NOSONAR
        }
    }

    /**
     * Finds the index of the first word that starts with the given prefix. If
     * not found, returns -1
     *
     * @param text the text in which to search for the prefix
     * @param prefix the text to find, in upper case letters
     */
    private static int indexOfWordPrefix(CharSequence text, char[] prefix) { //NOSONAR
        if (TextUtils.isEmpty(text) || prefix == null) { //NOSONAR
            return -1; //NOSONAR
        }

        final int tlen = text.length(); //NOSONAR
        final int plen = prefix.length; //NOSONAR

        if (plen == 0 || tlen < plen) { //NOSONAR
            return -1; //NOSONAR
        }

        int i = 0; //NOSONAR
        while (i < tlen) { //NOSONAR
            // Skip non-word characters
            while (i < tlen && !Character.isLetterOrDigit(text.charAt(i))) { //NOSONAR
                i++; //NOSONAR
            }

            if (i + plen > tlen) { //NOSONAR
                return -1; //NOSONAR
            }

            //  Compare the prefixes
            int j; //NOSONAR
            for (j = 0; j < plen; j++) { //NOSONAR
                if (Character.toUpperCase(text.charAt(i + j)) != prefix[j]) { //NOSONAR
                    break; //NOSONAR
                }
            }
            if (j == plen) { //NOSONAR
                return i; //NOSONAR
            }

            // Skip this word
            while (i < tlen && Character.isLetterOrDigit(text.charAt(i))) { //NOSONAR
                i++; //NOSONAR
            }
        }
        return -1; //NOSONAR
    }
}
