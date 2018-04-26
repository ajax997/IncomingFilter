package com.android.internal.telephony;

/**
 * Created by nguyennghi on 3/9/18 10:55 AM
 */
 public interface ITelephony {

    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
