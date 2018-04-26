package com.nguyennghi.incomingfilter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by nguyennghi on 3/19/18 3:56 PM
 */
public class HeadlessSmsSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
