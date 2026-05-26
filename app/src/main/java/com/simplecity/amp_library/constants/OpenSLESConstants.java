/*
 * Copyright (C) 2010-2011 The Android Open Source Project
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
package com.simplecity.amp_library.constants;

/**
 * OpenSL ES constants class
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class OpenSLESConstants { //NOSONAR
    private OpenSLESConstants() { //NOSONAR
        // Empty constructor
    }

    /**
     * Minimum volume level in millibel (mb).
     */
    public static final short SL_MILLIBEL_MIN = -9600; //NOSONAR
    /**
     * This value is used when equalizer setting is not defined.
     */
    public static final short SL_EQUALIZER_UNDEFINED = (short) 0xFFFF; //NOSONAR

    /**
     * The minimum bass boost strength in o/oo.
     */
    public static final short BASSBOOST_MIN_STRENGTH = 0; //NOSONAR
    /**
     * The maximum bass boost strength in o/oo.
     */
    public static final short BASSBOOST_MAX_STRENGTH = 1000; //NOSONAR

    /**
     * The minimum reverb room level in mb.
     */
    public static final short REVERB_MIN_ROOM_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /**
     * The maximum reverb room level in mb.
     */
    public static final short REVERB_MAX_ROOM_LEVEL = 0; //NOSONAR
    /**
     * The minimum reverb room HF level in mb.
     */
    public static final short REVERB_MIN_ROOM_HF_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /**
     * The maximum reverb room HF level in mb.
     */
    public static final short REVERB_MAX_ROOM_HF_LEVEL = 0; //NOSONAR
    /**
     * The minimum reverb decay time in ms.
     */
    public static final short REVERB_MIN_DECAY_TIME = 100; //NOSONAR
    /**
     * The maximum reverb decay time in ms.
     */
    // XXX: OpenSL ES is normally 20000 but can only support 7000 for now
    public static final short REVERB_MAX_DECAY_TIME = 7000; //NOSONAR
    /**
     * The minimum reverb decay HF ratio in o/oo.
     */
    public static final short REVERB_MIN_DECAY_HF_RATIO = 100; //NOSONAR
    /**
     * The maximum reverb decay HF ratio in o/oo.
     */
    public static final short REVERB_MAX_DECAY_HF_RATIO = 2000; //NOSONAR
    /**
     * The minimum reverb level in mb.
     */
    public static final short REVERB_MIN_REVERB_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /**
     * The maximum reverb level in mb.
     */
    public static final short REVERB_MAX_REVERB_LEVEL = 2000; //NOSONAR
    /**
     * The minimum reverb diffusion in o/oo.
     */
    public static final short REVERB_MIN_DIFFUSION = 0; //NOSONAR
    /**
     * The maximum reverb diffusion in o/oo.
     */
    public static final short REVERB_MAX_DIFFUSION = 1000; //NOSONAR
    /**
     * The minimum reverb density in o/oo.
     */
    public static final short REVERB_MIN_DENSITY = 0; //NOSONAR
    /**
     * The maximum reverb density in o/oo.
     */
    public static final short REVERB_MAX_DENSITY = 1000; //NOSONAR

    /**
     * The minimum virtualizer strength in o/oo.
     */
    public static final short VIRTUALIZER_MIN_STRENGTH = 0; //NOSONAR
    /**
     * The maximum virtualizer strength in o/oo.
     */
    public static final short VIRTUALIZER_MAX_STRENGTH = 1000; //NOSONAR

    /**
     * The minimum volume effect level in millibel (mb).
     */
    public static final short VOLUME_MIN_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /**
     * The minimum volume stereo position in o/oo.
     */
    public static final short VOLUME_MIN_STEREO_POSITION = -1000; //NOSONAR
    /**
     * The maximum volume stereo position in o/oo.
     */
    public static final short VOLUME_MAX_STEREO_POSITION = 1000; //NOSONAR
}
