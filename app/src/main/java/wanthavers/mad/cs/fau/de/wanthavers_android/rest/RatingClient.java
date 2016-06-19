package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Rating;
import de.fau.cs.mad.wanthavers.common.rest.api.RatingResource;

public class RatingClient extends RestClient {
    private static RatingClient INSTANCE;

    private RatingResource ratingEndpoint;

    private RatingClient(Context context) {
        super(context);
    }

    @Override
    protected void buildNewEndpoint() {
        ratingEndpoint = null;
        ratingEndpoint = WebResourceFactory.newResource(RatingResource.class, target);
    }

    public static RatingClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RatingClient(context);
        }
        return INSTANCE;
    }

    public List<Rating> getAllRatings(long userId) {
        return ratingEndpoint.getAllRatings(userId);
    }

    public Rating createRating(long rateeId, long desireId, float stars, String comment) {
        return ratingEndpoint.createRating(null, rateeId, desireId, stars, comment);
    }

    public Rating get(long rateeId, long ratingId) {
        return ratingEndpoint.get(rateeId, ratingId);
    }

    public Rating updateRating(long rateeId, long ratingId, float stars, String comment) {
        return ratingEndpoint.updateRating(null, rateeId, ratingId, stars, comment);
    }

    public Rating avgRating(long userId) {
        return ratingEndpoint.avgRating(userId);
    }

}
