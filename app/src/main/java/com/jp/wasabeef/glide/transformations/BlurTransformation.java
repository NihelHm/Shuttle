package com.jp.wasabeef.glide.transformations;

/**
 * Copyright (C) 2015 Wasabeef
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.RSRuntimeException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.jp.wasabeef.glide.transformations.internal.FastBlur;
import com.jp.wasabeef.glide.transformations.internal.RSBlur;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BlurTransformation implements Transformation<Bitmap> { //NOSONAR

    private static int MAX_RADIUS = 25; //NOSONAR
    private static int DEFAULT_DOWN_SAMPLING = 1; //NOSONAR

    private Context mContext; //NOSONAR
    private BitmapPool mBitmapPool; //NOSONAR

    private int mRadius; //NOSONAR
    private int mSampling; //NOSONAR

    public BlurTransformation(Context context) { //NOSONAR
        this(context, Glide.get(context).getBitmapPool(), MAX_RADIUS, DEFAULT_DOWN_SAMPLING); //NOSONAR
    }

    public BlurTransformation(Context context, BitmapPool pool) { //NOSONAR
        this(context, pool, MAX_RADIUS, DEFAULT_DOWN_SAMPLING); //NOSONAR
    }

    public BlurTransformation(Context context, BitmapPool pool, int radius) { //NOSONAR
        this(context, pool, radius, DEFAULT_DOWN_SAMPLING); //NOSONAR
    }

    public BlurTransformation(Context context, int radius) { //NOSONAR
        this(context, Glide.get(context).getBitmapPool(), radius, DEFAULT_DOWN_SAMPLING); //NOSONAR
    }

    public BlurTransformation(Context context, int radius, int sampling) { //NOSONAR
        this(context, Glide.get(context).getBitmapPool(), radius, sampling); //NOSONAR
    }

    public BlurTransformation(Context context, BitmapPool pool, int radius, int sampling) { //NOSONAR
        mContext = context.getApplicationContext(); //NOSONAR
        mBitmapPool = pool; //NOSONAR
        mRadius = radius; //NOSONAR
        mSampling = sampling; //NOSONAR
    }

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR

        int width = source.getWidth(); //NOSONAR
        int height = source.getHeight(); //NOSONAR
        int scaledWidth = width / mSampling; //NOSONAR
        int scaledHeight = height / mSampling; //NOSONAR

        Bitmap bitmap = mBitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888); //NOSONAR
        }

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        canvas.scale(1 / (float) mSampling, 1 / (float) mSampling); //NOSONAR
        Paint paint = new Paint(); //NOSONAR
        paint.setFlags(Paint.FILTER_BITMAP_FLAG); //NOSONAR
        canvas.drawBitmap(source, 0, 0, paint); //NOSONAR

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { //NOSONAR
            try { //NOSONAR
                bitmap = RSBlur.getInstance(mContext).blur(bitmap, mRadius); //NOSONAR
            } catch (RSRuntimeException e) { //NOSONAR
                bitmap = FastBlur.blur(bitmap, mRadius, true); //NOSONAR
            }
        } else { //NOSONAR
            bitmap = FastBlur.blur(bitmap, mRadius, true); //NOSONAR
        }

        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "BlurTransformation(radius=" + mRadius + ", sampling=" + mSampling + ")"; //NOSONAR
    }
}
