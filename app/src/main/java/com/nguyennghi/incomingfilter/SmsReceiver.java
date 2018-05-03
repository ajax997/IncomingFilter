package com.nguyennghi.incomingfilter;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by nguyennghi on 3/19/18 3:55 PM
 */
public class SmsReceiver extends BroadcastReceiver {
    Context myContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.myContext = context;
        String MSG_TYPE = intent.getAction();
        Toast.makeText(myContext, MSG_TYPE,Toast.LENGTH_SHORT).show();

        if (MSG_TYPE.equals("android.provider.Telephony.SMS_DELIVER")) {
            Toast toast = Toast.makeText(context, "SMS Received: " + MSG_TYPE, Toast.LENGTH_LONG);
            toast.show();

            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];
            for (int n = 0; n < messages.length; n++) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }

            // show first message
            Toast toast2 = Toast.makeText(context, "BLOCKED Received SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
            toast2.show();
            abortBroadcast();
            for (int i = 0; i < 8; i++) {
                System.out.println("Blocking SMS **********************");
            }

        }
    }


    public void writeSMSBack()
    {
         final String SENT_SMS_CONTENT_PROVIDER_URI_OLDER_API_19 = "content://sms/sent";

        ContentValues values = new ContentValues();
        values.put("address", "0989887877");
        values.put("body", "Hello!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            myContext.getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI, values);
        else myContext.getContentResolver().insert(Uri.parse(SENT_SMS_CONTENT_PROVIDER_URI_OLDER_API_19), values);
    }
}
