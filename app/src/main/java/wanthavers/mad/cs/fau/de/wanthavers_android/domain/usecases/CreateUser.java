package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateUser extends UseCase<CreateUser.RequestValues, CreateUser.ResponseValue> {

    private final UserRepository mUserRepository;


    public CreateUser(@NonNull UserRepository userRepository) {
        mUserRepository = checkNotNull(userRepository, "userRepository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {

        mUserRepository.createUser(values.getUser(), values.getPassword(), new UserDataSource.CreateUserCallback() {
            @Override
            public void onUserCreated(User user) {
                ResponseValue responseValue = new ResponseValue(user);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onCreationFailed() {
                getUseCaseCallback().onError();
            }

        });
    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final User mUser;
        private final String mPassword;

        public RequestValues(@NonNull User user, @NonNull String password) {
            mUser = checkNotNull(user, "user cannot be null!");
            mPassword = checkNotNull(password, "password cannot be null!");
        }

        public User getUser() {
            return mUser;
        }

        public String getPassword(){return mPassword;}
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
