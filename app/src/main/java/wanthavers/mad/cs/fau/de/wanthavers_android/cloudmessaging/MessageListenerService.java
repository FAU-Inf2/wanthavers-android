package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Date;

import de.fau.cs.mad.wanthavers.common.AppChatLastSeen;
import de.fau.cs.mad.wanthavers.common.CloudMessageSubject;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.AppChatLastSeenDatabaseHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;


public class MessageListenerService extends FirebaseMessagingService {
    private static final String TAG = "MessageListenerService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {




        String message = remoteMessage.getNotification().getBody();
        String from = remoteMessage.getData().get(CloudMessageSubject.NEWMESSAGE_SENDER);
        String chatId = remoteMessage.getData().get(CloudMessageSubject.NEWMESSAGE_CHATID);


        //save new ChatInfo to Db
        AppChatLastSeen chatLastSeen = new AppChatLastSeen(chatId, new Date(),message);
        AppChatLastSeenDatabaseHelper appChatLastSeenDatabaseHelper = AppChatLastSeenDatabaseHelper.getInstance(getApplicationContext());
        appChatLastSeenDatabaseHelper.createOrUpdate(chatLastSeen);


        //push notification
        Bundle extras = new Bundle();
        PushMessageNotification pushMessage = new PushMessageNotification(from, message);
        pushMessage.setBackupNotifier("false");
        extras.putParcelable("WH_PUSH_NOTIFICATION", pushMessage);

        //start broadcast
        Intent intent = new Intent(this, MessagePushIntentService.class);
        intent.putExtras(extras);
        startService(intent);

        //sendNotification(message);
    }

    /**
     * Creates simple -->in app<-- notification
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        // TODO: add multiple notifications for different notification classes -> e.g. chat notifications should open chats

        Intent intent = new Intent(this, DesireListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // notifications with same IDs will overwrite each other
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo).setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle("wanthavers " + getResources().getString(R.string.new_message_from))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
