package com.nguyennghi.incomingfilter;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nguyennghi on 3/19/18 3:55 PM
 */
public class SmsReceiver extends BroadcastReceiver {

    Context myContext;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast t = Toast.makeText(context, "Tricked", Toast.LENGTH_LONG);
        t.show();
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

            Toast toast2 = Toast.makeText(context, "BLOCKED Received SMS: " + smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
            toast2.show();
            String mess_from = smsMessage[0].getDisplayOriginatingAddress();
            String mess_content = smsMessage[0].getMessageBody();

            ArrayList<FilterUnit> filterUnits = Function.getListBlockItems(context);
            for (FilterUnit filterUnit : filterUnits) {
                if (filterUnit.blocking_incoming_mess)
                    switch (filterUnit.getUnitType()) {
                        case NUM:
                            if (filterUnit.num.equals(mess_from))
                             if(filterUnit.mess_auto_sms)
                                 Function.sendSMS(mess_from, filterUnit.auto_text_content);
                            return;

                        case START_NUM:
                            if (mess_from.startsWith(filterUnit.num))
                                if(filterUnit.mess_auto_sms)
                                    Function.sendSMS(mess_from, filterUnit.auto_text_content);
                            return;

                        case END_NUM:
                            if (mess_content.endsWith(filterUnit.num))
                                if(filterUnit.mess_auto_sms)
                                    Function.sendSMS(mess_from, filterUnit.auto_text_content);
                            return;
                        case PROVIDER:
                            if(Function.checkProvider(mess_content, filterUnit.provider))
                                if(filterUnit.mess_auto_sms)
                                    Function.sendSMS(mess_from, filterUnit.auto_text_content);
                            return;

                    }
            }

            Function.writeSMSBack(myContext,mess_from,mess_content,"inbox");
            Function.showNotification(context, mess_from, mess_content);

            for (int i = 0; i < 8; i++) {
                System.out.println("Blocking SMS **********************");
            }
        }
    }



}
