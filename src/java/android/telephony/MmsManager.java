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

package android.telephony;

import com.android.internal.telephony.mms.IMms;

import android.app.PendingIntent;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Manages MMS operations like sending, downloading MMS messages.
 * Get instance of this class by calling the static method {@link #getDefault()}
 *
 * {@hide}
 */
public final class MmsManager {
    private static final String LOG_TAG = "MmsManager";
    private static final String SERVICE = "com.android.internal.telephony.mms.IMms";

    private static final MmsManager sInstance = new MmsManager();

    // MMS send/download failure result codes

    public static final int RESULT_ERROR_UNSPECIFIED = 1;
    public static final int RESULT_ERROR_INVALID_APN = 2;
    public static final int RESULT_ERROR_UNABLE_CONNECT_MMS = 3;
    public static final int RESULT_ERROR_HTTP_FAILURE = 4;

    // Intent extra name for result data
    public static final String EXTRA_DATA = "data";

    /**
     * Get the default instance of the MmsManager
     *
     * @return the default instance of the MmsManager
     */
    public static MmsManager getDefault() {
        return sInstance;
    }

    private MmsManager() {
        // Do nothing
    }

    /**
     * Send an MMS message
     *
     * @param pdu the MMS message encoded in standard MMS PDU format
     * @param locationUrl the optional location url where message should be sent to
     * @param sentIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is successfully sent, or failed
     */
    public void sendMessage(byte[] pdu, String locationUrl, PendingIntent sentIntent) {
        if (pdu == null || pdu.length == 0) {
            throw new IllegalArgumentException("Empty or zero length PDU");
        }
        try {
            final IMms iMms = IMms.Stub.asInterface(ServiceManager.getService(SERVICE));
            if (iMms == null) {
                Log.e(LOG_TAG, "Can not find Mms service");
                return;
            }
            iMms.sendMessage(pdu, locationUrl, sentIntent);
        } catch (RemoteException e) {
            // Ignore it
        }
    }

    /**
     * Download an MMS message from carrier by a given location URL
     *
     * @param locationUrl the location URL of the MMS message to be downloaded, usually obtained
     *  from the MMS WAP push notification
     * @param downloadedIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is downloaded, or the download is failed
     */
    public void downloadMessage(String locationUrl, PendingIntent downloadedIntent) {
        if (TextUtils.isEmpty(locationUrl)) {
            throw new IllegalArgumentException("Empty MMS location URL");
        }
        try {
            final IMms iMms = IMms.Stub.asInterface(ServiceManager.getService(SERVICE));
            if (iMms == null) {
                Log.e(LOG_TAG, "Can not find Mms service");
                return;
            }
            iMms.downloadMessage(locationUrl, downloadedIntent);
        } catch (RemoteException e) {
            // Ignore it
        }
    }
}
