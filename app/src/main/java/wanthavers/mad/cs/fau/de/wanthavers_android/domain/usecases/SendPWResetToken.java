package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class SendPWResetToken extends UseCase<SendPWResetToken.RequestValues, SendPWResetToken.ResponseValue> {

    private final UserRepository mUserRepository;

    public SendPWResetToken(@NonNull UserRepository userRepository) {
        mUserRepository = checkNotNull(userRepository, "userRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mUserRepository.sendPWResetToken(values.getEmail(), new UserDataSource.SendPWResetTokenCallback() {

            @Override
            public void onTokenSent() {
                ResponseValue responseValue = new ResponseValue();
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onSendFailed() {
                getUseCaseCallback().onError();
            }

        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mEmail;

        public RequestValues(String email) {
            mEmail = email;
        }

        public String getEmail() {
            return mEmail;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {

        }

    }

}
