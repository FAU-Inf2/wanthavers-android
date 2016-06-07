package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateUser extends UseCase<UpdateUser.RequestValues, UpdateUser.ResponseValue> {

    private final UserRepository mUserRepository;

    public UpdateUser(@NonNull UserRepository userRepository) {
        mUserRepository = checkNotNull(userRepository, "user repository cannot be null");
    }

    protected void executeUseCase(final RequestValues values) {

        mUserRepository.updateUser(values.getUser(),
                new UserDataSource.UpdateUserCallback() {

                    @Override
                    public void onUserUpdated(User user) {
                        ResponseValue responseValue = new ResponseValue();
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onUpdateFailed() {
                        getUseCaseCallback().onError();
                    }

                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final User mUser;

        public RequestValues(User user) {
            mUser = user;
        }

        public User getUser() {
            return mUser;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }

}
