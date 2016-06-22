package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

public class MessageShowPushNotification extends IntentService {

    public MessageShowPushNotification() {
        super("MessageShowPushNotification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handleNotification(intent.getExtras());
    }



    private void handleNotification(Bundle extras){



        //TODO check if message is really a message and not some other push notification
        //set newMessagetoTrue as this was reached
        WantHaversApplication.setNewMessages(true);

        Intent intent = new Intent(this, DesireListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // notifications with same IDs will overwrite each other
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        PushMessageNotification pushMessageNotification = extras.getParcelable("WH_PUSH_NOTIFICATION");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("wanthavers " + getResources().getString(R.string.new_message_from))
                .setContentText(pushMessageNotification.message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
