package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.rest.api.DesireResource;

public class DesireClient extends RestClient {
    private static DesireClient INSTANCE;

    private DesireResource desireEndpoint;

    private DesireClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        desireEndpoint = null;
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

    public List<Desire> getByFilter(Long category, Double price_min, Double price_max, Double reward_min, Float rating_min, Double lat, Double lon, Double radius, List<Integer> status) {
        return desireEndpoint.getByFilters(category, price_min, price_max, reward_min, rating_min, lat, lon, radius, status);
    }

    public Desire createDesire(Desire desire) {
        return desireEndpoint.createDesire(null, desire);
    }

    public Desire updateDesire(long desireId, Desire desire) {
        return desireEndpoint.updateDesire(null, desireId, desire);
    }

    public Desire updateDesireStatus(long desireId, int status) {
        return desireEndpoint.updateDesireStatus(null, desireId, status);
    }
}
