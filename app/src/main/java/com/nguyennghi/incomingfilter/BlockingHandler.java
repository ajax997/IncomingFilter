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
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nguyennghi on 3/11/18 8:46 PM
 */
public class BlockingHandler extends PhoneStateReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
   private int savedState;
    private void disconnectPhoneITelephony(Context context, Action action) {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
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

            final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            switch (action)
            {
                case HANGUP:
                    telephonyService.endCall();
                    break;
                case PICKUP:
                    telephonyService.answerRingingCall();
                    break;
                case SILENT:
                    savedState = mode.getRingerMode();
                    mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;
                case VIBRATE:
                    savedState = mode.getRingerMode();
                    mode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onIncomingCallStarted(String number, Date start) {
        final String numberCall = number;
        if (numberCall != null) {
            Toast.makeText(savedContext, "Match - PHONE CALL", Toast.LENGTH_LONG).show();
            ArrayList<FilterUnit> filterUnits = Function.getListBlockItems(savedContext);
            for (FilterUnit filterUnit : filterUnits) {
                if (filterUnit.blocking_incoming_calls)
                    switch (filterUnit.getUnitType()) {
                        case NUM:
                            if (filterUnit.num.equals(numberCall))
                                disconnectPhoneITelephony(savedContext, Function.coverAction(filterUnit.incoming_call_action));
                            break;

                        case START_NUM:
                            if (numberCall.startsWith(filterUnit.num))
                                disconnectPhoneITelephony(savedContext, Function.coverAction(filterUnit.incoming_call_action));
                            break;

                        case END_NUM:
                            if (numberCall.endsWith(filterUnit.num))
                                disconnectPhoneITelephony(savedContext, Function.coverAction(filterUnit.incoming_call_action));
                            break;
                        case PROVIDER:
                            Function.checkProvider(numberCall, filterUnit.provider);
                            disconnectPhoneITelephony(savedContext, Function.coverAction(filterUnit.incoming_call_action));
                            break;

                    }
            }
        }
    }

    @Override
    protected void onOutgoingCallStarted(String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(String number, Date start, Date end) {

    }

    @Override
    protected void onOutgoingCallEnded(String number, Date start, Date end) {
        final AudioManager mode = (AudioManager) savedContext.getSystemService(Context.AUDIO_SERVICE);
        mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    @Override
    protected void onMissedCall(String number, Date start) {
        Toast.makeText(savedContext, "CallEnded!", Toast.LENGTH_LONG).show();
    }
}
