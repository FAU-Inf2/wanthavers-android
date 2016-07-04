package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.CloudMessageToken;

/**
 * Created by Nico on 11.06.2016.
 */
public interface CloudMessageTokenDataSource {

    interface GetAllTokensForUserCallback {

        void onAllTokensForUserLoaded(List<CloudMessageToken> tokens);

        void onDataNotAvailable();

    }

    interface CreateTokenCallback {

        void onTokenCreated(CloudMessageToken token);

        void onCreateFailed();

    }

    interface GetTokenCallback {

        void onTokenLoaded(CloudMessageToken token);

        void onDataNotAvailable();

    }

    interface UpdateTokenCallback {

        void onTokenUpdated(CloudMessageToken token);

        void onUpdateFailed();

    }

    interface DeleteTokenCallback {

        void onTokenDeleted();

        void onDeleteFailed();

    }

    void getAllTokensForUser(@NonNull GetAllTokensForUserCallback callback);

    void createToken(@NonNull CloudMessageToken token, @NonNull CreateTokenCallback callback);

    void getToken(@NonNull long tokenId, @NonNull GetTokenCallback callback);

    void updateToken(@NonNull long tokenId, @NonNull CloudMessageToken token, @NonNull UpdateTokenCallback callback);

    void deleteToken(@NonNull String token, @NonNull DeleteTokenCallback callback);

}
