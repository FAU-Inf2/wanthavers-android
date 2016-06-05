package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginUser extends UseCase<LoginUser.RequestValues, LoginUser.ResponseValue> {

    private final UserRepository mUserRepository;


    public LoginUser(@NonNull UserRepository userRepository) {
        mUserRepository = checkNotNull(userRepository, "userRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mUserRepository.login(new UserDataSource.LoginCallback() {
            @Override
            public void onLoginSuccessful(User user) {
                ResponseValue responseValue = new ResponseValue(user);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onLoginFailed() {
                getUseCaseCallback().onError();
            }

        });
    }



    public static final class RequestValues implements UseCase.RequestValues {


        public RequestValues() {}


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
