package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.v7.widget.AppCompatTextView; // NOSONAR
import android.util.AttributeSet; // NOSONAR

/** // NOSONAR
 * A class that represents a breadcrumb item // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BreadcrumbItem extends AppCompatTextView { //NOSONAR

    private String mItemPath; //NOSONAR

    /** // NOSONAR
     * Constructor of <code>BreadcrumbItem</code> // NOSONAR
     */ // NOSONAR
    public BreadcrumbItem(Context context) { //NOSONAR
        super(context); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Constructor of <code>BreadcrumbItem</code> // NOSONAR
     * // NOSONAR
     * @param attrs The attributes of the XML tag that is inflating the view // NOSONAR
     */ // NOSONAR
    public BreadcrumbItem(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Constructor of <code>BreadcrumbItem</code> // NOSONAR
     * // NOSONAR
     * @param context The current context // NOSONAR
     * @param attrs The attributes of the XML tag that is inflating the view. // NOSONAR
     * @param defStyle The default style to apply to this view. If 0, no style // NOSONAR
     * will be applied (beyond what is included in the theme). This may // NOSONAR
     * either be an attribute resource, whose value will be retrieved // NOSONAR
     * from the current theme, or an explicit style resource. // NOSONAR
     */ // NOSONAR
    public BreadcrumbItem(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Returns the item path associated with with this breadcrumb item // NOSONAR
     * // NOSONAR
     * @return String The item path associated // NOSONAR
     */ // NOSONAR
    public String getItemPath() { //NOSONAR
        return this.mItemPath; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Sets the item path associated with with this breadcrumb item // NOSONAR
     * // NOSONAR
     * @param itemPath The item path // NOSONAR
     */ // NOSONAR
    protected void setItemPath(String itemPath) { //NOSONAR
        this.mItemPath = itemPath; //NOSONAR
    } // NOSONAR
} // NOSONAR
