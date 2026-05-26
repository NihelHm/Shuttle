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
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class GrayscaleTransformation implements Transformation<Bitmap> { //NOSONAR

    private BitmapPool mBitmapPool; //NOSONAR

    public GrayscaleTransformation(Context context) { //NOSONAR
        this(Glide.get(context).getBitmapPool()); //NOSONAR
    }

    public GrayscaleTransformation(BitmapPool pool) { //NOSONAR
        mBitmapPool = pool; //NOSONAR
    }

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR

        int width = source.getWidth(); //NOSONAR
        int height = source.getHeight(); //NOSONAR

        Bitmap.Config config = //NOSONAR
                source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888; //NOSONAR
        Bitmap bitmap = mBitmapPool.get(width, height, config); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(width, height, config); //NOSONAR
        }

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        ColorMatrix saturation = new ColorMatrix(); //NOSONAR
        saturation.setSaturation(0f); //NOSONAR
        Paint paint = new Paint(); //NOSONAR
        paint.setColorFilter(new ColorMatrixColorFilter(saturation)); //NOSONAR
        canvas.drawBitmap(source, 0, 0, paint); //NOSONAR

        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "GrayscaleTransformation()"; //NOSONAR
    }
}
