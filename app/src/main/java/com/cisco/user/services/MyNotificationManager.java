package com.cisco.user.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.cisco.user.R;

/**
 * Created by Tris on 05/04/2017.
 */
public class MyNotificationManager {

    //public static final int ID_BIG_NOTIFICATION = 234;
    private static final int ID_SMALL_NOTIFICATION = 235;

    private Context mCtx;

    MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Long time, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setWhen(time)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setContentText(message)
                .setSound(defaultSoundUri)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }


    //the method will show a big notification with an imageerror
    //parameters are title for message title, message for message text, url of the big imageerror and an intent that will open
    //when you will tap on the notification
//    public void showBigNotification(String title, String message, String url, Intent intent) {
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        mCtx,
//                        ID_BIG_NOTIFICATION,
//                        intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//        bigPictureStyle.setBigContentTitle(title);
//        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
//        bigPictureStyle.bigPicture(getBitmapFromURL(url));
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
//        Notification notification;
//        notification = mBuilder.setSmallIcon(R.mipmap.b).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentIntent(resultPendingIntent)
//                .setContentTitle(title)
//                .setStyle(bigPictureStyle)
//                .setSmallIcon(R.mipmap.b)
//                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.b))
//                .setContentText(message)
//                .build();
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
//    }
//
//    //The method will return Bitmap from an imageerror URL
//    private Bitmap getBitmapFromURL(String strURL) {
//        try {
//            URL url = new URL(strURL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
