package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;

public class DesireClient extends RestClient {
    private static DesireClient INSTANCE;

    private DesireResource desireEndpoint;

    private DesireClient(Context context) {
        super(context);
        desireEndpoint = WebResourceFactory.newResource(DesireResource.class, target);
    }

    public static DesireClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DesireClient(context);
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

    public Desire createDesire(Desire desire) {
        return desireEndpoint.createDesire(null, desire);
    }

    public Desire updateDesire(long desireId, Desire desire) {
        return desireEndpoint.updateDesire(desireId, desire);
    }

    public void deleteDesire(long desireId) {
        desireEndpoint.deleteDesire(desireId);
    }
}
