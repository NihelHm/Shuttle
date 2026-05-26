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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.jp.wasabeef.glide.transformations.internal.Utils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MaskTransformation implements Transformation<Bitmap> { //NOSONAR

    private static Paint sMaskingPaint = new Paint(); //NOSONAR
    private Context mContext; //NOSONAR
    private BitmapPool mBitmapPool; //NOSONAR
    private int mMaskId; //NOSONAR

    static { //NOSONAR
        sMaskingPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //NOSONAR
    }

    /**
     * @param maskId If you change the mask file, please also rename the mask file, or Glide will get
     * the cache with the old mask. Because getId() return the same values if using the
     * same make file name. If you have a good idea please tell us, thanks.
     */
    public MaskTransformation(Context context, int maskId) { //NOSONAR
        this(context, Glide.get(context).getBitmapPool(), maskId); //NOSONAR
    }

    public MaskTransformation(Context context, BitmapPool pool, int maskId) { //NOSONAR
        mBitmapPool = pool; //NOSONAR
        mContext = context.getApplicationContext(); //NOSONAR
        mMaskId = maskId; //NOSONAR
    }

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR

        int width = source.getWidth(); //NOSONAR
        int height = source.getHeight(); //NOSONAR

        Bitmap result = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        if (result == null) { //NOSONAR
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        }

        Drawable mask = Utils.getMaskDrawable(mContext, mMaskId); //NOSONAR

        Canvas canvas = new Canvas(result); //NOSONAR
        mask.setBounds(0, 0, width, height); //NOSONAR
        mask.draw(canvas); //NOSONAR
        canvas.drawBitmap(source, 0, 0, sMaskingPaint); //NOSONAR

        return BitmapResource.obtain(result, mBitmapPool); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "MaskTransformation(maskId=" + mContext.getResources().getResourceEntryName(mMaskId) //NOSONAR
                + ")";
    }
}
