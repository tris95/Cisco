package com.cisco.user.services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.cisco.user.activities.DeskripsiPromosi;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tris on 05/04/2017.
 */
@SuppressLint("Registered")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    public static String title, message;
    MyNotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");
            Long tsLong = System.currentTimeMillis();

            //parsing json data
            title = data.getString("title");
            message = data.getString("message");
            Log.e("message", message);

            //creating MyNotificationManager object
            mNotificationManager = new MyNotificationManager(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), DeskripsiPromosi.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mNotificationManager.showSmallNotification(title, message, tsLong, intent);

            //creating an intent for the notification --> bottom

//            if there is no imageerror
            //if(imageUrl.equals("null")){
            //              displaying small notification
//            mNotificationManager.showSmallNotification(title, message, intent);
            //}
            //else{
            //if there is an imageerror
            //displaying a big notification
            //mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            //}
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}
