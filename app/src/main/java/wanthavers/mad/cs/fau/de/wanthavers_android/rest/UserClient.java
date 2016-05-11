package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;

public class UserClient extends RestClient {
    private static UserClient INSTANCE;

    private UserResource userEndpoint;

    private UserClient() {
        super();
        userEndpoint = WebResourceFactory.newResource(UserResource.class, target);
    }

    public static UserClient getInstance() {
        if(INSTANCE == null){
            INSTANCE = new UserClient();
        }
        return INSTANCE;
    }

    public List<User> get() {
        return userEndpoint.get();
    }

    public User get(long userId) {
        return userEndpoint.get(userId);
    }

    public User createUser(User user) {
        return userEndpoint.createUser(user);
    }

    public User updateUser(long userId, User user) {
        return userEndpoint.updateUser(userId, user);
    }

    public void deleteUser(long userId) {
        userEndpoint.deleteUser(userId);
    }

    public List<Desire> getDesires(long userId) {
        return userEndpoint.getDesires(userId);
    }
}
