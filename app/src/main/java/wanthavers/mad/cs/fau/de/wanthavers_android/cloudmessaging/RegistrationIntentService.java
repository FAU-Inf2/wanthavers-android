package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;


public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // sharedPreferences...

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "token is " + refreshedToken);

        // sendRegistrationToServer
    }
}
