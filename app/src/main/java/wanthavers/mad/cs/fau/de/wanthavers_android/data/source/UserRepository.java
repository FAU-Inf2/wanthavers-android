package wanthavers.mad.cs.fau.de.wanthavers_android.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 10.05.2016.
 */
public class UserRepository implements UserDataSource {

    private static UserRepository INSTANCE;

    private final UserDataSource userRemoteDataSource;

    private final UserDataSource userLocalDataSource;

    // Prevent direct instantiation.
    private UserRepository(@NonNull UserDataSource userRemoteDataSource,
                           @NonNull UserDataSource userLocalDataSource) {
        this.userRemoteDataSource = checkNotNull(userRemoteDataSource);
        this.userLocalDataSource = checkNotNull(userLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param userRemoteDataSource the backend data source
     * @param userLocalDataSource  the device storage data source
     * @return the {@link UserRepository} instance
     */
    public static UserRepository getInstance(UserDataSource userRemoteDataSource,
                                             UserDataSource userLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository(userRemoteDataSource, userLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(UserDataSource, UserDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getUser(@NonNull final long userId, @NonNull final GetUserCallback callback) {
        checkNotNull(userId);
        checkNotNull(callback);

        User user = null;
        userLocalDataSource.getUser(userId, new GetUserCallback() {
            @Override
            public void onUserLoaded(User user) {
                callback.onUserLoaded(user);
            }

            @Override
            public void onDataNotAvailable() {
                getUserFromRemoteDataSource(userId, callback);
            }
        });
    }

    @Override
    public void getAllUsers(@NonNull final GetAllUsersCallback callback) {
        checkNotNull(callback);

        userLocalDataSource.getAllUsers(new GetAllUsersCallback() {
            @Override
            public void onAllUsersLoaded(List<User> users) {
                callback.onAllUsersLoaded(users);
            }

            @Override
            public void onDataNotAvailable() {
                getAllUsersFromRemoteDataSource(callback);
            }
        });
    }

    @Override
    public void createUser(@NonNull User user, @NonNull CreateUserCallback callback) {
        checkNotNull(user);
        checkNotNull(callback);

        userRemoteDataSource.createUser(user, callback);
        userLocalDataSource.createUser(user, callback);
    }

    @Override
    public void updateUser(@NonNull User user, @NonNull UpdateUserCallback callback) {
        checkNotNull(user);
        checkNotNull(callback);

        userRemoteDataSource.updateUser(user, callback);
        userLocalDataSource.updateUser(user, callback);
    }

    @Override
    public void deleteUser(@NonNull User user, @NonNull DeleteUserCallback callback) {
        checkNotNull(user);
        checkNotNull(callback);

        userRemoteDataSource.deleteUser(user, callback);
        userLocalDataSource.deleteUser(user, callback);
    }

    private void getUserFromRemoteDataSource(@NonNull long userId, @NonNull final GetUserCallback callback) {
        userRemoteDataSource.getUser(userId, new GetUserCallback() {
            @Override
            public void onUserLoaded(User user) {
                callback.onUserLoaded(user);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getAllUsersFromRemoteDataSource(@NonNull final GetAllUsersCallback callback) {
        userRemoteDataSource.getAllUsers(new GetAllUsersCallback() {
            @Override
            public void onAllUsersLoaded(List<User> users) {
                callback.onAllUsersLoaded(users);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
