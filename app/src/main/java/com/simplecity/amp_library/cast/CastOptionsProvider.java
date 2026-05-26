
package com.simplecity.amp_library.cast;

import android.content.Context;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.simplecity.amp_library.constants.Config;
import java.util.List;

/**
 * Specify receiver application ID for cast
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class CastOptionsProvider implements OptionsProvider { //NOSONAR

    @Override //NOSONAR
    public CastOptions getCastOptions(Context context) { //NOSONAR
        return new CastOptions.Builder() //NOSONAR
                .setReceiverApplicationId(Config.CHROMECAST_APP_ID) //NOSONAR
                .build(); //NOSONAR
    }

    @Override //NOSONAR
    public List<SessionProvider> getAdditionalSessionProviders(Context context) { //NOSONAR
        return null; //NOSONAR
    }
}
