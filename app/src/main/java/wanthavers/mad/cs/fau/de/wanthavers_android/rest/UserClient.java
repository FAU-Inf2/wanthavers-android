package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Location;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.UserFlag;
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

    public User login() {
        return loginEndpoint.login(null);
    }

    public List<Location> getSavedLocations() {
        return userEndpoint.getSavedLocations(null);
    }

    public void sendToken(String email) {
        userEndpoint.sendToken(email);
    }

    public UserFlag flagUser(long id) {
        return userEndpoint.flagUser(null, id);
    }
}
