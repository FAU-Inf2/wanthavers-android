package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;

public class PushNotificationError implements Handler.Callback {


    @Override
    public boolean handleMessage(Message msg) {
        throw new IllegalArgumentException("PUSH_RECEIVED NOT HANDLED!");
    }
}
