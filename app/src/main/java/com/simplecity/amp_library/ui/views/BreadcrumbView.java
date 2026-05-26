package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.ViewBackgroundAction;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.interfaces.Breadcrumb;
import com.simplecity.amp_library.interfaces.BreadcrumbListener;
import com.simplecity.amp_library.utils.FileHelper;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.afollestad.aesthetic.Rx.distinctToMainThread;
import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow;

/**
 * A view that holds the navigation breadcrumb pattern
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BreadcrumbView extends RelativeLayout implements Breadcrumb, OnClickListener { //NOSONAR

    HorizontalScrollView mScrollView; //NOSONAR
    private ViewGroup mBreadcrumbBar; //NOSONAR
    private int mTextColor = -1; //NOSONAR

    private List<BreadcrumbListener> mBreadcrumbListeners; //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    /**
     * Constructor of <code>BreadcrumbView</code>
     */
    public BreadcrumbView(Context context) { //NOSONAR
        super(context); //NOSONAR
        init(); //NOSONAR
    }

    /**
     * Constructor of <code>BreadcrumbView</code>
     *
     * @param attrs The attributes of the XML tag that is inflating the view
     */
    public BreadcrumbView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
        init(); //NOSONAR
    }

    /**
     * Constructor of <code>BreadcrumbView</code>
     *
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view. If 0, no style
     * will be applied (beyond what is included in the theme). This may
     * either be an attribute resource, whose value will be retrieved
     * from the current theme, or an explicit style resource.
     */
    public BreadcrumbView(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR
        init(); //NOSONAR
    }

    /**
     * Initialises the view. Loads all necessary
     * information and creates an appropriate layout for the view
     */
    private void init() { //NOSONAR
        //Initialise the listeners
        this.mBreadcrumbListeners = Collections.synchronizedList(new ArrayList<BreadcrumbListener>()); //NOSONAR

        //Add the view of the breadcrumb
        addView(inflate(getContext(), R.layout.breadcrumb_view, null)); //NOSONAR

        //Recover all views
        this.mScrollView = findViewById(R.id.breadcrumb_scrollview); //NOSONAR
        this.mBreadcrumbBar = findViewById(R.id.breadcrumb); //NOSONAR
    }

    @Override //NOSONAR
    protected void onAttachedToWindow() { //NOSONAR
        super.onAttachedToWindow(); //NOSONAR

        Aesthetic.get(getContext()) //NOSONAR
                .colorPrimary() //NOSONAR
                .take(1) //NOSONAR
                .subscribe(color -> ViewBackgroundAction.create(this) //NOSONAR
                        .accept(color), onErrorLogAndRethrow()); //NOSONAR

        aestheticDisposable = (Aesthetic.get(getContext()) //NOSONAR
                .colorPrimary() //NOSONAR
                .compose(distinctToMainThread()) //NOSONAR
                .subscribe(color -> ViewBackgroundAction.create(this) //NOSONAR
                        .accept(color), onErrorLogAndRethrow())); //NOSONAR
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    }

    @Override //NOSONAR
    public void addBreadcrumbListener(BreadcrumbListener listener) { //NOSONAR
        this.mBreadcrumbListeners.add(listener); //NOSONAR
    }

    @Override //NOSONAR
    public void removeBreadcrumbListener(BreadcrumbListener listener) { //NOSONAR
        this.mBreadcrumbListeners.remove(listener); //NOSONAR
    }

    @Override //NOSONAR
    public void setTextColor(int textColor) { //NOSONAR
        mTextColor = textColor; //NOSONAR
    }

    @Override //NOSONAR
    public void changeBreadcrumbPath(final String newPath) { //NOSONAR

        if (TextUtils.isEmpty(newPath)) { //NOSONAR
            return; //NOSONAR
        }

        //Remove all views
        this.mBreadcrumbBar.removeAllViews(); //NOSONAR

        this.mBreadcrumbBar.addView(createBreadcrumbItem(new File(FileHelper.ROOT_DIRECTORY))); //NOSONAR

        //Add the rest of the path
        String[] dirs = newPath.split(File.separator); //NOSONAR
        int cc = dirs.length; //NOSONAR

        for (int i = 1; i < cc; i++) { //NOSONAR
            this.mBreadcrumbBar.addView(createItemDivider()); //NOSONAR
            this.mBreadcrumbBar.addView(createBreadcrumbItem(createFile(dirs, i))); //NOSONAR
        }

        //Set scrollbar at the end
        this.mScrollView.post(() -> BreadcrumbView.this.mScrollView.fullScroll(View.FOCUS_RIGHT)); //NOSONAR
    }

    /**
     * Creates a new path divider
     *
     * @return View divider icon
     */
    private ImageView createItemDivider() { //NOSONAR
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); //NOSONAR
        ImageView imageView = (ImageView) inflater.inflate(R.layout.breadcrumb_item_divider, this.mBreadcrumbBar, false); //NOSONAR
        imageView.setColorFilter(new LightingColorFilter(mTextColor, 0)); //NOSONAR
        return imageView; //NOSONAR
    }

    /**
     * Creates a new split path
     *
     * @param dir The path
     * @return BreadcrumbItem The view to create
     */
    private BreadcrumbItem createBreadcrumbItem(File dir) { //NOSONAR
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); //NOSONAR
        BreadcrumbItem item = (BreadcrumbItem) inflater.inflate(R.layout.breadcrumb_item, this.mBreadcrumbBar, false); //NOSONAR
        item.setText(dir.getName().length() != 0 ? dir.getName() : dir.getPath()); //NOSONAR
        item.setItemPath(dir.getPath()); //NOSONAR
        item.setOnClickListener(this); //NOSONAR
        item.setTextColor(mTextColor); //NOSONAR

        return item; //NOSONAR
    }

    /**
     * Creates the a new file reference for a partial breadcrumb item.
     *
     * @param dirs The split strings directory
     * @param pos The position up to which to create
     * @return File The file reference
     */
    private File createFile(String[] dirs, int pos) { //NOSONAR
        File parent = new File(FileHelper.ROOT_DIRECTORY); //NOSONAR
        for (int i = 1; i < pos; i++) { //NOSONAR
            parent = new File(parent, dirs[i]); //NOSONAR
        }
        return new File(parent, dirs[pos]); //NOSONAR
    }

    @Override //NOSONAR
    public void onClick(View v) { //NOSONAR
        BreadcrumbItem item = (BreadcrumbItem) v; //NOSONAR
        int cc = this.mBreadcrumbListeners.size(); //NOSONAR
        for (int i = 0; i < cc; i++) { //NOSONAR
            this.mBreadcrumbListeners.get(i).onBreadcrumbItemClick(item); //NOSONAR
        }
    }
}
