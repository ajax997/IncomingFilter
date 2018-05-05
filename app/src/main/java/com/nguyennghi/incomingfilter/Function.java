package com.nguyennghi.incomingfilter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import com.nguyennghi.incomingfilter.model.TaskDatabaseAdapter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by SHAJIB on 7/10/2017.
 */

public class Function {


    static final String _ID = "_id";
    static final String KEY_THREAD_ID = "thread_id";
    static final String KEY_NAME = "name";
    static final String KEY_PHONE = "phone";
    static final String KEY_MSG = "msg";
    static final String KEY_TYPE = "type";
    static final String KEY_TIMESTAMP = "timestamp";
    static final String KEY_TIME = "time";


    public static  boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String converToTime(String timestamp)
    {
        long datetime = Long.parseLong(timestamp);
        Date date = new Date(datetime);
        DateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        return formatter.format(date);
    }


    public static HashMap<String, String> mappingInbox(String _id, String thread_id, String name, String phone, String msg, String type, String timestamp, String time)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(_ID, _id);
        map.put(KEY_THREAD_ID, thread_id);
        map.put(KEY_NAME, name);
        map.put(KEY_PHONE, phone);
        map.put(KEY_MSG, msg);
        map.put(KEY_TYPE, type);
        map.put(KEY_TIMESTAMP, timestamp);
        map.put(KEY_TIME, time);
        return map;
    }


    public static  ArrayList<HashMap<String, String>> removeDuplicates( ArrayList<HashMap<String, String>> smsList)
    {
        ArrayList<HashMap<String, String>> gpList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i<smsList.size(); i++)
        {
            boolean available = false;
            for (int j = 0; j<gpList.size(); j++)
            {
                if( Integer.parseInt(gpList.get(j).get(KEY_THREAD_ID)) == Integer.parseInt(smsList.get(i).get(KEY_THREAD_ID)))
                {
                    available = true;
                    break;
                }
            }

            if(!available)
            {
                gpList.add(mappingInbox(smsList.get(i).get(_ID), smsList.get(i).get(KEY_THREAD_ID),
                        smsList.get(i).get(KEY_NAME), smsList.get(i).get(KEY_PHONE),
                        smsList.get(i).get(KEY_MSG), smsList.get(i).get(KEY_TYPE),
                        smsList.get(i).get(KEY_TIMESTAMP), smsList.get(i).get(KEY_TIME)));
            }
        }
        return gpList;
    }


    public static boolean sendSMS(String toPhoneNumber, String smsMessage) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void showNotification(Context context, String title, String text) {
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, SMSMainActivity.class), 0);
        Resources r = context.getResources();
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(title)
                .setSmallIcon(R.drawable.ic_mail)
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentText((text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }



    public static String getContactbyPhoneNumber(Context c, String phoneNumber) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = c.getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            return phoneNumber;
        }else {
            String name = phoneNumber;
            try {

                if (cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }

            } finally {
                cursor.close();
            }

            return name;
        }
    }


    public static void createCachedFile (Context context, String key, ArrayList<HashMap<String, String>> dataList) throws IOException {
            FileOutputStream fos = context.openFileOutput (key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject (dataList);
            oos.close ();
            fos.close ();
    }

    public static Object readCachedFile (Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput (key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject ();
        return object;
    }



    public static ArrayList<FilterUnit> getListBlockItems(Context context)
    {
        TaskDatabaseAdapter taskDatabaseAdapter = new TaskDatabaseAdapter(context);
        taskDatabaseAdapter.open();
        ArrayList<FilterUnit> filterUnits = new ArrayList<>();
        for(FilterUnit unit : taskDatabaseAdapter.listUnits())
        {
           // if (unit.isEnable())
                filterUnits.add(unit);
        }
        return filterUnits;
    }

    public static boolean checkProvider(String number, String provider)
    {
        String[] viettel = {"086", "096", "097", "098", "0162", "0163", "0164", "0165", "0166", "0167", "0168", "0169"};
        String[] mobi = {"090", "093", "0120", "0121", "0122", "0126", "0128"};
        String[] vina = { "091", "094", "0123", "0124", "0125", "0127", "0129"};
        String[] vnmb = { "092", "0188", "0186"};
        String[] beeline = {"099", "0199"};
        switch (provider)
        {
            case "Viettel":
                return checkNum(number, viettel);

            case "Mobifone":
                return checkNum(number, mobi);

            case "Vinaphone":
                return checkNum(number, vina);

            case "Vietnamobile":
                return checkNum(number, vnmb);

            case "Gmobile":
                return checkNum(number, beeline);



        }
        return false;
    }

    public static boolean checkNum(String num, String[] strings)
    {
        for(String string:strings)
        {
            if(num.startsWith(string)) return true;
        }
        return false;
    }


    public static Action coverAction(int value)
    {
        return value == 0? Action.HANGUP:(value==1?Action.PICKUP:(value==2?Action.SILENT:Action.VIBRATE));
    }

    public static void writeSMSBack(Context myContext, String dest, String message, String folder) {
        final String SENT_SMS_CONTENT_PROVIDER_URI_OLDER_API_19 = "content://sms/"+folder;

        ContentValues values = new ContentValues();
        values.put("address", dest);
        values.put("body", message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(folder.equals("inbox"))
                myContext.getContentResolver().insert(Telephony.Sms.Inbox.CONTENT_URI, values);
            else
                myContext.getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI, values);
        }
        else
            myContext.getContentResolver().insert(Uri.parse(SENT_SMS_CONTENT_PROVIDER_URI_OLDER_API_19), values);
    }

}
