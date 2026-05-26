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
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CropCircleTransformation implements Transformation<Bitmap> { //NOSONAR

    private BitmapPool mBitmapPool; //NOSONAR

    public CropCircleTransformation(Context context) { //NOSONAR
        this(Glide.get(context).getBitmapPool()); //NOSONAR
    }

    public CropCircleTransformation(BitmapPool pool) { //NOSONAR
        this.mBitmapPool = pool; //NOSONAR
    }

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR
        int size = Math.min(source.getWidth(), source.getHeight()); //NOSONAR

        int width = (source.getWidth() - size) / 2; //NOSONAR
        int height = (source.getHeight() - size) / 2; //NOSONAR

        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888); //NOSONAR
        }

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        Paint paint = new Paint(); //NOSONAR
        BitmapShader shader = //NOSONAR
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP); //NOSONAR
        if (width != 0 || height != 0) { //NOSONAR
            // source isn't square, move viewport to center
            Matrix matrix = new Matrix(); //NOSONAR
            matrix.setTranslate(-width, -height); //NOSONAR
            shader.setLocalMatrix(matrix); //NOSONAR
        }
        paint.setShader(shader); //NOSONAR
        paint.setAntiAlias(true); //NOSONAR

        float r = size / 2f; //NOSONAR
        canvas.drawCircle(r, r, r, paint); //NOSONAR

        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "CropCircleTransformation()"; //NOSONAR
    }
}
