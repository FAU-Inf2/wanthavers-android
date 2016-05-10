package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;

public class HaverClient implements HaverResource {
    private static HaverClient INSTANCE;

    private HaverResource haverEndpoint;

    private HaverClient() {
        //TODO: get API-URL from shared preferences or something like that
        final String API_URL = "http://faui21f.informatik.uni-erlangen.de:9090/";
        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(API_URL);
        haverEndpoint = WebResourceFactory.newResource(HaverResource.class, target);
    }

    public static HaverClient getInstance() {
        if(INSTANCE == null){
            INSTANCE = new HaverClient();
        }
        return INSTANCE;
    }

    @Override
    public List<Haver> getAllHavers(@PathParam("desire-id") long l) {
        return haverEndpoint.getAllHavers(l);
    }

    @Override
    public Haver createHaver(@PathParam("desire-id") long l, Haver haver, User user) {
        return haverEndpoint.createHaver(l, haver, user);
    }

    @Override
    public Haver get(@PathParam("desire-id") long l, @PathParam("id") long l1) {
        return haverEndpoint.get(l, l1);
    }

    @Override
    public Haver updateHaver(@PathParam("desire-id") long l, @PathParam("id") long l1, Haver haver) {
        return haverEndpoint.updateHaver(l, l1, haver);
    }

    @Override
    public void deleteHaver(@PathParam("desire-id") long l, @PathParam("id") long l1) {
        haverEndpoint.deleteHaver(l, l1);
    }
}
