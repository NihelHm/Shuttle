package com.simplecity.amp_library; // NOSONAR

import android.content.Context; // NOSONAR
import android.support.test.InstrumentationRegistry; // NOSONAR
import android.support.test.runner.AndroidJUnit4; // NOSONAR

import org.junit.Test; // NOSONAR
import org.junit.runner.RunWith; // NOSONAR

import static org.junit.Assert.assertTrue; // NOSONAR

/** // NOSONAR
 * Instrumentation test, which will execute on an Android device. // NOSONAR
 * // NOSONAR
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a> // NOSONAR
 */ // NOSONAR
@RunWith(AndroidJUnit4.class) //NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ExampleInstrumentedTest { //NOSONAR
    @Test //NOSONAR
    public void useAppContext() throws Exception { //NOSONAR
        // Context of the app under test. // NOSONAR
        Context appContext = InstrumentationRegistry.getTargetContext(); //NOSONAR
        assertTrue( //NOSONAR
                appContext.getPackageName().contains("com.simplecity.amp_pro") || //NOSONAR
                        appContext.getPackageName().contains("another.music.player") //NOSONAR

        ); // NOSONAR
    } // NOSONAR
} // NOSONAR
