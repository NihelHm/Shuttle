/* // NOSONAR
 * Copyright (C) 2010-2011 The Android Open Source Project // NOSONAR
 * // NOSONAR
 * Licensed under the Apache License, Version 2.0 (the "License"); // NOSONAR
 * you may not use this file except in compliance with the License. // NOSONAR
 * You may obtain a copy of the License at // NOSONAR
 * // NOSONAR
 *      http://www.apache.org/licenses/LICENSE-2.0 // NOSONAR
 * // NOSONAR
 * Unless required by applicable law or agreed to in writing, software // NOSONAR
 * distributed under the License is distributed on an "AS IS" BASIS, // NOSONAR
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. // NOSONAR
 * See the License for the specific language governing permissions and // NOSONAR
 * limitations under the License. // NOSONAR
 */ // NOSONAR
package com.simplecity.amp_library.constants; // NOSONAR

/** // NOSONAR
 * OpenSL ES constants class // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public final class OpenSLESConstants { //NOSONAR
    private OpenSLESConstants() { //NOSONAR
        // Empty constructor // NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Minimum volume level in millibel (mb). // NOSONAR
     */ // NOSONAR
    public static final short SL_MILLIBEL_MIN = -9600; //NOSONAR
    /** // NOSONAR
     * This value is used when equalizer setting is not defined. // NOSONAR
     */ // NOSONAR
    public static final short SL_EQUALIZER_UNDEFINED = (short) 0xFFFF; //NOSONAR

    /** // NOSONAR
     * The minimum bass boost strength in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short BASSBOOST_MIN_STRENGTH = 0; //NOSONAR
    /** // NOSONAR
     * The maximum bass boost strength in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short BASSBOOST_MAX_STRENGTH = 1000; //NOSONAR

    /** // NOSONAR
     * The minimum reverb room level in mb. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MIN_ROOM_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /** // NOSONAR
     * The maximum reverb room level in mb. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MAX_ROOM_LEVEL = 0; //NOSONAR
    /** // NOSONAR
     * The minimum reverb room HF level in mb. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MIN_ROOM_HF_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /** // NOSONAR
     * The maximum reverb room HF level in mb. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MAX_ROOM_HF_LEVEL = 0; //NOSONAR
    /** // NOSONAR
     * The minimum reverb decay time in ms. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MIN_DECAY_TIME = 100; //NOSONAR
    /** // NOSONAR
     * The maximum reverb decay time in ms. // NOSONAR
     */ // NOSONAR
    // XXX: OpenSL ES is normally 20000 but can only support 7000 for now // NOSONAR
    public static final short REVERB_MAX_DECAY_TIME = 7000; //NOSONAR
    /** // NOSONAR
     * The minimum reverb decay HF ratio in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MIN_DECAY_HF_RATIO = 100; //NOSONAR
    /** // NOSONAR
     * The maximum reverb decay HF ratio in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MAX_DECAY_HF_RATIO = 2000; //NOSONAR
    /** // NOSONAR
     * The minimum reverb level in mb. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MIN_REVERB_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /** // NOSONAR
     * The maximum reverb level in mb. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MAX_REVERB_LEVEL = 2000; //NOSONAR
    /** // NOSONAR
     * The minimum reverb diffusion in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MIN_DIFFUSION = 0; //NOSONAR
    /** // NOSONAR
     * The maximum reverb diffusion in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MAX_DIFFUSION = 1000; //NOSONAR
    /** // NOSONAR
     * The minimum reverb density in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MIN_DENSITY = 0; //NOSONAR
    /** // NOSONAR
     * The maximum reverb density in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short REVERB_MAX_DENSITY = 1000; //NOSONAR

    /** // NOSONAR
     * The minimum virtualizer strength in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short VIRTUALIZER_MIN_STRENGTH = 0; //NOSONAR
    /** // NOSONAR
     * The maximum virtualizer strength in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short VIRTUALIZER_MAX_STRENGTH = 1000; //NOSONAR

    /** // NOSONAR
     * The minimum volume effect level in millibel (mb). // NOSONAR
     */ // NOSONAR
    public static final short VOLUME_MIN_LEVEL = SL_MILLIBEL_MIN; //NOSONAR
    /** // NOSONAR
     * The minimum volume stereo position in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short VOLUME_MIN_STEREO_POSITION = -1000; //NOSONAR
    /** // NOSONAR
     * The maximum volume stereo position in o/oo. // NOSONAR
     */ // NOSONAR
    public static final short VOLUME_MAX_STEREO_POSITION = 1000; //NOSONAR
} // NOSONAR
