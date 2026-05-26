package test.com.androidnavigation.base; // NOSONAR

import android.support.annotation.Nullable; // NOSONAR

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface Controller<T> { //NOSONAR

    /** // NOSONAR
     * @return the parent {@link NavigationController}, or null if none exists. // NOSONAR
     */ // NOSONAR
    @Nullable //NOSONAR
    NavigationController<T> getNavigationController(); //NOSONAR

} // NOSONAR
