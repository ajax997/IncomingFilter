package com.nguyennghi.incomingfilter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

/**
 * Created by nguyennghi on 3/11/18 8:01 PM
 */
public class SendMessage {
    public SendMessage()
    {

    }
    public void send(Context context, String phoneNumber, String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("sms_body", message);
//        context.startActivity(intent);
    }
}
