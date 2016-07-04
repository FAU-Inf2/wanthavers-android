package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;
import android.util.Log;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.cloudmessagetoken.CloudMessageTokenRepository;

import static com.google.common.base.Preconditions.checkNotNull;


public class DeleteToken extends UseCase<DeleteToken.RequestValues, DeleteToken.ResponseValue> {
    private final CloudMessageTokenRepository mCloudMessageTokenRepository;

    public DeleteToken(@NonNull CloudMessageTokenRepository cloudMessageTokenRepository) {
        mCloudMessageTokenRepository = checkNotNull(cloudMessageTokenRepository, "cloudMessageTokenRepo cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        mCloudMessageTokenRepository.deleteToken(values.getToken(), new CloudMessageTokenDataSource.DeleteTokenCallback() {
            @Override
            public void onTokenDeleted() {
                ResponseValue responseValue = new ResponseValue();
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDeleteFailed() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final String mToken;

        public RequestValues(@NonNull String token) {
            mToken = checkNotNull(token, "versionCode must'nt be null");
        }

        public String getToken() {
            return mToken;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        public ResponseValue() {
            //nothing to do
        }
    }
}
