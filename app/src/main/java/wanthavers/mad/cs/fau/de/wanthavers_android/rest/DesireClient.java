package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;

public class DesireClient {
    private static DesireClient INSTANCE;

    private DesireResource desireEndpoint;

    private DesireClient() {
        //TODO: get API-URL from shared preferences or something like that
        final String API_URL = "http://faui21f.informatik.uni-erlangen.de:9090/";
        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(API_URL);
        desireEndpoint = WebResourceFactory.newResource(DesireResource.class, target);
    }

    public static DesireClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DesireClient();
        }
        return INSTANCE;
    }

    public List<Desire> get() {
        return desireEndpoint.get();
    }

    public Desire get(@PathParam("id") long l) {
        return desireEndpoint.get(l);
    }

    public Desire createDesire(Desire desire, User user) {
        return desireEndpoint.createDesire(desire, user);
    }

    public Desire updateDesire(@PathParam("id") long l, Desire desire) {
        return desireEndpoint.updateDesire(l, desire);
    }

    public void deleteDesire(@PathParam("id") long l) {
        desireEndpoint.deleteDesire(l);
    }

    public void createDummies() {
        throw new UnsupportedOperationException();
    }
}
