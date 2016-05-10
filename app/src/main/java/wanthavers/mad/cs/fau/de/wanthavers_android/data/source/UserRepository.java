package wanthavers.mad.cs.fau.de.wanthavers_android.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.fau.cs.mad.wanthavers.common.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 10.05.2016.
 */
public class UserRepository implements UserDataSource {

    private static UserRepository INSTANCE;

    private final UserDataSource userRemoteDataSource;

    private final UserDataSource userLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Long, User> cachedUsers;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

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

        if(cachedUsers != null && cachedUsers.size() > 0 && !cacheIsDirty){
            user = cachedUsers.get(userId);

            if(user != null){
                callback.onUserLoaded(user);
                return;
            }
        }

        if(cacheIsDirty){
            getUserFromRemoteDataSource(userId, callback);
        } else {
            userLocalDataSource.getUser(userId, new GetUserCallback() {
                @Override
                public void onUserLoaded(User user) {
                    refreshCache(user);
                    callback.onUserLoaded(user);
                }

                @Override
                public void onDataNotAvailable() {
                    getUserFromRemoteDataSource(userId, callback);
                }
            });
        }
    }

    @Override
    public void getAllUsers(@NonNull final GetAllUsers callback) {
        checkNotNull(callback);

        if(cachedUsers != null && !cacheIsDirty){
            callback.onAllUsersLoaded(new ArrayList<User>(cachedUsers.values()));
            return;
        }

        if(cacheIsDirty) {
            getAllUsersFromRemoteDataSource(callback);
        } else {
            userLocalDataSource.getAllUsers(new GetAllUsers() {
                @Override
                public void onAllUsersLoaded(List<User> users) {
                    refreshCache(users);
                    callback.onAllUsersLoaded(users);
                }

                @Override
                public void onDataNotAvailable() {
                    getAllUsersFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveUser(@NonNull User user) {
        checkNotNull(user);

        userRemoteDataSource.saveUser(user);
        userLocalDataSource.saveUser(user);

        if(cachedUsers == null) {
            cachedUsers = new LinkedHashMap<>();
        }

        cachedUsers.put(user.getID(), user);
    }

    @Override
    public void deleteUser(@NonNull User user) {
        checkNotNull(user);

        userRemoteDataSource.deleteUser(user);
        userLocalDataSource.deleteUser(user);

        if(cachedUsers != null) {
            cachedUsers.remove(user.getID());
        }
    }

    @Override
    public void deleteAllUsers() {
        userLocalDataSource.deleteAllUsers();

        if(cachedUsers != null) {
            cachedUsers.clear();
        }
    }

    public void refreshUsers(){
        cacheIsDirty = true;
    }

    private void getUserFromRemoteDataSource(@NonNull long userId, @NonNull final GetUserCallback callback) {
        userRemoteDataSource.getUser(userId, new GetUserCallback() {
            @Override
            public void onUserLoaded(User user) {
                refreshCache(user);
                refreshLocalDataSource(user);
                callback.onUserLoaded(user);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getAllUsersFromRemoteDataSource(@NonNull final GetAllUsers callback) {
        userRemoteDataSource.getAllUsers(new GetAllUsers() {
            @Override
            public void onAllUsersLoaded(List<User> users) {
                refreshLocalDataSource(users);
                refreshLocalDataSource(users);
                callback.onAllUsersLoaded(users);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(User user) {
        if (cachedUsers == null) {
            cachedUsers = new LinkedHashMap<>();
        }

        cachedUsers.clear();

        cachedUsers.put(user.getID(), user);

        cacheIsDirty = false;
    }

    private void refreshCache(List<User> users) {
        if (cachedUsers == null) {
            cachedUsers = new LinkedHashMap<>();
        }

        cachedUsers.clear();

        for (User u : users) {
            cachedUsers.put(u.getID(), u);
        }

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<User> users) {
        userLocalDataSource.deleteAllUsers();
        for (User u : users) {
            userLocalDataSource.saveUser(u);
        }
    }

    private void refreshLocalDataSource(User user) {
        userLocalDataSource.deleteUser(user);
        userLocalDataSource.saveUser(user);
    }
}
