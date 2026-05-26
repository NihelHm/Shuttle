package com.simplecity.amp_library.interfaces; // NOSONAR

import com.simplecity.amp_library.ui.views.BreadcrumbItem; // NOSONAR

/** // NOSONAR
 * Interface with events from a breadcrumb // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public interface BreadcrumbListener { //NOSONAR
    /** // NOSONAR
     * This method is called when a breadcrumb item is clicked // NOSONAR
     * // NOSONAR
     * @param item The breadcrumb item click // NOSONAR
     */ // NOSONAR
    void onBreadcrumbItemClick(BreadcrumbItem item); //NOSONAR
} // NOSONAR
