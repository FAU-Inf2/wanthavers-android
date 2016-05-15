package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.UserClient;

/**
 * Created by Nico on 10.05.2016.
 */
public class UserRemoteDataSource implements UserDataSource {
    private static UserRemoteDataSource INSTANCE;

    private UserClient userEndpoint;

    // Prevent direct instantiation.
    private UserRemoteDataSource(Context context) {
        userEndpoint = UserClient.getInstance(context);
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
            User user = userEndpoint.get(userId);
            callback.onUserLoaded(user);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllUsers(@NonNull GetAllUsersCallback callback) {
        try {
            List<User> users = userEndpoint.get();
            callback.onAllUsersLoaded(users);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createUser(@NonNull User user, @NonNull CreateUserCallback callback) {
        try {
            User ret = userEndpoint.createUser(user);
            callback.onUserCreated(ret);
        } catch (Throwable t) {
            callback.onCreationFailed();
        }
    }

    @Override
    public void updateUser(@NonNull User user, @NonNull UpdateUserCallback callback) {
        try {
            User ret = userEndpoint.updateUser(user.getID(), user);
            callback.onUserUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteUser(@NonNull User user, @NonNull DeleteUserCallback callback) {
        try {
            userEndpoint.deleteUser(user.getID());
            callback.onUserDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

}
