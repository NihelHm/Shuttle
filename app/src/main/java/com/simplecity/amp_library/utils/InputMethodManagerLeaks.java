package com.simplecity.amp_library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;

/**
 * Copied from: https://gist.github.com/pyricau/4df64341cc978a7de414
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class InputMethodManagerLeaks { //NOSONAR

    static class ReferenceCleaner //NOSONAR
            implements MessageQueue.IdleHandler, View.OnAttachStateChangeListener, //NOSONAR
            ViewTreeObserver.OnGlobalFocusChangeListener { //NOSONAR

        private final InputMethodManager inputMethodManager; //NOSONAR
        private final Field mHField; //NOSONAR
        private final Field mServedViewField; //NOSONAR
        private final Method finishInputLockedMethod; //NOSONAR

        ReferenceCleaner(InputMethodManager inputMethodManager, Field mHField, Field mServedViewField, //NOSONAR
                Method finishInputLockedMethod) { //NOSONAR
            this.inputMethodManager = inputMethodManager; //NOSONAR
            this.mHField = mHField; //NOSONAR
            this.mServedViewField = mServedViewField; //NOSONAR
            this.finishInputLockedMethod = finishInputLockedMethod; //NOSONAR
        }

        @Override //NOSONAR
        public void onGlobalFocusChanged(View oldFocus, View newFocus) { //NOSONAR
            if (newFocus == null) { //NOSONAR
                return; //NOSONAR
            }
            if (oldFocus != null) { //NOSONAR
                oldFocus.removeOnAttachStateChangeListener(this); //NOSONAR
            }
            Looper.myQueue().removeIdleHandler(this); //NOSONAR
            newFocus.addOnAttachStateChangeListener(this); //NOSONAR
        }

        @Override //NOSONAR
        public void onViewAttachedToWindow(View v) { //NOSONAR
            // Intentionally left empty.
        }

        @Override //NOSONAR
        public void onViewDetachedFromWindow(View v) { //NOSONAR
            v.removeOnAttachStateChangeListener(this); //NOSONAR
            Looper.myQueue().removeIdleHandler(this); //NOSONAR
            Looper.myQueue().addIdleHandler(this); //NOSONAR
        }

        @Override //NOSONAR
        public boolean queueIdle() { //NOSONAR
            clearInputMethodManagerLeak(); //NOSONAR
            return false; //NOSONAR
        }

        private void clearInputMethodManagerLeak() { //NOSONAR
            try { //NOSONAR
                final Object lock = mHField.get(inputMethodManager); //NOSONAR
                if (lock == null) return; //NOSONAR
                // This is highly dependent on the InputMethodManager implementation.
                synchronized (lock) { //NOSONAR
                    View servedView = (View) mServedViewField.get(inputMethodManager); //NOSONAR
                    if (servedView != null) { //NOSONAR

                        boolean servedViewAttached = servedView.getWindowVisibility() != View.GONE; //NOSONAR

                        if (servedViewAttached) { //NOSONAR
                            // The view held by the IMM was replaced without a global focus change. Let's make
                            // sure we get notified when that view detaches.

                            // Avoid double registration.
                            servedView.removeOnAttachStateChangeListener(this); //NOSONAR
                            servedView.addOnAttachStateChangeListener(this); //NOSONAR
                        } else { //NOSONAR
                            // servedView is not attached. InputMethodManager is being stupid!
                            Activity activity = extractActivity(servedView.getContext()); //NOSONAR
                            if (activity == null || activity.getWindow() == null) { //NOSONAR
                                // Unlikely case. Let's finish the input anyways.
                                finishInputLockedMethod.invoke(inputMethodManager); //NOSONAR
                            } else { //NOSONAR
                                View decorView = activity.getWindow().peekDecorView(); //NOSONAR
                                boolean windowAttached = decorView.getWindowVisibility() != View.GONE; //NOSONAR
                                if (!windowAttached) { //NOSONAR
                                    finishInputLockedMethod.invoke(inputMethodManager); //NOSONAR
                                } else { //NOSONAR
                                    decorView.requestFocusFromTouch(); //NOSONAR
                                }
                            }
                        }
                    }
                }
            } catch (Exception unexpected) { //NOSONAR
                Log.e("IMMLeaks", "Unexpected reflection exception", unexpected); //NOSONAR
            }
        }

        private Activity extractActivity(Context context) { //NOSONAR
            while (true) { //NOSONAR
                if (context instanceof Application) { //NOSONAR
                    return null; //NOSONAR
                } else if (context instanceof Activity) { //NOSONAR
                    return (Activity) context; //NOSONAR
                } else if (context instanceof ContextWrapper) { //NOSONAR
                    Context baseContext = ((ContextWrapper) context).getBaseContext(); //NOSONAR
                    // Prevent Stack Overflow.
                    if (baseContext == context) { //NOSONAR
                        return null; //NOSONAR
                    }
                    context = baseContext; //NOSONAR
                } else { //NOSONAR
                    return null; //NOSONAR
                }
            }
        }
    }

    /**
     * Fix for https://code.google.com/p/android/issues/detail?id=171190 .
     * <p>
     * When a view that has focus gets detached, we wait for the main thread to be idle and then
     * check if the InputMethodManager is leaking a view. If yes, we tell it that the decor view got
     * focus, which is what happens if you press home and come back from recent apps. This replaces
     * the reference to the detached view with a reference to the decor view.
     * <p>
     * Should be called from {@link Activity#onCreate(android.os.Bundle)} )}.
     */
    @SuppressLint("PrivateApi") //NOSONAR
    public static void fixFocusedViewLeak(Application application) { //NOSONAR

        // Still not fixed until android 23
        if (SDK_INT < KITKAT || SDK_INT > Build.VERSION_CODES.N_MR1) { //NOSONAR
            return; //NOSONAR
        }

        final InputMethodManager inputMethodManager = //NOSONAR
                (InputMethodManager) application.getSystemService(INPUT_METHOD_SERVICE); //NOSONAR

        final Field mServedViewField; //NOSONAR
        final Field mHField; //NOSONAR
        final Method finishInputLockedMethod; //NOSONAR
        final Method focusInMethod; //NOSONAR
        try { //NOSONAR
            mServedViewField = InputMethodManager.class.getDeclaredField("mServedView"); //NOSONAR
            mServedViewField.setAccessible(true); //NOSONAR
            mHField = InputMethodManager.class.getDeclaredField("mServedView"); //NOSONAR
            mHField.setAccessible(true); //NOSONAR
            finishInputLockedMethod = InputMethodManager.class.getDeclaredMethod("finishInputLocked"); //NOSONAR
            finishInputLockedMethod.setAccessible(true); //NOSONAR
            focusInMethod = InputMethodManager.class.getDeclaredMethod("focusIn", View.class); //NOSONAR
            focusInMethod.setAccessible(true); //NOSONAR
        } catch (NoSuchMethodException | NoSuchFieldException unexpected) { //NOSONAR
            Log.e("IMMLeaks", "Unexpected reflection exception", unexpected); //NOSONAR
            return; //NOSONAR
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { //NOSONAR
            @Override //NOSONAR
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) { //NOSONAR
                // Intentionally left empty.
            }

            @Override //NOSONAR
            public void onActivityStarted(Activity activity) { //NOSONAR
                ReferenceCleaner cleaner = //NOSONAR
                        new ReferenceCleaner(inputMethodManager, mHField, mServedViewField, //NOSONAR
                                finishInputLockedMethod); //NOSONAR
                View rootView = activity.getWindow().getDecorView().getRootView(); //NOSONAR
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver(); //NOSONAR
                viewTreeObserver.addOnGlobalFocusChangeListener(cleaner); //NOSONAR
            }

            @Override //NOSONAR
            public void onActivityResumed(Activity activity) { //NOSONAR
                // Intentionally left empty.
            }

            @Override //NOSONAR
            public void onActivityPaused(Activity activity) { //NOSONAR
                // Intentionally left empty.
            }

            @Override //NOSONAR
            public void onActivityStopped(Activity activity) { //NOSONAR
                // Intentionally left empty.
            }

            @Override //NOSONAR
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) { //NOSONAR
                // Intentionally left empty.
            }

            @Override //NOSONAR
            public void onActivityDestroyed(Activity activity) { //NOSONAR
                // Intentionally left empty.
            }
        });
    }
}
