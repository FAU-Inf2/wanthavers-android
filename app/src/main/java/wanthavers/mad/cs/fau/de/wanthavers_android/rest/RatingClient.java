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

    public Rating createRating(long userId, long desireId, float stars, String comment) {
        return ratingEndpoint.createRating(null, userId, desireId, stars, comment);
    }

    public Rating get(long userId, long ratingId) {
        return ratingEndpoint.get(userId, ratingId);
    }

    public Rating updateRating(long userId, long ratingId, float stars, String comment) {
        return ratingEndpoint.updateRating(null, userId, ratingId, stars, comment);
    }

    public Rating avgRating(long userId) {
        return ratingEndpoint.avgRating(userId);
    }

}
