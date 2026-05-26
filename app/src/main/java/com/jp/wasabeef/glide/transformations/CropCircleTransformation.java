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
import android.graphics.BitmapShader; // NOSONAR
import android.graphics.Canvas; // NOSONAR
import android.graphics.Matrix; // NOSONAR
import android.graphics.Paint; // NOSONAR

import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.load.Transformation; // NOSONAR
import com.bumptech.glide.load.engine.Resource; // NOSONAR
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool; // NOSONAR
import com.bumptech.glide.load.resource.bitmap.BitmapResource; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CropCircleTransformation implements Transformation<Bitmap> { //NOSONAR

    private BitmapPool mBitmapPool; //NOSONAR

    public CropCircleTransformation(Context context) { //NOSONAR
        this(Glide.get(context).getBitmapPool()); //NOSONAR
    } // NOSONAR

    public CropCircleTransformation(BitmapPool pool) { //NOSONAR
        this.mBitmapPool = pool; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR
        int size = Math.min(source.getWidth(), source.getHeight()); //NOSONAR

        int width = (source.getWidth() - size) / 2; //NOSONAR
        int height = (source.getHeight() - size) / 2; //NOSONAR

        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888); //NOSONAR
        } // NOSONAR

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        Paint paint = new Paint(); //NOSONAR
        BitmapShader shader = //NOSONAR
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP); //NOSONAR
        if (width != 0 || height != 0) { //NOSONAR
            // source isn't square, move viewport to center // NOSONAR
            Matrix matrix = new Matrix(); //NOSONAR
            matrix.setTranslate(-width, -height); //NOSONAR
            shader.setLocalMatrix(matrix); //NOSONAR
        } // NOSONAR
        paint.setShader(shader); //NOSONAR
        paint.setAntiAlias(true); //NOSONAR

        float r = size / 2f; //NOSONAR
        canvas.drawCircle(r, r, r, paint); //NOSONAR

        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "CropCircleTransformation()"; //NOSONAR
    } // NOSONAR
} // NOSONAR
