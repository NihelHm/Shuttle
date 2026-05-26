/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simplecity.amp_library.androidauto;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.os.Process;
import android.util.Base64;
import android.util.Log;
import com.simplecity.amp_library.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Validates that the calling package is authorized to browse a
 * {@link android.service.media.MediaBrowserService}.
 *
 * The list of allowed signing certificates and their corresponding package names is defined in
 * res/xml/allowed_media_browser_callers.xml.
 *
 * If you add a new valid caller to allowed_media_browser_callers.xml and you don't know
 * its signature, this class will print to logcat (INFO level) a message with the proper base64
 * version of the caller certificate that has not been validated. You can copy from logcat and
 * paste into allowed_media_browser_callers.xml. Spaces and newlines are ignored.
 */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PackageValidator { //NOSONAR
    private static final String TAG = "PackageValidator"; //NOSONAR

    /**
     * Map allowed callers' certificate keys to the expected caller information.
     */
    private final Map<String, ArrayList<CallerInfo>> mValidCertificates; //NOSONAR

    public PackageValidator(Context ctx) { //NOSONAR
        mValidCertificates = readValidCertificates(ctx.getResources().getXml( //NOSONAR
                R.xml.allowed_media_browser_callers)); //NOSONAR
    }

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
                    }
                    Log.v(TAG, String.format("Adding allowed caller: %s package=%s release=%s certificate=%s", info.name, info.packageName, info.release, certificate)); //NOSONAR
                    infos.add(info); //NOSONAR
                }
                eventType = parser.next(); //NOSONAR
            }
        } catch (XmlPullParserException | IOException e) { //NOSONAR
            Log.e(TAG, String.format("%s Could not read allowed callers from XML.", e)); //NOSONAR
        }
        return validCertificates; //NOSONAR
    }

    /**
     * @return false if the caller is not authorized to get data from this MediaBrowserService
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") //NOSONAR
    public boolean isCallerAllowed(Context context, String callingPackage, int callingUid) { //NOSONAR
        // Always allow calls from the framework, self app or development environment.
        if (Process.SYSTEM_UID == callingUid || Process.myUid() == callingUid) { //NOSONAR
            return true; //NOSONAR
        }

        if (isPlatformSigned(context, callingPackage)) { //NOSONAR
            return true; //NOSONAR
        }

        PackageInfo packageInfo = getPackageInfo(context, callingPackage); //NOSONAR
        if (packageInfo == null) { //NOSONAR
            return false; //NOSONAR
        }
        if (packageInfo.signatures.length != 1) { //NOSONAR
            Log.w(TAG, "Caller does not have exactly one signature certificate!"); //NOSONAR
            return false; //NOSONAR
        }
        String signature = Base64.encodeToString( //NOSONAR
                packageInfo.signatures[0].toByteArray(), Base64.NO_WRAP); //NOSONAR

        // Test for known signatures:
        ArrayList<CallerInfo> validCallers = mValidCertificates.get(signature); //NOSONAR
        if (validCallers == null) { //NOSONAR
            Log.v(TAG, "Signature for caller " + callingPackage + " is not valid: \n" + signature); //NOSONAR
            if (mValidCertificates.isEmpty()) { //NOSONAR
                Log.w(TAG, String.format( //NOSONAR
                        "The list of valid certificates is empty. Either your file res/xml/allowed_media_browser_callers.xml is empty or there was an error while reading it. Check previous log messages.")); //NOSONAR
            }
            return false; //NOSONAR
        }

        // Check if the package name is valid for the certificate:
        StringBuffer expectedPackages = new StringBuffer(); //NOSONAR
        for (CallerInfo info : validCallers) { //NOSONAR
            if (callingPackage.equals(info.packageName)) { //NOSONAR
                Log.v(TAG, String.format("Valid caller: %s  package=%s release=%s", info.name, info.packageName, info.release)); //NOSONAR
                return true; //NOSONAR
            }
            expectedPackages.append(info.packageName).append(' '); //NOSONAR
        }

        Log.i(TAG, String.format( //NOSONAR
                "Caller has a valid certificate, but its package doesn't match any expected package for the given certificate. Caller's package is %s. Expected packages as defined in res/xml/allowed_media_browser_callers.xml are (%s). This caller's certificate is: \n%s", //NOSONAR
                callingPackage, expectedPackages, signature)); //NOSONAR

        return false; //NOSONAR
    }

    /**
     * @return true if the installed package signature matches the platform signature.
     */
    private boolean isPlatformSigned(Context context, String pkgName) { //NOSONAR
        PackageInfo platformPackageInfo = getPackageInfo(context, "android"); //NOSONAR

        // Should never happen.
        if (platformPackageInfo == null || platformPackageInfo.signatures == null //NOSONAR
                || platformPackageInfo.signatures.length == 0) { //NOSONAR
            return false; //NOSONAR
        }

        PackageInfo clientPackageInfo = getPackageInfo(context, pkgName); //NOSONAR

        return (clientPackageInfo != null && clientPackageInfo.signatures != null //NOSONAR
                && clientPackageInfo.signatures.length > 0 && //NOSONAR
                platformPackageInfo.signatures[0].equals(clientPackageInfo.signatures[0])); //NOSONAR
    }

    /**
     * @return {@link PackageInfo} for the package name or null if it's not found.
     */
    private PackageInfo getPackageInfo(Context context, String pkgName) { //NOSONAR
        try { //NOSONAR
            final PackageManager pm = context.getPackageManager(); //NOSONAR
            return pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES); //NOSONAR
        } catch (PackageManager.NameNotFoundException e) { //NOSONAR
            Log.w(TAG, String.format("%s Package manager can't find package: %s", e, pkgName)); //NOSONAR
        }
        return null; //NOSONAR
    }

    private final static class CallerInfo { //NOSONAR
        final String name; //NOSONAR
        final String packageName; //NOSONAR
        final boolean release; //NOSONAR

        public CallerInfo(String name, String packageName, boolean release) { //NOSONAR
            this.name = name; //NOSONAR
            this.packageName = packageName; //NOSONAR
            this.release = release; //NOSONAR
        }
    }
}
