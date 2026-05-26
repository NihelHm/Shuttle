package com.afollestad.aesthetic; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.annotation.RestrictTo; // NOSONAR
import android.support.v4.view.LayoutInflaterFactory; // NOSONAR
import android.util.AttributeSet; // NOSONAR
import android.view.View; // NOSONAR
import android.widget.LinearLayout; // NOSONAR

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP; // NOSONAR
import static com.afollestad.aesthetic.Util.resolveResId; // NOSONAR

/** @author Aidan Follestad (afollestad) */ // NOSONAR
@RestrictTo(LIBRARY_GROUP) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
final class InflationInterceptor implements LayoutInflaterFactory { //NOSONAR

  @Override //NOSONAR
  public View onCreateView(View parent, final String name, Context context, AttributeSet attrs) { //NOSONAR
    View view = null; //NOSONAR
    final int viewId = resolveResId(context, attrs, android.R.attr.id); //NOSONAR

    switch (name) { //NOSONAR
      case "ImageView": //NOSONAR
      case "android.support.v7.widget.AppCompatImageView": //NOSONAR
        view = new AestheticImageView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "ImageButton": //NOSONAR
      case "android.support.v7.widget.AppCompatImageButton": //NOSONAR
        view = new AestheticImageButton(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v4.widget.DrawerLayout": //NOSONAR
        view = new AestheticDrawerLayout(context, attrs); //NOSONAR
        break; //NOSONAR
      case "Toolbar": //NOSONAR
      case "android.support.v7.widget.Toolbar": //NOSONAR
        view = new AestheticToolbar(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v7.widget.AppCompatTextView": //NOSONAR
      case "TextView": //NOSONAR
        if (viewId == R.id.snackbar_text) { //NOSONAR
          view = null; //NOSONAR
        } else { //NOSONAR
          view = new AestheticTextView(context, attrs); //NOSONAR
          if (parent instanceof LinearLayout && view.getId() == android.R.id.message) { //NOSONAR
            // This is for a toast message // NOSONAR
            view = null; //NOSONAR
          } // NOSONAR
        } // NOSONAR
        break; //NOSONAR
      case "Button": //NOSONAR
      case "android.support.v7.widget.AppCompatButton": //NOSONAR
        if (viewId == android.R.id.button1 //NOSONAR
            || viewId == android.R.id.button2 //NOSONAR
            || viewId == android.R.id.button3) { //NOSONAR
          view = new AestheticDialogButton(context, attrs); //NOSONAR
        } else if (viewId == R.id.snackbar_action) { //NOSONAR
          view = new AestheticSnackBarButton(context, attrs); //NOSONAR
        } else { //NOSONAR
          view = new AestheticButton(context, attrs); //NOSONAR
        } // NOSONAR
        break; //NOSONAR
      case "android.support.v7.widget.AppCompatCheckBox": //NOSONAR
      case "CheckBox": //NOSONAR
        view = new AestheticCheckBox(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v7.widget.AppCompatRadioButton": //NOSONAR
      case "RadioButton": //NOSONAR
        view = new AestheticRadioButton(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v7.widget.AppCompatEditText": //NOSONAR
      case "EditText": //NOSONAR
        view = new AestheticEditText(context, attrs); //NOSONAR
        break; //NOSONAR
      case "Switch": //NOSONAR
        view = new AestheticSwitch(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v7.widget.SwitchCompat": //NOSONAR
        view = new AestheticSwitchCompat(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v7.widget.AppCompatSeekBar": //NOSONAR
      case "SeekBar": //NOSONAR
        view = new AestheticSeekBar(context, attrs); //NOSONAR
        break; //NOSONAR
      case "ProgressBar": //NOSONAR
      case "me.zhanghai.android.materialprogressbar.MaterialProgressBar": //NOSONAR
        view = new AestheticProgressBar(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v7.view.menu.ActionMenuItemView": //NOSONAR
        view = new AestheticActionMenuItemView(context, attrs); //NOSONAR
        break; //NOSONAR

      case "android.support.v7.widget.RecyclerView": //NOSONAR
        view = new AestheticRecyclerView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v4.widget.NestedScrollView": //NOSONAR
        view = new AestheticNestedScrollView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "ListView": //NOSONAR
        view = new AestheticListView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "ScrollView": //NOSONAR
        view = new AestheticScrollView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.v4.view.ViewPager": //NOSONAR
        view = new AestheticViewPager(context, attrs); //NOSONAR
        break; //NOSONAR

      case "Spinner": //NOSONAR
      case "android.support.v7.widget.AppCompatSpinner": //NOSONAR
        view = new AestheticSpinner(context, attrs); //NOSONAR
        break; //NOSONAR

      case "android.support.design.widget.TextInputLayout": //NOSONAR
        view = new AestheticTextInputLayout(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.design.widget.TextInputEditText": //NOSONAR
        view = new AestheticTextInputEditText(context, attrs); //NOSONAR
        break; //NOSONAR

      case "android.support.v7.widget.CardView": //NOSONAR
        view = new AestheticCardView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.design.widget.TabLayout": //NOSONAR
        view = new AestheticTabLayout(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.design.widget.NavigationView": //NOSONAR
        view = new AestheticNavigationView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.design.widget.BottomNavigationView": //NOSONAR
        view = new AestheticBottomNavigationView(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.design.widget.FloatingActionButton": //NOSONAR
        view = new AestheticFab(context, attrs); //NOSONAR
        break; //NOSONAR
      case "android.support.design.widget.CoordinatorLayout": //NOSONAR
        view = new AestheticCoordinatorLayout(context, attrs); //NOSONAR
        break; //NOSONAR
    } // NOSONAR

    if (view != null && view.getTag() != null && ":aesthetic_ignore".equals(view.getTag())) { //NOSONAR
      // Set view back to null so we can let AppCompat handle this view instead. // NOSONAR
      view = null; //NOSONAR
    } // NOSONAR

    return view; //NOSONAR
  } // NOSONAR
} // NOSONAR
