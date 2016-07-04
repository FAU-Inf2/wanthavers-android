package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.CloudMessageToken;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.CloudMessageTokenClient;

/**
 * Created by Nico on 11.06.2016.
 */
public class CloudMessageTokenRemoteDataSource implements CloudMessageTokenDataSource {
    private static CloudMessageTokenRemoteDataSource INSTANCE;

    private CloudMessageTokenClient cloudMessageTokenClient;

    private CloudMessageTokenRemoteDataSource(@NonNull Context context) {
        cloudMessageTokenClient = CloudMessageTokenClient.getInstance(context);
    }

    public static CloudMessageTokenRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CloudMessageTokenRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllTokensForUser(@NonNull GetAllTokensForUserCallback callback) {
        try {
            List<CloudMessageToken> ret = cloudMessageTokenClient.getAllTokensForUser();
            callback.onAllTokensForUserLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createToken(@NonNull CloudMessageToken token, @NonNull CreateTokenCallback callback) {
        try {
            CloudMessageToken ret = cloudMessageTokenClient.createToken(token);
            callback.onTokenCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void getToken(@NonNull long tokenId, @NonNull GetTokenCallback callback) {
        try {
            CloudMessageToken ret = cloudMessageTokenClient.getToken(tokenId);
            callback.onTokenLoaded(ret);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void updateToken(@NonNull long tokenId, @NonNull CloudMessageToken token, @NonNull UpdateTokenCallback callback) {
        try {
            CloudMessageToken ret = cloudMessageTokenClient.updateToken(tokenId, token);
            callback.onTokenUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteToken(@NonNull String token, @NonNull DeleteTokenCallback callback) {
        try {
            cloudMessageTokenClient.deleteToken(token);
            callback.onTokenDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }
}
