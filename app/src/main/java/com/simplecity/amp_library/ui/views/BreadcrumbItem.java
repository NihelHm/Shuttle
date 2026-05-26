package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * A class that represents a breadcrumb item
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BreadcrumbItem extends AppCompatTextView { //NOSONAR

    private String mItemPath; //NOSONAR

    /**
     * Constructor of <code>BreadcrumbItem</code>
     */
    public BreadcrumbItem(Context context) { //NOSONAR
        super(context); //NOSONAR
    }

    /**
     * Constructor of <code>BreadcrumbItem</code>
     *
     * @param attrs The attributes of the XML tag that is inflating the view
     */
    public BreadcrumbItem(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
    }

    /**
     * Constructor of <code>BreadcrumbItem</code>
     *
     * @param context The current context
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view. If 0, no style
     * will be applied (beyond what is included in the theme). This may
     * either be an attribute resource, whose value will be retrieved
     * from the current theme, or an explicit style resource.
     */
    public BreadcrumbItem(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR
    }

    /**
     * Returns the item path associated with with this breadcrumb item
     *
     * @return String The item path associated
     */
    public String getItemPath() { //NOSONAR
        return this.mItemPath; //NOSONAR
    }

    /**
     * Sets the item path associated with with this breadcrumb item
     *
     * @param itemPath The item path
     */
    protected void setItemPath(String itemPath) { //NOSONAR
        this.mItemPath = itemPath; //NOSONAR
    }
}
