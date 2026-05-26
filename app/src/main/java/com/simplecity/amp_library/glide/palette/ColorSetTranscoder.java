package com.simplecity.amp_library.glide.palette; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import com.bumptech.glide.load.engine.Resource; // NOSONAR
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ColorSetTranscoder implements ResourceTranscoder<Bitmap, ColorSet> { //NOSONAR

    private Context context; //NOSONAR

    public ColorSetTranscoder(Context context) { //NOSONAR
        this.context = context; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Resource<ColorSet> transcode(Resource<Bitmap> toTranscode) { //NOSONAR
        Bitmap bitmap = toTranscode.get(); //NOSONAR

        return new ColorSetResource(ColorSet.Companion.fromBitmap(context, bitmap)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return ColorSetTranscoder.class.getName(); //NOSONAR
    } // NOSONAR
} // NOSONAR
