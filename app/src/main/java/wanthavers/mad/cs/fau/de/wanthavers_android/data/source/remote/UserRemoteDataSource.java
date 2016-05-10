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
        } catch (WebApplicationException e) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllUsers(@NonNull GetAllUsers callback) {
        try {
            List<User> users = userEndpoint.get();
            callback.onAllUsersLoaded(users);
        } catch (WebApplicationException e) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveUser(@NonNull User user) {
        //TODO: we should discriminate between create and update user here
        userEndpoint.createUser(user);
    }

    @Override
    public void deleteUser(@NonNull User user) {
        userEndpoint.deleteUser(user.getID());
    }

    @Override
    public void deleteAllUsers() {
        throw new UnsupportedOperationException("deleting all users via rest client is not allowed!");
    }
}
