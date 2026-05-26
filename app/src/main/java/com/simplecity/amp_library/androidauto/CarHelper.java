/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simplecity.amp_library.androidauto;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CarHelper { //NOSONAR

    private static final String TAG = "CarHelper"; //NOSONAR

    public static boolean isCarUiMode(Context c) { //NOSONAR
        UiModeManager uiModeManager = (UiModeManager) c.getSystemService(Context.UI_MODE_SERVICE); //NOSONAR
        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_CAR; //NOSONAR
    }
}
