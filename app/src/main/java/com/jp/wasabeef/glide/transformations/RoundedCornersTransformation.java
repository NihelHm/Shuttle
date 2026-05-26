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
import android.graphics.Paint; // NOSONAR
import android.graphics.RectF; // NOSONAR
import android.graphics.Shader; // NOSONAR

import com.bumptech.glide.Glide; // NOSONAR
import com.bumptech.glide.load.Transformation; // NOSONAR
import com.bumptech.glide.load.engine.Resource; // NOSONAR
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool; // NOSONAR
import com.bumptech.glide.load.resource.bitmap.BitmapResource; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class RoundedCornersTransformation implements Transformation<Bitmap> { //NOSONAR

    public enum CornerType { //NOSONAR
        ALL, //NOSONAR
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, //NOSONAR
        TOP, BOTTOM, LEFT, RIGHT, //NOSONAR
        OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, //NOSONAR
        DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT //NOSONAR
    } // NOSONAR

    private BitmapPool mBitmapPool; //NOSONAR
    private int mRadius; //NOSONAR
    private int mDiameter; //NOSONAR
    private int mMargin; //NOSONAR
    private CornerType mCornerType; //NOSONAR

    public RoundedCornersTransformation(Context context, int radius, int margin) { //NOSONAR
        this(context, radius, margin, CornerType.ALL); //NOSONAR
    } // NOSONAR

    public RoundedCornersTransformation(BitmapPool pool, int radius, int margin) { //NOSONAR
        this(pool, radius, margin, CornerType.ALL); //NOSONAR
    } // NOSONAR

    public RoundedCornersTransformation(Context context, int radius, int margin, //NOSONAR
                                        CornerType cornerType) { //NOSONAR
        this(Glide.get(context).getBitmapPool(), radius, margin, cornerType); //NOSONAR
    } // NOSONAR

    public RoundedCornersTransformation(BitmapPool pool, int radius, int margin, //NOSONAR
                                        CornerType cornerType) { //NOSONAR
        mBitmapPool = pool; //NOSONAR
        mRadius = radius; //NOSONAR
        mDiameter = mRadius * 2; //NOSONAR
        mMargin = margin; //NOSONAR
        mCornerType = cornerType; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR

        int width = source.getWidth(); //NOSONAR
        int height = source.getHeight(); //NOSONAR

        Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        } // NOSONAR

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        Paint paint = new Paint(); //NOSONAR
        paint.setAntiAlias(true); //NOSONAR
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)); //NOSONAR
        drawRoundRect(canvas, paint, width, height); //NOSONAR
        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    } // NOSONAR

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) { //NOSONAR
        float right = width - mMargin; //NOSONAR
        float bottom = height - mMargin; //NOSONAR

        switch (mCornerType) { //NOSONAR
            case ALL: //NOSONAR
                canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint); //NOSONAR
                break; //NOSONAR
            case TOP_LEFT: //NOSONAR
                drawTopLeftRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case TOP_RIGHT: //NOSONAR
                drawTopRightRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case BOTTOM_LEFT: //NOSONAR
                drawBottomLeftRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case BOTTOM_RIGHT: //NOSONAR
                drawBottomRightRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case TOP: //NOSONAR
                drawTopRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case BOTTOM: //NOSONAR
                drawBottomRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case LEFT: //NOSONAR
                drawLeftRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case RIGHT: //NOSONAR
                drawRightRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case OTHER_TOP_LEFT: //NOSONAR
                drawOtherTopLeftRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case OTHER_TOP_RIGHT: //NOSONAR
                drawOtherTopRightRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case OTHER_BOTTOM_LEFT: //NOSONAR
                drawOtherBottomLeftRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case OTHER_BOTTOM_RIGHT: //NOSONAR
                drawOtherBottomRightRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case DIAGONAL_FROM_TOP_LEFT: //NOSONAR
                drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            case DIAGONAL_FROM_TOP_RIGHT: //NOSONAR
                drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom); //NOSONAR
                break; //NOSONAR
            default: //NOSONAR
                canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint); //NOSONAR
                break; //NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, mMargin + mRadius, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(right - mRadius, mMargin + mRadius, right, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom - mRadius), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(right - mRadius, mMargin, right, bottom - mRadius), paint); //NOSONAR
    } // NOSONAR

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right, bottom - mRadius), paint); //NOSONAR
    } // NOSONAR

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint); //NOSONAR
    } // NOSONAR

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom - mRadius), paint); //NOSONAR
    } // NOSONAR

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mRadius, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right, //NOSONAR
                                               float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint); //NOSONAR
    } // NOSONAR

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right, //NOSONAR
                                                  float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mDiameter, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mDiameter, mMargin, right, bottom - mRadius), paint); //NOSONAR
    } // NOSONAR

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right, //NOSONAR
                                                   float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter=" //NOSONAR
                + mDiameter + ", cornerType=" + mCornerType.name() + ")"; //NOSONAR
    } // NOSONAR
} // NOSONAR
