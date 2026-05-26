package com.simplecity.amp_library.ui.screens.qcircle; // NOSONAR

import android.annotation.TargetApi; // NOSONAR
import android.content.BroadcastReceiver; // NOSONAR
import android.content.ComponentName; // NOSONAR
import android.content.ContentResolver; // NOSONAR
import android.content.Context; // NOSONAR
import android.content.Intent; // NOSONAR
import android.content.IntentFilter; // NOSONAR
import android.os.Build; // NOSONAR
import android.os.Bundle; // NOSONAR
import android.os.IBinder; // NOSONAR
import android.provider.Settings; // NOSONAR
import android.util.Log; // NOSONAR
import android.view.View; // NOSONAR
import android.view.Window; // NOSONAR
import android.view.WindowManager; // NOSONAR
import android.widget.ImageButton; // NOSONAR
import android.widget.RelativeLayout; // NOSONAR
import android.widget.TextView; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import com.simplecity.amp_library.model.Song; // NOSONAR
import com.simplecity.amp_library.playback.MediaManager; // NOSONAR
import com.simplecity.amp_library.playback.constants.InternalIntents; // NOSONAR
import com.simplecity.amp_library.ui.common.BaseActivity; // NOSONAR
import com.simplecity.amp_library.ui.screens.main.MainActivity; // NOSONAR
import com.simplecity.amp_library.utils.MusicServiceConnectionUtils; // NOSONAR
import dagger.android.AndroidInjection; // NOSONAR
import javax.inject.Inject; // NOSONAR

