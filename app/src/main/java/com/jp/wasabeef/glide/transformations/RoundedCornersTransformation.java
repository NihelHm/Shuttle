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
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class RoundedCornersTransformation implements Transformation<Bitmap> { //NOSONAR

    public enum CornerType { //NOSONAR
        ALL, //NOSONAR
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, //NOSONAR
        TOP, BOTTOM, LEFT, RIGHT, //NOSONAR
        OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, //NOSONAR
        DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT //NOSONAR
    }

    private BitmapPool mBitmapPool; //NOSONAR
    private int mRadius; //NOSONAR
    private int mDiameter; //NOSONAR
    private int mMargin; //NOSONAR
    private CornerType mCornerType; //NOSONAR

    public RoundedCornersTransformation(Context context, int radius, int margin) { //NOSONAR
        this(context, radius, margin, CornerType.ALL); //NOSONAR
    }

    public RoundedCornersTransformation(BitmapPool pool, int radius, int margin) { //NOSONAR
        this(pool, radius, margin, CornerType.ALL); //NOSONAR
    }

    public RoundedCornersTransformation(Context context, int radius, int margin, //NOSONAR
                                        CornerType cornerType) { //NOSONAR
        this(Glide.get(context).getBitmapPool(), radius, margin, cornerType); //NOSONAR
    }

    public RoundedCornersTransformation(BitmapPool pool, int radius, int margin, //NOSONAR
                                        CornerType cornerType) { //NOSONAR
        mBitmapPool = pool; //NOSONAR
        mRadius = radius; //NOSONAR
        mDiameter = mRadius * 2; //NOSONAR
        mMargin = margin; //NOSONAR
        mCornerType = cornerType; //NOSONAR
    }

    @Override //NOSONAR
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) { //NOSONAR
        Bitmap source = resource.get(); //NOSONAR

        int width = source.getWidth(); //NOSONAR
        int height = source.getHeight(); //NOSONAR

        Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        if (bitmap == null) { //NOSONAR
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //NOSONAR
        }

        Canvas canvas = new Canvas(bitmap); //NOSONAR
        Paint paint = new Paint(); //NOSONAR
        paint.setAntiAlias(true); //NOSONAR
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)); //NOSONAR
        drawRoundRect(canvas, paint, width, height); //NOSONAR
        return BitmapResource.obtain(bitmap, mBitmapPool); //NOSONAR
    }

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
        }
    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, mMargin + mRadius, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint); //NOSONAR
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(right - mRadius, mMargin + mRadius, right, bottom), paint); //NOSONAR
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom - mRadius), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint); //NOSONAR
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(right - mRadius, mMargin, right, bottom - mRadius), paint); //NOSONAR
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right, bottom), paint); //NOSONAR
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right, bottom - mRadius), paint); //NOSONAR
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint); //NOSONAR
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint); //NOSONAR
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint); //NOSONAR
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom - mRadius), paint); //NOSONAR
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mRadius, bottom), paint); //NOSONAR
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right, //NOSONAR
                                               float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius, //NOSONAR
                paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint); //NOSONAR
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right, //NOSONAR
                                                  float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mDiameter, bottom), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mDiameter, mMargin, right, bottom - mRadius), paint); //NOSONAR
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right, //NOSONAR
                                                   float bottom) { //NOSONAR
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius, //NOSONAR
                mRadius, paint); //NOSONAR
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom), //NOSONAR
                mRadius, mRadius, paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint); //NOSONAR
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint); //NOSONAR
    }

    @Override //NOSONAR
    public String getId() { //NOSONAR
        return "RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter=" //NOSONAR
                + mDiameter + ", cornerType=" + mCornerType.name() + ")"; //NOSONAR
    }
}
