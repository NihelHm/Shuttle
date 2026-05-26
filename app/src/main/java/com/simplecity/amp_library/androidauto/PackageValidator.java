/* // NOSONAR
 * Copyright (C) 2014 The Android Open Source Project // NOSONAR
 * // NOSONAR
 * Licensed under the Apache License, Version 2.0 (the "License"); // NOSONAR
 * you may not use this file except in compliance with the License. // NOSONAR
 * You may obtain a copy of the License at // NOSONAR
 * // NOSONAR
 *      http://www.apache.org/licenses/LICENSE-2.0 // NOSONAR
 * // NOSONAR
 * Unless required by applicable law or agreed to in writing, software // NOSONAR
 * distributed under the License is distributed on an "AS IS" BASIS, // NOSONAR
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. // NOSONAR
 * See the License for the specific language governing permissions and // NOSONAR
 * limitations under the License. // NOSONAR
 */ // NOSONAR
package com.simplecity.amp_library.androidauto; // NOSONAR

import android.content.Context; // NOSONAR
import android.content.pm.PackageInfo; // NOSONAR
import android.content.pm.PackageManager; // NOSONAR
import android.content.res.XmlResourceParser; // NOSONAR
import android.os.Process; // NOSONAR
import android.util.Base64; // NOSONAR
import android.util.Log; // NOSONAR
import com.simplecity.amp_library.R; // NOSONAR
import java.io.IOException; // NOSONAR
import java.util.ArrayList; // NOSONAR
import java.util.HashMap; // NOSONAR
import java.util.Map; // NOSONAR
import org.xmlpull.v1.XmlPullParserException; // NOSONAR

