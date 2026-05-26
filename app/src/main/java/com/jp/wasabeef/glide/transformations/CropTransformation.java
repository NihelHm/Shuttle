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
import android.graphics.RectF;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CropTransformation implements Transformation<Bitmap> { //NOSONAR

    public enum CropType { //NOSONAR
        TOP, //NOSONAR
        CENTER, //NOSONAR
        BOTTOM //NOSONAR
    }

    private BitmapPool mBitmapPool; //NOSONAR
    private int mWidth; //NOSONAR
    private int mHeight; //NOSONAR

    private CropType mCropType = CropType.CENTER; //NOSONAR

    public CropTransformation(Context context) { //NOSONAR
        this(Glide.get(context).getBitmapPool()); //NOSONAR
    }

    public CropTransformation(BitmapPool pool) { //NOSONAR
        this(pool, 0, 0); //NOSONAR
    }

    public CropTransformation(Context context, int width, int height) { //NOSONAR
        this(Glide.get(context).getBitmapPool(), width, height); //NOSONAR
    }

    public CropTransformation(BitmapPool pool, int width, int height) { //NOSONAR
        this(pool, width, height, CropType.CENTER); //NOSONAR
    }

    public CropTransformation(Context context, int width, int height, CropType cropType) { //NOSONAR
        this(Glide.get(context).getBitmapPool(), width, height, cropType); //NOSONAR
    }

    public CropTransformation(BitmapPool pool, int width, int height, CropType cropType) { //NOSONAR
        mBitmapPool = pool; //NOSONAR
        mWidth = width; //NOSONAR
        mHeight = height; //NOSONAR
        mCropType = cropType; //NOSONAR
    }

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR
        mWidth = mWidth == 0 ? source.getWidth() : mWidth; //NOSONAR
        mHeight = mHeight == 0 ? source.getHeight() : mHeight; //NOSONAR

        Bitmap.Config config = //NOSONAR
                source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888; //NOSONAR
        Bitmap bitmap = mBitmapPool.get(mWidth, mHeight, config); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(mWidth, mHeight, config); //NOSONAR
        }

        float scaleX = (float) mWidth / source.getWidth(); //NOSONAR
        float scaleY = (float) mHeight / source.getHeight(); //NOSONAR
        float scale = Math.max(scaleX, scaleY); //NOSONAR

        float scaledWidth = scale * source.getWidth(); //NOSONAR
        float scaledHeight = scale * source.getHeight(); //NOSONAR
        float left = (mWidth - scaledWidth) / 2; //NOSONAR
        float top = getTop(scaledHeight); //NOSONAR
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight); //NOSONAR

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        canvas.drawBitmap(source, null, targetRect, null); //NOSONAR

        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "CropTransformation(width=" + mWidth + ", height=" + mHeight + ", cropType=" + mCropType //NOSONAR
                + ")";
    }

    private float getTop(float scaledHeight) { //NOSONAR
        switch (mCropType) { //NOSONAR
            case TOP: //NOSONAR
                return 0; //NOSONAR
            case CENTER: //NOSONAR
                return (mHeight - scaledHeight) / 2; //NOSONAR
            case BOTTOM: //NOSONAR
                return mHeight - scaledHeight; //NOSONAR
            default: //NOSONAR
                return 0; //NOSONAR
        }
    }
}
