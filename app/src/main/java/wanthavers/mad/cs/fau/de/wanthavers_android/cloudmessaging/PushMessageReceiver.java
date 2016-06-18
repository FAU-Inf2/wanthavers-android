package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushMessageReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent push = new Intent(context, MessageShowPushNotification.class);
        push.putExtras(getResultExtras(true));
        context.startService(push);
        setResultCode(Activity.RESULT_OK);
    }
}