/** // NOSONAR
 * Validates that the calling package is authorized to browse a // NOSONAR
 * {@link android.service.media.MediaBrowserService}. // NOSONAR
 * // NOSONAR
 * The list of allowed signing certificates and their corresponding package names is defined in // NOSONAR
 * res/xml/allowed_media_browser_callers.xml. // NOSONAR
 * // NOSONAR
 * If you add a new valid caller to allowed_media_browser_callers.xml and you don't know // NOSONAR
 * its signature, this class will print to logcat (INFO level) a message with the proper base64 // NOSONAR
 * version of the caller certificate that has not been validated. You can copy from logcat and // NOSONAR
 * paste into allowed_media_browser_callers.xml. Spaces and newlines are ignored. // NOSONAR
 */ // NOSONAR
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PackageValidator { //NOSONAR
    private static final String TAG = "PackageValidator"; //NOSONAR

    /** // NOSONAR
     * Map allowed callers' certificate keys to the expected caller information. // NOSONAR
     */ // NOSONAR
    private final Map<String, ArrayList<CallerInfo>> mValidCertificates; //NOSONAR

    public PackageValidator(Context ctx) { //NOSONAR
        mValidCertificates = readValidCertificates(ctx.getResources().getXml( //NOSONAR
                R.xml.allowed_media_browser_callers)); //NOSONAR
    } // NOSONAR

    private Map<String, ArrayList<CallerInfo>> readValidCertificates(XmlResourceParser parser) { //NOSONAR
        HashMap<String, ArrayList<CallerInfo>> validCertificates = new HashMap<>(); //NOSONAR
        try { //NOSONAR
            int eventType = parser.next(); //NOSONAR
            while (eventType != XmlResourceParser.END_DOCUMENT) { //NOSONAR
                if (eventType == XmlResourceParser.START_TAG //NOSONAR
                        && parser.getName().equals("signing_certificate")) { //NOSONAR

                    String name = parser.getAttributeValue(null, "name"); //NOSONAR
                    String packageName = parser.getAttributeValue(null, "package"); //NOSONAR
                    boolean isRelease = parser.getAttributeBooleanValue(null, "release", false); //NOSONAR
                    String certificate = parser.nextText().replaceAll("\\s|\\n", ""); //NOSONAR

                    CallerInfo info = new CallerInfo(name, packageName, isRelease); //NOSONAR

                    ArrayList<CallerInfo> infos = validCertificates.get(certificate); //NOSONAR
                    if (infos == null) { //NOSONAR
                        infos = new ArrayList<>(); //NOSONAR
                        validCertificates.put(certificate, infos); //NOSONAR
                    } // NOSONAR
                    Log.v(TAG, String.format("Adding allowed caller: %s package=%s release=%s certificate=%s", info.name, info.packageName, info.release, certificate)); //NOSONAR
                    infos.add(info); //NOSONAR
                } // NOSONAR
                eventType = parser.next(); //NOSONAR
            } // NOSONAR
        } catch (XmlPullParserException | IOException e) { //NOSONAR
            Log.e(TAG, String.format("%s Could not read allowed callers from XML.", e)); //NOSONAR
        } // NOSONAR
        return validCertificates; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return false if the caller is not authorized to get data from this MediaBrowserService // NOSONAR
     */ // NOSONAR
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") //NOSONAR
    public boolean isCallerAllowed(Context context, String callingPackage, int callingUid) { //NOSONAR
        // Always allow calls from the framework, self app or development environment. // NOSONAR
        if (Process.SYSTEM_UID == callingUid || Process.myUid() == callingUid) { //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        if (isPlatformSigned(context, callingPackage)) { //NOSONAR
            return true; //NOSONAR
        } // NOSONAR

        PackageInfo packageInfo = getPackageInfo(context, callingPackage); //NOSONAR
        if (packageInfo == null) { //NOSONAR
            return false; //NOSONAR
        } // NOSONAR
        if (packageInfo.signatures.length != 1) { //NOSONAR
            Log.w(TAG, "Caller does not have exactly one signature certificate!"); //NOSONAR
            return false; //NOSONAR
        } // NOSONAR
        String signature = Base64.encodeToString( //NOSONAR
                packageInfo.signatures[0].toByteArray(), Base64.NO_WRAP); //NOSONAR

        // Test for known signatures: // NOSONAR
        ArrayList<CallerInfo> validCallers = mValidCertificates.get(signature); //NOSONAR
        if (validCallers == null) { //NOSONAR
            Log.v(TAG, "Signature for caller " + callingPackage + " is not valid: \n" + signature); //NOSONAR
            if (mValidCertificates.isEmpty()) { //NOSONAR
                Log.w(TAG, String.format( //NOSONAR
                        "The list of valid certificates is empty. Either your file res/xml/allowed_media_browser_callers.xml is empty or there was an error while reading it. Check previous log messages.")); //NOSONAR
            } // NOSONAR
            return false; //NOSONAR
        } // NOSONAR

        // Check if the package name is valid for the certificate: // NOSONAR
        StringBuffer expectedPackages = new StringBuffer(); //NOSONAR
        for (CallerInfo info : validCallers) { //NOSONAR
            if (callingPackage.equals(info.packageName)) { //NOSONAR
                Log.v(TAG, String.format("Valid caller: %s  package=%s release=%s", info.name, info.packageName, info.release)); //NOSONAR
                return true; //NOSONAR
            } // NOSONAR
            expectedPackages.append(info.packageName).append(' '); //NOSONAR
        } // NOSONAR

        Log.i(TAG, String.format( //NOSONAR
                "Caller has a valid certificate, but its package doesn't match any expected package for the given certificate. Caller's package is %s. Expected packages as defined in res/xml/allowed_media_browser_callers.xml are (%s). This caller's certificate is: \n%s", //NOSONAR
                callingPackage, expectedPackages, signature)); //NOSONAR

        return false; //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return true if the installed package signature matches the platform signature. // NOSONAR
     */ // NOSONAR
    private boolean isPlatformSigned(Context context, String pkgName) { //NOSONAR
        PackageInfo platformPackageInfo = getPackageInfo(context, "android"); //NOSONAR

        // Should never happen. // NOSONAR
        if (platformPackageInfo == null || platformPackageInfo.signatures == null //NOSONAR
                || platformPackageInfo.signatures.length == 0) { //NOSONAR
            return false; //NOSONAR
        } // NOSONAR

        PackageInfo clientPackageInfo = getPackageInfo(context, pkgName); //NOSONAR

        return (clientPackageInfo != null && clientPackageInfo.signatures != null //NOSONAR
                && clientPackageInfo.signatures.length > 0 && //NOSONAR
                platformPackageInfo.signatures[0].equals(clientPackageInfo.signatures[0])); //NOSONAR
    } // NOSONAR

    /** // NOSONAR
     * @return {@link PackageInfo} for the package name or null if it's not found. // NOSONAR
     */ // NOSONAR
    private PackageInfo getPackageInfo(Context context, String pkgName) { //NOSONAR
        try { //NOSONAR
            final PackageManager pm = context.getPackageManager(); //NOSONAR
            return pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES); //NOSONAR
        } catch (PackageManager.NameNotFoundException e) { //NOSONAR
            Log.w(TAG, String.format("%s Package manager can't find package: %s", e, pkgName)); //NOSONAR
        } // NOSONAR
        return null; //NOSONAR
    } // NOSONAR

    private final static class CallerInfo { //NOSONAR
        final String name; //NOSONAR
        final String packageName; //NOSONAR
        final boolean release; //NOSONAR

        public CallerInfo(String name, String packageName, boolean release) { //NOSONAR
            this.name = name; //NOSONAR
            this.packageName = packageName; //NOSONAR
            this.release = release; //NOSONAR
        } // NOSONAR
    } // NOSONAR
} // NOSONAR
