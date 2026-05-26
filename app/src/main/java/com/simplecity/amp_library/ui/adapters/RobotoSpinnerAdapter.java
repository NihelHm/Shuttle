package com.simplecity.amp_library.ui.adapters; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Typeface; // NOSONAR
import android.view.View; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.ArrayAdapter; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.simplecity.amp_library.utils.TypefaceManager; // NOSONAR
import java.util.Arrays; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class RobotoSpinnerAdapter<T> extends ArrayAdapter<T> { //NOSONAR

    private Typeface mTypeface; //NOSONAR

    public RobotoSpinnerAdapter(Context context, int textViewResourceId, T[] objects) { //NOSONAR
        super(context, textViewResourceId, 0, Arrays.asList(objects)); //NOSONAR
        mTypeface = TypefaceManager.getInstance().getTypeface(context, TypefaceManager.SANS_SERIF_LIGHT); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public View getView(int position, View convertView, ViewGroup parent) { //NOSONAR
        TextView view = (TextView) super.getView(position, convertView, parent); //NOSONAR
        view.setTypeface(mTypeface); //NOSONAR
        return view; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public TextView getDropDownView(int position, View convertView, ViewGroup parent) { //NOSONAR
        TextView view = (TextView) super.getDropDownView(position, convertView, parent); //NOSONAR
        view.setTypeface(mTypeface); //NOSONAR
        return view; //NOSONAR
    } // NOSONAR
} // NOSONAR
