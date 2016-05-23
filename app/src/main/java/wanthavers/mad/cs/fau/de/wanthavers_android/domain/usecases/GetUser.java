package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetUser extends UseCase<GetUser.RequestValues, GetUser.ResponseValue> {

    private final UserRepository mUserRepository;


    public GetUser(@NonNull UserRepository userRepository) {
        mUserRepository = checkNotNull(userRepository, "userRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mUserRepository.getUser(values.getUserid(), new UserDataSource.GetUserCallback() {
            @Override
            public void onUserLoaded(User user) {
                ResponseValue responseValue = new ResponseValue(user);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final long mUserId;

        public RequestValues(@NonNull long userId) {
            mUserId = checkNotNull(userId, "userId cannot be null!");
        }

        public long getUserid() {
            return mUserId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private User mUser;

        public ResponseValue(@NonNull User user) {
            mUser = checkNotNull(user, "user cannot be null!");
        }

        public User getUser() {
            return mUser;
        }
    }

}
