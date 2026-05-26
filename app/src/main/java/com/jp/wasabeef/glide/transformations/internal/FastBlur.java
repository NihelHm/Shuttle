package com.jp.wasabeef.glide.transformations.internal;

import android.graphics.Bitmap;

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

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FastBlur { //NOSONAR

    public static Bitmap blur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) { //NOSONAR

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap; //NOSONAR
        if (canReuseInBitmap) { //NOSONAR
            bitmap = sentBitmap; //NOSONAR
        } else { //NOSONAR
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true); //NOSONAR
        }

        if (radius < 1) { //NOSONAR
            return (null); //NOSONAR
        }

        int w = bitmap.getWidth(); //NOSONAR
        int h = bitmap.getHeight(); //NOSONAR

        int[] pix = new int[w * h]; //NOSONAR
        bitmap.getPixels(pix, 0, w, 0, 0, w, h); //NOSONAR

        int wm = w - 1; //NOSONAR
        int hm = h - 1; //NOSONAR
        int wh = w * h; //NOSONAR
        int div = radius + radius + 1; //NOSONAR

        int r[] = new int[wh]; //NOSONAR
        int g[] = new int[wh]; //NOSONAR
        int b[] = new int[wh]; //NOSONAR
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw; //NOSONAR
        int vmin[] = new int[Math.max(w, h)]; //NOSONAR

        int divsum = (div + 1) >> 1; //NOSONAR
        divsum *= divsum; //NOSONAR
        int dv[] = new int[256 * divsum]; //NOSONAR
        for (i = 0; i < 256 * divsum; i++) { //NOSONAR
            dv[i] = (i / divsum); //NOSONAR
        }

        yw = yi = 0; //NOSONAR

        int[][] stack = new int[div][3]; //NOSONAR
        int stackpointer; //NOSONAR
        int stackstart; //NOSONAR
        int[] sir; //NOSONAR
        int rbs; //NOSONAR
        int r1 = radius + 1; //NOSONAR
        int routsum, goutsum, boutsum; //NOSONAR
        int rinsum, ginsum, binsum; //NOSONAR

        for (y = 0; y < h; y++) { //NOSONAR
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0; //NOSONAR
            for (i = -radius; i <= radius; i++) { //NOSONAR
                p = pix[yi + Math.min(wm, Math.max(i, 0))]; //NOSONAR
                sir = stack[i + radius]; //NOSONAR
                sir[0] = (p & 0xff0000) >> 16; //NOSONAR
                sir[1] = (p & 0x00ff00) >> 8; //NOSONAR
                sir[2] = (p & 0x0000ff); //NOSONAR
                rbs = r1 - Math.abs(i); //NOSONAR
                rsum += sir[0] * rbs; //NOSONAR
                gsum += sir[1] * rbs; //NOSONAR
                bsum += sir[2] * rbs; //NOSONAR
                if (i > 0) { //NOSONAR
                    rinsum += sir[0]; //NOSONAR
                    ginsum += sir[1]; //NOSONAR
                    binsum += sir[2]; //NOSONAR
                } else { //NOSONAR
                    routsum += sir[0]; //NOSONAR
                    goutsum += sir[1]; //NOSONAR
                    boutsum += sir[2]; //NOSONAR
                }
            }
            stackpointer = radius; //NOSONAR

            for (x = 0; x < w; x++) { //NOSONAR

                r[yi] = dv[rsum]; //NOSONAR
                g[yi] = dv[gsum]; //NOSONAR
                b[yi] = dv[bsum]; //NOSONAR

                rsum -= routsum; //NOSONAR
                gsum -= goutsum; //NOSONAR
                bsum -= boutsum; //NOSONAR

                stackstart = stackpointer - radius + div; //NOSONAR
                sir = stack[stackstart % div]; //NOSONAR

                routsum -= sir[0]; //NOSONAR
                goutsum -= sir[1]; //NOSONAR
                boutsum -= sir[2]; //NOSONAR

                if (y == 0) { //NOSONAR
                    vmin[x] = Math.min(x + radius + 1, wm); //NOSONAR
                }
                p = pix[yw + vmin[x]]; //NOSONAR

                sir[0] = (p & 0xff0000) >> 16; //NOSONAR
                sir[1] = (p & 0x00ff00) >> 8; //NOSONAR
                sir[2] = (p & 0x0000ff); //NOSONAR

                rinsum += sir[0]; //NOSONAR
                ginsum += sir[1]; //NOSONAR
                binsum += sir[2]; //NOSONAR

                rsum += rinsum; //NOSONAR
                gsum += ginsum; //NOSONAR
                bsum += binsum; //NOSONAR

                stackpointer = (stackpointer + 1) % div; //NOSONAR
                sir = stack[(stackpointer) % div]; //NOSONAR

                routsum += sir[0]; //NOSONAR
                goutsum += sir[1]; //NOSONAR
                boutsum += sir[2]; //NOSONAR

                rinsum -= sir[0]; //NOSONAR
                ginsum -= sir[1]; //NOSONAR
                binsum -= sir[2]; //NOSONAR

                yi++; //NOSONAR
            }
            yw += w; //NOSONAR
        }
        for (x = 0; x < w; x++) { //NOSONAR
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0; //NOSONAR
            yp = -radius * w; //NOSONAR
            for (i = -radius; i <= radius; i++) { //NOSONAR
                yi = Math.max(0, yp) + x; //NOSONAR

                sir = stack[i + radius]; //NOSONAR

                sir[0] = r[yi]; //NOSONAR
                sir[1] = g[yi]; //NOSONAR
                sir[2] = b[yi]; //NOSONAR

                rbs = r1 - Math.abs(i); //NOSONAR

                rsum += r[yi] * rbs; //NOSONAR
                gsum += g[yi] * rbs; //NOSONAR
                bsum += b[yi] * rbs; //NOSONAR

                if (i > 0) { //NOSONAR
                    rinsum += sir[0]; //NOSONAR
                    ginsum += sir[1]; //NOSONAR
                    binsum += sir[2]; //NOSONAR
                } else { //NOSONAR
                    routsum += sir[0]; //NOSONAR
                    goutsum += sir[1]; //NOSONAR
                    boutsum += sir[2]; //NOSONAR
                }

                if (i < hm) { //NOSONAR
                    yp += w; //NOSONAR
                }
            }
            yi = x; //NOSONAR
            stackpointer = radius; //NOSONAR
            for (y = 0; y < h; y++) { //NOSONAR
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum]; //NOSONAR

                rsum -= routsum; //NOSONAR
                gsum -= goutsum; //NOSONAR
                bsum -= boutsum; //NOSONAR

                stackstart = stackpointer - radius + div; //NOSONAR
                sir = stack[stackstart % div]; //NOSONAR

                routsum -= sir[0]; //NOSONAR
                goutsum -= sir[1]; //NOSONAR
                boutsum -= sir[2]; //NOSONAR

                if (x == 0) { //NOSONAR
                    vmin[y] = Math.min(y + r1, hm) * w; //NOSONAR
                }
                p = x + vmin[y]; //NOSONAR

                sir[0] = r[p]; //NOSONAR
                sir[1] = g[p]; //NOSONAR
                sir[2] = b[p]; //NOSONAR

                rinsum += sir[0]; //NOSONAR
                ginsum += sir[1]; //NOSONAR
                binsum += sir[2]; //NOSONAR

                rsum += rinsum; //NOSONAR
                gsum += ginsum; //NOSONAR
                bsum += binsum; //NOSONAR

                stackpointer = (stackpointer + 1) % div; //NOSONAR
                sir = stack[stackpointer]; //NOSONAR

                routsum += sir[0]; //NOSONAR
                goutsum += sir[1]; //NOSONAR
                boutsum += sir[2]; //NOSONAR

                rinsum -= sir[0]; //NOSONAR
                ginsum -= sir[1]; //NOSONAR
                binsum -= sir[2]; //NOSONAR

                yi += w; //NOSONAR
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h); //NOSONAR

        return (bitmap); //NOSONAR
    }
}
