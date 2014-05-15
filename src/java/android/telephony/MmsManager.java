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
     * @param sentIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is successfully sent, or failed
     * @param deliveryIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is delivered to the recipient
     * @param readIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is read by the recipient
     */
    public void sendMessage(byte[] pdu, PendingIntent sentIntent, PendingIntent deliveryIntent,
            PendingIntent readIntent) {
        if (pdu == null || pdu.length == 0) {
            throw new IllegalArgumentException("Empty or zero length PDU");
        }
        try {
            final IMms iMms = IMms.Stub.asInterface(ServiceManager.getService(SERVICE));
            if (iMms == null) {
                Log.e(LOG_TAG, "Can not find Mms service");
                return;
            }
            iMms.sendMessage(pdu, sentIntent, deliveryIntent, readIntent);
        } catch (RemoteException e) {
            // Ignore it
        }
    }

    /**
     * Download an MMS message from carrier by a given location URL
     *
     * @param locationUrl the location URL of the MMS message to be downloaded, usually obtained
     *  from the MMS WAP push notification
     * @param transactionId the transaction ID of the MMS message, usually obtained from the
     *  MMS WAP push notification
     * @param downloadedIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is downloaded, or the download is failed
     */
    public void downloadMessage(String locationUrl, String transactionId,
            PendingIntent downloadedIntent) {
        if (TextUtils.isEmpty(locationUrl)) {
            throw new IllegalArgumentException("Empty MMS location URL");
        }
        try {
            final IMms iMms = IMms.Stub.asInterface(ServiceManager.getService(SERVICE));
            if (iMms != null) {
                Log.e(LOG_TAG, "Can not find Mms service");
                return;
            }
            iMms.downloadMessage(locationUrl, transactionId, downloadedIntent);
        } catch (RemoteException e) {
            // Ignore it
        }
    }
}
