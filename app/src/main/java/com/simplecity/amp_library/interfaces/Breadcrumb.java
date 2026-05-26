package com.simplecity.amp_library.interfaces;

/**
 * An interface that defines the breadcrumb operations
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public interface Breadcrumb {

    /**
     * Changes the path of the breadcrumb
     *
     * @param newPath The new path
     */
    void changeBreadcrumbPath(final String newPath);

    /**
     * Adds a new breadcrumb listener.
     *
     * @param listener The breadcrumb listener to add
     */
    void addBreadcrumbListener(BreadcrumbListener listener);

    /**
     * Sdds an active breadcrumb listener.
     *
     * @param listener The breadcrumb listener to remove
     */
    void removeBreadcrumbListener(BreadcrumbListener listener);

    void setTextColor(int textColor);
}
