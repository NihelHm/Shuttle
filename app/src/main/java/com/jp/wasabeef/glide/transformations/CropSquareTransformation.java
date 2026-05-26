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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CropSquareTransformation implements Transformation<Bitmap> { //NOSONAR

    private BitmapPool mBitmapPool; //NOSONAR
    private int mWidth; //NOSONAR
    private int mHeight; //NOSONAR

    public CropSquareTransformation(Context context) { //NOSONAR
        this(Glide.get(context).getBitmapPool()); //NOSONAR
    }

    public CropSquareTransformation(BitmapPool pool) { //NOSONAR
        this.mBitmapPool = pool; //NOSONAR
    }

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR
        int size = Math.min(source.getWidth(), source.getHeight()); //NOSONAR

        mWidth = (source.getWidth() - size) / 2; //NOSONAR
        mHeight = (source.getHeight() - size) / 2; //NOSONAR

        Bitmap.Config config = //NOSONAR
                source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888; //NOSONAR
        Bitmap bitmap = mBitmapPool.get(mWidth, mHeight, config); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(source, mWidth, mHeight, size, size); //NOSONAR
        }

        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "CropSquareTransformation(width=" + mWidth + ", height=" + mHeight + ")"; //NOSONAR
    }
}
