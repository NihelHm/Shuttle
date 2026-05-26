
package com.simplecity.amp_library.cast; // NOSONAR

import android.content.Context; // NOSONAR
import com.google.android.gms.cast.framework.CastOptions; // NOSONAR
import com.google.android.gms.cast.framework.OptionsProvider; // NOSONAR
import com.google.android.gms.cast.framework.SessionProvider; // NOSONAR
import com.simplecity.amp_library.constants.Config; // NOSONAR
import java.util.List; // NOSONAR

/** // NOSONAR
 * Specify receiver application ID for cast // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CastOptionsProvider implements OptionsProvider { //NOSONAR

    @Override //NOSONAR
    public CastOptions getCastOptions(Context context) { //NOSONAR
        return new CastOptions.Builder() //NOSONAR
                .setReceiverApplicationId(Config.CHROMECAST_APP_ID) //NOSONAR
                .build(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public List<SessionProvider> getAdditionalSessionProviders(Context context) { //NOSONAR
        return null; //NOSONAR
    } // NOSONAR
} // NOSONAR
