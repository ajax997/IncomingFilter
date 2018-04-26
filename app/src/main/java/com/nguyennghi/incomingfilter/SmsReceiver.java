package com.nguyennghi.incomingfilter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by nguyennghi on 3/19/18 3:55 PM
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String MSG_TYPE = intent.getAction();
        switch (MSG_TYPE) {
            case "android.provider.Telephony.SMS_RECEIVED": {
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

                break;
            }
            case "android.provider.Telephony.SEND_SMS": {
                Toast toast = Toast.makeText(context, "SMS SENT: " + MSG_TYPE, Toast.LENGTH_LONG);
                toast.show();
                abortBroadcast();
                for (int i = 0; i < 8; i++) {
                    System.out.println("Blocking SMS **********************");
                }

                break;
            }
            default: {

                Toast toast = Toast.makeText(context, "SIN ELSE: " + MSG_TYPE, Toast.LENGTH_LONG);
                toast.show();
                //abortBroadcast();
                for (int i = 0; i < 8; i++) {
                    System.out.println("Blocking SMS **********************");
                }

                break;
            }
        }

    }

}
