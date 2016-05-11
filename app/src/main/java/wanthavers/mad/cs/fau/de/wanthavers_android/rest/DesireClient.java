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

public class DesireClient extends RestClient {
    private static DesireClient INSTANCE;

    private DesireResource desireEndpoint;

    private DesireClient() {
        super();
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

    public Desire get(long desireId) {
        return desireEndpoint.get(desireId);
    }

    public List<Desire> getByLocation(double lat, double lon, double radius) {
        return desireEndpoint.getByLocation(lat, lon, radius);
    }

    public Desire createDesire(Desire desire, User user) {
        return desireEndpoint.createDesire(desire, user);
    }

    public Desire updateDesire(long desireId, Desire desire) {
        return desireEndpoint.updateDesire(desireId, desire);
    }

    public void deleteDesire(long desireId) {
        desireEndpoint.deleteDesire(desireId);
    }
}
