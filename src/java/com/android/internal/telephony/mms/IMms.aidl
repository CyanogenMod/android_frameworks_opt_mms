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

package com.android.internal.telephony.mms;

import android.app.PendingIntent;

/**
 * Service interface to handle MMS API requests
 */
interface IMms {
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
    void sendMessage(in byte[] pdu, in PendingIntent sentIntent, in PendingIntent deliveryIntent,
            in PendingIntent readIntent);

    /**
     * Download an MMS message using known location and transaction id
     *
     * @param location the location URL of the MMS message to be downloaded, usually obtained
     *  from the MMS WAP push notification
     * @param transactionId the transaction ID of the MMS message, usually obtained from the
     *  MMS WAP push notification
     * @param downloadedIntent if not NULL this <code>PendingIntent</code> is
     *  broadcast when the message is downloaded, or the download is failed
     */
    void downloadMessage(String location, String transactionId, in PendingIntent downloadedIntent);
}
