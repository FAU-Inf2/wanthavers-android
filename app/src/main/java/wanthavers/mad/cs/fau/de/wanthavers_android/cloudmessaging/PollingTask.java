package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PollingTask implements Runnable {

    private Context mAppContext;


    public void PollingTask(Context appContext){
        mAppContext = appContext;
   }

    public void setAppContext(Context appContext){
        mAppContext = appContext;
    }

    @Override
    public void run() {
        //push notification

        Bundle extras = new Bundle();
        PushMessageNotification pushMessage = new PushMessageNotification(MessageNotificationType.POLLING_MESSAGE.toString(), "WH_BACKUP_MESSAGE");

        extras.putParcelable("WH_PUSH_NOTIFICATION", pushMessage);

        //start broadcast
        Intent intent = new Intent(mAppContext, MessagePushIntentService.class);
        intent.putExtras(extras);
        mAppContext.startService(intent);

    }
}
