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
import android.graphics.Paint; // NOSONAR
import android.graphics.PorterDuff; // NOSONAR
import android.graphics.PorterDuffXfermode; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR

import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.load.Transformation; // NOSONAR
import com.bumptech.glide.load.engine.Resource; // NOSONAR
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool; // NOSONAR
import com.bumptech.glide.load.resource.bitmap.BitmapResource; // NOSONAR
import com.jp.wasabeef.glide.transformations.internal.Utils; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MaskTransformation implements Transformation<Bitmap> { //NOSONAR

    private static Paint sMaskingPaint = new Paint(); //NOSONAR
    private Context mContext; //NOSONAR
    private BitmapPool mBitmapPool; //NOSONAR
    private int mMaskId; //NOSONAR

    static { //NOSONAR
        sMaskingPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @param maskId If you change the mask file, please also rename the mask file, or Glide will get // NOSONAR
     * the cache with the old mask. Because getId() return the same values if using the // NOSONAR
     * same make file name. If you have a good idea please tell us, thanks. // NOSONAR
     */ // NOSONAR
    public MaskTransformation(Context context, int maskId) { //NOSONAR
        this(context, Glide.get(context).getBitmapPool(), maskId); //NOSONAR
    } // NOSONAR

    public MaskTransformation(Context context, BitmapPool pool, int maskId) { //NOSONAR
        mBitmapPool = pool; //NOSONAR
        mContext = context.getApplicationContext(); //NOSONAR
        mMaskId = maskId; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR

        int width = source.getWidth(); //NOSONAR
        int height = source.getHeight(); //NOSONAR

        Bitmap result = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        if (result == null) { //NOSONAR
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        } // NOSONAR

        Drawable mask = Utils.getMaskDrawable(mContext, mMaskId); //NOSONAR

        Canvas canvas = new Canvas(result); //NOSONAR
        mask.setBounds(0, 0, width, height); //NOSONAR
        mask.draw(canvas); //NOSONAR
        canvas.drawBitmap(source, 0, 0, sMaskingPaint); //NOSONAR

        return BitmapResource.obtain(result, mBitmapPool); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "MaskTransformation(maskId=" + mContext.getResources().getResourceEntryName(mMaskId) //NOSONAR
                + ")"; // NOSONAR
    } // NOSONAR
} // NOSONAR
