package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.LoginResource;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;

public class UserClient extends RestClient {
    private static UserClient INSTANCE;

    private UserResource userEndpoint;
    private LoginResource loginEndpoint;

    private UserClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        userEndpoint = null;
        loginEndpoint = null;
        userEndpoint = WebResourceFactory.newResource(UserResource.class, target);
        loginEndpoint = WebResourceFactory.newResource(LoginResource.class, target);
    }

    public static UserClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserClient(context);
        }
        return INSTANCE;
    }

    public User get(long userId) {
        return userEndpoint.get(userId);
    }

    public User createUser(User user, String password) {
        return userEndpoint.createUser(user, password);
    }

    public User updateUser(User user) {
        return userEndpoint.updateUser(null, user);
    }

    public void deleteUser() {
        userEndpoint.deleteUser(null);
    }

    public List<Desire> getDesires(long userId) {
        return userEndpoint.getDesires(userId);
    }

    public List<Desire> getDesiresAsHaver(long userId, List<Integer> status) {
        return userEndpoint.getDesiresAsHaver(userId, status);
    }

    public User login() {
        return loginEndpoint.login(null);
    }
}
