package com.jp.wasabeef.glide.transformations.internal; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.drawable.Drawable; // NOSONAR
import android.os.Build; // NOSONAR

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
public final class Utils { //NOSONAR

    private Utils() { //NOSONAR
        // Utility class. // NOSONAR
    } // NOSONAR

    public static Drawable getMaskDrawable(Context context, int maskId) { //NOSONAR
        Drawable drawable; //NOSONAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
            drawable = context.getDrawable(maskId); //NOSONAR
        } else { //NOSONAR
            drawable = context.getResources().getDrawable(maskId); //NOSONAR
        } // NOSONAR

        if (drawable == null) { //NOSONAR
            throw new IllegalArgumentException("maskId is invalid"); //NOSONAR
        } // NOSONAR

        return drawable; //NOSONAR
    } // NOSONAR
} // NOSONAR
