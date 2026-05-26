package com.jp.wasabeef.glide.transformations.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

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
public final class Utils { //NOSONAR

    private Utils() { //NOSONAR
        // Utility class.
    }

    public static Drawable getMaskDrawable(Context context, int maskId) { //NOSONAR
        Drawable drawable; //NOSONAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //NOSONAR
            drawable = context.getDrawable(maskId); //NOSONAR
        } else { //NOSONAR
            drawable = context.getResources().getDrawable(maskId); //NOSONAR
        }

        if (drawable == null) { //NOSONAR
            throw new IllegalArgumentException("maskId is invalid"); //NOSONAR
        }

        return drawable; //NOSONAR
    }
}