//To do later: Reapply themes // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class QCircleActivity extends BaseActivity { //NOSONAR

    // [START]declared in LGIntent.java of LG Framework // NOSONAR
    public static final int EXTRA_ACCESSORY_COVER_OPENED = 0; //NOSONAR
    public static final int EXTRA_ACCESSORY_COVER_CLOSED = 1; //NOSONAR
    public static final String EXTRA_ACCESSORY_COVER_STATE = "com.lge.intent.extra.ACCESSORY_COVER_STATE"; //NOSONAR
    public static final String ACTION_ACCESSORY_COVER_EVENT = "com.lge.android.intent.action.ACCESSORY_COVER_EVENT"; //NOSONAR
    // [END]declared in LGIntent.java of LG Framework // NOSONAR

    // [START] QuickCover Settings DB // NOSONAR
    public static final String QUICKCOVERSETTINGS_QUICKCOVER_ENABLE = "quick_view_enable"; //NOSONAR
    // [END] QuickCover Settings DB // NOSONAR

    // [START] QuickCircle info. // NOSONAR
    static boolean quickCircleEnabled = false; //NOSONAR
    int circleWidth = 0; //NOSONAR
    int circleHeight = 0; //NOSONAR
    int circleXpos = 0; //NOSONAR
    int circleYpos = 0; //NOSONAR
    int circleDiameter = 0; //NOSONAR
    // [END] QuickCircle info. // NOSONAR

    // ------------------------------------------------------------------------------- // NOSONAR
    private final boolean DEBUG = true; //NOSONAR
    private final String TAG = "QCircleActivity"; //NOSONAR
    int mQuickCoverState = 0; //NOSONAR
    Context mContext; //NOSONAR
    private Window win = null; //NOSONAR
    private ContentResolver contentResolver = null; //NOSONAR

    //For buttons // NOSONAR
    ImageButton backBtn = null; //NOSONAR
    ImageButton skipBtn = null; //NOSONAR
    ImageButton prevBtn = null; //NOSONAR
    ImageButton pauseBtn = null; //NOSONAR

    TextView textOne; //NOSONAR
    TextView textTwo; //NOSONAR

    @Inject //NOSONAR
    MediaManager mediaManager; //NOSONAR

    @Override //NOSONAR
    protected void onCreate(Bundle savedInstanceState) { //NOSONAR
        AndroidInjection.inject(this); //NOSONAR
        super.onCreate(savedInstanceState); //NOSONAR

        setContentView(R.layout.activity_qcircle); //NOSONAR

        //Retrieve a view for the QuickCircle window. // NOSONAR
        final View circlemainView = findViewById(R.id.cover_main_view); //NOSONAR

        //Set QR images for the image view. // NOSONAR
        //setQrImage(); // NOSONAR

        //Get application context // NOSONAR
        mContext = getApplicationContext(); //NOSONAR

        //Get content resolver // NOSONAR
        contentResolver = getContentResolver(); //NOSONAR

        //Register an IntentFilter and a broadcast receiver // NOSONAR
        registerIntentReceiver(); //NOSONAR

        //Set window flags // NOSONAR
        setQuickCircleWindowParam(); //NOSONAR

        //Get QuickCircle window information // NOSONAR
        initializeViewInformationFromDB(); //NOSONAR

        //Initialize buttons // NOSONAR
        initButtons(); //NOSONAR
        initTextViews(); //NOSONAR
        initializeBackButton(); //NOSONAR

        //Crops a layout for the QuickCircle window // NOSONAR
        setCircleLayoutParam(circlemainView); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onStart() { //NOSONAR
        super.onStart(); //NOSONAR

        final IntentFilter filter = new IntentFilter(); //NOSONAR
        filter.addAction(InternalIntents.PLAY_STATE_CHANGED); //NOSONAR
        filter.addAction(InternalIntents.META_CHANGED); //NOSONAR
        registerReceiver(mStatusListener, new IntentFilter(filter)); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    public void onStop() { //NOSONAR

        unregisterReceiver(mStatusListener); //NOSONAR
        super.onStop(); //NOSONAR
    } // NOSONAR

    @Override //NOSONAR
    protected void onDestroy() { //NOSONAR
        super.onDestroy(); //NOSONAR
        mContext.unregisterReceiver(mIntentReceiver); //NOSONAR
    } // NOSONAR

    private void registerIntentReceiver() { //NOSONAR

        IntentFilter filter = new IntentFilter(); //NOSONAR
        // Add QCircle intent to the intent filter // NOSONAR
        filter.addAction(ACTION_ACCESSORY_COVER_EVENT); //NOSONAR
        // Register a broadcast receiver with the system // NOSONAR
        mContext.registerReceiver(mIntentReceiver, filter); //NOSONAR
    } // NOSONAR

    void setQuickCircleWindowParam() { //NOSONAR
        win = getWindow(); //NOSONAR
        if (win != null) { //NOSONAR
            // Show the sample application view on top // NOSONAR
            win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //NOSONAR
                    | WindowManager.LayoutParams.FLAG_FULLSCREEN //NOSONAR
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //NOSONAR
        } // NOSONAR
    } // NOSONAR

    void setCircleLayoutParam(View view) { //NOSONAR

        RelativeLayout layout = (RelativeLayout) view; //NOSONAR
        RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) layout.getLayoutParams(); //NOSONAR

        //Set layout size same as a circle window size // NOSONAR
        layoutParam.width = circleDiameter; //NOSONAR
        layoutParam.height = circleDiameter; //NOSONAR

        if (circleXpos < 0) { //NOSONAR

            //Place a layout to the center // NOSONAR
            layoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE); //NOSONAR
        } else { //NOSONAR
            layoutParam.leftMargin = circleXpos; //NOSONAR
        } // NOSONAR
        //Set top margin to the offset // NOSONAR
        layoutParam.topMargin = circleYpos + (circleHeight - circleDiameter) / 2; //NOSONAR
        layout.setLayoutParams(layoutParam); //NOSONAR
    } // NOSONAR

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) //NOSONAR
    void initializeViewInformationFromDB() { //NOSONAR

        Log.d(TAG, "initializeViewInformationFromDB"); //NOSONAR
        if (contentResolver == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Log.d(TAG, "initializeViewInformationFromDB"); //NOSONAR

        //Check the availability of the case // NOSONAR
        quickCircleEnabled = Settings.Global.getInt(contentResolver, //NOSONAR
                QUICKCOVERSETTINGS_QUICKCOVER_ENABLE, 0) == 0; //NOSONAR
        if (DEBUG) { //NOSONAR
            Log.d(TAG, "quickCircleEnabled:" + quickCircleEnabled); //NOSONAR
        } // NOSONAR

        //[START] Get the QuickCircle window information // NOSONAR
        int id = getResources().getIdentifier("config_circle_window_width", "dimen", //NOSONAR
                "com.lge.internal"); //NOSONAR
        circleWidth = getResources().getDimensionPixelSize(id); //NOSONAR
        if (DEBUG) { //NOSONAR
            Log.d(TAG, "circleWidth:" + circleWidth); //NOSONAR
        } // NOSONAR

        id = getResources() //NOSONAR
                .getIdentifier("config_cover_window_height", "dimen", "com.lge.internal"); //NOSONAR
        circleHeight = getResources().getDimensionPixelSize(id); //NOSONAR
        if (DEBUG) { //NOSONAR
            Log.d(TAG, "circleHeight:" + circleHeight); //NOSONAR
        } // NOSONAR

        id = getResources() //NOSONAR
                .getIdentifier("config_circle_window_x_pos", "dimen", "com.lge.internal"); //NOSONAR
        circleXpos = getResources().getDimensionPixelSize(id); //NOSONAR
        if (DEBUG) { //NOSONAR
            Log.d(TAG, "circleXpos:" + circleXpos); //NOSONAR
        } // NOSONAR

        id = getResources() //NOSONAR
                .getIdentifier("config_circle_window_y_pos", "dimen", "com.lge.internal"); //NOSONAR
        circleYpos = getResources().getDimensionPixelSize(id); //NOSONAR
        if (DEBUG) { //NOSONAR
            Log.d(TAG, "circleYpos:" + circleYpos); //NOSONAR
        } // NOSONAR

        id = getResources().getIdentifier("config_circle_diameter", "dimen", "com.lge.internal"); //NOSONAR
        circleDiameter = getResources().getDimensionPixelSize(id); //NOSONAR
        if (DEBUG) { //NOSONAR
            Log.d(TAG, "circleDiameter:" + circleDiameter); //NOSONAR
        } // NOSONAR
        //[END] // NOSONAR
    } // NOSONAR

    private void initButtons() { //NOSONAR

        prevBtn = findViewById(R.id.btn_prev); //NOSONAR
        skipBtn = findViewById(R.id.btn_skip); //NOSONAR
        pauseBtn = findViewById(R.id.btn_pause); //NOSONAR
        setPauseButtonImage(); //NOSONAR

        prevBtn.setOnClickListener(v -> mediaManager.previous(false)); //NOSONAR

        skipBtn.setOnClickListener(v -> mediaManager.next()); //NOSONAR

        pauseBtn.setOnClickListener(v -> { //NOSONAR
            mediaManager.togglePlayback(); //NOSONAR
            setPauseButtonImage(); //NOSONAR
        }); // NOSONAR
    } // NOSONAR

    public void initTextViews() { //NOSONAR
        textOne = findViewById(R.id.text1); //NOSONAR
        textTwo = findViewById(R.id.text2); //NOSONAR
    } // NOSONAR

    public void setPauseButtonImage() { //NOSONAR

        if (pauseBtn == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR
        if (MusicServiceConnectionUtils.serviceBinder != null && mediaManager.isPlaying()) { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } else { //NOSONAR
            // Intentionally left empty. // NOSONAR
        } // NOSONAR
    } // NOSONAR

    private void initializeBackButton() { //NOSONAR
        backBtn = findViewById(R.id.back_btn); //NOSONAR
        backBtn.setOnClickListener(v -> QCircleActivity.this.finish()); //NOSONAR
    } // NOSONAR

    void updateTrackInfo() { //NOSONAR
        if (textOne == null || textTwo == null) { //NOSONAR
            return; //NOSONAR
        } // NOSONAR

        Song song = mediaManager.getSong(); //NOSONAR
        if (song == null) return; //NOSONAR

        textOne.setText(song.albumArtistName); //NOSONAR
        textTwo.setText(song.name); //NOSONAR
    } // NOSONAR

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { //NOSONAR
        @Override //NOSONAR
        public void onReceive(Context context, Intent intent) { //NOSONAR

            String action = intent.getAction(); //NOSONAR
            if (action == null) { //NOSONAR
                return; //NOSONAR
            } // NOSONAR

            //Receives a LG QCirle intent for the cover event // NOSONAR
            if (ACTION_ACCESSORY_COVER_EVENT.equals(action)) { //NOSONAR

                if (DEBUG) { //NOSONAR
                    Log.d(TAG, "ACTION_ACCESSORY_COVER_EVENT"); //NOSONAR
                } // NOSONAR

                //Gets the current state of the cover // NOSONAR
                mQuickCoverState = intent.getIntExtra(EXTRA_ACCESSORY_COVER_STATE, //NOSONAR
                        EXTRA_ACCESSORY_COVER_OPENED); //NOSONAR

                if (DEBUG) { //NOSONAR
                    Log.d(TAG, "mQuickCoverState:" + mQuickCoverState); //NOSONAR
                } // NOSONAR

                if (mQuickCoverState == EXTRA_ACCESSORY_COVER_CLOSED) { // closed //NOSONAR
                    //Set window flags // NOSONAR
                    setQuickCircleWindowParam(); //NOSONAR
                } else if (mQuickCoverState == EXTRA_ACCESSORY_COVER_OPENED) { // opened //NOSONAR
                    //Call FullScreenActivity // NOSONAR
                    Intent callFullscreen = new Intent(mContext, MainActivity.class); //NOSONAR
                    startActivity(callFullscreen); //NOSONAR

                    //Finish QCircleActivity // NOSONAR
                    QCircleActivity.this.finish(); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    }; // NOSONAR

    @Override //NOSONAR
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) { //NOSONAR
        super.onServiceConnected(componentName, iBinder); //NOSONAR
        updateTrackInfo(); //NOSONAR
    } // NOSONAR

    private final BroadcastReceiver mStatusListener = new BroadcastReceiver() { //NOSONAR
        @Override //NOSONAR
        public void onReceive(Context context, Intent intent) { //NOSONAR

            final String action = intent.getAction(); //NOSONAR
            if (action != null) { //NOSONAR
                if (action.equals(InternalIntents.META_CHANGED)) { //NOSONAR
                    updateTrackInfo(); //NOSONAR
                    setPauseButtonImage(); //NOSONAR
                } else if (action.equals(InternalIntents.PLAY_STATE_CHANGED)) { //NOSONAR
                    setPauseButtonImage(); //NOSONAR
                } // NOSONAR
            } // NOSONAR
        } // NOSONAR
    }; // NOSONAR

    @Override //NOSONAR
    protected String screenName() { //NOSONAR
        return TAG; //NOSONAR
    } // NOSONAR
} // NOSONAR
