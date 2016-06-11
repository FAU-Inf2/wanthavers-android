package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.CloudMessageToken;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 11.06.2016.
 */
public class CloudMessageTokenRepository implements CloudMessageTokenDataSource {

    private static CloudMessageTokenRepository INSTANCE;

    private final CloudMessageTokenDataSource cloudMessageTokenRemoteDataSource;

    private final CloudMessageTokenDataSource cloudMessageTokenLocalDataSource;

    private CloudMessageTokenRepository(@NonNull CloudMessageTokenDataSource cloudMessageTokenRemoteDataSource, @NonNull CloudMessageTokenDataSource cloudMessageTokenLocalDataSource) {
        this.cloudMessageTokenRemoteDataSource = checkNotNull(cloudMessageTokenRemoteDataSource);
        this.cloudMessageTokenLocalDataSource = checkNotNull(cloudMessageTokenLocalDataSource);
    }

    public static CloudMessageTokenRepository getInstance(@NonNull CloudMessageTokenDataSource cloudMessageTokenRemoteDataSource, @NonNull CloudMessageTokenDataSource cloudMessageTokenLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CloudMessageTokenRepository(checkNotNull(cloudMessageTokenRemoteDataSource), checkNotNull(cloudMessageTokenLocalDataSource));
        }
        return INSTANCE;
    }

    @Override
    public void getAllTokensForUser(@NonNull final GetAllTokensForUserCallback callback) {
        checkNotNull(callback);

        cloudMessageTokenLocalDataSource.getAllTokensForUser(new GetAllTokensForUserCallback() {
            @Override
            public void onAllTokensForUserLoaded(List<CloudMessageToken> tokens) {
                callback.onAllTokensForUserLoaded(tokens);
            }

            @Override
            public void onDataNotAvailable() {
                cloudMessageTokenRemoteDataSource.getAllTokensForUser(new GetAllTokensForUserCallback() {
                    @Override
                    public void onAllTokensForUserLoaded(List<CloudMessageToken> tokens) {
                        callback.onAllTokensForUserLoaded(tokens);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void createToken(@NonNull CloudMessageToken token, @NonNull final CreateTokenCallback callback) {
        checkNotNull(token);
        checkNotNull(callback);

        cloudMessageTokenRemoteDataSource.createToken(token, new CreateTokenCallback() {
            @Override
            public void onTokenCreated(CloudMessageToken token) {
                callback.onTokenCreated(token);
            }

            @Override
            public void onCreateFailed() {
                callback.onCreateFailed();
            }
        });
    }

    @Override
    public void getToken(@NonNull final long tokenId, @NonNull final GetTokenCallback callback) {
        checkNotNull(tokenId);
        checkNotNull(callback);

        cloudMessageTokenLocalDataSource.getToken(tokenId, new GetTokenCallback() {
            @Override
            public void onTokenLoaded(CloudMessageToken token) {
                callback.onTokenLoaded(token);
            }

            @Override
            public void onDataNotAvailable() {
                cloudMessageTokenRemoteDataSource.getToken(tokenId, new GetTokenCallback() {
                    @Override
                    public void onTokenLoaded(CloudMessageToken token) {
                        callback.onTokenLoaded(token);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void updateToken(@NonNull long tokenId, @NonNull CloudMessageToken token, @NonNull final UpdateTokenCallback callback) {
        checkNotNull(tokenId);
        checkNotNull(token);
        checkNotNull(callback);

        cloudMessageTokenRemoteDataSource.updateToken(tokenId, token, new UpdateTokenCallback() {
            @Override
            public void onTokenUpdated(CloudMessageToken token) {
                callback.onTokenUpdated(token);
            }

            @Override
            public void onUpdateFailed() {
                callback.onUpdateFailed();
            }
        });
    }

    @Override
    public void deleteToken(@NonNull long tokenId, @NonNull final DeleteTokenCallback callback) {
        checkNotNull(tokenId);
        checkNotNull(callback);

        cloudMessageTokenRemoteDataSource.deleteToken(tokenId, new DeleteTokenCallback() {
            @Override
            public void onTokenDeleted() {
                callback.onTokenDeleted();
            }

            @Override
            public void onDeleteFailed() {
                callback.onDeleteFailed();
            }
        });
    }
}
