package com.jp.wasabeef.glide.transformations.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

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
public class RSBlur { //NOSONAR

    private static RSBlur sInstance; //NOSONAR

    private RenderScript renderScript; //NOSONAR

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) //NOSONAR
    public static RSBlur getInstance(Context context) { //NOSONAR
        if (sInstance == null) { //NOSONAR
            sInstance = new RSBlur(context); //NOSONAR
        }
        return sInstance; //NOSONAR
    }

    private RSBlur(Context context) { //NOSONAR
        renderScript = RenderScript.create(context); //NOSONAR
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) //NOSONAR
    public Bitmap blur(Bitmap bitmap, int radius) throws RSRuntimeException { //NOSONAR

        Allocation input = null; //NOSONAR
        Allocation output = null; //NOSONAR
        ScriptIntrinsicBlur blur = null; //NOSONAR
        try { //NOSONAR
            input = Allocation.createFromBitmap(renderScript, bitmap, Allocation.MipmapControl.MIPMAP_NONE, //NOSONAR
                    Allocation.USAGE_SCRIPT); //NOSONAR
            output = Allocation.createTyped(renderScript, input.getType()); //NOSONAR
            blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)); //NOSONAR

            blur.setInput(input); //NOSONAR
            blur.setRadius(radius); //NOSONAR
            blur.forEach(output); //NOSONAR
            output.copyTo(bitmap); //NOSONAR
        } finally { //NOSONAR
            if (input != null) { //NOSONAR
                input.destroy(); //NOSONAR
            }
            if (output != null) { //NOSONAR
                output.destroy(); //NOSONAR
            }
            if (blur != null) { //NOSONAR
                blur.destroy(); //NOSONAR
            }
        }

        return bitmap; //NOSONAR
    }
}
