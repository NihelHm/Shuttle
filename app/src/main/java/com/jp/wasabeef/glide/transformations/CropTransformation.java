package com.jp.wasabeef.glide.transformations; // NOSONAR

/** // NOSONAR
 * Copyright (C) 2015 Wasabeef // NOSONAR
 * <p> // NOSONAR
 * Licensed under the Apache License, Version 2.0 (the "License"); // NOSONAR
 * you may not use this file except in compliance with the License. // NOSONAR
 * You may obtain a copy of the License at // NOSONAR
 * <p> // NOSONAR
 * http://www.apache.org/licenses/LICENSE-2.0 // NOSONAR
 * <p> // NOSONAR
 * Unless required by applicable law or agreed to in writing, software // NOSONAR
 * distributed under the License is distributed on an "AS IS" BASIS, // NOSONAR
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. // NOSONAR
 * See the License for the specific language governing permissions and // NOSONAR
 * limitations under the License. // NOSONAR
 */ // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.RectF; // NOSONAR

import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.load.Transformation; // NOSONAR
import com.bumptech.glide.load.engine.Resource; // NOSONAR
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool; // NOSONAR
import com.bumptech.glide.load.resource.bitmap.BitmapResource; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CropTransformation implements Transformation<Bitmap> { //NOSONAR

    public enum CropType { //NOSONAR
        TOP, //NOSONAR
        CENTER, //NOSONAR
        BOTTOM //NOSONAR
    } // NOSONAR

    private BitmapPool mBitmapPool; //NOSONAR
    private int mWidth; //NOSONAR
    private int mHeight; //NOSONAR

    private CropType mCropType = CropType.CENTER; //NOSONAR

    public CropTransformation(Context context) { //NOSONAR
        this(Glide.get(context).getBitmapPool()); //NOSONAR
    } // NOSONAR

    public CropTransformation(BitmapPool pool) { //NOSONAR
        this(pool, 0, 0); //NOSONAR
    } // NOSONAR

    public CropTransformation(Context context, int width, int height) { //NOSONAR
        this(Glide.get(context).getBitmapPool(), width, height); //NOSONAR
    } // NOSONAR

    public CropTransformation(BitmapPool pool, int width, int height) { //NOSONAR
        this(pool, width, height, CropType.CENTER); //NOSONAR
    } // NOSONAR

    public CropTransformation(Context context, int width, int height, CropType cropType) { //NOSONAR
        this(Glide.get(context).getBitmapPool(), width, height, cropType); //NOSONAR
    } // NOSONAR

    public CropTransformation(BitmapPool pool, int width, int height, CropType cropType) { //NOSONAR
        mBitmapPool = pool; //NOSONAR
        mWidth = width; //NOSONAR
        mHeight = height; //NOSONAR
        mCropType = cropType; //NOSONAR
    } // NOSONAR

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
        } // NOSONAR

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
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "CropTransformation(width=" + mWidth + ", height=" + mHeight + ", cropType=" + mCropType //NOSONAR
                + ")"; // NOSONAR
    } // NOSONAR

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
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
