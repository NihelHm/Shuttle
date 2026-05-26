package com.simplecity.amp_library.ui.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.simplecity.amp_library.BuildConfig;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.utils.AnalyticsManager;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static com.simplecity.amp_library.utils.AnimUtils.lerp;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.round;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.toRadians;
import static java.util.concurrent.TimeUnit.SECONDS;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class SnowfallView extends View { //NOSONAR

    private static final String TAG = SnowfallView.class.getSimpleName(); //NOSONAR

    /** The Remote Config key used to determine if it snows */
    private static final String LET_IT_SNOW = "let_it_snow"; //NOSONAR

    /** Forecast a <= 10% chance of snowing */
    private static final float LUCKY = 0.1f; //NOSONAR

    /** Wait a few seconds before displaying snow */
    private static final long SNOWFALL_DELAY = SECONDS.toMillis(5); //NOSONAR

    /** Interval between adding more snow */
    private static final long SNOWFALL_TIME_INCREMENT = SECONDS.toMillis(2); //NOSONAR

    /** The total number of snowflakes to generate */
    private static final int TOTAL_FLAKES = 200; //NOSONAR

    /** The increment with which to generate more snowflakes */
    private static final int FLAKE_INCREMENT = 30; //NOSONAR

    /** Default min and max snowflake alpha */
    private static final int MIN_ALPHA = 100; //NOSONAR
    private static final int MAX_ALPHA = 250; //NOSONAR

    /** Default min and max snowflake angle */
    private static final double MIN_ANGLE = 80d; //NOSONAR
    private static final double MAX_ANGLE = 100d; //NOSONAR

    /** Default min and max snowflake velocity */
    private static final float MIN_SPEED = 2f; //NOSONAR
    private static final float MAX_SPEED = 7f; //NOSONAR

    /** Default min and max snowflake size */
    private static final float MIN_SIZE = 2f; //NOSONAR
    private static final float MAX_SIZE = 15f; //NOSONAR

    /** Used to paint each snowflake */
    private final Paint snowPaint = new Paint(ANTI_ALIAS_FLAG); //NOSONAR
    /** The snowflakes currently falling */
    private final List<Snowflake> snowflakes = new ArrayList<>(0); //NOSONAR
    /** Used to randomly generate snowflake params */
    private final Random snowRng = new SecureRandom(); //NOSONAR
    /** Used to delay snowfall */
    final Handler snowHandler = new Handler(); //NOSONAR

    /** Used to determine if we let it snow */
    @Nullable //NOSONAR
    private FirebaseRemoteConfig remoteConfig = null; //NOSONAR

    public SnowfallView(Context context, @Nullable AttributeSet attrs) { //NOSONAR
        super(context, attrs); //NOSONAR
        snowPaint.setColor(Color.WHITE); //NOSONAR
        snowPaint.setStyle(Paint.Style.FILL); //NOSONAR

        if(!isInEditMode()) { //NOSONAR
            remoteConfig = FirebaseRemoteConfig.getInstance(); //NOSONAR
            remoteConfig.setDefaults(R.xml.remote_config_defaults); //NOSONAR
            remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder() //NOSONAR
                    .setDeveloperModeEnabled(BuildConfig.DEBUG) //NOSONAR
                    .build()); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onDraw(Canvas canvas) { //NOSONAR
        super.onDraw(canvas); //NOSONAR
        if (!snowflakes.isEmpty()) { //NOSONAR
            for (int i = snowflakes.size() - 1; i >= 0; i--) { //NOSONAR
                Snowflake snowflake = snowflakes.get(i); //NOSONAR
                if (round(snowflake.snowY()) > getHeight()) { //NOSONAR
                    if (snowflake.shouldRemove) { //NOSONAR
                        snowflakes.remove(snowflake); //NOSONAR
                    } else { //NOSONAR
                        snowflake.reset(); //NOSONAR
                    }
                }
                snowPaint.setAlpha(snowflake.alpha); //NOSONAR
                canvas.drawCircle(snowflake.snowX(), snowflake.snowY(), snowflake.snowR, snowPaint); //NOSONAR
            }
            invalidate(); //NOSONAR
        }
    }

    @Override //NOSONAR
    protected void onDetachedFromWindow() { //NOSONAR
        clear(); //NOSONAR
        super.onDetachedFromWindow(); //NOSONAR
    }

    public void letItSnow(AnalyticsManager analyticsManager) { //NOSONAR
        if (snowflakes.isEmpty()) { //NOSONAR
            if (lerp(0f, 1f, snowRng.nextFloat()) <= LUCKY) { //NOSONAR
                fetchSnowConfig(analyticsManager); //NOSONAR
            }
        }
    }

    public boolean isSnowing() { //NOSONAR
        return !snowflakes.isEmpty(); //NOSONAR
    }

    public void clear() { //NOSONAR
        snowHandler.removeCallbacksAndMessages(null); //NOSONAR
        snowflakes.clear(); //NOSONAR
        invalidate(); //NOSONAR
    }

    private void fetchSnowConfig(AnalyticsManager analyticsManager) { //NOSONAR
        if (isInEditMode()) { //NOSONAR
            return; //NOSONAR
        }
        remoteConfig.fetch().addOnCompleteListener((Activity) getContext(), task -> { //NOSONAR
            if (task.isSuccessful()) { //NOSONAR
                remoteConfig.activateFetched(); //NOSONAR
                if (remoteConfig.getBoolean(LET_IT_SNOW)) { //NOSONAR
                    snowHandler.removeCallbacksAndMessages(null); //NOSONAR
                    snowHandler.postDelayed(this::generateSnow, SNOWFALL_DELAY); //NOSONAR
                    analyticsManager.didSnow(); //NOSONAR
                }
            }
        });
    }

    void generateSnow() { //NOSONAR
        if (snowflakes.size() <= TOTAL_FLAKES) { //NOSONAR

            int flakesToAdd = Math.min(10 + snowRng.nextInt(FLAKE_INCREMENT), TOTAL_FLAKES - snowflakes.size()); //NOSONAR
            if (flakesToAdd > 0) { //NOSONAR
                addSnow(flakesToAdd); //NOSONAR
                snowHandler.postDelayed(this::generateSnow, SNOWFALL_TIME_INCREMENT); //NOSONAR
            }
        }
    }

    void addSnow(int numFlakes) { //NOSONAR
        for (int i = 0; i < numFlakes; i++) { //NOSONAR
            final double angle = toRadians(lerp(MIN_ANGLE, MAX_ANGLE, snowRng.nextDouble())); //NOSONAR
            final float speed = lerp(MIN_SPEED, MAX_SPEED, snowRng.nextFloat()); //NOSONAR
            final float velX = (float) ((double) speed * cos(angle)); //NOSONAR
            final float velY = (float) ((double) speed * sin(angle)); //NOSONAR
            final float size = lerp(MIN_SIZE, MAX_SIZE, snowRng.nextFloat()); //NOSONAR
            final float startX = lerp(0f, (float) getWidth(), snowRng.nextFloat()); //NOSONAR
            float startY = lerp(0f, (float) getHeight(), snowRng.nextFloat()); //NOSONAR
            startY -= (float) getHeight() - size; //NOSONAR
            final int alpha = (int) lerp((float) MIN_ALPHA, (float) MAX_ALPHA, snowRng.nextFloat()); //NOSONAR
            snowflakes.add(new Snowflake(startX, startY, velX, velY, size, alpha)); //NOSONAR
        }
        invalidate(); //NOSONAR
    }

    public void removeSnow() { //NOSONAR
        if (snowflakes.size() > 0) { //NOSONAR
            snowHandler.removeCallbacksAndMessages(null); //NOSONAR
            for (Snowflake snowflake : snowflakes) { //NOSONAR
                snowflake.shouldRemove = true; //NOSONAR
            }
        }
    }

    static final class Snowflake { //NOSONAR

        float snowX; //NOSONAR
        float snowY; //NOSONAR

        final float startX; //NOSONAR
        final float startY; //NOSONAR
        final float velX; //NOSONAR
        final float velY; //NOSONAR
        final float snowR; //NOSONAR
        final int alpha; //NOSONAR
        boolean shouldRemove; //NOSONAR

        Snowflake(float startX, float startY, float velX, float velY, float snowR, int alpha) { //NOSONAR
            snowX = startX; //NOSONAR
            snowY = startY; //NOSONAR
            this.startX = startX; //NOSONAR
            this.startY = startY; //NOSONAR
            this.velX = velX; //NOSONAR
            this.velY = velY; //NOSONAR
            this.snowR = snowR; //NOSONAR
            this.alpha = alpha; //NOSONAR
        }

        float snowX() { //NOSONAR
            return snowX += velX; //NOSONAR
        }

        float snowY() { //NOSONAR
            return snowY += velY; //NOSONAR
        }

        void reset() { //NOSONAR
            snowX = startX; //NOSONAR
            snowY = startY; //NOSONAR
        }
    }
}
