package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;

public class HaverClient extends RestClient {
    private static HaverClient INSTANCE;

    private HaverResource haverEndpoint;

    private HaverClient(Context context) {
        super(context);
        haverEndpoint = WebResourceFactory.newResource(HaverResource.class, target);
    }

    public static HaverClient getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = new HaverClient(context);
        }
        return INSTANCE;
    }

    public List<Haver> getAllHavers(long desireId) {
        return haverEndpoint.getAllHavers(desireId);
    }

    public Haver createHaver(long desireId, Haver haver, User user) {
        return haverEndpoint.createHaver(desireId, haver, user);
    }

    public Haver get(long desireId, long haverId) {
        return haverEndpoint.get(desireId, haverId);
    }

    public Haver updateHaver(long desireId, long haverId, Haver haver) {
        return haverEndpoint.updateHaver(desireId, haverId, haver);
    }

    public void deleteHaver(long desireId, long haverId) {
        haverEndpoint.deleteHaver(desireId, haverId);
    }

    public Haver acceptHaver(long desireId, long haverId, Haver haver) {
        haver.setAccepted(true);
        return updateHaver(desireId, haverId, haver);
    }
}
