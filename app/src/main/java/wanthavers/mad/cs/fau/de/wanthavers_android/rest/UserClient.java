package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.UserResource;

public class UserClient implements UserResource {
    private static UserClient INSTANCE;

    private UserResource userEndpoint;

    private UserClient() {
        //TODO: get API-URL from shared preferences or something like that
        final String API_URL = "http://faui21f.informatik.uni-erlangen.de:9090/";
        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(API_URL);
        userEndpoint = WebResourceFactory.newResource(UserResource.class, target);
    }

    public static UserClient getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserClient();
        }
        return INSTANCE;
    }

    @Override
    public List<User> get() {
        return userEndpoint.get();
    }

    @Override
    public User get(@PathParam("id") long l) {
        return userEndpoint.get(l);
    }

    @Override
    public User createUser(User user) {
        return userEndpoint.createUser(user);
    }

    @Override
    public User updateUser(@PathParam("id") long l, User user) {
        return userEndpoint.updateUser(l, user);
    }

    @Override
    public void deleteUser(@PathParam("id") long l) {
        userEndpoint.deleteUser(l);
    }

    @Override
    public List<Desire> getDesires(@PathParam("id") long l) {
        return userEndpoint.getDesires(l);
    }

    @Override
    public void createDummies() {
        throw new UnsupportedOperationException();
    }
}
