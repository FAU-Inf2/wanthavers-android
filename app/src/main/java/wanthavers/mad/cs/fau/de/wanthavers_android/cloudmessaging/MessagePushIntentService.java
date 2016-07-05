package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MessagePushIntentService extends IntentService {

    private Handler.Callback callback = new PushNotificationError();

    public MessagePushIntentService() {
        super("MessagePushIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        if (extras.getParcelable("WH_PUSH_NOTIFICATION") != null) {
            sendNotification((PushMessageNotification) extras.getParcelable("WH_PUSH_NOTIFICATION"));
        } else {
            String message = extras.getString("message");
            String url = extras.getString("url");
            PushMessageNotification notification = new PushMessageNotification(url, message);

            sendNotification(notification, extras);

        }
    }

    private void sendNotification(PushMessageNotification pushMessage) {
        sendNotification(pushMessage, new Bundle());
    }

    private void sendNotification(PushMessageNotification pushMessage, Bundle extras) {

        Intent broadcast = new Intent();
        extras.putParcelable("WH_PUSH_NOTIFICATION", pushMessage);
        broadcast.putExtras(extras);

        broadcast.setAction("WH_PUSH_NOTIFICATION_BROADCAST");

        sendOrderedBroadcast(broadcast, null, null, new Handler(callback), Activity.RESULT_OK, null, extras);


    }


}
