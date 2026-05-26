package com.simplecity.amp_library.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.simplecity.amp_library.utils.TypefaceManager;
import java.util.Arrays;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class RobotoSpinnerAdapter<T> extends ArrayAdapter<T> {

    private Typeface mTypeface;

    public RobotoSpinnerAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, 0, Arrays.asList(objects));
        mTypeface = TypefaceManager.getInstance().getTypeface(context, TypefaceManager.SANS_SERIF_LIGHT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(mTypeface);
        return view;
    }

    @Override
    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(mTypeface);
        return view;
    }
}
