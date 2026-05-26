package com.simplecity.amp_library.interfaces; // NOSONAR

/** // NOSONAR
 * An interface that defines the breadcrumb operations // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface Breadcrumb { //NOSONAR

    /** // NOSONAR
     * Changes the path of the breadcrumb // NOSONAR
     * // NOSONAR
     * @param newPath The new path // NOSONAR
     */ // NOSONAR
    void changeBreadcrumbPath(final String newPath); //NOSONAR

    /** // NOSONAR
     * Adds a new breadcrumb listener. // NOSONAR
     * // NOSONAR
     * @param listener The breadcrumb listener to add // NOSONAR
     */ // NOSONAR
    void addBreadcrumbListener(BreadcrumbListener listener); //NOSONAR

    /** // NOSONAR
     * Sdds an active breadcrumb listener. // NOSONAR
     * // NOSONAR
     * @param listener The breadcrumb listener to remove // NOSONAR
     */ // NOSONAR
    void removeBreadcrumbListener(BreadcrumbListener listener); //NOSONAR

    void setTextColor(int textColor); //NOSONAR
} // NOSONAR
