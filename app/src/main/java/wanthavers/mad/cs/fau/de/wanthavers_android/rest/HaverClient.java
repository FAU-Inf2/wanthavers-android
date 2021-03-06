package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Haver;
import de.fau.cs.mad.wanthavers.common.HaverStatus;
import de.fau.cs.mad.wanthavers.common.User;
import de.fau.cs.mad.wanthavers.common.rest.api.HaverResource;

public class HaverClient extends RestClient {
    private static HaverClient INSTANCE;

    private HaverResource haverEndpoint;

    private HaverClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        haverEndpoint = null;
        haverEndpoint = WebResourceFactory.newResource(HaverResource.class, target);
    }

    public static HaverClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HaverClient(context);
        }
        return INSTANCE;
    }

    public List<Haver> getAllHavers(long desireId) {
        return haverEndpoint.getAllHavers(desireId);
    }

    public Haver createHaver(long desireId, Haver haver) {
        return haverEndpoint.createHaver(null, desireId, haver);
    }

    public Haver get(long desireId, long userId) {
        return haverEndpoint.get(desireId, userId);
    }

    public Haver updateHaver(long desireId, long haverId, Haver haver) {
        return haverEndpoint.updateHaver(null, desireId, haverId, haver);
    }

    public Haver acceptHaver(long desireId, long haverId, Haver haver) {
        haver.setStatus(HaverStatus.ACCEPTED);
        return haverEndpoint.updateHaver(null, desireId, haverId, haver);
    }

    public Haver unacceptHaver(long desireId, long haverId, Haver haver) {
        haver.setStatus(HaverStatus.ADDED);
        return haverEndpoint.updateHaver(null, desireId, haverId, haver);
    }

    public Haver getAcceptedHaver(long desireId) {
        return haverEndpoint.getAccepted(desireId);
    }

    public Haver deleteHaver(long desireId, long userId) {
        return haverEndpoint.updateHaverStatus(null, desireId, userId, HaverStatus.DELETED);
    }

    public Haver setHaverStatus(long desireId, long userId, int status) {
        return haverEndpoint.updateHaverStatus(null, desireId, userId, status);
    }

    public Haver updateRequestedPrice(long desireId, long userId, double requestedPrice) {
        return haverEndpoint.updateRequestedPrice(null, desireId, userId, requestedPrice);
    }
}
