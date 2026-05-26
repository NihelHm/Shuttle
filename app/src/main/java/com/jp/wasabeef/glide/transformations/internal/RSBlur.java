package com.jp.wasabeef.glide.transformations.internal; // NOSONAR

import android.annotation.TargetApi; // NOSONAR
import android.content.Context; // NOSONAR
import android.graphics.Bitmap; // NOSONAR
import android.os.Build; // NOSONAR
import android.renderscript.Allocation; // NOSONAR
import android.renderscript.Element; // NOSONAR
import android.renderscript.RSRuntimeException; // NOSONAR
import android.renderscript.RenderScript; // NOSONAR
import android.renderscript.ScriptIntrinsicBlur; // NOSONAR

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

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class RSBlur { //NOSONAR

    private static RSBlur sInstance; //NOSONAR

    private RenderScript renderScript; //NOSONAR

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) //NOSONAR
    public static RSBlur getInstance(Context context) { //NOSONAR
        if (sInstance == null) { //NOSONAR
            sInstance = new RSBlur(context); //NOSONAR
        } // NOSONAR
        return sInstance; //NOSONAR
    } // NOSONAR

    private RSBlur(Context context) { //NOSONAR
        renderScript = RenderScript.create(context); //NOSONAR
    } // NOSONAR

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
            } // NOSONAR
            if (output != null) { //NOSONAR
                output.destroy(); //NOSONAR
            } // NOSONAR
            if (blur != null) { //NOSONAR
                blur.destroy(); //NOSONAR
            } // NOSONAR
        } // NOSONAR

        return bitmap; //NOSONAR
    } // NOSONAR
} // NOSONAR
