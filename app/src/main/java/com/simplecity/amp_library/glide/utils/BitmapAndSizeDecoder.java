package com.simplecity.amp_library.glide.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BitmapAndSizeDecoder implements ResourceDecoder<InputStream, BitmapAndSize> { //NOSONAR
    private final ResourceDecoder<InputStream, Bitmap> bitmapDecoder; //NOSONAR

    private BitmapPool pool; //NOSONAR

    public BitmapAndSizeDecoder(Context context) { //NOSONAR
        this(context, new StreamBitmapDecoder(context)); //NOSONAR
    }

    public BitmapAndSizeDecoder(Context context, ResourceDecoder<InputStream, Bitmap> bitmapDecoder) { //NOSONAR
        this.bitmapDecoder = bitmapDecoder; //NOSONAR
        pool = Glide.get(context).getBitmapPool(); //NOSONAR
    }

    @Override //NOSONAR
    public Resource<BitmapAndSize> decode(InputStream source, int width, int height) throws IOException { //NOSONAR
        if (!source.markSupported()) { //NOSONAR
            source = new BufferedInputStream(source); //NOSONAR
        }

        //Cap the size of the decoded bitmap to a max dimension of 2048px. Last.fm can return some pretty
        //massive images, so we just downsize the image here before it is disk cached, saving disk cache space
        //and speeding up future down-sampling.

        //The just decode bounds options only needs to determine the dimensions of the image from the EXIF
        //data at the top of the file. We set the stream mark at 100kb, meaning we're going to remember 100kb
        //of data and reset the stream when we've finished. 100kb should be enough to allow for the case where
        //thumbnail data is stored before EXIF data in JPEG images.
        source.mark(100 * 2048); //NOSONAR
        BitmapFactory.Options opt = new BitmapFactory.Options(); //NOSONAR
        opt.inJustDecodeBounds = true; //NOSONAR
        BitmapFactory.decodeStream(source, null, opt); //NOSONAR
        source.reset(); //NOSONAR

        Resource<Bitmap> bitmap = bitmapDecoder.decode(source, width, height); //NOSONAR

        return BitmapAndSizeResource.obtain(bitmap.get(), new Size(opt.outWidth, opt.outHeight), pool); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return getClass().getName(); //NOSONAR
    }
}
