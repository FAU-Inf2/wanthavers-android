package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote;

import android.support.annotation.NonNull;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.UserDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.UserClient;

/**
 * Created by Nico on 10.05.2016.
 */
public class UserRemoteDataSource implements UserDataSource {
    private static UserRemoteDataSource INSTANCE;

    private UserClient userEndpoint = UserClient.getInstance();

    // Prevent direct instantiation.
    private UserRemoteDataSource() {}

    public static UserRemoteDataSource getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new UserRemoteDataSource();
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
    public void getAllUsers(@NonNull GetAllUsers callback) {
        try {
            List<User> users = userEndpoint.get();
            callback.onAllUsersLoaded(users);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createUser(@NonNull User user, @NonNull CreateUser callback) {
        try {
            User ret = userEndpoint.createUser(user);
            callback.onUserCreated(ret);
        } catch (Throwable t) {
            callback.onCreationFailed();
        }
    }

    @Override
    public void updateUser(@NonNull User user, @NonNull UpdateUser callback) {
        try {
            User ret = userEndpoint.updateUser(user.getID(), user);
            callback.onUserUpdated(ret);
        } catch (Throwable t) {
            callback.onUpdateFailed();
        }
    }

    @Override
    public void deleteUser(@NonNull User user, @NonNull DeleteUser callback) {
        try {
            userEndpoint.deleteUser(user.getID());
            callback.onUserDeleted();
        } catch (Throwable t) {
            callback.onDeleteFailed();
        }
    }

}
