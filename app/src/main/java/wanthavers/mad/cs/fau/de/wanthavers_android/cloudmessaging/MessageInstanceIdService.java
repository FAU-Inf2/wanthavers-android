package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MessageInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MessageIdService";

    /**
     * Gets called if InstanceID token is updated and on startup of the app
     */
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
