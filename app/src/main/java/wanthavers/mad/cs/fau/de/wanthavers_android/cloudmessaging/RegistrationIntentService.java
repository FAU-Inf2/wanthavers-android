package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;


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

        // Manual heart beat to Google server for faster receiving of notifications
        Context context = getApplicationContext();
        context.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        context.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        CloudMessageTokenRepository tokenRepository = CloudMessageTokenRepository.getInstance(
                CloudMessageTokenRemoteDataSource.getInstance(getApplicationContext()),
                CloudMessageTokenLocalDataSource.getInstance(getApplicationContext()));

        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(SharedPreferencesHelper.NAME_USER, getApplicationContext());
        long userId = sharedPreferencesHelper.loadLong(SharedPreferencesHelper.KEY_USERID, 6L);

        CloudMessageToken token = new CloudMessageToken();
        token.setToken(refreshedToken);
        token.setUserId(userId);

        tokenRepository.createToken(token, new CloudMessageTokenDataSource.CreateTokenCallback(){

            @Override
            public void onTokenCreated(CloudMessageToken token) {
                Log.i(TAG, "Registered CloudMessageToken " + token.getToken() + " for User " + token.getUserId());
                return;
            }

            @Override
            public void onCreateFailed() {
                Log.i(TAG, "Registration of token failed!");
                return;
            }
        });
    }
}
