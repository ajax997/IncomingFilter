package com.nguyennghi.incomingfilter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.Date;

public abstract class PhoneStateReceiver extends BroadcastReceiver {


    private String blockingNumber = "090909090";

    static PhonecallStartEndDetector listener;
    String outgoingSavedNumber;
    Context savedContext;


    @Override
    public void onReceive(final Context context, final Intent intent) {

        savedContext = context;
        if(listener == null){
            listener = new PhonecallStartEndDetector();
        }



        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    protected abstract void onIncomingCallStarted(String number, Date start);
    protected abstract void onOutgoingCallStarted(String number, Date start);
    protected abstract void onIncomingCallEnded(String number, Date start, Date end);
    protected abstract void onOutgoingCallEnded(String number, Date start, Date end);
    protected abstract void onMissedCall(String number, Date start);

    public class PhonecallStartEndDetector extends PhoneStateListener {
        int lastState = TelephonyManager.CALL_STATE_IDLE;
        Date callStartTime;
        boolean isIncoming;
        String savedNumber;  //because the passed incoming is only valid in ringing

        public PhonecallStartEndDetector() {}

        //The outgoing number is only sent via a separate intent, so we need to store it out of band
        public void setOutgoingNumber(String number){
            savedNumber = number;
        }

        //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
        //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if(lastState == state){
                //No change, debounce extras
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    callStartTime = new Date();
                    savedNumber = incomingNumber;
                    onIncomingCallStarted(incomingNumber, callStartTime);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //Transition of ringing->offhook are pickups of incoming calls.  Nothing donw on them
                    if(lastState != TelephonyManager.CALL_STATE_RINGING){
                        isIncoming = false;
                        callStartTime = new Date();
                        onOutgoingCallStarted(savedNumber, callStartTime);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                    if(lastState == TelephonyManager.CALL_STATE_RINGING){
                        //Ring but no pickup-  a miss
                        onMissedCall(savedNumber, callStartTime);
                    }
                    else if(isIncoming){
                        onIncomingCallEnded(savedNumber, callStartTime, new Date());
                    }
                    else{
                        onOutgoingCallEnded(savedNumber, callStartTime, new Date());
                    }
                    break;
            }
            lastState = state;
        }

    }


}
