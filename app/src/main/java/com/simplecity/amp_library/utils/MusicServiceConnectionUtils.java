package com.simplecity.amp_library.utils;

import android.arch.lifecycle.Lifecycle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.simplecity.amp_library.playback.LocalBinder;
import com.simplecity.amp_library.playback.MusicService;
import com.simplecity.amp_library.rx.UnsafeConsumer;
import java.util.WeakHashMap;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class MusicServiceConnectionUtils { //NOSONAR

    private static final String TAG = "MusicServiceConnectionU"; //NOSONAR

    public static LocalBinder serviceBinder = null; //NOSONAR

    private static final WeakHashMap<Context, ServiceBinder> connectionMap = new WeakHashMap<>(); //NOSONAR

    private MusicServiceConnectionUtils() { //NOSONAR
        // Intentionally left empty.
    }

    public static void bindToService(Lifecycle lifecycle, Context context, AnalyticsManager analyticsManager, ServiceConnection callback, UnsafeConsumer<ServiceToken> tokenCallback) { //NOSONAR
        new ResumingServiceManager(lifecycle, analyticsManager).startService(context, new Intent(context, MusicService.class), () -> { //NOSONAR
            ServiceBinder binder = new ServiceBinder(callback); //NOSONAR
            if (context.bindService(new Intent().setClass(context, MusicService.class), binder, 0)) { //NOSONAR
                connectionMap.put(context, binder); //NOSONAR
                tokenCallback.accept(new ServiceToken(context)); //NOSONAR
            } else { //NOSONAR
                tokenCallback.accept(null); //NOSONAR
            }
            return null; //NOSONAR
        });
    }

    public static void unbindFromService(ServiceToken token) { //NOSONAR
        if (token == null) { //NOSONAR
            return; //NOSONAR
        }
        final Context context = token.context; //NOSONAR
        final ServiceBinder binder = connectionMap.remove(context); //NOSONAR
        if (binder == null) { //NOSONAR
            return; //NOSONAR
        }
        context.unbindService(binder); //NOSONAR
        if (connectionMap.isEmpty()) { //NOSONAR
            serviceBinder = null; //NOSONAR
        }
    }

    public static final class ServiceBinder implements ServiceConnection { //NOSONAR

        private final ServiceConnection callback; //NOSONAR

        ServiceBinder(final ServiceConnection callback) { //NOSONAR
            this.callback = callback; //NOSONAR
        }

        @Override //NOSONAR
        public void onServiceConnected(final ComponentName className, final IBinder service) { //NOSONAR
            serviceBinder = (LocalBinder) service; //NOSONAR

            if (callback != null) { //NOSONAR
                callback.onServiceConnected(className, service); //NOSONAR
            }
        }

        @Override //NOSONAR
        public void onServiceDisconnected(final ComponentName className) { //NOSONAR
            if (callback != null) { //NOSONAR
                callback.onServiceDisconnected(className); //NOSONAR
            }
            serviceBinder = null; //NOSONAR
        }
    }

    public static final class ServiceToken { //NOSONAR

        @SuppressWarnings("java:S1104") //NOSONAR

        public Context context; //NOSONAR

        ServiceToken(final Context context) { //NOSONAR
            this.context = context; //NOSONAR
        }
    }
}
