package com.simplecity.amp_library.ui.views; // NOSONAR

import android.content.Context; // NOSONAR
import android.graphics.LightingColorFilter; // NOSONAR
import android.text.TextUtils; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.LayoutInflater; // NOSONAR
import android.view.View; // NOSONAR
import android.view.View.OnClickListener; // NOSONAR
import android.view.ViewGroup; // NOSONAR
import android.widget.HorizontalScrollView; // NOSONAR
import android.widget.ImageView; // NOSONAR
import android.widget.RelativeLayout; // NOSONAR
import com.afollestad.aesthetic.Aesthetic; // NOSONAR
import com.afollestad.aesthetic.ViewBackgroundAction; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.interfaces.Breadcrumb; // NOSONAR
import com.simplecity.amp_library.interfaces.BreadcrumbListener; // NOSONAR
import com.simplecity.amp_library.utils.FileHelper; // NOSONAR
import io.reactivex.disposables.Disposable; // NOSONAR
import java.io.File; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.Collections; // NOSONAR
import java.util.List; // NOSONAR

import static com.afollestad.aesthetic.Rx.distinctToMainThread; // NOSONAR
import static com.afollestad.aesthetic.Rx.onErrorLogAndRethrow; // NOSONAR

/** // NOSONAR
 * A view that holds the navigation breadcrumb pattern // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class BreadcrumbView extends RelativeLayout implements Breadcrumb, OnClickListener { //NOSONAR

    HorizontalScrollView mScrollView; //NOSONAR
    private ViewGroup mBreadcrumbBar; //NOSONAR
    private int mTextColor = -1; //NOSONAR

    private List<BreadcrumbListener> mBreadcrumbListeners; //NOSONAR

    private Disposable aestheticDisposable; //NOSONAR

    /** // NOSONAR
     * Constructor of <code>BreadcrumbView</code> // NOSONAR
     */ // NOSONAR
    public BreadcrumbView(Context context) { //NOSONAR
        super(context); //NOSONAR
        init(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Constructor of <code>BreadcrumbView</code> // NOSONAR
     * // NOSONAR
     * @param attrs The attributes of the XML tag that is inflating the view // NOSONAR
     */ // NOSONAR
    public BreadcrumbView(Context context, AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
        init(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Constructor of <code>BreadcrumbView</code> // NOSONAR
     * // NOSONAR
     * @param attrs The attributes of the XML tag that is inflating the view. // NOSONAR
     * @param defStyle The default style to apply to this view. If 0, no style // NOSONAR
     * will be applied (beyond what is included in the theme). This may // NOSONAR
     * either be an attribute resource, whose value will be retrieved // NOSONAR
     * from the current theme, or an explicit style resource. // NOSONAR
     */ // NOSONAR
    public BreadcrumbView(Context context, AttributeSet attrs, int defStyle) { //NOSONAR
        super(context, attrs, defStyle); //NOSONAR
        init(); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Initialises the view. Loads all necessary // NOSONAR
     * information and creates an appropriate layout for the view // NOSONAR
     */ // NOSONAR
    private void init() { //NOSONAR
        //Initialise the listeners // NOSONAR
        this.mBreadcrumbListeners = Collections.synchronizedList(new ArrayList<BreadcrumbListener>()); //NOSONAR

        //Add the view of the breadcrumb // NOSONAR
        addView(inflate(getContext(), R.layout.breadcrumb_view, null)); //NOSONAR

        //Recover all views // NOSONAR
        this.mScrollView = findViewById(R.id.breadcrumb_scrollview); //NOSONAR
        this.mBreadcrumbBar = findViewById(R.id.breadcrumb); //NOSONAR
    } // NOSONAR

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
    } // NOSONAR

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        aestheticDisposable.dispose(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void addBreadcrumbListener(BreadcrumbListener listener) { //NOSONAR
        this.mBreadcrumbListeners.add(listener); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void removeBreadcrumbListener(BreadcrumbListener listener) { //NOSONAR
        this.mBreadcrumbListeners.remove(listener); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void setTextColor(int textColor) { //NOSONAR
        mTextColor = textColor; //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void changeBreadcrumbPath(final String newPath) { //NOSONAR

        if (TextUtils.isEmpty(newPath)) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        //Remove all views // NOSONAR
        this.mBreadcrumbBar.removeAllViews(); //NOSONAR

        this.mBreadcrumbBar.addView(createBreadcrumbItem(new File(FileHelper.ROOT_DIRECTORY))); //NOSONAR

        //Add the rest of the path // NOSONAR
        String[] dirs = newPath.split(File.separator); //NOSONAR
        int cc = dirs.length; //NOSONAR

        for (int i = 1; i < cc; i++) { //NOSONAR
            this.mBreadcrumbBar.addView(createItemDivider()); //NOSONAR
            this.mBreadcrumbBar.addView(createBreadcrumbItem(createFile(dirs, i))); //NOSONAR
        } // NOSONAR

        //Set scrollbar at the end // NOSONAR
        this.mScrollView.post(() -> BreadcrumbView.this.mScrollView.fullScroll(View.FOCUS_RIGHT)); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Creates a new path divider // NOSONAR
     * // NOSONAR
     * @return View divider icon // NOSONAR
     */ // NOSONAR
    private ImageView createItemDivider() { //NOSONAR
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); //NOSONAR
        ImageView imageView = (ImageView) inflater.inflate(R.layout.breadcrumb_item_divider, this.mBreadcrumbBar, false); //NOSONAR
        imageView.setColorFilter(new LightingColorFilter(mTextColor, 0)); //NOSONAR
        return imageView; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Creates a new split path // NOSONAR
     * // NOSONAR
     * @param dir The path // NOSONAR
     * @return BreadcrumbItem The view to create // NOSONAR
     */ // NOSONAR
    private BreadcrumbItem createBreadcrumbItem(File dir) { //NOSONAR
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); //NOSONAR
        BreadcrumbItem item = (BreadcrumbItem) inflater.inflate(R.layout.breadcrumb_item, this.mBreadcrumbBar, false); //NOSONAR
        item.setText(dir.getName().length() != 0 ? dir.getName() : dir.getPath()); //NOSONAR
        item.setItemPath(dir.getPath()); //NOSONAR
        item.setOnClickListener(this); //NOSONAR
        item.setTextColor(mTextColor); //NOSONAR

        return item; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * Creates the a new file reference for a partial breadcrumb item. // NOSONAR
     * // NOSONAR
     * @param dirs The split strings directory // NOSONAR
     * @param pos The position up to which to create // NOSONAR
     * @return File The file reference // NOSONAR
     */ // NOSONAR
    private File createFile(String[] dirs, int pos) { //NOSONAR
        File parent = new File(FileHelper.ROOT_DIRECTORY); //NOSONAR
        for (int i = 1; i < pos; i++) { //NOSONAR
            parent = new File(parent, dirs[i]); //NOSONAR
        } // NOSONAR
        return new File(parent, dirs[pos]); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onClick(View v) { //NOSONAR
        BreadcrumbItem item = (BreadcrumbItem) v; //NOSONAR
        int cc = this.mBreadcrumbListeners.size(); //NOSONAR
        for (int i = 0; i < cc; i++) { //NOSONAR
            this.mBreadcrumbListeners.get(i).onBreadcrumbItemClick(item); //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
