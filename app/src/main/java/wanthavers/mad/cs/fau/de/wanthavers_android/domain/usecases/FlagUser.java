package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.UserFlag;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlagUser extends UseCase<FlagUser.RequestValues, FlagUser.ResponseValue> {

    private final UserRepository mUserRepository;

    public FlagUser(@NonNull UserRepository userRepository) {
        mUserRepository = checkNotNull(userRepository, "user repository cannot be null");
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mUserRepository.flagUser(values.getUserId(), new UserDataSource.FlagUserCallback() {
            @Override
            public void onUserFlagged(UserFlag userFlag) {
                ResponseValue responseValue = new ResponseValue(userFlag);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onFlagFailed() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mUserId;

        public RequestValues(long userId) {
            mUserId = userId;
        }

        public long getUserId() {
            return mUserId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private UserFlag mUserFlag;

        public ResponseValue(UserFlag userFlag) {
            mUserFlag = userFlag;
        }

        public UserFlag getUserFlag() {
            return mUserFlag;
        }

    }

}
