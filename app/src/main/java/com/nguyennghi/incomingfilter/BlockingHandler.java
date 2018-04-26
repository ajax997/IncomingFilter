package com.nguyennghi.incomingfilter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by nguyennghi on 3/11/18 8:46 PM
 */
public class BlockingHandler extends PhoneStateReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            final String numberCall = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (numberCall != null) {
                Log.e("Number", numberCall);

                Toast.makeText(context, "Match - PHONE CALL", Toast.LENGTH_LONG).show();

                disconnectPhoneITelephony(context);
            }
        }
    }


    // Keep this method as it is
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void disconnectPhoneITelephony(Context context) {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);

            SendMessage sendMessage = new SendMessage();
            sendMessage.send(context, "098867677", "I love you!");
            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        Runtime.getRuntime().exec( "input keyevent " + KeyEvent.KEYCODE_HEADSETHOOK );
                    }
                    catch (Throwable t) {

                        // do something proper here.
                    }
                }
            }).start();
            telephonyService.endCall();
            final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//            switch( mode.getRingerMode() ){
//                case AudioManager.RINGER_MODE_NORMAL:
//                    break;
//                case AudioManager.RINGER_MODE_SILENT:
//                    break;
//                case AudioManager.RINGER_MODE_VIBRATE:
//                    break;
//            }

            mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);

       //     mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    protected void onIncomingCallStarted(String number, Date start) {

    }

    @Override
    protected void onOutgoingCallStarted(String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(String number, Date start, Date end) {

    }

    @Override
    protected void onOutgoingCallEnded(String number, Date start, Date end) {

    }

    @Override
    protected void onMissedCall(String number, Date start) {
        Toast.makeText(savedContext, "CallEnded!", Toast.LENGTH_LONG).show();
    }
}
