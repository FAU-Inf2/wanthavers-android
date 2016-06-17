package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.UserClient;

/**
 * Created by Nico on 10.05.2016.
 */
public class UserRemoteDataSource implements UserDataSource {
    private static UserRemoteDataSource INSTANCE;

    private UserClient userClient;

    // Prevent direct instantiation.
    private UserRemoteDataSource(Context context) {
        userClient = UserClient.getInstance(context);
    }

    public static UserRemoteDataSource getInstance(Context context){
        if(INSTANCE == null) {
            INSTANCE = new UserRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getUser(@NonNull long userId, @NonNull GetUserCallback callback) {
        try {
            User user = userClient.get(userId);
            callback.onUserLoaded(user);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createUser(@NonNull User user, @NonNull String password, @NonNull CreateUserCallback callback) {
        try {
            User ret = userClient.createUser(user, password);
            callback.onUserCreated(ret);
        } catch (Throwable t) {
            callback.onCreationFailed();
        }
    }

    @Override
    public void updateUser(@NonNull User user, @NonNull UpdateUserCallback callback) {
        try {
            User ret = userClient.updateUser(user);
            callback.onUserUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteUser(@NonNull DeleteUserCallback callback) {
        try {
            userClient.deleteUser();
            callback.onUserDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

    @Override
    public void login(@NonNull LoginCallback callback) {
        try {
            User ret = userClient.login();
            callback.onLoginSuccessful(ret);
        } catch (Throwable t) {
            callback.onLoginFailed();
        }
    }

}
