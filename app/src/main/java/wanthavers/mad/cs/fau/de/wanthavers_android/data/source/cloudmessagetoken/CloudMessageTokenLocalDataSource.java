package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.CloudMessageToken;

/**
 * Created by Nico on 11.06.2016.
 */
public class CloudMessageTokenLocalDataSource implements CloudMessageTokenDataSource {
    private static CloudMessageTokenLocalDataSource INSTANCE;

    private Context context;

    private CloudMessageTokenLocalDataSource(@NonNull Context context) {
        this.context = context;
    }

    public static CloudMessageTokenLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CloudMessageTokenLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllTokensForUser(@NonNull GetAllTokensForUserCallback callback) {
        //TODO
        callback.onDataNotAvailable();
    }

    @Override
    public void createToken(@NonNull CloudMessageToken token, @NonNull CreateTokenCallback callback) {
        //TODO
        callback.onCreateFailed();
    }

    @Override
    public void getToken(@NonNull long tokenId, @NonNull GetTokenCallback callback) {
        //TODO
        callback.onDataNotAvailable();
    }

    @Override
    public void updateToken(@NonNull long tokenId, @NonNull CloudMessageToken token, @NonNull UpdateTokenCallback callback) {
        //TODO
        callback.onUpdateFailed();
    }

    @Override
    public void deleteToken(@NonNull long tokenId, @NonNull DeleteTokenCallback callback) {
        //TODO
        callback.onDeleteFailed();
    }
}
